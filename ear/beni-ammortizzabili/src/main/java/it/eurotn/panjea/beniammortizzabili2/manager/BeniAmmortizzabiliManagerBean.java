package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.codice.generator.interfaces.LastCodiceGenerator;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.beniammortizzabili.util.MessageBeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.DatiCivilistici;
import it.eurotn.panjea.beniammortizzabili2.domain.DatiFiscali;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamento;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoCivilistico;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.OperazioniBeneAmmortizzabileManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.BeniAmmortizzabiliManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.BeniAmmortizzabiliManager")
public class BeniAmmortizzabiliManagerBean implements BeniAmmortizzabiliManager {

	private static Logger logger = Logger.getLogger(BeniAmmortizzabiliManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected OperazioniBeneAmmortizzabileManager operazioniBeneAmmortizzabileManager;

	@Resource(mappedName = "queue/VenditaBeneAmmortizzabile")
	private Queue queue;

	@Resource(mappedName = "ConnectionFactory")
	private ConnectionFactory jmsConnectionFactory;

	@Resource
	protected SessionContext sessionContext;

	@EJB
	private LastCodiceGenerator lastCodiceGenerator;

	@Override
	public void cancellaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) throws VincoloException {
		logger.debug("--> Enter cancellaBeneAmmortizzabile");

		List<QuotaAmmortamentoFiscale> quoteAmmortamentoFiscaliConsolidate = caricaQuoteAmmortamentoFiscali(
				beneAmmortizzabile, true);
		if (quoteAmmortamentoFiscaliConsolidate.size() > 0) {
			logger.warn("--> Non posso cancellare il bene perch� ci sono delle quote fiscali consolidate.");
			throw new VincoloException("Non posso cancellare il bene perchè ci sono delle quote fiscali consolidate.");
		}

		List<BeneAmmortizzabile> beniFigli = caricaBeniAmmortizzabiliFigli(beneAmmortizzabile);
		if ((beniFigli != null) && (beniFigli.size() > 0)) {
			logger.warn("--> Non posso cancellare il bene perch� ci sono dei figli collegati.");
			throw new VincoloException("Non posso cancellare il bene perchè ci sono dei figli collegati.");
		}

		try {
			panjeaDAO.delete(beneAmmortizzabile);
			logger.debug("--> Cancellato il bene ammortizzabile " + beneAmmortizzabile.getId());
		} catch (DAOException e) {
			logger.error("--> Errore durante la cancellazione del bene ammortizzabile " + beneAmmortizzabile.getId(), e);
			throw new RuntimeException("Errore durante la cancellazione del bene ammortizzabile "
					+ beneAmmortizzabile.getId());
		}
		logger.debug("--> Exit cancellaBeneAmmortizzabile");
	}

	@Override
	public void cancellaValutazioneBene(ValutazioneBene valutazioneBene) {
		logger.debug("--> Enter cancellaValutazioneBene");
		try {
			panjeaDAO.delete(valutazioneBene);
			logger.debug("--> Eliminata la valutazione bene " + valutazioneBene.getId());
		} catch (DAOException e) {
			logger.error("--> Errore durante la cancellazione della valutazione bene " + valutazioneBene, e);
			throw new RuntimeException("Errore durante la cancellazione della valutazione bene " + valutazioneBene, e);
		}
		logger.debug("--> Exit cancellaValutazioneBene");
	}

	@Override
	public void cancellaVenditaBene(VenditaBene venditaBene) {
		logger.debug("--> Enter cancellaVenditaBene");
		try {
			panjeaDAO.delete(venditaBene);
			logger.error("--> Cancellata la sottospecie " + venditaBene.getId());
		} catch (DAOException e) {
			logger.error("--> Errore durante la cancellazione della vendita: " + venditaBene, e);
			throw new RuntimeException("Errore durante la cancellazione della vendita: " + venditaBene, e);
		}
		logger.debug("--> Exit cancellaVenditaBene");
	}

