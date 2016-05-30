package it.eurotn.panjea.preventivi.manager.documento;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.iva.util.IImponibiliIvaQueryExecutor;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.Totalizzatore;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.TotalizzazioneNormale;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.preventivi.domain.AttributoRiga;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.AreaPreventivoManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoManager;
import it.eurotn.panjea.preventivi.service.exception.ClientePotenzialePresenteException;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.RegoleCambioStatoAreaPreventivo;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaPreventivoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaPreventivoManager")
public class AreaPreventivoManagerBean implements AreaPreventivoManager {

	private static Logger logger = Logger.getLogger(AreaPreventivoManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private DocumentiManager documentiManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private IImponibiliIvaQueryExecutor imponibiliIvaQueryExecutor;

	@EJB
	private AreaIvaManager areaIvaManager;

	@EJB
	private AreaRateManager areaRateManager;

	@EJB
	private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

	@EJB
	private PrezzoArticoloCalculator prezzoArticoloCalculator;

	@EJB
	@IgnoreDependency
	private RigaPreventivoManager rigaPreventivoManager;

	@SuppressWarnings("unchecked")
	@Override
	public void aggiungiVariazione(Integer idAreaPreventivo, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
		logger.debug("--> Enter aggiungiVariazione");
		Query query = panjeaDAO.prepareNamedQueryWithoutFlush(RigaArticolo.QUERY_CARICA_BY_ID_AREA_PREVENTIVO);
		query.setParameter("idAreaPreventivo", idAreaPreventivo);
		List<RigaArticolo> righe = query.getResultList();

		for (RigaArticolo rigaArticolo : righe) {

			PoliticaPrezzo politicaPrezzo = calcolaPoliticaPrezzo(rigaArticolo);
			rigaArticolo.setPoliticaPrezzo(politicaPrezzo);
			rigaArticolo = (RigaArticolo) variazioneScontoStrategy.applicaVariazione(rigaArticolo, variazione,
					tipoVariazioneScontoStrategy);
			rigaArticolo = (RigaArticolo) variazioneProvvigioneStrategy.applicaVariazione(rigaArticolo,
					percProvvigione, tipoVariazioneProvvigioneStrategy);
			try {
				panjeaDAO.save(rigaArticolo);
			} catch (DAOException e) {
				logger.error("-->errore nel salvare la riga dell'ordine " + rigaArticolo, e);
				throw new RuntimeException("-->errore nel caricare la riga dell'ordine " + rigaArticolo, e);
			}
		}
		AreaPreventivo areaPreventivo = new AreaPreventivo();
		areaPreventivo.setId(idAreaPreventivo);
		areaPreventivo = caricaAreaPreventivo(areaPreventivo);
		areaPreventivo.setStatoAreaPreventivo(StatoAreaPreventivo.PROVVISORIO);
		areaPreventivo.getDatiValidazioneRighe().invalida();
		salvaAreaPreventivo(areaPreventivo);
		logger.debug("--> Exit aggiungiVariazione");

	}

	@Override
	public AreaPreventivo bloccaPreventivo(AreaPreventivo areaPreventivo, boolean blocca) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void calcolaGiacenzaRighePreventivo(Integer idAreaPreventivo) {
		// TODO Auto-generated method stub

	}

	/**
	 * Calcola la politica prezzo della riga di riferimento.
	 *
	 * @param rigaArticolo
	 *            riga articolo
	 * @return politica prezzo
	 */
	private PoliticaPrezzo calcolaPoliticaPrezzo(RigaArticolo rigaArticolo) {
		ArticoloLite articolo = rigaArticolo.getArticolo();
		AreaPreventivo areaPreventivo = rigaArticolo.getAreaPreventivo();
		Integer idListino = null;
		Integer idListinoAlternativo = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;

		if (areaPreventivo.getListino() != null) {
			idListino = areaPreventivo.getListino().getId();
		}

		if (areaPreventivo.getListinoAlternativo() != null) {
			idListinoAlternativo = areaPreventivo.getListinoAlternativo().getId();
		}

		if (areaPreventivo.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaPreventivo.getDocumento().getSedeEntita().getId();
		}

		AreaRate areaRate = areaRateManager.caricaAreaRate(rigaArticolo.getAreaPreventivo().getDocumento());
		CodicePagamento codicePagamento = areaRate.getCodicePagamento();
		BigDecimal scontoCommerciale = null;
		if (codicePagamento != null) {
			scontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
		}

		ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(), areaPreventivo
				.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null, idSedeEntita, null, null,
				ProvenienzaPrezzo.LISTINO, idTipoMezzo, null, articolo.getProvenienzaPrezzoArticolo(), areaPreventivo
				.getDocumento().getTotale().getCodiceValuta(), null, scontoCommerciale);
		return prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
	}

