package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.RegistroBeniManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.ReportBeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.sqlBuilder.SituazioneBeneSqlBuilder;
import it.eurotn.panjea.beniammortizzabili2.manager.sqlBuilder.VenditeAnnualiQueryBuilder;
import it.eurotn.panjea.beniammortizzabili2.util.SituazioneBene;
import it.eurotn.panjea.beniammortizzabili2.util.registrobeni.RegistroBene;
import it.eurotn.panjea.beniammortizzabili2.util.venditeannuali.VenditaAnnualeBene;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;
import ca.odell.glazedlists.SortedList;

/**
 *
 * Manager responsabile del caricamento dati per i report dei beni ammortizzabili.
 *
 * @author adriano
 * @version 1.0, 03/ott/07
 *
 */
@Stateless(name = "Panjea.ReportBeniAmmortizzabiliManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.ReportBeniAmmortizzabiliManager")
public class ReportBeniAmmortizzabiliManagerBean implements ReportBeniAmmortizzabiliManager {

	private static Logger logger = Logger.getLogger(ReportBeniAmmortizzabiliManagerBean.class);

	@EJB
	protected BeniAmmortizzabiliManager beniAmmortizzabiliManager;

	@EJB
	protected RegistroBeniManager registroBeniManager;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext sessionContext;

