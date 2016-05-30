package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabileEstrattoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.EstrattoContoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.SaldoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.EstrattoContoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.EstrattoContoManager")
public class EstrattoContoManagerBean implements EstrattoContoManager {
	private static Logger logger = Logger.getLogger(EstrattoContoManagerBean.class);

	@EJB
	private SaldoManager saldoManager;

	@EJB
	private AziendeManager aziendeManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	private PianoContiManager pianoContiManager;

	/**
	 * Calcola il saldo progressivo per le righe estratto conto trovate, ciclando per ogni riga presente.
	 *
	 * @param righe
	 *            le righe di cui aggiornare il progressivo
	 * @param importoSaldoPrecedente
	 *            l'importo del saldo precedente da cui partire
	 * @param tipoConto
	 *            it.eurotn.panjea.contabilita.domain.Conto.TipoConto
	 * @return List<RigaContabileEstrattoConto>
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 * @throws ContabilitaException
	 *             ContabilitaException
	 */
	private List<RigaContabileEstrattoConto> calcolaSaldoRigheEstrattoConto(List<RigaContabileEstrattoConto> righe,
			final BigDecimal importoSaldoPrecedente) throws TipoDocumentoBaseException, ContabilitaException {
		// Carico il saldo per ogni movimento (rigaContabileEstrattoConto)
		// Se trovo dei movimenti di apertura/chiusura vanno visualizzati ma non va modificato il saldo
		TipiDocumentoBase tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
		List<TipoAreaContabile> tipiAreaAperturaChiusura = tipiDocumentoBase.getTipiDocumentiAperturaChiusura();

		List<TipoDocumento> tipiDocumentoAperturaChiusura = new ArrayList<TipoDocumento>();
		for (TipoAreaContabile tipoAreaContabile : tipiAreaAperturaChiusura) {
			tipiDocumentoAperturaChiusura.add(tipoAreaContabile.getTipoDocumento());
		}

		List<RigaContabileEstrattoConto> righeDefinitive = new ArrayList<RigaContabileEstrattoConto>();
		int idRigaPrecedente = -1;
		BigDecimal progressivoImporto = importoSaldoPrecedente;
		int ordinamento = 0;
		for (RigaContabileEstrattoConto rigaContabileEstrattoConto : righe) {
			if (idRigaPrecedente != -1
					&& rigaContabileEstrattoConto.getIdRigaContabile().intValue() == idRigaPrecedente) {
				RigaContabileEstrattoConto rcec = righeDefinitive.get(righeDefinitive.size() - 1);
				rcec.addDocumentiCollegati(rigaContabileEstrattoConto.getDocumentoCollegato(),
						rigaContabileEstrattoConto.getEntCollegato());
			} else {
				ordinamento++;
				rigaContabileEstrattoConto.setOrdinamento(ordinamento);

				if (!tipiDocumentoAperturaChiusura.contains(rigaContabileEstrattoConto.getTipoDocumento())) {
					BigDecimal importoDare = (rigaContabileEstrattoConto.getImportoDare() != null) ? rigaContabileEstrattoConto
							.getImportoDare() : BigDecimal.ZERO;
							BigDecimal importoAvere = (rigaContabileEstrattoConto.getImportoAvere() != null) ? rigaContabileEstrattoConto
									.getImportoAvere() : BigDecimal.ZERO;
									progressivoImporto = progressivoImporto.add(importoDare.subtract(importoAvere));
				}
				rigaContabileEstrattoConto.setProgressivoImporto(progressivoImporto);

				rigaContabileEstrattoConto.addDocumentiCollegati(rigaContabileEstrattoConto.getDocumentoCollegato(),
						rigaContabileEstrattoConto.getEntCollegato());
				righeDefinitive.add(rigaContabileEstrattoConto);
			}
			idRigaPrecedente = rigaContabileEstrattoConto.getIdRigaContabile().intValue();
		}
		return righeDefinitive;
	}

