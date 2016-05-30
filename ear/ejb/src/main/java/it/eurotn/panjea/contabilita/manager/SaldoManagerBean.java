package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnnualeManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.SaldoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.querybuilder.SaldoContiQueryBuilderInterface;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Gestisce i vari metodi per il calcolo del saldo di un conto.
 *
 * @author adriano
 * @version 1.0, 03/set/07
 */

@Stateless(name = "Panjea.SaldoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SaldoManager")
public class SaldoManagerBean implements SaldoManager {

	private static Logger logger = Logger.getLogger(SaldoManagerBean.class);

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB(beanName = "Panjea.SaldoContiQueryBuilder")
	private SaldoContiQueryBuilderInterface saldoContiQueryBuilder;

	@EJB(beanName = "Panjea.SaldoCentriCostoQueryBuilder")
	private SaldoContiQueryBuilderInterface saldoCentriCostoQueryBuilder;

	@EJB
	private AreaContabileManager areaContabileManager;

	@IgnoreDependency
	@EJB
	private ContabilitaAnnualeManager contabilitaAnnualeManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	@EJB
	private PianoContiManager pianoContiManager;

	@Override
	public SaldoConti calcolaSaldiInizioAnno(TipoConto tipoConto, SottoConto sottoConto, CentroCosto centroCosto,
			Integer annoEsercizio, AziendaLite aziendaLite) throws TipoDocumentoBaseException, ContabilitaException {
		logger.debug("--> Enter calcolaSaldiInizioAnno");
		SaldoConti saldoConti = new SaldoConti();

		if (sottoConto != null) {
			// mi serve il saldo solamente del conto
			// setto il tipoConto, altrimenti avrei il sottoConto a null ed il tipoConto già settato
			tipoConto = sottoConto.getConto().getTipoConto();
		}

		// il tipoConto Economico ha saldo a zero
		if (tipoConto == TipoConto.ECONOMICO || centroCosto != null) {
			logger.debug("--> Exit calcolaSaldoInizioAnno. Saldo di un conto economico=0");
			return saldoConti;
		}

		Integer annoInizioCalcoloSaldo = contabilitaSettingsManager.caricaContabilitaSettings()
				.getAnnoInizioCalcoloSaldo();
		if (annoInizioCalcoloSaldo.equals(annoEsercizio)) {
			logger.debug("-->Exit calcolaSaldoInizioAnno. AnnoInizioCalcoloSaldo = AnnoEsercizio richiesto. Non calcolo il saldo iniziale");
			return saldoConti;
		}

		Integer annoUtileTrovato = annoInizioCalcoloSaldo;

		// Carico i tipoDocumento associati ai vari tipiOperazione (chisura/apertura)
		TipiDocumentoBase tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();

		AreaContabile areaContabileAperturaChiusura = caricaAraeContabileUtile(annoEsercizio, tipoConto,
				tipiDocumentoBase);
		if (areaContabileAperturaChiusura != null) {
			// Carico le righe contabili e creo i sottoContiDTO
			// Per performance se ho solamente il sottoconto carico le righe del sottoconto
			List<RigaContabile> righeContabiliAperturaChiusura = null;
			if (sottoConto != null) {
				righeContabiliAperturaChiusura = areaContabileManager.caricaRigheContabiliPerSottoConto(
						areaContabileAperturaChiusura.getId(), sottoConto);
			} else {
				righeContabiliAperturaChiusura = areaContabileManager
						.caricaRigheContabili(areaContabileAperturaChiusura.getId());
			}

			saldoConti.sommaRigheContabili(righeContabiliAperturaChiusura);

			annoUtileTrovato = areaContabileAperturaChiusura.getAnnoMovimento();
			// Controllo se il documento è un apertura o una chiusura
			if (areaContabileAperturaChiusura.getTipoAreaContabile().equals(
					tipiDocumentoBase.get(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(tipoConto)))) {
				// Negando il saldo ho praticamente un movimento di apertura dell'anno successivo.
				// Quindi l'anno utile trovato passa all'anno successivo. (potrebbe già essere l'anno richiesto)
				for (Entry<String, SaldoConto> saldoEntry : saldoConti.entrySet()) {
					saldoEntry.getValue().negate();
				}
				annoUtileTrovato++;
			}
		}

		// Se il movimento è nell'anno di esercizio richiesto ho trovato i saldi iniziali
		// altrimenti devo sommare i movimenti fino ad inizio anno
		if (!annoUtileTrovato.equals(annoEsercizio)) {
			if (annoUtileTrovato == 0) {
				// Calcolo dall'inizio perchè non ho un movimento di apertura
				// trovo l'anno del primo movimento
				String sql = "select min(YEAR(ac.dataRegistrazione)) from AreaContabile ac";
				Query query = panjeaDAO.prepareQuery(sql);
				try {
					annoUtileTrovato = (Integer) panjeaDAO.getSingleResult(query);
				} catch (DAOException e) {
					logger.error("-->errore nel trovare l'anno del primo movimento utile", e);
					throw new RuntimeException(e);
				}
			}
			// Devo trovare i saldi dei movimenti tra l'anno del movimento trovato e l'anno richiesto.
			// Per ogni anno di esercizio calcolo i saldo dell'anno. Non posso fare in un'unica query
			// con dataRegistrazione>dataInizioAnnoUtileTrovato perchè devo sempre controllare l'anno contabile.
			List<StatoAreaContabile> stati = new ArrayList<StatoAreaContabile>();
			stati.add(StatoAreaContabile.CONFERMATO);
			stati.add(StatoAreaContabile.VERIFICATO);
			if (annoUtileTrovato == null) {
				annoUtileTrovato = 1970;
			}
			for (; annoUtileTrovato < annoEsercizio; annoUtileTrovato++) {
				if (annoUtileTrovato.equals(annoInizioCalcoloSaldo)) {
					// Il saldo fin qui calcolato deve essere azzerato perchè anno esercizio = anno inizio calcolo saldo
					saldoConti = new SaldoConti();
				}
				if (annoUtileTrovato != 0) {
					List<SaldoConto> saldiMancanti = calcoloSaldi(sottoConto, null, null, null, annoUtileTrovato,
							aziendaLite, stati, false);
					// il risultato è una lista di saldi compresi i conti non del TipoConto che interessa (se sottoconto
					// è
					// nullo).
					// Li aggiungo al movimento utile filtrandoli per tipo conto, se il tipoConto è impostato.
					saldoConti.sommaSaldi(saldiMancanti, tipoConto);
				}
			}
		}
		logger.debug("--> Exit calcolaSaldiInizioAnno");
		return saldoConti;
	}