	@Override
	public List<RegistroBene> caricaRegistroBeni(Map<String, Object> parameters) {
		return registroBeniManager.caricaRegistroBeni(parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SituazioneBene> caricaSituazioneBeni(Map<Object, Object> parameters) {

		boolean visualizzaBeniFigli = ((Boolean) parameters.get("visualizzafigli")).booleanValue();

		SQLQuery queryBeniPadre = ((Session) panjeaDAO.getEntityManager().getDelegate())
				.createSQLQuery(SituazioneBeneSqlBuilder.getSituazioneBeniPadriSql());
		queryBeniPadre.setResultTransformer(Transformers.aliasToBean(SituazioneBene.class));

		queryBeniPadre.setParameter("codiceAzienda", parameters.get("azienda"));
		queryBeniPadre.setParameter("fornitore", parameters.get("fornitore"));

		List<SituazioneBene> listBeniPadre = new ArrayList<SituazioneBene>();

		queryBeniPadre.addScalar("id");
		queryBeniPadre.addScalar("codice");
		queryBeniPadre.addScalar("descrizione");
		queryBeniPadre.addScalar("idUbicazione");
		queryBeniPadre.addScalar("codiceUbicazione");
		queryBeniPadre.addScalar("descrizioneUbicazione");
		queryBeniPadre.addScalar("idSpecie");
		queryBeniPadre.addScalar("codiceSpecie");
		queryBeniPadre.addScalar("descrizioneSpecie");
		queryBeniPadre.addScalar("idSottoSpecie");
		queryBeniPadre.addScalar("codiceSottoSpecie");
		queryBeniPadre.addScalar("descrizioneSottoSpecie");
		queryBeniPadre.addScalar("dataInizioAmmortamento");
		queryBeniPadre.addScalar("importoSoggettoAdAmmortamento");
		queryBeniPadre.addScalar("importoVariazioniBene");
		queryBeniPadre.addScalar("importoVenditeBene");
		queryBeniPadre.addScalar("importoAmmortamentoOrdinario");
		queryBeniPadre.addScalar("importoAmmortamentoAnticipato");
		queryBeniPadre.addScalar("importoVariazioniFondo");
		queryBeniPadre.addScalar("importoVenditeFondo");
		listBeniPadre = queryBeniPadre.list();

		List<SituazioneBene> listBeniResult = listBeniPadre;

		if (visualizzaBeniFigli) {

			// aggiungo tutti i padri in una mappa con chiave id del bene e
			// valore il bene
			Map<Integer, SituazioneBene> beniPadriMap = new HashMap<Integer, SituazioneBene>();
			for (SituazioneBene situazioneBene : listBeniPadre) {
				beniPadriMap.put(situazioneBene.getBene().getId(), situazioneBene);
			}

			// carico tutti i beni figli
			SQLQuery queryBeniFigli = ((Session) panjeaDAO.getEntityManager().getDelegate())
					.createSQLQuery(SituazioneBeneSqlBuilder.getBeniFigliSql());
			queryBeniFigli.setResultTransformer(Transformers.aliasToBean(SituazioneBene.class));
			queryBeniFigli.setParameter("codiceAzienda", parameters.get("azienda"));
			queryBeniFigli.addScalar("id");
			queryBeniFigli.addScalar("codice");
			queryBeniFigli.addScalar("descrizione");
			queryBeniFigli.addScalar("dataInizioAmmortamento");
			queryBeniFigli.addScalar("importoSoggettoAdAmmortamento");
			queryBeniFigli.addScalar("importoVariazioniBene");
			queryBeniFigli.addScalar("importoVenditeBene");
			queryBeniFigli.addScalar("importoAmmortamentoOrdinario");
			queryBeniFigli.addScalar("importoAmmortamentoAnticipato");
			queryBeniFigli.addScalar("importoVariazioniFondo");
			queryBeniFigli.addScalar("importoVenditeFondo");
			queryBeniFigli.addScalar("benePadreId");

			List<SituazioneBene> listBeniFigli = new ArrayList<SituazioneBene>();
			listBeniFigli = queryBeniFigli.list();

			// raggruppo i beni figli in base al bene padre
			Comparator<SituazioneBene> beniFigliComparator = new Comparator<SituazioneBene>() {

				@Override
				public int compare(SituazioneBene o1, SituazioneBene o2) {
					return o1.getBenePadreId().compareTo(o2.getBenePadreId());
				}
			};

			EventList<SituazioneBene> eventListBeniFigli = new BasicEventList<SituazioneBene>();
			eventListBeniFigli.addAll(listBeniFigli);
			GroupingList<SituazioneBene> figliGrouping = new GroupingList<SituazioneBene>(eventListBeniFigli,
					beniFigliComparator);

			// aggiungo i beni figli al padre
			for (List<SituazioneBene> listFigliGroup : figliGrouping) {
				Integer idBenePadre = listFigliGroup.get(0).getBenePadreId();

				if (beniPadriMap.containsKey(idBenePadre)) {
					for (SituazioneBene beneFiglio : listFigliGroup) {
						beniPadriMap.get(idBenePadre).getBeniFigli().add(beneFiglio);
					}
				}
			}

			Comparator<SituazioneBene> beniComparator = new Comparator<SituazioneBene>() {

				@Override
				public int compare(SituazioneBene o1, SituazioneBene o2) {

					BeneAmmortizzabileLite bene1 = o1.getBene();
					BeneAmmortizzabileLite bene2 = o2.getBene();

					if (bene1.getUbicazione().getId().compareTo(bene2.getUbicazione().getId()) != 0) {
						return bene1.getUbicazione().getId().compareTo(bene2.getUbicazione().getId());
					} else {
						if (bene1.getSottoSpecie().getSpecie().getCodice()
								.compareTo(bene2.getSottoSpecie().getSpecie().getCodice()) != 0) {
							return bene1.getSottoSpecie().getSpecie().getCodice()
									.compareTo(bene2.getSottoSpecie().getSpecie().getCodice());
						} else {
							if (bene1.getSottoSpecie().getCodice().compareTo(bene2.getSottoSpecie().getCodice()) != 0) {
								return bene1.getSottoSpecie().getCodice().compareTo(bene2.getSottoSpecie().getCodice());
							} else {
								return bene1.getCodice().compareTo(bene2.getCodice());
							}
						}
					}
				}
			};
			EventList<SituazioneBene> eventBeniPadri = new BasicEventList<SituazioneBene>();
			eventBeniPadri.addAll(beniPadriMap.values());

			SortedList<SituazioneBene> sortedListBeni = new SortedList<SituazioneBene>(eventBeniPadri, beniComparator);

			listBeniResult = new ArrayList<SituazioneBene>();
			listBeniResult.addAll(sortedListBeni);
		}

		return listBeniResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VenditaAnnualeBene> caricaVenditeAnnualiBeniPadri(Map<Object, Object> parameters) {

		Integer anno = (Integer) parameters.get("anno");
		Integer idSpecie = (Integer) parameters.get("specie");
		Integer idSottoSpecie = (Integer) parameters.get("sottospecie");

		SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate())
				.createSQLQuery(VenditeAnnualiQueryBuilder.getVenditeBeniPadre(anno, idSpecie, idSottoSpecie));
		query.setResultTransformer(Transformers.aliasToBean((VenditaAnnualeBene.class)));

		List<VenditaAnnualeBene> result = Collections.emptyList();

		query.addScalar("idBene");
		query.addScalar("codiceBene");
		query.addScalar("descrizioneBene");
		query.addScalar("annoAcquisto");
		query.addScalar("importoSoggettoAdAmmortamentoSingolo");
		query.addScalar("idSpecie");
		query.addScalar("codiceSpecie");
		query.addScalar("descrizioneSpecie");
		query.addScalar("idSottoSpecie");
		query.addScalar("codiceSottoSpecie");
		query.addScalar("descrizioneSottoSpecie");
		query.addScalar("rivalutazioni");
		query.addScalar("venditeAnnualiBene");
		query.addScalar("vendite");
		query.addScalar("valoreQuoteFondo");
		query.addScalar("valoreValutazioniFondo");
		query.addScalar("valoreVenditeFondo");
		query.addScalar("importoSoggettoAdAmmortamentoFigli");
		query.addScalar("rivalutazioniFigli");
		query.addScalar("venditeFigli");
		query.addScalar("rivalutazioniFondoFigli");
		query.addScalar("venditeFondoFigli");
		query.addScalar("venditeAnnualiFigli");
		query.addScalar("plusMinusValoreAnnualiFigli");
		query.addScalar("plusMinusValoreAnnualiBene");
		query.addScalar("tipologiaEliminazione");

		result = query.list();

		return result;
	}

	/*
	 * @see it.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * ReportBeniAmmortizzabiliManager#ricercaBeniAcquistati (it.eurotn.panjea.beniammortizzabili2
	 * .domain.CriteriaRicercaBeniAmmortizzabili)
	 */
	@Override
	public List<BeneAmmortizzabileConFigli> ricercaBeniAcquistati(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter caricaStampaBeniAcquistati");
		List<BeneAmmortizzabileConFigli> beniAmmortizzabiliConFigli = null;
		try {
			beniAmmortizzabiliConFigli = beniAmmortizzabiliManager
					.ricercaBeniAmmortizzabiliPerFornitore(criteriaRicercaBeniAmmortizzabili);
		} catch (Exception e) {
			logger.debug("--> Errore nella ricerca Beni ammortizzabili per fornitore", e);
			throw new RuntimeException("--> Errore nella ricerca Beni ammortizzabili per fornitore", e);
		}

		logger.debug("--> Trovati " + beniAmmortizzabiliConFigli.size() + " beni con figli");
		logger.debug("--> Exit ricercaBeniAcquistati");
		return beniAmmortizzabiliConFigli;
	}

	/*
	 * @see it.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * ReportBeniAmmortizzabiliManager#ricercaQuoteAmmortamento (it.eurotn.panjea
	 * .beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili)
	 */
	@Override
	public List<QuotaAmmortamentoFiscale> ricercaQuoteAmmortamento(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) throws BeniAmmortizzabiliException {
		logger.debug("--> Enter ricercaQuoteAmmortamento");
		if (criteriaRicercaBeniAmmortizzabili.getAnnoAmmortamento() == null) {
			logger.warn("--> anno ammortamento in ricerca quote ammortamento is null");
			throw new BeniAmmortizzabiliException("Parametro anno ammortamento assente");
		}
		List<QuotaAmmortamentoFiscale> quote = beniAmmortizzabiliManager
				.ricercaQuoteAmmortamentoFiscali(criteriaRicercaBeniAmmortizzabili);
		logger.debug("--> Trovate " + quote.size() + " quote");
		logger.debug("--> Exit ricercaQuoteAmmortamento");
		return quote;
	}

	/*
	 * @see it.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * ReportBeniAmmortizzabiliManager#ricercaRubricaBeni(it .eurotn.panjea.beniammortizzabili2
	 * .domain.CriteriaRicercaBeniAmmortizzabili)
	 */
	@Override
	public List<BeneAmmortizzabileConFigli> ricercaRubricaBeni(
			CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili) {
		logger.debug("--> Enter ricercaRubricaBeni");
		List<BeneAmmortizzabileConFigli> beniAmmortizzabiliConFigli = beniAmmortizzabiliManager
				.ricercaBeniAmmortizzabiliPerUbicazione(criteriaRicercaBeniAmmortizzabili);
		logger.debug("--> Trovati " + beniAmmortizzabiliConFigli.size() + " beni con figli");
		logger.debug("--> Exit ricercaRubricaBeni");
		return beniAmmortizzabiliConFigli;
	}
}