	/**
	 *
	 * @param areaPreventivo
	 *            areaPreventivo
	 * @param statoDaApplicare
	 *            stato da applicare
	 * @return areaPreventivo modificata
	 * @throws DAOException
	 */
	@Override
	public AreaPreventivo cambiaStatoSePossibile(AreaPreventivo areaPreventivo, StatoAreaPreventivo statoDaApplicare)
			throws ClientePotenzialePresenteException {

		if (areaPreventivo.getStatoAreaPreventivo() == statoDaApplicare) {
			throw new RuntimeException("L'area preventivo è già nello stato voluto");
		}

		if (areaPreventivo.isProcessato()) {
			throw new RuntimeException("Non è possibile modificare lo stato di un preventivo processato.");
		}

		RegoleCambioStatoAreaPreventivo regole = new RegoleCambioStatoAreaPreventivo();

		if (!regole.isCambioStatoPossibileSuDocumento(areaPreventivo)) {
			throw new RuntimeException("Il documento non può modificare lo stato.");
		}

		StatoAreaPreventivo statoOriginale = areaPreventivo.getStatoAreaPreventivo();

		if (!regole.isCambioStatoPossibile(statoOriginale, statoDaApplicare)) {
			throw new GenericException("Il documenton non può passare allo stato " + statoDaApplicare);
		}

		if (statoDaApplicare == StatoAreaPreventivo.ACCETTATO
				&& ClientePotenzialeLite.class.equals(areaPreventivo.getDocumento().getEntita().getClass())) {
			throw new ClientePotenzialePresenteException((ClientePotenzialeLite) areaPreventivo.getDocumento()
					.getEntita());
		}

		areaPreventivo.setStatoAreaPreventivo(statoDaApplicare);
		regole.postCambioStato(areaPreventivo, statoOriginale);

		try {
			return panjeaDAO.save(areaPreventivo);
		} catch (DAOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public AreaPreventivo caricaAreaPreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter caricaAreaPreventivo");
		AreaPreventivo areaPreventivoCaricata = null;
		try {
			areaPreventivoCaricata = panjeaDAO.load(AreaPreventivo.class, areaPreventivo.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaAreaPreventivo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAreaPreventivo");
		return areaPreventivoCaricata;
	}

	@Override
	public AreaPreventivo caricaAreaPreventivoByDocumento(Documento documento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AreaPreventivo checkInvalidaAreaPreventivo(AreaPreventivo areaPreventivo) {
		if (areaPreventivo.getStatoAreaPreventivo() == StatoAreaPreventivo.IN_ATTESA) {
			// || (areaPreventivo.getStatoAreaPreventivo() == StatoAreaPreventivo.BLOCCATO)) {
			areaPreventivo.setStatoAreaPreventivo(StatoAreaPreventivo.PROVVISORIO);
			try {
				areaPreventivo.getDatiValidazioneRighe().invalida();
				areaPreventivo = panjeaDAO.save(areaPreventivo);
			} catch (Exception e) {
				logger.error("--> errore nel salvare il documento durante l'invalidazione dell'area preventivo", e);
				throw new RuntimeException(
						"--> errore nel salvare il documento durante l'invalidazione dell'area preventivo", e);
			}
		}
		return areaPreventivo;
	}

	@Override
	public AreaPreventivo copiaPreventivo(Integer idAreaPreventivo) {
		AreaPreventivo areaDaClonare = new AreaPreventivo();
		areaDaClonare.setId(idAreaPreventivo);
		areaDaClonare = caricaAreaPreventivo(areaDaClonare);
		areaDaClonare.getRighe().size();

		AreaPreventivo areaClone = (AreaPreventivo) areaDaClonare.clone();
		Set<RigaPreventivo> righeClone = areaClone.getRighe();

		areaClone.setRighe(new TreeSet<RigaPreventivo>());

		// salvo l'area ordine
		areaClone = salvaAreaPreventivo(areaClone);

		// salvo le righe ordine
		for (RigaPreventivo rigaOrdine : righeClone) {
			rigaOrdine.setAreaPreventivo(areaClone);
			// Cambio anche agli attributi il riferimento alla riga.
			if (rigaOrdine instanceof RigaArticolo) {
				RigaArticolo rigaArticolo = (RigaArticolo) rigaOrdine;
				List<AttributoRiga> attributi = rigaArticolo.getAttributi();
				rigaArticolo.setAttributi(new ArrayList<AttributoRiga>());
				for (AttributoRiga attributo : attributi) {
					attributo.setId(null);
					attributo.setVersion(null);
					attributo.setRigaArticolo(rigaArticolo);
					rigaArticolo.getAttributi().add(attributo);
				}
			}
			rigaPreventivoManager.getDao(rigaOrdine).salvaRigaPreventivo(rigaOrdine);
		}

		AreaRate areaRate = areaRateManager.caricaAreaRate(areaDaClonare.getDocumento());

		// salvo l'area rate se è present
		if (areaRate.getId() != null) {
			AreaRate areaRateClone = (AreaRate) areaRate.clone();
			areaRateClone.setDocumento(areaClone.getDocumento());
			areaRateManager.salvaAreaRate(areaRateClone);
		}

		return areaClone;
	}

	/**
	 *
	 * @return azienda
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 *
	 * @return principal loggato
	 */
	private JecPrincipal getJecPrincipal() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal());
	}

	@Override
	public void ricalcolaPrezziPreventivo(Integer idAreaPreventivo) {
		// carico l'area ordine
		AreaPreventivo areaPreventivo;
		try {
			areaPreventivo = panjeaDAO.load(AreaPreventivo.class, idAreaPreventivo);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'area preventivo.", e);
			throw new RuntimeException("errore durante il caricamento dell'area ordine.", e);
		}

		// carica l'area rate per avere il codice pagamento
		AreaRate areaRate = areaRateManager.caricaAreaRate(areaPreventivo.getDocumento());

		List<RigaPreventivo> righeOrdine = rigaPreventivoManager.getDao().caricaRighePreventivo(areaPreventivo);

		// ricalcolo i prezzi se la riga non è chiusa
		for (RigaPreventivo rigaOrdine : righeOrdine) {
			if (rigaOrdine instanceof RigaArticolo) {
				rigaPreventivoManager.ricalcolaPrezziRigaArticolo((RigaArticolo) rigaOrdine,
						areaRate.getCodicePagamento());
			}
		}

		// valido le righe ordine senza cambiare lo stato per lanciare la totalizzazione del documento
		validaRighePreventivo(areaPreventivo, areaRate, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaPreventivoRicerca> ricercaAreePreventivo(
			ParametriRicercaAreaPreventivo parametriRicercaAreaPreventivo) {
		logger.debug("--> Enter ricercaAreePreventivo");

		StringBuilder sb = new StringBuilder();
		sb.append("select  ");
		sb.append("a.id as idAreaDocumento, ");
		sb.append("a.tipoAreaPreventivo.id as idTipoAreaDocumento, ");
		sb.append("a.documento.codiceAzienda as azienda, ");
		sb.append("a.documento.id as idDocumento, ");
		sb.append("a.documento.tipoDocumento.id as idTipoDocumento, ");
		sb.append("a.documento.entita.id as idEntita, ");
		sb.append("a.dataRegistrazione as dataRegistrazione, ");
		sb.append("a.statoAreaPreventivo as statoAreaPreventivo, ");
		sb.append("a.documento.tipoDocumento.tipoEntita as tipoEntita, ");
		sb.append("a.documento.entita.codice as codiceEntita, ");
		sb.append("a.documento.entita.anagrafica.denominazione as denominazioneEntita, ");
		sb.append("a.documento.tipoDocumento.codice as codiceTipoDocumento, ");
		sb.append("a.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
		sb.append("a.documento.dataDocumento as dataDocumento, ");
		sb.append("a.documento.codice as codiceDocumento, ");
		sb.append("a.documento.totale.importoInValutaAzienda as totaleDocumento, ");
		sb.append("a.processato as processato, ");
		sb.append(" a.areaPreventivoNote.noteTestata as note, ");
		sb.append("sed.id as idSede, ");
		sb.append("sed.descrizione as descrizioneSede, ");
		sb.append("sed.indirizzo as indirizzoSede, ");
		sb.append("loc.descrizione as descrizioneLocalitaSede, ");
		sb.append("a.documento.sedeEntita.codice as codiceSede ");
		sb.append(" from AreaPreventivo a left join a.areaPreventivoNote left join a.documento.sedeEntita.sede sed  left join a.documento.entita left join a.documento.entita.anagrafica left join sed.datiGeografici.localita loc ");
		sb.append(" where a.documento.codiceAzienda = :codiceAzienda  ");

		if (parametriRicercaAreaPreventivo.getDataDocumentoFinale() != null
				&& parametriRicercaAreaPreventivo.getDataDocumentoIniziale() != null) {
			sb.append(" and a.documento.dataDocumento >= :dataDocumentoIniziale and a.documento.dataDocumento <= :dataDocumentoFinale ");
		}

		if (!parametriRicercaAreaPreventivo.getNumeroDocumentoIniziale().isEmpty()) {
			sb.append(" and a.documento.codice.codiceOrder >= :numeroDocumentoIniziale ");
		}

		if (parametriRicercaAreaPreventivo.getDataCreazioneTimeStamp() != null) {
			sb.append(" and a.dataCreazioneTimeStamp=:dataCreazioneTimeStamp");
		}

		if (!parametriRicercaAreaPreventivo.getNumeroDocumentoFinale().isEmpty()) {
			sb.append(" and a.documento.codice.codiceOrder <= :numeroDocumentoFinale");
		}

		if (parametriRicercaAreaPreventivo.getAnnoCompetenza() != null
				&& parametriRicercaAreaPreventivo.getAnnoCompetenza() != -1) {
			sb.append(" and a.annoMovimento=:annoCompetenza ");
		}

		if (parametriRicercaAreaPreventivo.getEntita() != null) {
			sb.append(" and (a.documento.entita = :entita) ");
		}

		if ((parametriRicercaAreaPreventivo.getTipiAreaPreventivo() != null)
				&& (parametriRicercaAreaPreventivo.getTipiAreaPreventivo().size() > 0)) {
			sb.append(" and (a.tipoAreaPreventivo in (:tipiAreaPreventivo)) ");
		}

		if (parametriRicercaAreaPreventivo.getUtente() != null) {
			sb.append(" and (a.userInsert = :codiceUtente) ");
		}

		if ((parametriRicercaAreaPreventivo.getStatiAreaPreventivo() != null)
				&& (parametriRicercaAreaPreventivo.getStatiAreaPreventivo().size() > 0)) {
			sb.append(" and (a.statoAreaPreventivo in (:statiAreaPreventivo)) ");
		}

		switch (parametriRicercaAreaPreventivo.getStatoPreventivo()) {
		case PROCESSATO:
			sb.append(" and (a.processato = true) ");
			break;
		case NON_PROCESSATO:
			sb.append(" and (a.processato = false) ");
			break;
		default:
			// non filtro per stato del preventivo
			break;
		}

		parametriRicercaAreaPreventivo.setCodiceAzienda(getAzienda());

		Query query = panjeaDAO.prepareQuery(sb.toString());
		((QueryImpl) query).getHibernateQuery().setResultTransformer(
				Transformers.aliasToBean((AreaPreventivoRicerca.class)));

		((QueryImpl) query).getHibernateQuery().setProperties(parametriRicercaAreaPreventivo);
		if (!parametriRicercaAreaPreventivo.getNumeroDocumentoIniziale().isEmpty()) {
			query.setParameter("numeroDocumentoIniziale", parametriRicercaAreaPreventivo.getNumeroDocumentoIniziale()
					.getCodiceOrder());
		}
		if (!parametriRicercaAreaPreventivo.getNumeroDocumentoFinale().isEmpty()) {
			query.setParameter("numeroDocumentoFinale", parametriRicercaAreaPreventivo.getNumeroDocumentoFinale()
					.getCodiceOrder());
		}

		List<AreaPreventivoRicerca> result = Collections.emptyList();

		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore nella ricerca documento", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit ricercaAreeOrdine");
		return result;
	}

	@Override
	public AreaPreventivo salvaAreaPreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter salvaAreaPreventivo");

		// allinea l'attributo tipoDocumento di Documento con il tipoDocumento
		// di TipoAreaOrdine
		areaPreventivo.getDocumento().setTipoDocumento(areaPreventivo.getTipoAreaPreventivo().getTipoDocumento());

		if (areaPreventivo.getDocumento().isNew()) {
			// inizializza il codiceAzienda di documento
			areaPreventivo.getDocumento().setCodiceAzienda(getAzienda());
		}
		// save di documento
		Documento documento;
		try {
			documento = documentiManager.salvaDocumento(areaPreventivo.getDocumento());
		} catch (DocumentoDuplicateException e) {
			logger.error("--> errore DocumentoDuplicateException in salvaAreaPreventivo", e);
			throw new RuntimeException(e);
		}
		areaPreventivo.setDocumento(documento);

		try {
			if (areaPreventivo.getAreaPreventivoNote().isEmpty()) {
				areaPreventivo.setAreaPreventivoNote(null);
			}
			areaPreventivo = panjeaDAO.save(areaPreventivo);
		} catch (Exception e) {
			logger.error("--> errore nel salvare l'area preventivo", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaAreaPreventivo");
		return areaPreventivo;
	}

	@Override
	public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
		logger.debug("--> Enter spostaRighe");

		SpostaRighePreventivoManager spostaRigheManager = new SpostaRighePreventivoManager(panjeaDAO);
		spostaRigheManager.spostaRighe(righeDaSpostare, idDest);
		logger.debug("--> Exit spostaRighe");

	}

	@Override
	public AreaPreventivo totalizzaDocumento(AreaPreventivo areaPreventivo, AreaPartite areaPartite) {
		imponibiliIvaQueryExecutor.setAreaDocumento(areaPreventivo);
		imponibiliIvaQueryExecutor.setQueryString("RigaArticoloPreventivo.caricaImponibiliIva");
		List<RigaIva> righeIva = areaIvaManager.generaRigheIva(imponibiliIvaQueryExecutor,
				areaPreventivo.getDocumento(), areaPreventivo.isAddebitoSpeseIncasso(), null, areaPartite);

		totalizzatoriQueryExecutor.setAreaDocumento(areaPreventivo);
		totalizzatoriQueryExecutor.setQueryString("RigaArticoloPreventivo.caricaByTipo");

		Totalizzatore totalizzatore = new TotalizzazioneNormale();

		Documento documentoTotalizzato = totalizzatore.totalizzaDocumento(areaPreventivo.getDocumento(),
				areaPreventivo.getTotaliArea(), totalizzatoriQueryExecutor, righeIva);
		areaPreventivo.setDocumento(documentoTotalizzato);

		return areaPreventivo;
	}

	@Override
	public AreaPreventivo validaRighePreventivo(AreaPreventivo areaPreventivo, AreaPartite areaPartite,
			boolean cambioStato) {
		logger.debug("--> Enter validaRighePreventivo");

		logger.debug("--> Totalizzo il documento");
		areaPreventivo = totalizzaDocumento(areaPreventivo, areaPartite);

		if (cambioStato) {
			// if (areaPreventivo.getTipoAreaPreventivo().getTipoDocumento().getTipoEntita() != TipoEntita.AZIENDA
			// && areaPreventivo.getDocumento().getSedeEntita().getBloccoSede().isBlocco()) {
			// areaPreventivo.setStatoAreaPreventivo(StatoAreaPreventivo.BLOCCATO);
			// } else {
			areaPreventivo.setStatoAreaPreventivo(StatoAreaPreventivo.IN_ATTESA);
			// }

			// setto l'area validata
			areaPreventivo.getDatiValidazioneRighe().valida(getJecPrincipal().getUserName());
		}

		AreaPreventivo areaPreventivoSave = null;
		try {
			areaPreventivoSave = salvaAreaPreventivo(areaPreventivo);
			// TODO: riabilitare ordinamento righe dopo salvataggio area preventivo validata
			// ordinaRighe(areaPreventivoSave);
		} catch (Exception e) {
			logger.error("--> errore nel salvare l'area preventivo " + areaPreventivoSave, e);
			throw new RuntimeException(e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> area preventivo salvata " + areaPreventivoSave);
		}
		logger.debug("--> Exit validaRighePreventivo");
		return areaPreventivoSave;
	}

}