	@Override
	public List<SaldoConto> calcoloSaldi(Date dataInizio, Date dataFine, Integer annoEsercizio,
			AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree, boolean esclusiEconomici)
					throws TipoDocumentoBaseException, ContabilitaException {
		return calcoloSaldi(null, null, dataInizio, dataFine, annoEsercizio, aziendaLite, listaStatiAree,
				esclusiEconomici);
	}

	@Override
	public List<SaldoConto> calcoloSaldi(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio,
			Date dataFine, Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici) throws TipoDocumentoBaseException, ContabilitaException {
		return calcoloSaldi(sottoConto, centroCosto, dataInizio, dataFine, annoEsercizio, aziendaLite, listaStatiAree,
				esclusiEconomici, centroCosto != null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SaldoConto> calcoloSaldi(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio,
			Date dataFine, Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici, boolean caricaCentriCosto) throws TipoDocumentoBaseException,
			ContabilitaException {

		Query query = saldoContiQueryBuilder.getQuery(sottoConto, centroCosto, dataInizio, dataFine, annoEsercizio,
				aziendaLite, listaStatiAree, esclusiEconomici);
		((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((SaldoConto.class)));
		List<SaldoConto> saldoSottoConti = new ArrayList<SaldoConto>();
		try {
			saldoSottoConti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del bilancio", e);
			throw new ContabilitaException("Errore durante il caricamento del bilancio", e);
		}
		if (caricaCentriCosto) {
			Map<Integer, SaldoConto> saldi = new HashMap<Integer, SaldoConto>();
			for (SaldoConto saldoConto : saldoSottoConti) {
				saldi.put(saldoConto.getSottoContoId(), saldoConto);
			}
			Query query2 = saldoCentriCostoQueryBuilder.getQuery(sottoConto, centroCosto, dataInizio, dataFine,
					annoEsercizio, aziendaLite, listaStatiAree, esclusiEconomici);
			((QueryImpl) query2).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((SaldoConto.class)));
			List<SaldoConto> saldoCentriCosto = new ArrayList<SaldoConto>();
			try {
				saldoCentriCosto = panjeaDAO.getResultList(query2);
			} catch (DAOException e) {
				logger.error("--> Errore durante il caricamento del bilancio", e);
				throw new ContabilitaException("Errore durante il caricamento del bilancio", e);
			}
			for (SaldoConto saldoConto : saldoCentriCosto) {
				SaldoConto saldoPerCentroCosto = saldi.get(saldoConto.getSottoContoId());
				saldoPerCentroCosto.aggiungiCentroCosto(saldoConto);
			}
		}
		return saldoSottoConti;
	}

	@Override
	public SaldoConto calcoloSaldo(SottoConto sottoConto, CentroCosto centroCosto, Date dataInizio, Date dataFine,
			Integer annoEsercizio, AziendaLite aziendaLite, List<StatoAreaContabile> listaStatiAree,
			boolean esclusiEconomici) throws TipoDocumentoBaseException, ContabilitaException {
		logger.debug("--> Enter calcolaSaldo");
		List<SaldoConto> saldoConti = calcoloSaldi(sottoConto, centroCosto, dataInizio, dataFine, annoEsercizio,
				aziendaLite, listaStatiAree, esclusiEconomici);
		logger.debug("--> Exit calcoloSaldo");
		if (saldoConti.isEmpty()) {
			if (sottoConto != null) {
				return new SaldoConto(sottoConto);
			} else {
				return new SaldoConto();
			}
		} else {
			SaldoConto saldoTotale = new SaldoConto();
			for (SaldoConto saldoConto : saldoConti) {
				saldoTotale.aggiungiCentroCosto(saldoConto);
				saldoTotale.aggiungiImportoAvere(saldoConto.getImportoAvere());
				saldoTotale.aggiungiImportoDare(saldoConto.getImportoDare());
			}
			return saldoTotale;
		}
	}

	@Override
	public SaldoConto calcoloSaldo(SottoConto sottoConto, CentroCosto centroCosto, Date data, Integer annoEsercizio,
			AziendaLite aziendaLite) throws TipoDocumentoBaseException, ContabilitaException {
		// Carico il saldo di inizio anno
		logger.debug("--> Enter calcoloSaldo");
		data = PanjeaEJBUtil.getDateTimeToZero(data);

		TipoConto tipoConto = null;
		if (sottoConto != null) {
			sottoConto = pianoContiManager.caricaSottoConto(sottoConto.getId());
			tipoConto = sottoConto.getConto().getTipoConto();
		}

		SaldoConti saldoConti = calcolaSaldiInizioAnno(tipoConto, sottoConto, centroCosto, annoEsercizio, aziendaLite);

		List<SaldoConto> saldoInizialeList = saldoConti.asList();
		SaldoConto saldoConto = null;
		if (sottoConto == null) {
			saldoConto = new SaldoConto();
		} else {
			saldoConto = new SaldoConto(sottoConto);
		}
		if (!saldoInizialeList.isEmpty()) {
			// prendo il primo perchè è l'unico presente
			saldoConto = saldoConti.asList().get(0);
		}
		Date dataInizioEsercizio = aziendaLite.getDataInizioEsercizio(annoEsercizio);
		if (!dataInizioEsercizio.equals(data)) {
			List<StatoAreaContabile> stati = new ArrayList<StatoAreaContabile>();
			stati.add(StatoAreaContabile.CONFERMATO);
			stati.add(StatoAreaContabile.VERIFICATO);
			SaldoConto saldoMovimenti = calcoloSaldo(sottoConto, centroCosto, dataInizioEsercizio, data, annoEsercizio,
					aziendaLite, stati, false);
			saldoConto.aggiungiImportoAvere(saldoMovimenti.getImportoAvere());
			saldoConto.aggiungiImportoDare(saldoMovimenti.getImportoDare());
		}
		logger.debug("--> Exit calcoloSaldo");
		return saldoConto;
	}

	/**
	 * Carica la lista di aperture/chiusure a restituisce l'area contabile "utile".Cioè quella più vicina all'anno
	 * richiesto.<br/>
	 * Null se non viene trovata
	 *
	 * @param annoEsercizio
	 *            anno di Esercizio per calcolare l'area contabile utile
	 * @param tipoConto
	 *            tipo del conto per il quale cercare il movimento
	 * @param tipiDocumentoBase
	 *            {@link TipiDocumentoBase} inizializzati.
	 * @return l'arae contabile di apertura o chiusura più vicinal all'annoEsercizio.NULL se non aree di
	 *         apertura/chisura
	 * @throws ContabilitaException
	 *             lanciata su errore generico
	 * @throws TipoDocumentoBaseException
	 *             lanciata se non trovo i {@link TipoDocumentoBase} per apertura/chisura
	 */
	private AreaContabile caricaAraeContabileUtile(Integer annoEsercizio, TipoConto tipoConto,
			TipiDocumentoBase tipiDocumentoBase) throws ContabilitaException, TipoDocumentoBaseException {
		// Trovo i movimenti di chiusuraApertura fino all'anno desiderato
		List<AreaContabile> movimentiChiusuraApertura = contabilitaAnnualeManager.caricaAreeContabiliAperturaChiusura(
				annoEsercizio, tipoConto);
		// Se non ne ho nemmeno uno il saldo==0
		if (movimentiChiusuraApertura.isEmpty()) {
			return null;
		}

		// Recupero il primo movimento di apertura/chiusura trovato
		AreaContabile areaContabileAperturaChiusura = movimentiChiusuraApertura.get(0);
		// Controllo se il primo movimento utile è la chiusura nell'anno
		if (areaContabileAperturaChiusura.getTipoAreaContabile().equals(
				tipiDocumentoBase.get(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(tipoConto)))
				&& areaContabileAperturaChiusura.getAnnoMovimento() != null
				&& areaContabileAperturaChiusura.getAnnoMovimento().equals(annoEsercizio)) {
			// se ho solo questo movimento il saldo=0.
			if (movimentiChiusuraApertura.size() == 1) {
				return null;
			} else {
				// prendo il movimento successivo (quindi sicuramente l'apertura nell'anno)
				areaContabileAperturaChiusura = movimentiChiusuraApertura.get(1);
			}
		}
		return areaContabileAperturaChiusura;
	}
}