	@Override
	public EstrattoConto caricaEstrattoConto(ParametriRicercaEstrattoConto parametriRicercaEstrattoConto)
			throws ContabilitaException, TipoDocumentoBaseException {
		logger.debug("--> Enter caricaEstrattoConto");
		SaldoConto saldoPrecedente = null;
		SaldoConto saldoFinale = null;

		SottoConto sottoConto = null;
		if (parametriRicercaEstrattoConto.getSottoConto() != null
				&& parametriRicercaEstrattoConto.getSottoConto().getId() != null) {
			sottoConto = pianoContiManager.caricaSottoConto(parametriRicercaEstrattoConto.getSottoConto().getId());
		}

		EstrattoConto estrattoConto = new EstrattoConto();

		AziendaLite aziendaLite;
		try {
			aziendaLite = aziendeManager.caricaAzienda(getAzienda(), false);
		} catch (AnagraficaServiceException e) {
			logger.error("--> errore nel caricare l'azienda lite", e);
			throw new RuntimeException(e);
		}

		// Calcolo il saldo precedente; se ho la data di inizio attivita' devo trovare
		// solamente il saldoIniziale,altrimenti il saldo del giorno prima
		Date dataSaldoPrecedente = PanjeaEJBUtil.getDateTimeToZero(parametriRicercaEstrattoConto.getDataRegistrazione()
				.getDataIniziale());

		if (parametriRicercaEstrattoConto.getAnnoCompetenza() != null
				&& dataSaldoPrecedente != null
				&& dataSaldoPrecedente.compareTo(aziendaLite.getDataInizioEsercizio(parametriRicercaEstrattoConto
						.getAnnoCompetenza())) != 0) {
			Calendar calendarDataSaldoPrecedente = Calendar.getInstance();
			calendarDataSaldoPrecedente.setTime(dataSaldoPrecedente);
			calendarDataSaldoPrecedente.add(Calendar.DAY_OF_MONTH, -1);
			dataSaldoPrecedente = calendarDataSaldoPrecedente.getTime();
		}
		saldoPrecedente = saldoManager.calcoloSaldo(sottoConto, parametriRicercaEstrattoConto.getCentroCosto(),
				dataSaldoPrecedente, parametriRicercaEstrattoConto.getAnnoCompetenza(), aziendaLite);

		// Calcolo saldo di fine periodo
		saldoFinale = saldoManager.calcoloSaldo(sottoConto, parametriRicercaEstrattoConto.getCentroCosto(),
				parametriRicercaEstrattoConto.getDataRegistrazione().getDataFinale(),
				parametriRicercaEstrattoConto.getAnnoCompetenza(), aziendaLite);

		estrattoConto.setSaldoFinale(saldoFinale.getSaldo());
		estrattoConto.setSaldoPrecedente(saldoPrecedente.getSaldo());

		List<RigaContabileEstrattoConto> righeEstrattoConto = caricaRigheEstrattoConto(parametriRicercaEstrattoConto);
		righeEstrattoConto = calcolaSaldoRigheEstrattoConto(righeEstrattoConto, saldoPrecedente.getSaldo());
		estrattoConto.setRigheEstrattoConto(righeEstrattoConto);
		logger.debug("--> Exit caricaEstrattoConto");
		return estrattoConto;
	}

