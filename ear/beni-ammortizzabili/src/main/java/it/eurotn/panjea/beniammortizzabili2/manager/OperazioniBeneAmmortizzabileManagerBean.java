/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.beniammortizzabili.util.BeneAmmortizzabileCalcoloAmmortamento;
import it.eurotn.panjea.beniammortizzabili.util.BeneImportoSoggettoAdAmmortamento;
import it.eurotn.panjea.beniammortizzabili.util.QuotaAmmortamentoFiscaleImporto;
import it.eurotn.panjea.beniammortizzabili.util.ValutazioneBeneImporto;
import it.eurotn.panjea.beniammortizzabili.util.VenditaBeneImporto;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.OperazioniBeneAmmortizzabileManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * Utilizzata per calcolare i valori di ammortamento del Bene. I valori o sono quelli attuali (dall'ultimo
 * consolidamento) oppure quelli riferiti ad una simulazione
 * 
 * @author Adry
 * @version 1.0, 03/ott/07
 * 
 */
@Stateful(name = "Panjea.OperazioniBeneAmmortizzabileManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.OperazioniBeneAmmortizzabileMamanger")
public class OperazioniBeneAmmortizzabileManagerBean implements OperazioniBeneAmmortizzabileManager, Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(OperazioniBeneAmmortizzabileManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext sessionContext;

	private Simulazione simulazione;

	private Integer idBeneAmmortizzabile;

	private Map<Integer, BeneAmmortizzabileCalcoloAmmortamento> beneAmmortizzabileValori = null;

	/**
	 * carica dal lato di persistenza la somma degli importi di valutazione del bene e del fondo per tutti i
	 * {@link BeneAmmortizzabile} da consolidare.
	 * 
	 * @return valutazioni bene importo caricate
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<ValutazioneBeneImporto> getImportiValutazioniBeni() throws DAOException {
		logger.debug("--> Enter getSommaImportoValutazioniBeni");
		Query query = null;
		if (this.idBeneAmmortizzabile == null) {
			query = panjeaDAO.prepareNamedQuery("ValutazioneBene.caricaImportiValutazioni");
			query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
		} else {
			query = panjeaDAO.prepareNamedQuery("ValutazioneBene.caricaImportiValutazioniPerBene");
			query.setParameter("paramIdBeneAmmortizzabile", this.idBeneAmmortizzabile);
		}
		List<ValutazioneBeneImporto> list = panjeaDAO.getResultList(query);
		logger.debug("--> Exit getSommaImportoValutazioniBeni");
		return list;
	}

	/**
	 * recupera dal lato di persistenza l'importo storico di riferimento e l'importo di storno del fondo di ammortamento
	 * per tutti i {@link BeneAmmortizzabile} non consolidati.
	 * 
	 * @return vendite bene importo caricate
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<VenditaBeneImporto> getImportiVendite() throws DAOException {
		logger.debug("--> Enter getSommaImportoStoricoVendite");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer selectHql;
		StringBuffer whereHql;
		String groupHql;
		if (this.idBeneAmmortizzabile == null) {
			selectHql = new StringBuffer(
					" select new it.eurotn.panjea.beniammortizzabili.util.VenditaBeneImporto(b.id, sum( v.importoStornoValoreBene)  , sum( v.importoStornoFondoAmmortamento) ) from VenditaBene v inner join v.bene b ");
			whereHql = new StringBuffer(
					" where b.codiceAzienda = :paramCodiceAzienda and (b.datiCivilistici.ammortamentoInCorso = true or b.datiFiscali.ammortamentoInCorso = true) and b.beneDiProprieta = true and b.indAmmortamento = true and b.benePadre is null ");
			groupHql = " group by b.id ";
			paramMap.put("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
			if (simulazione != null) {
				whereHql.append(" and v.dataVendita <= :paramDataVendita ");
				paramMap.put("paramDataVendita", simulazione.getData());
			}
		} else {
			selectHql = new StringBuffer(
					" select new it.eurotn.panjea.beniammortizzabili.util.VenditaBeneImporto(b.id, sum( v.importoStornoValoreBene)  , sum( v.importoStornoFondoAmmortamento) ) from VenditaBene v inner join v.bene b ");
			whereHql = new StringBuffer(
					" where b.id = :paramIdBeneAmmortizzabile and (b.datiCivilistici.ammortamentoInCorso = true or b.datiFiscali.ammortamentoInCorso = true) and b.beneDiProprieta = true and b.indAmmortamento = true and b.benePadre is null ");
			groupHql = " group by b.id ";
			paramMap.put("paramIdBeneAmmortizzabile", this.idBeneAmmortizzabile);
			if (simulazione != null) {
				whereHql.append(" and v.dataVendita <= :paramDataVendita ");
				paramMap.put("paramDataVendita", simulazione.getData());
			}
		}
		Query query = panjeaDAO.prepareQuery(selectHql.append(whereHql.toString()).append(groupHql).toString());
		for (String paramName : paramMap.keySet()) {
			query.setParameter(paramName, paramMap.get(paramName));
		}
		List<VenditaBeneImporto> list = panjeaDAO.getResultList(query);
		logger.debug("--> Exit getSommaImportoStoricoVendite");
		return list;
	}

	/**
	 * Restituisce l'utente loggato.
	 * 
	 * @return utente loggato
	 */
	private JecPrincipal getPrincipal() {
		JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
		return jecPrincipal;
	}

	/**
	 * Carica la quota ammortamento della simulazione collegata.
	 * 
	 * @param paramSimulazione
	 *            simulazione di riferimento
	 * @param quotaAmmortamentoFiscaleImporto
	 *            importo quota fiscale
	 * @return quota caricata
	 */
	private QuotaAmmortamentoFiscaleImporto getQuotaAmmortamentoSimulazioniCollegate(Simulazione paramSimulazione,
			QuotaAmmortamentoFiscaleImporto quotaAmmortamentoFiscaleImporto) {
		logger.debug("--> Enter getQuotaAmmortamentoSimulazioniCollegate");
		if (paramSimulazione == null) {
			return quotaAmmortamentoFiscaleImporto;
		}
		Query query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloBene.caricaImportoAnticipatoSimulazioniCollegate");
		query.setParameter("paramIdSimulazione", paramSimulazione.getId());
		query.setParameter("paramIdBeneAmmortizzabile", idBeneAmmortizzabile);
		QuotaAmmortamentoFiscaleImporto quotaAmmortamentoFiscaleImporto2 = (QuotaAmmortamentoFiscaleImporto) query
				.getSingleResult();
		if (quotaAmmortamentoFiscaleImporto == null) {
			quotaAmmortamentoFiscaleImporto = new QuotaAmmortamentoFiscaleImporto();
		}
		quotaAmmortamentoFiscaleImporto.setIdBeneAmmortizzabile(quotaAmmortamentoFiscaleImporto2
				.getIdBeneAmmortizzabile());
		quotaAmmortamentoFiscaleImporto.setImpQuotaAmmortamentoAnticipato(quotaAmmortamentoFiscaleImporto
				.getImpQuotaAmmortamentoAnticipato().add(
						quotaAmmortamentoFiscaleImporto2.getImpQuotaAmmortamentoAnticipato()));
		quotaAmmortamentoFiscaleImporto.setImpQuotaAmmortamentoOrdinario(quotaAmmortamentoFiscaleImporto
				.getImpQuotaAmmortamentoOrdinario().add(
						quotaAmmortamentoFiscaleImporto2.getImpQuotaAmmortamentoOrdinario()));
		if (paramSimulazione.getSimulazioneRiferimento() != null) {
			quotaAmmortamentoFiscaleImporto = getQuotaAmmortamentoSimulazioniCollegate(
					paramSimulazione.getSimulazioneRiferimento(), quotaAmmortamentoFiscaleImporto);
		}
		logger.debug("--> Exit getQuotaAmmortamentoImportoOrdinarioSimulazioniCollegate");
		return quotaAmmortamentoFiscaleImporto;
	}

	/**
	 * recupera dal lato di persistenza il totale dell'importo ordinario e anticipato delle
	 * {@link QuotaAmmortamentoFiscale} consolidate.
	 * 
	 * @return totale importo ordinario e anticipato
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<QuotaAmmortamentoFiscaleImporto> getQuoteAmmortamentoImportiFiscali() throws DAOException {
		logger.debug("--> Enter getQuoteAmmortamentoImportiFiscali");
		Query query;
		if (this.idBeneAmmortizzabile == null) {
			query = panjeaDAO.prepareNamedQuery("QuotaAmmortamentoFiscale.caricaImportiFiscaliConsolidati");
			query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
		} else {
			query = panjeaDAO.prepareNamedQuery("QuotaAmmortamentoFiscale.caricaImportiFiscaliConsolidatiPerBene");
			query.setParameter("paramIdBeneAmmortizzabile", this.idBeneAmmortizzabile);
		}
		List<QuotaAmmortamentoFiscaleImporto> list = panjeaDAO.getResultList(query);
		logger.debug("--> Exit getQuoteAmmortamentoImportiFiscali");
		return list;
	}

	/*
	 * 
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces. OperazioniBeneAmmortizzabileManager#
	 * getTotaleAmmortamentoFiscale(java.lang.Integer)
	 */
	@Override
	public BigDecimal getTotaleAmmortamentoFiscale(Integer paramIdBeneAmmortizzabile) {
		if (beneAmmortizzabileValori.containsKey(paramIdBeneAmmortizzabile)) {
			BeneAmmortizzabileCalcoloAmmortamento beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori
					.get(paramIdBeneAmmortizzabile);
			QuotaAmmortamentoFiscaleImporto quotaAmmortamentoFiscaleImporto = null;
			if (!paramIdBeneAmmortizzabile.equals(this.idBeneAmmortizzabile)) {
				// determina la quota di ammortamento per le simulazione
				// collegate e aggiorna l'oggetto
				// BeneAmmortizzabileCalcoloArrotondamento
				// se varia il bene ammortizzabile di riferimento
				this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
				quotaAmmortamentoFiscaleImporto = getQuotaAmmortamentoSimulazioniCollegate(
						simulazione.getSimulazioneRiferimento(), null);
			}
			if (quotaAmmortamentoFiscaleImporto != null) {
				beneAmmortizzabileCalcoloAmmortamento.setImportoAnticipatoFiscale(beneAmmortizzabileCalcoloAmmortamento
						.getImportoAnticipatoFiscale().add(
								quotaAmmortamentoFiscaleImporto.getImpQuotaAmmortamentoAnticipato()));
				beneAmmortizzabileCalcoloAmmortamento.setImportoOrdinarioFiscale(beneAmmortizzabileCalcoloAmmortamento
						.getImportoOrdinarioFiscale().add(
								quotaAmmortamentoFiscaleImporto.getImpQuotaAmmortamentoOrdinario()));
			}
			return beneAmmortizzabileCalcoloAmmortamento.getTotaleAmmortamentoFiscale();
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getTotaleAmmortizzato(Integer paramIdBeneAmmortizzabile) {
		if (beneAmmortizzabileValori.containsKey(paramIdBeneAmmortizzabile)) {
			return (beneAmmortizzabileValori.get(paramIdBeneAmmortizzabile)).getTotaleAmmortamentoFiscale();
		}
		return BigDecimal.ZERO;
	}

	/*
	 * 
	 * @see it.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * OperazioniBeneAmmortizzabileManager#getValoreBene(java .lang.Integer)
	 */
	@Override
	public BigDecimal getValoreBene(Integer paramIdBeneAmmortizzabile) {
		if (beneAmmortizzabileValori.containsKey(paramIdBeneAmmortizzabile)) {
			return (beneAmmortizzabileValori.get(paramIdBeneAmmortizzabile)).getValoreBene();
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getValoreFondo(Integer paramIdBeneAmmortizzabile) {
		if (beneAmmortizzabileValori.containsKey(paramIdBeneAmmortizzabile)) {
			BeneAmmortizzabileCalcoloAmmortamento beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori
					.get(paramIdBeneAmmortizzabile);
			QuotaAmmortamentoFiscaleImporto quotaAmmortamentoFiscaleImporto = null;
			if (!paramIdBeneAmmortizzabile.equals(this.idBeneAmmortizzabile)) {
				// determina la quota di ammortamento per le simulazione
				// collegate e aggiorna l'oggetto
				// BeneAmmortizzabileCalcoloArrotondamento
				// se varia il bene ammortizzabile di riferimento
				this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
				quotaAmmortamentoFiscaleImporto = getQuotaAmmortamentoSimulazioniCollegate(
						simulazione.getSimulazioneRiferimento(), null);
			}
			if (quotaAmmortamentoFiscaleImporto != null) {
				beneAmmortizzabileCalcoloAmmortamento.setImportoAnticipatoFiscale(beneAmmortizzabileCalcoloAmmortamento
						.getImportoAnticipatoFiscale().add(
								quotaAmmortamentoFiscaleImporto.getImpQuotaAmmortamentoAnticipato()));
				beneAmmortizzabileCalcoloAmmortamento.setImportoOrdinarioFiscale(beneAmmortizzabileCalcoloAmmortamento
						.getImportoOrdinarioFiscale().add(
								quotaAmmortamentoFiscaleImporto.getImpQuotaAmmortamentoOrdinario()));
			}
			return beneAmmortizzabileCalcoloAmmortamento.getValoreFondo();
		}
		return BigDecimal.ZERO;
	}

	/**
	 * Carica dal lato di persistenza l'importo soggetto ad ammortamento per tutti i {@link BeneAmmortizzabile} non
	 * ancora consolidati.
	 * 
	 * @param anno
	 *            anno di riferimento
	 * @return valori bene
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<BeneImportoSoggettoAdAmmortamento> getValoriBeniAmmortizzabili(Integer anno) throws DAOException {
		logger.debug("--> Enter getValoriBeniAmmortizzabili");
		Query query = null;
		if (this.idBeneAmmortizzabile == null) {
			query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamento");
			query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
			query.setParameter("paramAnnoAcquisto", anno);
		} else {
			query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamentoPerBene");
			query.setParameter("paramIdBeneAmmortizzabile", this.idBeneAmmortizzabile);
			query.setParameter("paramAnnoAcquisto", anno);
		}
		List<BeneImportoSoggettoAdAmmortamento> list = panjeaDAO.getResultList(query);
		logger.debug("--> Exit getValoriBeniAmmortizzabili list# " + list.size());
		return list;

	}

	/**
	 * Carica dal lato di persistenza l'importo soggetto ad ammortamento dei beni figli dei {@link BeneAmmortizzabile}
	 * non consolidati.
	 * 
	 * @param anno
	 *            anno di riferimento
	 * @return valori beni
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<BeneImportoSoggettoAdAmmortamento> getValoriBeniAmmortizzabiliFigli(Integer anno) throws DAOException {
		logger.debug("--> Enter getValoriBeniAmmortizzabiliFigli");
		Query query = null;
		if (this.idBeneAmmortizzabile == null) {
			query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamentoFigli");
			query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
			query.setParameter("paramAnnoAcquisto", anno);
		} else {
			query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaValoriSoggettiAdAmmortamentoFigliPerBene");
			query.setParameter("paramIdBeneAmmortizzabile", this.idBeneAmmortizzabile);
			query.setParameter("paramAnnoAcquisto", anno);
		}
		List<BeneImportoSoggettoAdAmmortamento> list = panjeaDAO.getResultList(query);
		logger.debug("--> Exit getValoriBeniAmmortizzabiliFigli");
		return list;
	}

	/*
	 * @see it.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * OperazioniBeneAmmortizzabileManager#initialize(it.eurotn .panjea.beniammortizzabili2.domain.Simulazione)
	 */
	@Override
	public void initialize(Simulazione paramSimulazione) {
		logger.debug("--> Enter initialize");
		initialize(paramSimulazione, null);
		logger.debug("--> Exit initialize");
	}

	/*
	 * @see it.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * OperazioniBeneAmmortizzabileManager#initializa(it.eurotn .panjea.beniammortizzabili2.domain.Simulazione,
	 * java.lang.Integer)
	 */
	@Override
	public void initialize(Simulazione paramSimulazione, Integer paramIdBeneAmmortizzabile) {
		logger.debug("--> Enter initializa");
		this.simulazione = paramSimulazione;
		this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
		Calendar cal = Calendar.getInstance();
		if (paramSimulazione != null) {
			cal.setTime(paramSimulazione.getData());
		}
		int anno = cal.get(Calendar.YEAR);
		try {
			initializeBeneAmmortizzabileValori(anno);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile caricare i valori necessari al calcolo dell'ammortamento ", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Inizializza i valori dei beni.
	 * 
	 * @param anno
	 *            anno di riferimento
	 * 
	 * @throws DAOException
	 *             exception
	 */
	private void initializeBeneAmmortizzabileValori(Integer anno) throws DAOException {
		logger.debug("--> Enter initializeBeneAmmortizzabileValori");
		beneAmmortizzabileValori = new HashMap<Integer, BeneAmmortizzabileCalcoloAmmortamento>();
		BeneAmmortizzabileCalcoloAmmortamento beneAmmortizzabileCalcoloAmmortamento;
		List<BeneImportoSoggettoAdAmmortamento> list = getValoriBeniAmmortizzabili(anno);
		for (BeneImportoSoggettoAdAmmortamento beneImportoSoggettoAdAmmortamento : list) {
			beneAmmortizzabileCalcoloAmmortamento = new BeneAmmortizzabileCalcoloAmmortamento();
			beneAmmortizzabileCalcoloAmmortamento.setIdBeneAmmortizzabile(beneImportoSoggettoAdAmmortamento
					.getIdBeneAmmortizzabile());
			beneAmmortizzabileCalcoloAmmortamento.setImportoSoggettoAmmortamento(beneImportoSoggettoAdAmmortamento
					.getImportoSoggettoAdAmmortamento());
			beneAmmortizzabileValori.put(beneAmmortizzabileCalcoloAmmortamento.getIdBeneAmmortizzabile(),
					beneAmmortizzabileCalcoloAmmortamento);
		}
		List<BeneImportoSoggettoAdAmmortamento> listFigli = getValoriBeniAmmortizzabiliFigli(anno);
		for (BeneImportoSoggettoAdAmmortamento beneImportoSoggettoAdAmmortamento2 : listFigli) {
			if (beneAmmortizzabileValori.containsKey(beneImportoSoggettoAdAmmortamento2.getIdBeneAmmortizzabile())) {
				beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori.get(beneImportoSoggettoAdAmmortamento2
						.getIdBeneAmmortizzabile());
				// beneAmmortizzabileCalcoloAmmortamento
				// .setImportoSoggettoAmmortamento(beneAmmortizzabileCalcoloAmmortamento
				// .getImportoSoggettoAmmortamento().add(
				// beneImportoSoggettoAdAmmortamento2.getImportoSoggettoAdAmmortamento()));
				beneAmmortizzabileCalcoloAmmortamento
						.setImportoSoggettoAmmortamentoFigli(beneAmmortizzabileCalcoloAmmortamento
								.getImportoSoggettoAmmortamentoFigli().add(
										beneImportoSoggettoAdAmmortamento2.getImportoSoggettoAdAmmortamento()));

			}
		}
		List<ValutazioneBeneImporto> list2 = getImportiValutazioniBeni();
		for (ValutazioneBeneImporto valutazioneBeneImporto : list2) {
			if (beneAmmortizzabileValori.containsKey(valutazioneBeneImporto.getIdBeneAmmortizzabile())) {
				beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori.get(valutazioneBeneImporto
						.getIdBeneAmmortizzabile());
				beneAmmortizzabileCalcoloAmmortamento.setImportoValutazioniBene(valutazioneBeneImporto
						.getImportoValutazioneBene());
				beneAmmortizzabileCalcoloAmmortamento.setImportoValutazioniFondo(valutazioneBeneImporto
						.getImportoValutazioneFondo());
				beneAmmortizzabileValori.put(beneAmmortizzabileCalcoloAmmortamento.getIdBeneAmmortizzabile(),
						beneAmmortizzabileCalcoloAmmortamento);
			}
		}
		List<VenditaBeneImporto> list3 = getImportiVendite();
		for (VenditaBeneImporto venditaBeneImporto : list3) {
			if (beneAmmortizzabileValori.containsKey(venditaBeneImporto.getIdBeneAmmortizzabile())) {
				beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori.get(venditaBeneImporto
						.getIdBeneAmmortizzabile());
				beneAmmortizzabileCalcoloAmmortamento.setImportoStoricoVendite(venditaBeneImporto
						.getImportoStornoValoreBene());
				beneAmmortizzabileCalcoloAmmortamento.setImportoStornoFondoVendite(venditaBeneImporto
						.getImportoStornoFondoAmmortamento());
				beneAmmortizzabileValori.put(beneAmmortizzabileCalcoloAmmortamento.getIdBeneAmmortizzabile(),
						beneAmmortizzabileCalcoloAmmortamento);
			}
		}
		List<QuotaAmmortamentoFiscaleImporto> list4 = getQuoteAmmortamentoImportiFiscali();
		for (QuotaAmmortamentoFiscaleImporto quotaAmmortamentoFiscaleImporto : list4) {
			if (beneAmmortizzabileValori.containsKey(quotaAmmortamentoFiscaleImporto.getIdBeneAmmortizzabile())) {
				beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori.get(quotaAmmortamentoFiscaleImporto
						.getIdBeneAmmortizzabile());
				beneAmmortizzabileCalcoloAmmortamento.setImportoAnticipatoFiscale(quotaAmmortamentoFiscaleImporto
						.getImpQuotaAmmortamentoAnticipato());
				beneAmmortizzabileCalcoloAmmortamento.setImportoOrdinarioFiscale(quotaAmmortamentoFiscaleImporto
						.getImpQuotaAmmortamentoOrdinario());
				beneAmmortizzabileValori.put(beneAmmortizzabileCalcoloAmmortamento.getIdBeneAmmortizzabile(),
						beneAmmortizzabileCalcoloAmmortamento);
			}
		}
		beneAmmortizzabileCalcoloAmmortamento = beneAmmortizzabileValori.get(663);
		logger.debug("--> Exit initializeBeneAmmortizzabileValori");
	}

}