	@Override
	public BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		logger.debug("--> Enter caricaBeneAammortizzabile");
		BeneAmmortizzabile beneAmmortizzabile2 = null;
		try {
			beneAmmortizzabile2 = panjeaDAO.load(BeneAmmortizzabile.class, beneAmmortizzabile.getId());
			logger.debug("--> Caricato il beneAmmortizzabile " + beneAmmortizzabile.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore durante il caricamento del beneAmortizzabile identificato da "
					+ beneAmmortizzabile.getId(), e);
			throw new RuntimeException("Errore durante il caricamento del beneAmortizzabile identitificato da "
					+ beneAmmortizzabile.getId(), e);
		}
		logger.debug("--> Exit caricaBeneAmmortizzabile");
		return beneAmmortizzabile2;
	}

	@Override
	public BeneAmmortizzabile caricaBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabileLite) {
		logger.debug("--> Enter caricaBeneAammortizzabile");
		BeneAmmortizzabile beneAmmortizzabile2 = null;
		try {
			beneAmmortizzabile2 = panjeaDAO.load(BeneAmmortizzabile.class, beneAmmortizzabileLite.getId());
			logger.debug("--> Caricato il beneAmmortizzabile " + beneAmmortizzabileLite.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore durante il caricamento del beneAmortizzabile identificato da "
					+ beneAmmortizzabileLite.getId(), e);
			throw new RuntimeException("Errore durante il caricamento del beneAmortizzabile identitificato da "
					+ beneAmmortizzabileLite.getId(), e);
		}
		logger.debug("--> Exit caricaBeneAmmortizzabile");
		return beneAmmortizzabile2;
	}

	@SuppressWarnings("unchecked")
	private List<BeneAmmortizzabileLite> caricaBeneAmmortizzabileByCodice(Integer codice) {
		logger.debug("--> Enter caricaBeneAmmortizzabileByCodice");
		List<BeneAmmortizzabileLite> beni = new ArrayList<BeneAmmortizzabileLite>();

		Query query = panjeaDAO.prepareQuery("select b from BeneAmmortizzabileLite b where b.codice = :paramCodice");
		query.setParameter("paramCodice", codice);

		try {
			beni = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento dei beni con codice " + codice, e);
			throw new RuntimeException("errore durante il caricamento dei beni con codice " + codice, e);
		}

		logger.debug("--> Exit caricaBeneAmmortizzabileByCodice");
		return beni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabile> caricaBeniAmmortizzabiliAzienda() {
		logger.debug("--> Enter caricaBeniAmmortizzabiliAzienda");
		List<BeneAmmortizzabile> beniCaricati = new ArrayList<BeneAmmortizzabile>();
		try {
			Query query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaAll");
			query.setParameter("paramCodiceAzienda", getAzienda());
			beniCaricati = panjeaDAO.getResultList(query);
			logger.debug("--> Caricati " + beniCaricati.size() + " beni ammortizzabili azienda.");
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei beni ammortizzabili azienda.", e);
			throw new RuntimeException("Errore durante il caricamento dei beni ammortizzabili azienda.");
		}
		logger.debug("--> Exit caricaBeniAmmortizzabiliAzienda");
		return beniCaricati;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabile> caricaBeniAmmortizzabiliFigli(BeneAmmortizzabile beneAmmortizzabilePadre) {
		logger.debug("--> Enter caricaBeniAmmortizzabiliFigli");

		try {
			List<BeneAmmortizzabile> beniCaricati = new ArrayList<BeneAmmortizzabile>();
			Query query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaBeniFigli");
			query.setParameter("paramIdBenePadre", beneAmmortizzabilePadre.getId());
			query.setParameter("paramCodiceAzienda", getAzienda());
			beniCaricati = panjeaDAO.getResultList(query);
			logger.debug("--> Caricati " + beniCaricati.size() + " beni ammortizzabili figli.");
			logger.debug("--> Exit caricaBeniAmmortizzabiliFigli");
			return beniCaricati;
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei beni ammortizzabili figli.", e);
			throw new RuntimeException("Errore durante il caricamento dei beni ammortizzabili figli.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabileLite> caricaBeniDaAmmortizzareLite(Date date) {
		logger.debug("--> Enter caricaBeniDaAmmortizzareLite");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		try {
			Query query = panjeaDAO.prepareNamedQuery("BeneAmmortizzabile.caricaBeniDaAmmortizzareLite");
			query.setParameter("paramCodiceAzienda", getAzienda());
			query.setParameter("paramAnno", calendar.get(Calendar.YEAR));
			query.setParameter("paramData", calendar.getTime());
			List<BeneAmmortizzabileLite> beniDaAmmortizzareLite = query.getResultList();
			logger.debug("--> Exit caricaBeniDaAmmortizzareLite trovati# " + beniDaAmmortizzareLite.size());
			return beniDaAmmortizzareLite;
		} catch (Exception e) {
			logger.error("--> errore in caricaBeniDaAmmortizzareLite", e);
			throw new RuntimeException("Errore in caricaBeniDaAmmortizzareLite", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuotaAmmortamentoCivilistico> caricaQuoteAmmortamentoCivilistiche(
			BeneAmmortizzabile beneAmmortizzabile, boolean isConsolidate) {
		logger.debug("--> Enter caricaQuoteAmmortamentoCivilistiche");
		logger.debug("--> Ricerca delle quote di ammortamento civilistiche consolidate = " + isConsolidate);
		List<QuotaAmmortamentoCivilistico> listQuote = new ArrayList<QuotaAmmortamentoCivilistico>();
		org.hibernate.Session hibernateSession = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
		Criteria criteria = hibernateSession.createCriteria(QuotaAmmortamentoCivilistico.class);
		listQuote = criteria.add(Restrictions.eq("beneAmmortizzabile.id", beneAmmortizzabile.getId()))
				.add(Restrictions.eq(QuotaAmmortamento.PROP_CONSOLIDATA, isConsolidate)).list();
		logger.debug("--> Caricate " + listQuote + " quote di ammortamento civilistiche per il bene "
				+ beneAmmortizzabile.getId());
		logger.debug("--> Exit caricaQuoteAmmortamentoCivilistiche");
		return listQuote;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuotaAmmortamentoFiscale> caricaQuoteAmmortamentoFiscali(BeneAmmortizzabile beneAmmortizzabile,
			boolean isConsolidate) {
		logger.debug("--> Enter caricaQuoteAmmortamentoFiscali");
		logger.debug("--> Ricerca delle quote di ammortamento Fiscali consolidate = " + isConsolidate);
		List<QuotaAmmortamentoFiscale> listQuote = new ArrayList<QuotaAmmortamentoFiscale>();
		org.hibernate.Session hibernateSession = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
		Criteria criteria = hibernateSession.createCriteria(QuotaAmmortamentoFiscale.class);
		listQuote = criteria.add(Restrictions.eq("beneAmmortizzabile.id", beneAmmortizzabile.getId()))
				.add(Restrictions.eq(QuotaAmmortamento.PROP_CONSOLIDATA, isConsolidate)).list();
		logger.debug("--> Caricate " + listQuote + " quote di ammortamento Fiscali per il bene "
				+ beneAmmortizzabile.getId());
		logger.debug("--> Exit caricaQuoteAmmortamentoFiscali");
		return listQuote;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValutazioneBene> caricaValutazioniBene(BeneAmmortizzabile beneAmmortizzabile) {
		logger.debug("--> Enter caricaValutazioniBene");
		List<ValutazioneBene> valutazioni = new ArrayList<ValutazioneBene>();
		try {
			Query query = panjeaDAO.prepareNamedQuery("ValutazioneBene.caricaByBene");
			query.setParameter("paramIdBene", beneAmmortizzabile.getId());
			valutazioni = panjeaDAO.getResultList(query);
			logger.debug("--> Caricate " + valutazioni.size() + " valutazioni per il bene ammortizzabile "
					+ beneAmmortizzabile.getId());
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle valutazioni per il bene ammortizzabile "
					+ beneAmmortizzabile.getId(), e);
			throw new RuntimeException("Errore durante il caricamento delle valutazioni per il bene ammortizzabile "
					+ beneAmmortizzabile.getId());
		}
		logger.debug("--> Exit caricaValutazioniBene");
		return valutazioni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VenditaBene> caricaVenditeBene(BeneAmmortizzabile beneAmmortizzabile) {
		logger.debug("--> Enter caricaVenditeBene");
		List<VenditaBene> vendite = new ArrayList<VenditaBene>();
		try {
			Query query = panjeaDAO.prepareNamedQuery("VenditaBene.caricaByBene");
			query.setParameter("paramIdBene", beneAmmortizzabile.getId());
			vendite = panjeaDAO.getResultList(query);
			logger.debug("--> Caricate " + vendite.size() + " vendite per il bene ammortizzabile "
					+ beneAmmortizzabile.getId());
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento delle valutazioni per il bene ammortizzabile "
					+ beneAmmortizzabile.getId(), e);
			throw new RuntimeException("Errore durante il caricamento delle valutazioni per il bene ammortizzabile "
					+ beneAmmortizzabile.getId());
		}
		logger.debug("--> Exit caricaVenditeBene");
		return vendite;
	}

	/**
	 * Confronta due beni ammortizzabili.
	 *
	 * @param beneAmmortizzabile0
	 *            primo bene
	 * @param beneAmmortizzabile1
	 *            secondo bene
	 * @return 0 se uguali
	 */
	private int compareBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile0, BeneAmmortizzabile beneAmmortizzabile1) {
		if (beneAmmortizzabile0.getSottoSpecie().getSpecie().getCodice()
				.equals(beneAmmortizzabile1.getSottoSpecie().getSpecie().getCodice())) {
			if (beneAmmortizzabile0.getSottoSpecie().getCodice()
					.equals(beneAmmortizzabile1.getSottoSpecie().getCodice())) {
				if (beneAmmortizzabile0.getCodice().equals(beneAmmortizzabile1.getCodice())) {
					/* per corrispondenza di codice verifica anno */
					return beneAmmortizzabile0.getAnnoAcquisto().compareTo(beneAmmortizzabile1.getAnnoAcquisto());
				}
				return beneAmmortizzabile0.getCodice().compareTo(beneAmmortizzabile1.getCodice());
			} else {
				return beneAmmortizzabile0.getSottoSpecie().getCodice()
						.compareTo(beneAmmortizzabile1.getSottoSpecie().getCodice());
			}
		} else {
			return beneAmmortizzabile0.getSottoSpecie().getSpecie().getCodice()
					.compareTo(beneAmmortizzabile1.getSottoSpecie().getSpecie().getCodice());
		}
	}

	/**
	 * Confronta due beni ammortizzabili.
	 *
	 * @param beneAmmortizzabileLite0
	 *            primo bene
	 * @param beneAmmortizzabileLite1
	 *            secondo bene
	 * @return 0 se uguali
	 */
	private int compareBeneAmmortizzabileLite(BeneAmmortizzabileLite beneAmmortizzabileLite0,
			BeneAmmortizzabileLite beneAmmortizzabileLite1) {
		if (beneAmmortizzabileLite0.getSottoSpecie().getSpecie().getCodice()
				.equals(beneAmmortizzabileLite1.getSottoSpecie().getSpecie().getCodice())) {
			if (beneAmmortizzabileLite0.getSottoSpecie().getCodice()
					.equals(beneAmmortizzabileLite1.getSottoSpecie().getCodice())) {
				if (beneAmmortizzabileLite0.getCodice().equals(beneAmmortizzabileLite1.getCodice())) {
					/* per corrispondenza di codice verifica anno */
					return beneAmmortizzabileLite0.getAnnoAcquisto().compareTo(
							beneAmmortizzabileLite1.getAnnoAcquisto());
				}
				return beneAmmortizzabileLite0.getCodice().compareTo(beneAmmortizzabileLite1.getCodice());
			} else {
				return beneAmmortizzabileLite0.getSottoSpecie().getCodice()
						.compareTo(beneAmmortizzabileLite1.getSottoSpecie().getCodice());
			}
		} else {
			return beneAmmortizzabileLite0.getSottoSpecie().getSpecie().getCodice()
					.compareTo(beneAmmortizzabileLite1.getSottoSpecie().getSpecie().getCodice());
		}

	}

	@Override
	public ValutazioneBene creaNuovaValutazioneBene(BeneAmmortizzabile beneAmmortizzabile) {
		logger.debug("--> Enter creaNuovaValutazioneBene");

		ValutazioneBene valutazioneBene = new ValutazioneBene();
		operazioniBeneAmmortizzabileManager.initialize(null, beneAmmortizzabile.getId());
		valutazioneBene.setBene(beneAmmortizzabile);

		logger.debug("--> Exit creaNuovaValutazioneBene");
		return valutazioneBene;
	}

	/**
	 *
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            criteria
	 * @return criteria
	 * @throws DAOException
	 *             DAOException
	 */
	private Criteria createCriteriaRicercaBeneAmmortizzabile(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) throws DAOException {
		logger.debug("--> Enter createCriteriaRicercaBeneAmmortizzabile");

		org.hibernate.Session hibernateSession = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
		Criteria criteria = hibernateSession.createCriteria(BeneAmmortizzabile.class);
		criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_CODICE_AZIENDA, getAzienda()));
		if (criteriaRicercaBeniAmmortizzabili.getAnno() != null) {
			logger.debug("--> aggiunta Restriction anno " + criteriaRicercaBeniAmmortizzabili.getAnno());
			criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_ANNO_ACQUISTO,
					criteriaRicercaBeniAmmortizzabili.getAnno()));
		}
		if ((criteriaRicercaBeniAmmortizzabili.getUbicazione() != null)
				&& (criteriaRicercaBeniAmmortizzabili.getUbicazione().getId() != null)) {
			logger.debug("--> aggiunta Restriction ubicazione " + criteriaRicercaBeniAmmortizzabili.getUbicazione());
			criteria.createAlias(BeneAmmortizzabile.PROP_UBICAZIONE, BeneAmmortizzabile.PROP_UBICAZIONE);
			criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_UBICAZIONE + ".id", criteriaRicercaBeniAmmortizzabili
					.getUbicazione().getId()));
		}
		if ((criteriaRicercaBeniAmmortizzabili.getFornitore() != null)
				&& (criteriaRicercaBeniAmmortizzabili.getFornitore().getId() != null)) {
			logger.debug("--> aggiunta Restriction fornitore " + criteriaRicercaBeniAmmortizzabili.getFornitore());
			criteria.createAlias(BeneAmmortizzabile.PROP_FORNITORE, BeneAmmortizzabile.PROP_FORNITORE);
			criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_FORNITORE + ".id", criteriaRicercaBeniAmmortizzabili
					.getFornitore().getId()));
		}

		if (criteriaRicercaBeniAmmortizzabili.isAmmortamentoInCorso()) {
			logger.debug("--> aggiunta restriction ammortamento in corso "
					+ criteriaRicercaBeniAmmortizzabili.isAmmortamentoInCorso());
			Criterion datiCivilistici = Restrictions.eq(
					"datiCivilistici." + DatiCivilistici.PROP_AMMORTAMENTO_IN_CORSO,
					criteriaRicercaBeniAmmortizzabili.isAmmortamentoInCorso());
			Criterion datiFiscali = Restrictions.eq("datiFiscali." + DatiFiscali.PROP_AMMORTAMENTO_IN_CORSO,
					criteriaRicercaBeniAmmortizzabili.isAmmortamentoInCorso());
			criteria.add(Restrictions.or(datiCivilistici, datiFiscali));

		}

		// se non selezionato il filtro bene eliminati filtro per i beni con
		// attributo stato uguale a 0
		if (!criteriaRicercaBeniAmmortizzabili.isBeniEliminati()) {
			logger.debug("--> aggiunta restriction beni eliminati "
					+ criteriaRicercaBeniAmmortizzabili.isBeniEliminati());
			criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_ELIMINATO, false));
		}
		if (criteriaRicercaBeniAmmortizzabili.isIntervalloSpecie()) {
			criteria.createAlias(BeneAmmortizzabile.PROP_SOTTO_SPECIE, "sottoSpecie");
			criteria.createAlias("sottoSpecie." + SottoSpecie.PROP_SPECIE, "specie");
			if ((criteriaRicercaBeniAmmortizzabili.getSpecieIniziale() != null)
					&& (criteriaRicercaBeniAmmortizzabili.getSpecieIniziale().getId() != null)) {
				logger.debug("--> aggiunta Restricton Specie iniziale "
						+ criteriaRicercaBeniAmmortizzabili.getSpecieIniziale());
				if ((criteriaRicercaBeniAmmortizzabili.getSpecieFinale() != null)
						&& (criteriaRicercaBeniAmmortizzabili.getSpecieFinale().getId() != null)) {
					/* intervallo */
					criteria.add(Restrictions.ge("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
							.getSpecieIniziale().getCodice()));
					criteria.add(Restrictions.le("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
							.getSpecieFinale().getCodice()));
				} else {
					/* equals */
					criteria.add(Restrictions.eq("specie." + Specie.PROP_ID, criteriaRicercaBeniAmmortizzabili
							.getSpecieIniziale().getId()));
					if (criteriaRicercaBeniAmmortizzabili.isIntervalloSottoSpecie()) {
						/*
						 * verifica in base solo alla selezione della singola Specie contenuta in specieIniziale
						 */
						if ((criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale() != null)
								&& (criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale().getId() != null)) {
							logger.debug("--> aggiunta Restriction SottoSpecie Iniziale "
									+ criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale());
							criteria.add(Restrictions.ge("sottoSpecie." + SottoSpecie.PROP_CODICE,
									criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale().getCodice()));
						}
						if ((criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale() != null)
								&& (criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale().getId() != null)) {
							logger.debug("--> aggiunta Restriction SottoSpecie Finale "
									+ criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale());
							criteria.add(Restrictions.le("sottoSpecie." + SottoSpecie.PROP_CODICE,
									criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale().getCodice()));
						}
					}
				}
			} else if ((criteriaRicercaBeniAmmortizzabili.getSpecieFinale() != null)
					&& (criteriaRicercaBeniAmmortizzabili.getSpecieFinale().getId() != null)) {
				logger.debug("--> aggiugo Restricton Specie finale "
						+ criteriaRicercaBeniAmmortizzabili.getSpecieFinale());
				criteria.add(Restrictions.le("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
						.getSpecieFinale().getCodice()));
			}
		}
		return criteria;
	}

	@Override
	public VenditaBene creaVenditaBene(BeneAmmortizzabile beneAmmortizzabile) {
		logger.debug("--> Enter creaVenditaBene");

		VenditaBene venditaBene = new VenditaBene();
		operazioniBeneAmmortizzabileManager.initialize(null, beneAmmortizzabile.getId());

		venditaBene.setValoreBene(operazioniBeneAmmortizzabileManager.getValoreBene(beneAmmortizzabile.getId()));
		venditaBene.setValoreFondo(operazioniBeneAmmortizzabileManager.getValoreFondo(beneAmmortizzabile.getId()));
		venditaBene.setBene(beneAmmortizzabile);

		logger.debug("--> Exit creaVenditaBene");
		return venditaBene;
	}

	@Override
	public void fillDataBaseBeniAmmortamento() {

	}

	/**
	 * Restituisce il codice azienda dell'utente loggato.
	 *
	 * @return codcie azienda
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 * Restituisce un implementazione dell'interfaccia Comparator basata sui seguenti criteri: 1.Fornitore 2.Specie
	 * 3.SottoSpecie.
	 *
	 * @return istanza di Comparatore
	 */
	private Comparator<BeneAmmortizzabileConFigli> getComparatorPerFornitore() {
		return new Comparator<BeneAmmortizzabileConFigli>() {

			@Override
			public int compare(BeneAmmortizzabileConFigli arg0, BeneAmmortizzabileConFigli arg1) {
				/* controllo fornitore */
				if (arg0.getBeneAmmortizzabile().getFornitore() == null) {
					return -1;
				}
				if (arg1.getBeneAmmortizzabile().getFornitore() == null) {
					return -2;
				}
				if (arg0.getBeneAmmortizzabile().getFornitore().getCodice()
						.equals(arg1.getBeneAmmortizzabile().getFornitore().getCodice())) {
					/* controllo specie */
					return compareBeneAmmortizzabile(arg0.getBeneAmmortizzabile(), arg1.getBeneAmmortizzabile());
				} else {
					return arg0.getBeneAmmortizzabile().getFornitore().getCodice()
							.compareTo(arg1.getBeneAmmortizzabile().getFornitore().getCodice());
				}
			}

		};
	}

	/**
	 * Restituisce il compatator per le quote ammortamento fiscale.
	 *
	 * @return comparatore
	 */
	private Comparator<QuotaAmmortamentoFiscale> getComparatorPerQuotaAmmortamento() {
		return new Comparator<QuotaAmmortamentoFiscale>() {

			@Override
			public int compare(QuotaAmmortamentoFiscale o1, QuotaAmmortamentoFiscale o2) {
				return compareBeneAmmortizzabileLite(o1.getBeneAmmortizzabile(), o2.getBeneAmmortizzabile());
			}

		};
	}

	/**
	 * Restituisce il compatator per le vendite del bene.
	 *
	 * @return comparatore
	 */
	private Comparator<VenditaBene> getComparatorPerVenditaBene() {
		return new Comparator<VenditaBene>() {

			@Override
			public int compare(VenditaBene o1, VenditaBene o2) {
				return compareBeneAmmortizzabile(o1.getBene(), o2.getBene());
			}

		};
	}

	/**
	 * Esegue l'ordinamento della list di <code>BeneAmmortizzabileConFigli</code> in base a <code>Comparator</code> .
	 *
	 * @param beneAmmortizzabileList
	 *            lista di beni
	 * @param comparator
	 *            comparatore
	 */
	private void ordinaListBeneAmmortizzabileConFigli(List<BeneAmmortizzabileConFigli> beneAmmortizzabileList,
			Comparator<BeneAmmortizzabileConFigli> comparator) {
		Collections.sort(beneAmmortizzabileList, comparator);
	}

	/**
	 * Esegue l'ordinamento della list di <code>QuotaAmmortamentoFiscale</code> in base a <code>Comparator</code>.
	 *
	 * @param quotaAmmortamentoList
	 *            lista di quote
	 * @param comparator
	 *            comparatore
	 */
	private void ordinaListQuotaAmmortamento(List<QuotaAmmortamentoFiscale> quotaAmmortamentoList,
			Comparator<QuotaAmmortamentoFiscale> comparator) {
		Collections.sort(quotaAmmortamentoList, comparator);
	}

	/**
	 * Esegue l'ordinamento della list di <code>VenditaBene</code> in base a <code>Comparator</code>.
	 *
	 * @param venditaBeneList
	 *            lista di vendite
	 * @param comparator
	 *            comparatore
	 */
	private void ordinaListVenditaBene(List<VenditaBene> venditaBeneList, Comparator<VenditaBene> comparator) {
		Collections.sort(venditaBeneList, comparator);
	}

	/**
	 * Ricalcola le simulazioni dell'anno del bene ammortizzabile.
	 *
	 * @param beneAmmortizzabile
	 *            bene ammortizzabile
	 * @param anno
	 *            anno
	 */
	private void ricalcolaSimulazioni(BeneAmmortizzabile beneAmmortizzabile, Integer anno) {
		logger.debug("--> Enter ricalcolaSimulazioni");
		MessageBeneAmmortizzabile messageBeneAmmortizzabile = new MessageBeneAmmortizzabile();
		messageBeneAmmortizzabile.setBeneAmmortizzabile(beneAmmortizzabile);
		messageBeneAmmortizzabile.setAnno(anno);
		messageBeneAmmortizzabile.setJecPrincipal((JecPrincipal) sessionContext.getCallerPrincipal());
		logger.debug("--> MessaggeBeneAmmortizzabile creato");
		Connection connection;
		try {
			connection = jmsConnectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			logger.debug("-->Session JMS creata");
			MessageProducer producer = session.createProducer(queue);
			ObjectMessage message = session.createObjectMessage();
			message.setObject(messageBeneAmmortizzabile);
			message.setJMSRedelivered(false);
			logger.debug("--> Spedizione messaggio MessageBeneAmmortizzabile ");
			producer.send(message);
			producer.close();
			session.close();
			connection.close();
			logger.debug("--> Messaggio MessageBeneAmmortizzabile spedito ");
		} catch (JMSException e) {
			logger.error("--> errore, impossibile inviare il messaggio MessageBeneAmmortizzabile ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit ricalcolaSimulazioni");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(Map<String, Object> parametri) {
		logger.debug("--> Enter ricercaBeniAmmortizzabili");
		String codiceAzienda = getAzienda();
		logger.debug("--> Abilita filtro codice azienda " + codiceAzienda);

		StringBuilder sb = new StringBuilder();
		sb.append("select  b.codice as codice,");
		sb.append(" 			 b.descrizione as descrizione,");
		sb.append(" 			 b.id as id,");
		sb.append("				 f.id as idFornitore,");
		sb.append("				 f.codice as codiceFornitore,");
		sb.append("				 a.denominazione as descrizioneFornitore,");
		sb.append("				 b.annoAcquisto as annoAcquisto,");
		sb.append("				 b.matricolaAziendale as matricolaAziendale,");
		sb.append("				 b.matricolaFornitore as matricolaFornitore ");
		sb.append("from  BeneAmmortizzabile b left join b.fornitore f left join f.anagrafica a ");
		sb.append("where b.codiceAzienda = :paramCodiceAzienda ");

		// azienda
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("paramCodiceAzienda", codiceAzienda);

		// anno acquisto
		Object value = parametri.get(BeneAmmortizzabile.PROP_ANNO_ACQUISTO);
		if (value != null && value instanceof String) {
			value = new Integer((String) value);
			sb.append(" and b.annoAcquisto = :paramAnnoAcquisto ");
			queryParams.put("paramAnnoAcquisto", value);
		}

		// codice
		value = parametri.get(BeneAmmortizzabile.PROP_CODICE);
		if (value != null && !value.equals("") && value instanceof String) {
			value = new Integer((String) value);
			sb.append(" and b.codice = :paramCodice ");
			queryParams.put("paramCodice", value);
		}

		// descrizione
		value = parametri.get(BeneAmmortizzabile.PROP_DESCRIZIONE);
		if (value != null && !value.equals("")) {
			sb.append(" and b.descrizione like :paramDescrizione ");
			queryParams.put("paramDescrizione", "%" + value + "%");
		}

		// di proprietà
		value = parametri.get(BeneAmmortizzabile.PROP_BENE_DI_PROPRIETA);
		if (value != null) {
			sb.append(" and b.beneDiProprieta = :paramBeneDiProprieta");
			queryParams.put("paramBeneDiProprieta", value);
		}

		// leasing
		value = parametri.get(BeneAmmortizzabile.PROP_BENE_IN_LEASING);
		if (value != null) {
			sb.append(" and b.beneInLeasing = :paramBeneInLeasing");
			queryParams.put("paramBeneInLeasing", value);
		}

		Query query = panjeaDAO.prepareQuery(sb.toString(), BeneAmmortizzabileLite.class, null);
		for (Entry<String, Object> entry : queryParams.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		List<BeneAmmortizzabileLite> beni;
		try {
			beni = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore durante la ricerca dei beni.", e);
			throw new RuntimeException("errore durante la ricerca dei beni.", e);
		}

		return beni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabileLite> ricercaBeniAmmortizzabili(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaZoneGeografiche");
		List<BeneAmmortizzabileLite> list = new ArrayList<BeneAmmortizzabileLite>();
		StringBuilder sb = new StringBuilder(
				"select bene from BeneAmmortizzabileLite bene where bene.codiceAzienda = :codiceAzienda ");
		if (valueSearch != null) {
			sb.append(" and ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("codiceAzienda", getAzienda());
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei mezzi di trasporto", e);
			throw new RuntimeException("Errore durante il caricamento dei mezzi di trasporto", e);
		}
		logger.debug("--> Exit caricaZoneGeografiche");
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabileConFigli> ricercaBeniAmmortizzabiliPerFornitore(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaBeniAmmortizzabiliPerFornitore");
		List<BeneAmmortizzabile> beniAmmortizzabili;
		List<BeneAmmortizzabileConFigli> beniAmmortizzabiliConFigli = new ArrayList<BeneAmmortizzabileConFigli>();
		try {
			Criteria criteria = createCriteriaRicercaBeneAmmortizzabile(criteriaRicercaBeniAmmortizzabili);
			beniAmmortizzabili = criteria.list();
			BeneAmmortizzabileConFigli beneAmmortizzabileConFigli;
			for (BeneAmmortizzabile beneAmmortizzabile : beniAmmortizzabili) {
				beneAmmortizzabileConFigli = new BeneAmmortizzabileConFigli();
				beneAmmortizzabileConFigli.setBeneAmmortizzabile(beneAmmortizzabile);
				beneAmmortizzabileConFigli
				.setBeniAmmortizzabiliFigli(caricaBeniAmmortizzabiliFigli(beneAmmortizzabile));
				beniAmmortizzabiliConFigli.add(beneAmmortizzabileConFigli);
			}
		} catch (DAOException e) {
			logger.error("--> errore ricerca ", e);
			throw new RuntimeException("errore durante la ricerca dei beni ammortizzabili", e);
		}
		ordinaListBeneAmmortizzabileConFigli(beniAmmortizzabiliConFigli, getComparatorPerFornitore());
		logger.debug("--> Exit ricercaBeniAmmortizzabiliPerFornitore");
		return beniAmmortizzabiliConFigli;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BeneAmmortizzabileConFigli> ricercaBeniAmmortizzabiliPerUbicazione(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaBeniAmmortizzabili");
		List<BeneAmmortizzabile> beniAmmortizzabili;
		List<BeneAmmortizzabileConFigli> beniAmmortizzabiliConFigli = new ArrayList<BeneAmmortizzabileConFigli>();
		try {
			Criteria criteria = createCriteriaRicercaBeneAmmortizzabile(criteriaRicercaBeniAmmortizzabili);
			beniAmmortizzabili = criteria.list();
			BeneAmmortizzabileConFigli beneAmmortizzabileConFigli;
			for (BeneAmmortizzabile beneAmmortizzabile : beniAmmortizzabili) {
				beneAmmortizzabileConFigli = new BeneAmmortizzabileConFigli();
				beneAmmortizzabileConFigli.setBeneAmmortizzabile(beneAmmortizzabile);
				beneAmmortizzabileConFigli
				.setBeniAmmortizzabiliFigli(caricaBeniAmmortizzabiliFigli(beneAmmortizzabile));
				beniAmmortizzabiliConFigli.add(beneAmmortizzabileConFigli);
			}
		} catch (DAOException e) {
			logger.error("--> errore ricerca ", e);
			throw new RuntimeException("errore durante la ricerca dei beni ammortizzabili");
		}
		logger.debug("--> Exit ricercaBeniAmmortizzabili");
		return beniAmmortizzabiliConFigli;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamentoFiscali(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaQuoteAmmortamentoFiscali");
		org.hibernate.Session hibernateSession = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
		Criteria criteria = hibernateSession.createCriteria(QuotaAmmortamentoFiscale.class);
		/* carica solo le quote consolidate */
		criteria.add(Restrictions.eq(QuotaAmmortamento.PROP_CONSOLIDATA, true));
		if (criteriaRicercaBeniAmmortizzabili.getAnnoAmmortamento() != null) {
			logger.debug("--> aggiunta estriction anno " + criteriaRicercaBeniAmmortizzabili.getAnnoAmmortamento());
			criteria.add(Restrictions.eq(QuotaAmmortamento.PROP_ANNO_SOLARE_AMMORTAMENTO,
					criteriaRicercaBeniAmmortizzabili.getAnnoAmmortamento()));
		}
		criteria = criteria.createCriteria(QuotaAmmortamento.PROP_BENE_AMMORTIZZABILE);
		criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_CODICE_AZIENDA, getAzienda()));
		if (criteriaRicercaBeniAmmortizzabili.isIntervalloSpecie()) {

			criteria.createAlias(BeneAmmortizzabile.PROP_SOTTO_SPECIE, "sottoSpecie");
			criteria.createAlias("sottoSpecie." + SottoSpecie.PROP_SPECIE, "specie");
			if ((criteriaRicercaBeniAmmortizzabili.getSpecieIniziale() != null)
					&& (criteriaRicercaBeniAmmortizzabili.getSpecieIniziale().getId() != null)) {
				logger.debug("--> aggiunta Restricton Specie iniziale "
						+ criteriaRicercaBeniAmmortizzabili.getSpecieIniziale());
				if ((criteriaRicercaBeniAmmortizzabili.getSpecieFinale() != null)
						&& (criteriaRicercaBeniAmmortizzabili.getSpecieFinale().getId() != null)) {
					/* intervallo */
					criteria.add(Restrictions.ge("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
							.getSpecieIniziale().getCodice()));
					criteria.add(Restrictions.le("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
							.getSpecieFinale().getCodice()));
				} else {
					/* equals */
					criteria.add(Restrictions.eq("specie." + Specie.PROP_ID, criteriaRicercaBeniAmmortizzabili
							.getSpecieIniziale().getId()));
					if (criteriaRicercaBeniAmmortizzabili.isIntervalloSottoSpecie()) {
						/*
						 * verifica in base solo alla selezione della singola Specie contenuta in specieIniziale
						 */
						if ((criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale() != null)
								&& (criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale().getId() != null)) {
							logger.debug("--> aggiunta Restriction SottoSpecie Iniziale "
									+ criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale());
							criteria.add(Restrictions.ge("sottoSpecie." + SottoSpecie.PROP_CODICE,
									criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale().getCodice()));
						}
						if ((criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale() != null)
								&& (criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale().getId() != null)) {
							logger.debug("--> aggiunta Restriction SottoSpecie Finale "
									+ criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale());
							criteria.add(Restrictions.le("sottoSpecie." + SottoSpecie.PROP_CODICE,
									criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale().getCodice()));
						}
					}
				}
			} else if ((criteriaRicercaBeniAmmortizzabili.getSpecieFinale() != null)
					&& (criteriaRicercaBeniAmmortizzabili.getSpecieFinale().getId() != null)) {
				logger.debug("--> aggiugo Restricton Specie finale "
						+ criteriaRicercaBeniAmmortizzabili.getSpecieFinale());
				criteria.add(Restrictions.le("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
						.getSpecieFinale().getCodice()));
			}
		}
		List<QuotaAmmortamentoFiscale> quoteAmmortamento = criteria.list();
		logger.debug("--> Trovate " + quoteAmmortamento.size() + " quote ammortamento");
		ordinaListQuotaAmmortamento(quoteAmmortamento, getComparatorPerQuotaAmmortamento());
		logger.debug("--> Exit ricercaQuoteAmmortamentoFiscali");
		return quoteAmmortamento;
	}

	/**
	 * Ricerca le vendite in base ai criteri di ricerca.
	 *
	 * @param codiceAzienda
	 *            codice dell'azienda
	 * @param criteriaRicercaBeniAmmortizzabili
	 *            criteri di ricerca
	 * @return vendite trovate
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<VenditaBene> ricercaVenditaBene(String codiceAzienda,
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) throws DAOException {
		logger.debug("--> Enter ricercaVenditaBene");
		org.hibernate.Session hibernateSession = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
		Criteria criteria = hibernateSession.createCriteria(VenditaBene.class);
		if (criteriaRicercaBeniAmmortizzabili.getAnno() != null) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(criteriaRicercaBeniAmmortizzabili.getAnno().intValue(), 0, 1);
			Date dateIniziale = calendar.getTime();
			calendar.set(criteriaRicercaBeniAmmortizzabili.getAnno().intValue(), 11, 31);
			Date dateFinale = calendar.getTime();
			logger.debug("--> aggiunta restriction anno vendita " + dateIniziale + " - " + dateFinale);
			criteria.add(Restrictions.between(VenditaBene.PROP_DATA_VENDITA, dateIniziale, dateFinale));
		}
		criteria = criteria.createCriteria(VenditaBene.PROP_BENE);
		criteria.add(Restrictions.eq(BeneAmmortizzabile.PROP_CODICE_AZIENDA, getAzienda()));
		if (criteriaRicercaBeniAmmortizzabili.isIntervalloSpecie()) {
			criteria.createAlias(BeneAmmortizzabile.PROP_SOTTO_SPECIE, "sottoSpecie");
			criteria.createAlias("sottoSpecie." + SottoSpecie.PROP_SPECIE, "specie");
			if ((criteriaRicercaBeniAmmortizzabili.getSpecieIniziale() != null)
					&& (criteriaRicercaBeniAmmortizzabili.getSpecieIniziale().getId() != null)) {
				logger.debug("--> aggiunta Restricton Specie iniziale "
						+ criteriaRicercaBeniAmmortizzabili.getSpecieIniziale());
				if ((criteriaRicercaBeniAmmortizzabili.getSpecieFinale() != null)
						&& (criteriaRicercaBeniAmmortizzabili.getSpecieFinale().getId() != null)) {
					/* intervallo */
					criteria.add(Restrictions.ge("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
							.getSpecieIniziale().getCodice()));
					criteria.add(Restrictions.le("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
							.getSpecieFinale().getCodice()));
				} else {
					/* equals */
					criteria.add(Restrictions.eq("specie." + Specie.PROP_ID, criteriaRicercaBeniAmmortizzabili
							.getSpecieIniziale().getId()));
					if (criteriaRicercaBeniAmmortizzabili.isIntervalloSottoSpecie()) {
						/*
						 * verifica in base solo alla selezione della singola Specie contenuta in specieIniziale
						 */
						if ((criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale() != null)
								&& (criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale().getId() != null)) {
							logger.debug("--> aggiunta Restriction SottoSpecie Iniziale "
									+ criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale());
							criteria.add(Restrictions.ge("sottoSpecie." + SottoSpecie.PROP_CODICE,
									criteriaRicercaBeniAmmortizzabili.getSottoSpecieIniziale().getCodice()));
						}
						if ((criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale() != null)
								&& (criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale().getId() != null)) {
							logger.debug("--> aggiunta Restriction SottoSpecie Finale "
									+ criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale());
							criteria.add(Restrictions.le("sottoSpecie." + SottoSpecie.PROP_CODICE,
									criteriaRicercaBeniAmmortizzabili.getSottoSpecieFinale().getCodice()));
						}
					}
				}
			} else if ((criteriaRicercaBeniAmmortizzabili.getSpecieFinale() != null)
					&& (criteriaRicercaBeniAmmortizzabili.getSpecieFinale().getId() != null)) {
				logger.debug("--> aggiugo Restricton Specie finale "
						+ criteriaRicercaBeniAmmortizzabili.getSpecieFinale());
				criteria.add(Restrictions.le("specie." + Specie.PROP_CODICE, criteriaRicercaBeniAmmortizzabili
						.getSpecieFinale().getCodice()));
			}
		}
		List<VenditaBene> list = criteria.list();
		logger.debug("--> Exit ricercaVenditaBene #record " + list.size());
		return list;
	}

	@Override
	public List<VenditaBene> ricercaVenditeBeni(CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaVenditeBeni");
		List<VenditaBene> list = null;
		try {
			list = ricercaVenditaBene(getAzienda(), criteriaRicercaBeniAmmortizzabili);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca Vendite Bene Ammortizzabile ", e);
			throw new RuntimeException("Errore durante la ricerca delle vendite del bene ammortizzabile ");
		}
		logger.debug("--> Exit ricercaVenditeBeni");
		ordinaListVenditaBene(list, getComparatorPerVenditaBene());
		return list;
	}

	@Override
	public BeneAmmortizzabile salvaBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		logger.debug("--> Enter salvaBeneAmmortizzabile");

		// se il bene ammortizzabile è nuovo vado a settargli l'azienda con quella del JECPrincipal
		if (beneAmmortizzabile.isNew()) {
			beneAmmortizzabile.setCodiceAzienda(getAzienda());
		}

		// se il bene non ha il codice lo assegno automaticamente
		if (beneAmmortizzabile.getCodice() == null) {
			beneAmmortizzabile.setCodice(lastCodiceGenerator.nextCodice(BeneAmmortizzabile.class,
					BeneAmmortizzabile.PROP_CODICE_AZIENDA));
		} else {
			// controllo che non ci sia già un bene con lo stesso codice
			List<BeneAmmortizzabileLite> beniPresenti = caricaBeneAmmortizzabileByCodice(beneAmmortizzabile.getCodice());
			// se lo stò inserendo controllo che non esistano altri beni con quel codice altrimenti (modifica di un bene
			// esistente) verifico che il bene presente non sia sè stesso
			if ((beneAmmortizzabile.isNew() && !beniPresenti.isEmpty())
					|| (!beneAmmortizzabile.isNew() && !beniPresenti.isEmpty() && !beniPresenti
							.contains(beneAmmortizzabile))) {
				throw new GenericException("Esiste già un bene con codice " + beneAmmortizzabile.getCodice());
			}
		}

		BeneAmmortizzabile beneAmmortizzabileSalvato = null;
		try {
			beneAmmortizzabileSalvato = panjeaDAO.save(beneAmmortizzabile);
			logger.debug("--> Salvato il bene ammortizzabile " + beneAmmortizzabile.getId());
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del beneAmmortizzabile " + beneAmmortizzabile, e);
			throw new RuntimeException("Errore durante il salvataggio del beneAmmortizzabile " + beneAmmortizzabile, e);
		}

		logger.debug("--> Exit salvaBeneAmmortizzabile");
		return beneAmmortizzabileSalvato;
	}

	@Override
	public QuotaAmmortamentoCivilistico salvaQuotaAmmortamentoCivilistico(
			QuotaAmmortamentoCivilistico quotaAmmortamentoCivilistico) {
		logger.debug("--> Enter salvaQuotaAmmortamentoCivilistico");
		QuotaAmmortamentoCivilistico ammortamentoCivilistico;

		try {
			ammortamentoCivilistico = panjeaDAO.save(quotaAmmortamentoCivilistico);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della QuotaAmmortamento.", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaQuotaAmmortamentoCivilistico");
		return ammortamentoCivilistico;
	}

	@Override
	public QuotaAmmortamentoFiscale salvaQuotaAmmortamentoFiscale(QuotaAmmortamentoFiscale quotaAmmortamentoFiscale) {
		logger.debug("--> Enter salvaQuotaAmmortamentoFiscale");
		QuotaAmmortamentoFiscale ammortamentoFiscale;
		try {
			ammortamentoFiscale = panjeaDAO.save(quotaAmmortamentoFiscale);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della QuotaAmmortamento.", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaQuotaAmmortamentoFiscale");
		return ammortamentoFiscale;
	}

	@Override
	public ValutazioneBene salvaValutazioneBene(ValutazioneBene valutazioneBene) {
		logger.debug("--> Enter salvaValutazioneBene");
		ValutazioneBene valBene;
		try {
			valBene = panjeaDAO.save(valutazioneBene);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di ValutazioneBene.", e);
			throw new RuntimeException("--> Errore durante il salvataggio di ValutazioneBene", e);
		}
		logger.debug("--> Exit salvaValutazioneBene");
		return valBene;
	}

	@Override
	public VenditaBene salvaVenditaBene(VenditaBene venditaBene) {
		logger.debug("--> Enter salvaVenditaBene");
		VenditaBene venditaBeneSalvata;
		try {
			venditaBeneSalvata = panjeaDAO.save(venditaBene);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della VenditaAmmortamento.", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaVenditaBene");
		return venditaBeneSalvata;
	}

	@Override
	public VenditaBene salvaVenditaBene(VenditaBene venditaBene, boolean forzaRicalcolo) {
		logger.debug("--> Enter salvaVenditaBene");
		VenditaBene venditaBeneSalvata = salvaVenditaBene(venditaBene);
		if (forzaRicalcolo) {
			// esegue il ricalcolo delle simulazioni collegate al bene venduto
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(venditaBene.getDataVendita());
			int anno = calendar.get(Calendar.YEAR);
			ricalcolaSimulazioni(venditaBene.getBene(), anno);
		}
		return venditaBeneSalvata;
	}

}