	/**
	 *
	 * @param parametriRicercaEstrattoConto
	 *            parametri per la costruzione dell'estratto conto
	 * @return righe dell'estratto conto
	 * @throws ContabilitaException
	 *             eccezione generica
	 */
	@SuppressWarnings("unchecked")
	private List<RigaContabileEstrattoConto> caricaRigheEstrattoConto(
			final ParametriRicercaEstrattoConto parametriRicercaEstrattoConto) throws ContabilitaException {
		List<RigaContabileEstrattoConto> righeContabiliEstrattoConto = new ArrayList<RigaContabileEstrattoConto>();

		// indica se la ricerca viene effettuata per conto o per centro di costo;
		// per ricerca per centro di costo, devo recuperare l'importo sulla riga centro costo e le note relative.
		boolean isRicercaConto = parametriRicercaEstrattoConto.isRicercaConto();
		boolean isRicercaCentroCosto = parametriRicercaEstrattoConto.isRicercaCentroCosto();

		StringBuffer queryString = new StringBuffer("select ");
		queryString.append(" rigaContabile.id as idRigaContabile,");
		queryString.append(" areaC.dataRegistrazione as dataRegistrazione,");
		queryString.append(" mastro.codice as codiceMastro,");
		queryString.append(" conto.codice as codiceConto,");
		queryString.append(" sott.codice as codiceSottoConto,");
		queryString.append(" sott.descrizione as sottoContoDescrizione,");
		if (isRicercaCentroCosto) {
			queryString
			.append(" CASE WHEN rigaContabile.contoInsert = it.eurotn.panjea.contabilita.domain.RigaContabile$EContoInsert.DARE THEN righeCentroCosto.importo else 0.0 END as importoDare, ");
			queryString
			.append(" CASE WHEN rigaContabile.contoInsert = it.eurotn.panjea.contabilita.domain.RigaContabile$EContoInsert.AVERE THEN righeCentroCosto.importo else 0.0 END as importoAvere, ");
		} else {
			queryString.append(" rigaContabile.importoDare as importoDare, ");
			queryString.append(" rigaContabile.importoAvere as importoAvere, ");
		}
		queryString.append(" doc.dataDocumento as dataDocumento, ");
		queryString.append(" doc.codice as numeroDocumento, ");
		queryString.append(" areaC.codice as protocollo, ");
		queryString.append(" doc.tipoDocumento as tipoDocumento, ");
		if (isRicercaCentroCosto) {
			queryString.append(" righeCentroCosto.nota as note, ");
		} else {
			queryString.append(" rigaContabile.note as note, ");
		}
		queryString.append(" areaC.note as noteAreaContabile, ");
		queryString.append(" areaC.statoAreaContabile as statoAreaContabile, ");
		queryString.append(" areaC.id as idAreaContabile, ");
		queryString.append(" ent as entita, ");
		queryString.append(" rigaContabile.numeroRigaGiornale as riga, ");
		queryString.append(" rigaContabile.paginaGiornale as pagina, ");
		queryString.append(" docCollegato.codice as numeroDocumentoCollegato, ");
		queryString.append(" docCollegato.dataDocumento as dataDocumentoCollegato, ");
		queryString.append(" tipoDocCollegato.codice as codiceTipoDocumentoCollegato, ");
		queryString.append(" tipoDocCollegato.descrizione as descrizioneTipoDocumentoCollegato, ");
		queryString.append(" anagCollegato.denominazione as entCollegato, ");
		queryString.append(" rata.numeroRata as numeroRata ");

		queryString.append(" from RigaContabile rigaContabile ");
		queryString.append(" inner join rigaContabile.areaContabile areaC ");
		queryString.append(" inner join areaC.documento doc ");
		queryString.append(" inner join rigaContabile.conto sott ");
		queryString.append(" inner join sott.conto conto ");
		queryString.append(" inner join conto.mastro mastro ");
		if (isRicercaCentroCosto) {
			queryString.append(" inner join rigaContabile.righeCentroCosto righeCentroCosto ");
		}
		queryString.append(" left join doc.entita ent ");

		queryString.append(" left join rigaContabile.pagamenti pags ");
		queryString.append(" left join pags.rata rata ");
		queryString.append(" left join rata.areaRate areaRate ");
		queryString.append(" left join areaRate.documento docCollegato ");
		queryString.append(" left join docCollegato.entita  entCollegato ");
		queryString.append(" left join entCollegato.anagrafica  anagCollegato ");
		queryString.append(" left join docCollegato.tipoDocumento tipoDocCollegato ");

		queryString.append(" where rigaContabile.areaContabile.documento.codiceAzienda = :paramCodiceAzienda ");
		queryString.append(" and rigaContabile.areaContabile.dataRegistrazione ");
		queryString.append(" between :paramDaDataRegistrazione and :paramADataRegistrazione ");
		queryString.append(" and rigaContabile.areaContabile.annoMovimento = :paramAnnoCompetenza ");
		if (isRicercaConto) {
			queryString.append(" and sott.id = :paramIdSottoConto ");
		}
		if (isRicercaCentroCosto) {
			queryString.append(" and righeCentroCosto.centroCosto.id = :paramIdCentroCosto ");
		}

		StringBuffer statoAreaQueryString = new StringBuffer(
				" and rigaContabile.areaContabile.statoAreaContabile in (:paramStatiAreaContabile) ");
		StringBuffer tipoDocumentoQueryString = new StringBuffer(
				" and rigaContabile.areaContabile.tipoAreaContabile.tipoDocumento in (:paramTipiAreaContabile) ");
		StringBuffer orderByString = new StringBuffer(
				" order by rigaContabile.areaContabile.dataRegistrazione,rigaContabile.areaContabile.id,rigaContabile.ordinamento,rigaContabile.id ");

		if (parametriRicercaEstrattoConto.getStatiAreaContabile().size() > 0) {
			queryString.append(statoAreaQueryString);
		}
		if (parametriRicercaEstrattoConto.getTipiDocumento().size() > 0) {
			queryString.append(tipoDocumentoQueryString);
		}
		queryString.append(orderByString);
		Query query = panjeaDAO.prepareQuery(queryString.toString());

		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramAnnoCompetenza", parametriRicercaEstrattoConto.getAnnoCompetenza());
		query.setParameter("paramDaDataRegistrazione", parametriRicercaEstrattoConto.getDataRegistrazione()
				.getDataIniziale(), TemporalType.DATE);
		query.setParameter("paramADataRegistrazione", parametriRicercaEstrattoConto.getDataRegistrazione()
				.getDataFinale(), TemporalType.DATE);

		if (isRicercaConto) {
			query.setParameter("paramIdSottoConto", parametriRicercaEstrattoConto.getSottoConto().getId());
		}
		if (isRicercaCentroCosto) {
			query.setParameter("paramIdCentroCosto", parametriRicercaEstrattoConto.getCentroCosto().getId());
		}
		if (parametriRicercaEstrattoConto.getStatiAreaContabile().size() > 0) {
			query.setParameter("paramStatiAreaContabile", parametriRicercaEstrattoConto.getStatiAreaContabile());
		}
		if (parametriRicercaEstrattoConto.getTipiDocumento().size() > 0) {
			query.setParameter("paramTipiAreaContabile", parametriRicercaEstrattoConto.getTipiDocumento());
		}
		try {
			((org.hibernate.ejb.HibernateQuery) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean(RigaContabileEstrattoConto.class));
			righeContabiliEstrattoConto = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento della lista dei estratto conto ", e);
			throw new RuntimeException("Impossibile caricare la lista dei estratto conto ", e);
		}
		return righeContabiliEstrattoConto;
	}

	/**
	 * @return codice azienda del principal loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}
}
