package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.locking.ILock;
import it.eurotn.locking.service.interfaces.LockingServiceRemote;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliSimulazioneException;
import it.eurotn.panjea.beniammortizzabili.exception.MancatoConsolidamentoAnnoPrecedenteException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoConsolidatoException;
import it.eurotn.panjea.beniammortizzabili.exception.VenditaInAnnoSimulatoException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloCivilistica;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloGruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloSottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamento;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces.BeniAmmortizzabiliContabilitaManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.OperazioniBeneAmmortizzabileManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.ReportBeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.SimulazioneAmmortamentoManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.TabelleBeniAmmortizzabiliManager;
import it.eurotn.panjea.beniammortizzabili2.util.SituazioneBene;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 *
 * @author adriano
 * @version 1.0, 03/ott/07
 */
@Stateless(name = "Panjea.SimulazioneAmmortamentoManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.SimulazioneAmmortamentoManager")
public class SimulazioneAmmortamentoManagerBean implements SimulazioneAmmortamentoManager {

	private static Logger logger = Logger.getLogger(SimulazioneAmmortamentoManagerBean.class);
	@EJB
	private PanjeaDAO panjeaDAO;
	@EJB
	private OperazioniBeneAmmortizzabileManager operazioniBeneAmmortizzabileManager;
	@EJB
	private TabelleBeniAmmortizzabiliManager tabelleBeniAmmortizzabiliManager;
	@EJB
	private BeniAmmortizzabiliManager beniAmmortizzabiliManager;
	@EJB
	private LockingServiceRemote lockingService;
	@EJB
	private ReportBeniAmmortizzabiliManager reportBeniAmmortizzabiliManager;
	@EJB
	@IgnoreDependency
	private BeniAmmortizzabiliContabilitaManager beniAmmortizzabiliContabilitaManager;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void aggiornaBeniInAmmortamento() {

		Map<Object, Object> parameters = new HashMap<Object, Object>();
		parameters.put("visualizzafigli", false);
		parameters.put("azienda", getAzienda());
		parameters.put("fornitore", new Integer(-1));

		List<SituazioneBene> situazioniBeni = reportBeniAmmortizzabiliManager.caricaSituazioneBeni(parameters);

		for (SituazioneBene situazioneBene : situazioniBeni) {

			if (situazioneBene.getImportoResiduo().compareTo(BigDecimal.ZERO) <= 0) {

				Query query = panjeaDAO
						.getEntityManager()
						.createNativeQuery(
								"update bamm_bene_ammortizzabile set indAmmortamento = false, ammortamento_in_corso_civ = false, ammortamento_in_corso_fisc = false where id="
										+ situazioneBene.getBene().getId());

				try {
					panjeaDAO.executeQuery(query);
				} catch (DAOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * Calcola la quota ammortamento fiscale per la politica calcolo data.
	 *
	 * @param politicaCalcoloBene
	 *            politica di calcolo
	 * @return quota calcolata
	 */
	private QuotaAmmortamentoFiscale calcolaQuotaAmmortamentoFiscale(PoliticaCalcoloBene politicaCalcoloBene) {
		logger.debug("--> Enter calcolaQuotaAmmortamentoFiscale");
		/* FIXME in questo metodo l'arrotondamento � fissato al secondo decimale */
		final int scale = 2;
		PoliticaCalcoloFiscale politicaCalcoloFiscale = politicaCalcoloBene.getPoliticaCalcoloFiscale();

		BigDecimal valoreBene = operazioniBeneAmmortizzabileManager
				.getValoreBene(politicaCalcoloBene.getBene().getId());
		// BigDecimal valoreBene = politicaCalcoloBene.getImportoSoggettoAmmortamento();
		BigDecimal impAmmOrdinario = BigDecimal.ZERO;
		BigDecimal impAmmAnticipato = BigDecimal.ZERO;

		Date dataSimulazione = politicaCalcoloBene.getSimulazione().getData();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataSimulazione);

		Integer annoSimulazione = cal.get(Calendar.YEAR);

		// creo la quota ammortamento fiscale
		QuotaAmmortamentoFiscale fiscale = new QuotaAmmortamentoFiscale();
		fiscale.setConsolidata(false);
		// in origine prendeva Calendar.getInstance().get(Calendar.YEAR),
		// ora setto l'anno della simulazione
		fiscale.setAnnoSolareAmmortamento(annoSimulazione);
		fiscale.setBeneAmmortizzabile(politicaCalcoloBene.getBene());
		fiscale.setImportoSoggettoAmmortamentoBene(politicaCalcoloBene.getImportoSoggettoAmmortamento());

		// se prevede l'ammortamento ordinario calcolo l'importo e salvo la
		// percentuale
		if (politicaCalcoloFiscale.isAmmortamentoOrdinario()) {
			fiscale.setPercQuotaAmmortamentoOrdinario(politicaCalcoloFiscale.getPercAmmortamentoOrdinario());

			// importo ordinario = valore del bene * percentuale di ammortamento
			// ordinario

			BigDecimal percAmmOrdinario = BigDecimal.valueOf(politicaCalcoloFiscale.getPercAmmortamentoOrdinario());
			impAmmOrdinario = valoreBene.multiply(percAmmOrdinario).divide(Importo.HUNDRED, scale,
					BigDecimal.ROUND_HALF_UP);
		}

		// se prevede l'ammortamento anticipato calcolo l'importo e salvo la
		// percentuale
		if (politicaCalcoloFiscale.isAmmortamentoAnticipato()) {
			fiscale.setPercQuotaAmmortamentoAnticipato(politicaCalcoloFiscale.getPercAmmortamentoAnticipato());

			// importo anticipato = valore del bene * percentuale di
			// ammortamento anticipato
			BigDecimal percAmmAnticipato = BigDecimal.valueOf(politicaCalcoloFiscale.getPercAmmortamentoAnticipato());
			impAmmAnticipato = valoreBene.multiply(percAmmAnticipato).divide(Importo.HUNDRED, scale,
					BigDecimal.ROUND_HALF_UP);
		}

		// se prevede l'ammortamento ordinario salvo la percentuale e ricalcolo
		// importo ordinario e anticipato
		if (politicaCalcoloFiscale.isAmmortamentoRidotto()) {
			fiscale.setPercQuotaAmmortamentoRidotto(politicaCalcoloFiscale.getPercAmmortamentoRidotto());

			// importo ordinario = importo ordinario - ( importo ordinario *
			// percentuale ammortamento ordinario)
			// importo anticipato = importo anticipato - ( importo anticipato *
			// percentuale ammortamento anticipato)
			BigDecimal percAmmRidotto = BigDecimal.valueOf(politicaCalcoloFiscale.getPercAmmortamentoRidotto());
			impAmmOrdinario = impAmmOrdinario.subtract((impAmmOrdinario.multiply(percAmmRidotto).divide(
					Importo.HUNDRED, scale, BigDecimal.ROUND_HALF_UP)));
			impAmmAnticipato = impAmmAnticipato.subtract((impAmmAnticipato.multiply(percAmmRidotto).divide(
					Importo.HUNDRED, scale, BigDecimal.ROUND_HALF_UP)));
		}

		// se � il primo anno di ammortizzamento del bene applico il 50%
		// all'importo ordinario e anticipato
		boolean percPrimoAnnoApplicata = false;
		Calendar inizioAmmortamento = Calendar.getInstance();
		inizioAmmortamento.setTimeInMillis(politicaCalcoloBene.getBene().getDataInizioAmmortamento().getTime());
		Calendar correnteAmmortamento = Calendar.getInstance();
		correnteAmmortamento.setTimeInMillis(politicaCalcoloBene.getSimulazione().getData().getTime());
		logger.debug("--> confronto anno inizio ammortamento - anno simulazione :"
				+ inizioAmmortamento.get(Calendar.YEAR) + " - " + correnteAmmortamento.get(Calendar.YEAR));
		if (inizioAmmortamento.get(Calendar.YEAR) == correnteAmmortamento.get(Calendar.YEAR)
				&& politicaCalcoloBene.getBene().isIndTestoUnico()) {
			impAmmOrdinario = impAmmOrdinario.divide(new BigDecimal(2), scale);
			impAmmAnticipato = impAmmAnticipato.divide(new BigDecimal(2), scale);
			percPrimoAnnoApplicata = true;
		}
		fiscale.setPercPrimoAnnoApplicata(percPrimoAnnoApplicata);

		// se il totale dell'ammortamento + l'importo amm. ordinario sono >= al
		// valore attuale del bene
		// importo ordinario = valore del bene - totale ammortamento
		// importo anticipato = 0

		BigDecimal totAmm = operazioniBeneAmmortizzabileManager.getTotaleAmmortizzato(politicaCalcoloBene.getBene()
				.getId());
		logger.debug("--> totAmm " + totAmm);
		logger.debug("--> impAmmOrdinario " + impAmmOrdinario);
		logger.debug("--> impAmmAnticipato " + impAmmAnticipato);
		if (valoreBene.subtract(totAmm).compareTo(impAmmOrdinario) <= 0) {
			impAmmOrdinario = valoreBene.subtract(totAmm);
			impAmmAnticipato = BigDecimal.ZERO;
		} else {
			// se il totale ammortamento + importo ordinario + importo
			// anticipato >= al valore del bene
			// importo anticipato = valore del bene - (totale ammortamento +
			// importo ordinario)
			if (totAmm.add(impAmmOrdinario).add(impAmmAnticipato).compareTo(valoreBene) >= 0) {
				impAmmAnticipato = valoreBene.subtract(totAmm).subtract(impAmmOrdinario);
			}
		}
		logger.debug("--> importo ammortamento ordinario: " + impAmmOrdinario);
		logger.debug("--> importo ammortamento anticipato: " + impAmmAnticipato);

		fiscale.setImpQuotaAmmortamentoOrdinario(impAmmOrdinario);
		fiscale.setImpQuotaAmmortamentoAnticipato(impAmmAnticipato);

		logger.debug("--> Exit calcolaQuotaAmmortamentoFiscale");
		return fiscale;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * SimulazioneAmmortamentoManager#calcolaSimulazioniBene
	 * (it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile, java.lang.Integer)
	 */
	@Override
	public void calcolaSimulazioniBene(BeneAmmortizzabile bene, Integer anno) {
		logger.debug("--> Enter calcolaSimulazioniBene");

		List<Simulazione> simulazioni = caricaSimulazioniAnno(anno);
		logger.debug("--> caricate " + simulazioni.size() + " simulazioni per l'anno " + anno);

		for (Simulazione simulazione : simulazioni) {

			if ((simulazione.getPoliticheCalcolo() == null) || (simulazione.getPoliticheCalcolo().size() == 0)) {
				simulazione.setPoliticheCalcolo(caricaPolitiche(simulazione.getId()));
			}

			try {
				salvaSimulazione(simulazione, true);
			} catch (AreeContabiliSimulazioneException e) {
				// non avrò mai questa eccezione perchè forzo la cancellazione delle aree contabili se ci sono
				logger.error("--> errore AreeContabiliSimulazioneException", e);
				throw new RuntimeException("errore AreeContabiliSimulazioneException", e);
			}
		}
		logger.debug("--> Exit calcolaSimulazioniBene");
	}

	/**
	 * Calcola i totali della simulazione riportando su gruppo, specie e sottospecie i totali cumulativi dei beni.
	 * contenuti
	 *
	 * @param simulazione
	 *            la simulazione di cui calcolare i totali
	 * @return la simulazione con le politiche di calcolo i cui totali sono aggiornati
	 */
	private Simulazione calcolaTotaliSimulazione(Simulazione simulazione) {

		List<PoliticaCalcolo> politicheList = simulazione.getPoliticheCalcolo();
		logger.debug("--> numero di politiche da calcolare " + politicheList.size());
		// List<PoliticaCalcolo> politicheModificate = new
		// ArrayList<PoliticaCalcolo>();

		PoliticaCalcolo politicaCalcoloGruppo = null;
		PoliticaCalcolo politicaCalcoloSpecie = null;
		PoliticaCalcolo politicaCalcoloSottoSpecie = null;
		PoliticaCalcolo politicaCalcoloBene = null;

		// ciclo per sommare i beni e riportando quindi il tot per sottospecie
		for (PoliticaCalcolo politicaCalcolo : politicheList) {
			if (politicaCalcolo instanceof PoliticaCalcoloGruppo) {
				politicaCalcoloGruppo = politicaCalcolo;
			} else if (politicaCalcolo instanceof PoliticaCalcoloSpecie) {
				politicaCalcoloSpecie = politicaCalcolo;
			} else if (politicaCalcolo instanceof PoliticaCalcoloSottoSpecie) {
				politicaCalcoloSottoSpecie = politicaCalcolo;
			} else if (politicaCalcolo instanceof PoliticaCalcoloBene) {
				politicaCalcoloBene = politicaCalcolo;

				politicaCalcoloBene.getPoliticaCalcoloFiscale().setTotaleAnticipato(
						((PoliticaCalcoloBene) politicaCalcoloBene).getQuotaFiscale()
						.getImpQuotaAmmortamentoAnticipato());
				politicaCalcoloBene.getPoliticaCalcoloFiscale().setTotaleOrdinario(
						((PoliticaCalcoloBene) politicaCalcoloBene).getQuotaFiscale()
						.getImpQuotaAmmortamentoOrdinario());

				BigDecimal totOrdSotSpe = politicaCalcoloSottoSpecie.getPoliticaCalcoloFiscale().getTotaleOrdinario();
				BigDecimal totAntSotSpe = politicaCalcoloSottoSpecie.getPoliticaCalcoloFiscale().getTotaleAnticipato();
				BigDecimal totOrdBene = politicaCalcoloBene.getPoliticaCalcoloFiscale().getTotaleOrdinario();
				BigDecimal totAntBene = politicaCalcoloBene.getPoliticaCalcoloFiscale().getTotaleAnticipato();

				// somma dei beni
				politicaCalcoloSottoSpecie.getPoliticaCalcoloFiscale().setTotaleOrdinario(totOrdSotSpe.add(totOrdBene));
				politicaCalcoloSottoSpecie.getPoliticaCalcoloFiscale()
				.setTotaleAnticipato(totAntSotSpe.add(totAntBene));

				BigDecimal totOrdSpecie = politicaCalcoloSpecie.getPoliticaCalcoloFiscale().getTotaleOrdinario();
				BigDecimal totAntSpecie = politicaCalcoloSpecie.getPoliticaCalcoloFiscale().getTotaleAnticipato();
				politicaCalcoloSpecie.getPoliticaCalcoloFiscale().setTotaleOrdinario(totOrdSpecie.add(totOrdBene));
				politicaCalcoloSpecie.getPoliticaCalcoloFiscale().setTotaleAnticipato(totAntSpecie.add(totAntBene));

				BigDecimal totOrdGruppo = politicaCalcoloGruppo.getPoliticaCalcoloFiscale().getTotaleOrdinario();
				BigDecimal totAntGruppo = politicaCalcoloGruppo.getPoliticaCalcoloFiscale().getTotaleAnticipato();
				politicaCalcoloGruppo.getPoliticaCalcoloFiscale().setTotaleOrdinario(totOrdGruppo.add(totOrdBene));
				politicaCalcoloGruppo.getPoliticaCalcoloFiscale().setTotaleAnticipato(totAntGruppo.add(totAntBene));
			}
		}
		return simulazione;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * SimulazioneAmmortamentoManager#cancellaSimulazione(it .eurotn.panjea.beniammortizzabili2.domain.Simulazione)
	 */
	@Override
	public void cancellaSimulazione(Simulazione simulazione) {
		try {
			Integer id = simulazione.getId();
			// controllo se ho simulazioni collegate
			// e in caso le cancello ricorsivamente
			logger.debug("--> Enter cancellaSimulazione con id " + id);
			List<Simulazione> simulazioniCollegate = caricaSimulazioniCollegate(id);
			for (Simulazione simulazioneCollegata : simulazioniCollegate) {
				logger.debug("--> Richiamo la cancella per la simulazione collegata " + simulazioneCollegata.getId());
				cancellaSimulazione(simulazioneCollegata);
			}

			logger.debug("--> Cancello le aree contabili");
			beniAmmortizzabiliContabilitaManager.cancellaAreeContabiliSimulazione(simulazione.getId());

			logger.debug("--> Cancello la simulazione");
			// Cancello le politiche di calcolo. Le quote sono cancellate dalla
			// cascade sulle politiche
			List<PoliticaCalcolo> politiche = caricaPolitiche(id);
			for (PoliticaCalcolo politicaCalcolo : politiche) {
				panjeaDAO.delete(politicaCalcolo);
			}
			// Cancello la simulazione
			panjeaDAO.delete(simulazione);
			logger.debug("--> Exit cancellaSimulazione");
		} catch (DAOException e) {
			logger.error("--> Errore nel cancellare la simulazione con id " + simulazione.getId(), e);
			throw new RuntimeException("--> Errore nel cancellare la simulazione con id " + simulazione.getId(), e);
		}
	}

	/**
	 * Cariva l'anno dell'ultima simulazione consolidata.
	 *
	 * @return l'anno dell'ultima simulazione consolidata
	 */
	private Integer caricaAnnoUltimaSimulazione() {
		logger.debug("--> Enter caricaAnnoUltimaSimulazione");
		try {
			Query query = panjeaDAO.prepareNamedQuery("Simulazione.caricaAnnoUltimaSimulazione");
			query.setParameter("paramCodiceAzienda", getAzienda());
			Object ultimaSimulazione = null;
			ultimaSimulazione = query.getSingleResult();

			if (ultimaSimulazione == null) {
				logger.debug("--> nessuna simulazione non consolidata ");
				return null;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) ultimaSimulazione);
			logger.debug("--> Exit caricaAnnoUltimaSimulazione");
			return calendar.get(Calendar.YEAR);
		} catch (Exception e) {
			logger.error("--> errore nel caricare l'anno dell'ultima simulazione", e);
			throw new RuntimeException("Errore nel caricare l'anno dell'ultima simulazione", e);
		}
	}

	/**
	 * Restituisce l'anno dell'ultima simulazione consolidata.
	 *
	 * @return l'anno dell'ultima simulazione consolidata
	 */
	private Integer caricaAnnoUltimaSimulazioneConsolidata() {
		logger.debug("--> Enter caricaAnnoUltimaSimulazioneConsolidata");
		try {
			Query query = panjeaDAO.prepareNamedQuery("Simulazione.caricaAnnoUltimaSimulazioneConsolidata");
			query.setParameter("paramCodiceAzienda", getAzienda());
			Date dataUltimaSimulazioneConsolidata = (Date) query.getSingleResult();
			logger.debug("--> Data ultima simulazione consolidata " + dataUltimaSimulazioneConsolidata);
			if (dataUltimaSimulazioneConsolidata == null) {
				logger.debug("--> Exit caricaAnnoUltimaSimulazioneConsolidata");
				return null;
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dataUltimaSimulazioneConsolidata);
				logger.debug("--> Exit caricaAnnoUltimaSimulazioneConsolidata");
				return calendar.get(Calendar.YEAR);
			}
		} catch (Exception e) {
			logger.error("--> Errore nel caricaAnnoUltimaSimulazioneConsolidata", e);
			throw new RuntimeException("--> Errore nel caricaAnnoUltimaSimulazioneConsolidata", e);
		}
	}

	@Override
	public PoliticaCalcolo caricaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo) {
		logger.debug("--> Enter caricaPoliticaCalcolo");
		PoliticaCalcolo politicaCalcoloCaricata = null;
		try {
			politicaCalcoloCaricata = panjeaDAO.load(PoliticaCalcolo.class, politicaCalcolo.getId());
			logger.debug("--> Caricata la politicaCalcolo " + politicaCalcoloCaricata.getId());
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento della politicaCalcolo " + politicaCalcolo.getId(), e);
			throw new RuntimeException(
					"Errore durante il caricamento della politicaCalcolo " + politicaCalcolo.getId(), e);
		}
		logger.debug("--> Exit caricaPoliticaCalcolo");
		return politicaCalcoloCaricata;
	}

	@Override
	public PoliticaCalcoloBene caricaPoliticaCalcoloBeneByQuotaAmmortamento(QuotaAmmortamento quotaAmmortamento) {
		logger.debug("--> Enter caricaPoliticaCalcoloBeneByQuotaAmmortamento");
		PoliticaCalcoloBene politicaCalcoloBeneTrovata = null;
		try {
			Query query = null;
			if (quotaAmmortamento instanceof QuotaAmmortamentoFiscale) {
				query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloBene.caricaFiscaleByQuota");
			} else {
				query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloBene.caricaCivilisticaByQuota");
			}
			query.setParameter("paramIdQuota", quotaAmmortamento.getId());
			politicaCalcoloBeneTrovata = (PoliticaCalcoloBene) panjeaDAO.getSingleResult(query);
			logger.debug("--> Exit caricaPoliticaCalcoloBeneByQuotaAmmortamento");
		} catch (ObjectNotFoundException e) {
			logger.warn("Attenzione non esiste la politica di calcolo per la quota.", e);
		} catch (Exception e) {
			logger.error(
					"--> Errore durante il caricamento della politicaCalcolo per la quota " + quotaAmmortamento.getId(),
					e);
			throw new RuntimeException("Errore durante il caricamento della politicaCalcolo per la quota "
					+ quotaAmmortamento.getId(), e);
		}
		return politicaCalcoloBeneTrovata;
	}

	/**
	 * Carica le politiche di calcolo della simulazione.
	 *
	 * @param simulazioneId
	 *            la simulazione da cui caricare le politiche
	 * @return List PoliticaCalcolo
	 * @throws DAOException
	 */
	private List<PoliticaCalcolo> caricaPolitiche(Integer simulazioneId) {

		List<PoliticaCalcolo> result = new ArrayList<PoliticaCalcolo>();
		try {
			result.addAll(caricaPoliticheCalcoloGruppo(simulazioneId));
			result.addAll(caricaPoliticheCalcoloSpecie(simulazioneId));
			result.addAll(caricaPoliticheCalcoloSottoSpecie(simulazioneId));
			result.addAll(caricaPoliticheCalcoloBeni(simulazioneId));

			Collections.sort(result, new Comparator<PoliticaCalcolo>() {

				@Override
				public int compare(PoliticaCalcolo o1, PoliticaCalcolo o2) {
					return o1.getIndicePolitica().compareTo(o2.getIndicePolitica());
				}
			});
		} catch (Exception e) {
			logger.error("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
			throw new RuntimeException("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
		}

		return result;
	}

	/**
	 * Carica le politiche di calcolo solamente per i beni.
	 *
	 * @param simulazioneId
	 *            id della simulazione
	 * @return politiche caricate
	 */
	@SuppressWarnings("unchecked")
	private List<PoliticaCalcoloBene> caricaPoliticheBene(Integer simulazioneId) {
		logger.debug("--> Enter caricaPoliticheBene");
		try {
			Query query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloBene.caricaByIdSimulazione");
			query.setParameter("paramIdSimulazione", simulazioneId);

			List<PoliticaCalcoloBene> politiche = null;

			politiche = panjeaDAO.getResultList(query);

			logger.debug("--> Caricate " + politiche.size() + " politiche");
			logger.debug("--> Exit caricaPoliticheBene");
			return politiche;
		} catch (DAOException e) {
			logger.error("--> Errore nel caricare le politiche di calcolo per la simulazione " + simulazioneId, e);
			throw new RuntimeException("--> Errore nel caricare le politiche di calcolo per la simulazione "
					+ simulazioneId, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoliticaCalcolo> caricaPoliticheCalcoloBeni(Integer simulazioneId) {
		List<PoliticaCalcolo> result = new ArrayList<PoliticaCalcolo>();
		try {
			Query query = panjeaDAO
					.prepareQuery("select pc from PoliticaCalcoloBene pc inner join fetch pc.bene b left join fetch b.fornitore f left join fetch f.anagrafica a left join fetch a.sedeAnagrafica sa left join fetch sa.datiGeografici.cap left join fetch sa.datiGeografici.localita left join fetch pc.quotaCivilistica left join fetch pc.quotaFiscale where pc.simulazione.id = :paramIdSimulazione ");
			query.setParameter("paramIdSimulazione", simulazioneId);
			result = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
			throw new RuntimeException("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoliticaCalcolo> caricaPoliticheCalcoloGruppo(Integer simulazioneId) {
		List<PoliticaCalcolo> result = new ArrayList<PoliticaCalcolo>();
		try {
			Query query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloGruppo.caricaPoliticheSimulazione");
			query.setParameter("paramIdSimulazione", simulazioneId);
			result = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
			throw new RuntimeException("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoliticaCalcolo> caricaPoliticheCalcoloSottoSpecie(Integer simulazioneId) {
		List<PoliticaCalcolo> result = new ArrayList<PoliticaCalcolo>();
		try {
			Query query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloSottoSpecie.caricaPoliticheSimulazione");
			query.setParameter("paramIdSimulazione", simulazioneId);
			result = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
			throw new RuntimeException("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PoliticaCalcolo> caricaPoliticheCalcoloSpecie(Integer simulazioneId) {
		List<PoliticaCalcolo> result = new ArrayList<PoliticaCalcolo>();
		try {
			Query query = panjeaDAO.prepareNamedQuery("PoliticaCalcoloSpecie.caricaPoliticheSimulazione");
			query.setParameter("paramIdSimulazione", simulazioneId);
			result = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
			throw new RuntimeException("--> Errore nel caricare le politiche della simulazione " + simulazioneId, e);
		}

		return result;
	}

	@Override
	public Simulazione caricaSimulazione(Simulazione simulazione) {
		logger.debug("--> Enter caricaSimulazione");
		Simulazione simulazioneCaricata = null;
		try {
			simulazioneCaricata = panjeaDAO.load(Simulazione.class, simulazione.getId());
			logger.debug("--> Caricata la simulazione " + simulazioneCaricata.getId());
			List<PoliticaCalcolo> list = caricaPolitiche(simulazioneCaricata.getId());
			simulazioneCaricata.setPoliticheCalcolo(list);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento della simulazione " + simulazione.getId(), e);
			throw new RuntimeException("Errore durante il caricamento della simulazione " + simulazione.getId(), e);
		}
		simulazioneCaricata = calcolaTotaliSimulazione(simulazioneCaricata);

		simulazioneCaricata = updateAllowConsolida(simulazioneCaricata);
		logger.debug("--> Exit caricaSimulazione");
		return simulazioneCaricata;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Simulazione> caricaSimulazioni() {
		logger.debug("--> Enter caricaSimulazioni");
		try {
			Query query = panjeaDAO.prepareNamedQuery("Simulazione.caricaAll");
			query.setParameter("paramCodiceAzienda", getAzienda());
			List<Simulazione> simulazioni = null;

			simulazioni = panjeaDAO.getResultList(query);

			logger.debug("--> Caricate " + simulazioni.size() + " simulazioni");
			logger.debug("--> Exit caricaSimulazioni");
			return simulazioni;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle simulazioni", e);
			throw new RuntimeException("Errore durante il caricamento delle simulazioni", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Simulazione> caricaSimulazioniAnno(Integer anno) {
		logger.debug("--> Enter caricaSimulazioni");
		try {
			Query query = panjeaDAO.prepareNamedQuery("Simulazione.caricaByAnno");
			query.setParameter("paramCodiceAzienda", getAzienda());
			query.setParameter("paramAnno", anno);
			List<Simulazione> simulazioni = null;

			simulazioni = panjeaDAO.getResultList(query);

			logger.debug("--> Caricate " + simulazioni.size() + " simulazioni");
			logger.debug("--> Exit caricaSimulazioni");
			return simulazioni;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle simulazioni", e);
			throw new RuntimeException("Errore durante il caricamento delle simulazioni", e);
		}
	}

	/**
	 * Carica Simulazioni Collegate alla simulazione di riferimento.
	 *
	 * @param id
	 *            la simulazione di riferimento
	 * @return List Simulazione
	 * @throws DAOException
	 *             exception
	 */
	@SuppressWarnings("unchecked")
	private List<Simulazione> caricaSimulazioniCollegate(Integer id) throws DAOException {
		try {
			Query query = panjeaDAO.prepareNamedQuery("Simulazione.caricaSimulazioniCollegate");
			query.setParameter("paramIdSimulazioneRiferimento", id);
			query.setParameter("paramCodiceAzienda", getAzienda());
			return panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore nel caricare le simulazioni collegate", e);
			throw new RuntimeException("--> Errore nel caricare le simulazioni collegate", e);
		}
	}

	/**
	 * Carica tutte le simulazioni non consolidate nell'anno precedente a quello dato.
	 *
	 * @param annoRiferimento
	 *            anno di riferimento
	 * @return List Simulazione
	 */
	@SuppressWarnings("unchecked")
	private List<Simulazione> caricaSimulazioniNonConsolidateInAnnoPrecedente(Integer annoRiferimento) {
		logger.debug("--> Enter caricaSimulazioniNonConsolidateInAnnoPrecedente");

		List<Simulazione> list = new ArrayList<Simulazione>();
		try {
			Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
			list = hibernateSession.createCriteria(Simulazione.class)
					.add(Restrictions.eq(Simulazione.PROP_ANNO, annoRiferimento - 1))
					.add(Restrictions.eq(Simulazione.PROP_CONSOLIDATA, false)).list();
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle simulazioni non consolidate per l'anno: "
					+ (annoRiferimento - 1), e);
			throw new RuntimeException("Errore durante il caricamento delle simulazioni non consolidate per l'anno: "
					+ (annoRiferimento - 1));
		}

		logger.debug("--> Exit caricaSimulazioniNonConsolidateInAnnoPrecedente");
		return list;
	}

	@Override
	public void consolidaSimulazione(Simulazione simulazione) throws MancatoConsolidamentoAnnoPrecedenteException {
		logger.debug("--> Enter consolidaSimulazione");
		// Controllo se posso consolidare la simulazione

		// il campo formula 'anno' sulla simulazione, non e' valorizzato quando salvo la simulazione.
		Integer ultimoAnnoConsolidata = caricaAnnoUltimaSimulazioneConsolidata();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(simulazione.getData());
		int anno = calendar.get(Calendar.YEAR);
		if ((ultimoAnnoConsolidata != null) && (ultimoAnnoConsolidata != anno - 1)) {
			throw new MancatoConsolidamentoAnnoPrecedenteException();
		}

		simulazione.setConsolidata(true);

		operazioniBeneAmmortizzabileManager.initialize(simulazione);

		// Mi carico le politiche per recuperare le quote e consolidarle
		List<PoliticaCalcoloBene> politiche = caricaPoliticheBene(simulazione.getId());
		for (PoliticaCalcoloBene politica : politiche) {
			// TODO da implementare quando ci sar� il calcolo della parte
			// civilistica
			// politica.getQuotaCivilistica().setConsolidata(true);
			politica.getQuotaFiscale().setConsolidata(true);

			// Controllo se il bene risulta totalmento ammortizzato al
			// consolidamento e
			// in questo caso setto il flag ammortamento in corso a false per la
			// parte
			// civilistica e fiscale
			BeneAmmortizzabile bene = null;
			try {
				bene = panjeaDAO.load(BeneAmmortizzabile.class, politica.getBene().getId());
			} catch (ObjectNotFoundException e1) {
				logger.error("--> Errore durante il caricamento del bene " + politica.getBene().getId(), e1);
				throw new RuntimeException("Errore durante il salvataggio del bene " + politica.getBene().getId(), e1);
			}

			if (operazioniBeneAmmortizzabileManager.getTotaleAmmortamentoFiscale(bene.getId()).compareTo(
					operazioniBeneAmmortizzabileManager.getValoreBene(bene.getId())) >= 0) {
				bene.getDatiFiscali().setAmmortamentoInCorso(false);
			}

			// TODO implementare lo stesso controllo per la parte civilistica
			BeneAmmortizzabile beneAmmortizzabileSalvato = null;
			try {
				beneAmmortizzabileSalvato = panjeaDAO.save(bene);
			} catch (Exception e) {
				logger.error("--> Errore durante il salvataggio del bene", e);
				throw new RuntimeException("Errore durante il salvataggio del bene", e);
			}
			// carico il bene lite per associarlo alla politica
			BeneAmmortizzabileLite beneLite = null;
			try {
				beneLite = panjeaDAO.load(BeneAmmortizzabileLite.class, beneAmmortizzabileSalvato.getId());
			} catch (ObjectNotFoundException e) {
				logger.error("--> Errore durante il caricamento del beneLite " + bene.getId(), e);
				throw new RuntimeException("Errore durante il salvataggio del beneLite " + bene.getId(), e);
			}
			politica.setBene(beneLite);
			try {
				panjeaDAO.save(politica);
			} catch (Exception e) {
				logger.error("--> Errore durante il salvataggio della politica " + politica.getId(), e);
				throw new RuntimeException("Errore durante il salvataggio della politica " + politica.getId(), e);
			}
		}

		// salvo la simulazione
		try {
			panjeaDAO.save(simulazione);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della simulazione " + simulazione.getId(), e);
			throw new RuntimeException("Errore durante il salvataggio della simulazione " + simulazione.getId(), e);
		}

		// Carico le simulazioni dell'anno in corso e le cancello
		// cancellandole cancello anche le simulazioni collegate ad esse
		List<Simulazione> simulazioni = caricaSimulazioniAnno(simulazione.getAnno());
		for (Simulazione simulazioneDaEliminare : simulazioni) {
			if (!simulazioneDaEliminare.equals(simulazione)) {
				cancellaSimulazione(simulazioneDaEliminare);
			}
		}

		aggiornaBeniInAmmortamento();
		logger.debug("--> Exit consolidaSimulazione");
	}

	/**
	 * Crea una nuova <code>PoliticaCalcoloCivilistica</code> con i valori di default.
	 *
	 * @param beneAmmortizzabileLite
	 *            bene ammortizzabile
	 * @return nuova <code>PoliticaCalcoloCivilistica</code>
	 */
	private PoliticaCalcoloCivilistica creaNuovaPoliticaCalcoloCivilistica(BeneAmmortizzabileLite beneAmmortizzabileLite) {
		logger.debug("--> Enter creaNuovaPoliticaCalcoloCivilistica");

		PoliticaCalcoloCivilistica civilistica = new PoliticaCalcoloCivilistica();
		civilistica.setAmmortamentoOrdinario(true);
		civilistica.setMaggioreUtilizzo(false);
		civilistica.setMinoreUtilizzo(false);
		if (beneAmmortizzabileLite != null) {
			civilistica.setPercAmmortamentoOrdinario(beneAmmortizzabileLite.getDatiCivilistici()
					.getPercentualeAmmortamentoOrdinario());
			civilistica.setPercMaggioreUtilizzo(beneAmmortizzabileLite.getDatiCivilistici()
					.getPercentualeMaggioreUtilizzoBene());
			civilistica.setPercMinoreUtilizzo(beneAmmortizzabileLite.getDatiCivilistici()
					.getPercentualeMinoreUtilizzoBene());
		}

		logger.debug("--> Exit creaNuovaPoliticaCalcoloCivilistica");
		return civilistica;
	}

	/**
	 * Crea una nuova <code>PoliticaCalcoloFiscale</code> con i valori di default.
	 *
	 * @param beneAmmortizzabileLite
	 *            bene ammortizzabile
	 * @return nuova <code>PoliticaCalcoloFiscale</code>
	 */
	private PoliticaCalcoloFiscale creaNuovaPoliticaCalcoloFiscale(BeneAmmortizzabileLite beneAmmortizzabileLite) {
		logger.debug("--> Enter creaNuovaPoliticaCalcoloFiscale");

		PoliticaCalcoloFiscale fiscale = new PoliticaCalcoloFiscale();
		fiscale.setAmmortamentoOrdinario(true);
		fiscale.setAmmortamentoAccelerato(false);
		fiscale.setAmmortamentoAnticipato(false);
		fiscale.setAmmortamentoRidotto(false);
		if (beneAmmortizzabileLite != null) {
			fiscale.setPercAmmortamentoOrdinario(beneAmmortizzabileLite.getDatiFiscali()
					.getPercentualeAmmortamentoOrdinario());
			fiscale.setPercAmmortamentoAccelerato(beneAmmortizzabileLite.getDatiFiscali()
					.getPercentualeAmmortamentoAccelerato());
			fiscale.setPercAmmortamentoAnticipato(beneAmmortizzabileLite.getDatiFiscali()
					.getPercentualeAmmortamentoAnticipato());
		}

		logger.debug("--> Exit creaNuovaPoliticaCalcoloFiscale");
		return fiscale;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * SimulazioneAmmortamentoManager#creaSimulazione(java.lang .String, java.util.Date,
	 * it.eurotn.panjea.beniammortizzabili2.domain.Simulazione)
	 */
	@Override
	public Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento) {
		logger.debug("--> Enter creaSimulazione");

		// creo una nuova simulazione settando descrizione e data
		Simulazione simulazione = new Simulazione();
		simulazione.setDescrizione(descrizione);
		simulazione.setCodiceAzienda(getAzienda());
		simulazione.setData(data);
		simulazione.setSimulazioneRiferimento(simulazioneRiferimento);

		int indexPolitica = 0;

		// creo una politica calcolo per il gruppo
		Gruppo gruppo = tabelleBeniAmmortizzabiliManager.caricaGruppoAzienda();
		PoliticaCalcoloGruppo politicaCalcoloGruppo = new PoliticaCalcoloGruppo();
		politicaCalcoloGruppo.setGruppo(gruppo);
		politicaCalcoloGruppo.setIndicePolitica(indexPolitica);

		politicaCalcoloGruppo.setPoliticaCalcoloCivilistica(creaNuovaPoliticaCalcoloCivilistica(null));
		politicaCalcoloGruppo.setPoliticaCalcoloFiscale(creaNuovaPoliticaCalcoloFiscale(null));
		simulazione.addTopoliticheCalcolo(politicaCalcoloGruppo);

		// creo le politiche di calcolo per specie, sottospecie e beni
		List<BeneAmmortizzabileLite> beniDaAmmortizzare = beniAmmortizzabiliManager.caricaBeniDaAmmortizzareLite(data);

		Integer idSpecie = null;
		Integer idSottoSpecie = null;
		for (BeneAmmortizzabileLite beneAmmortizzabile : beniDaAmmortizzare) {
			indexPolitica++;
			// se la specie cambia creo la sua politica di calcolo e la aggiungo
			// alla simulazione
			if (beneAmmortizzabile.getSottoSpecie().getSpecie().getId() != idSpecie) {
				// salvo l'id della nuova specie
				idSpecie = beneAmmortizzabile.getSottoSpecie().getSpecie().getId();
				PoliticaCalcoloSpecie politicaCalcoloSpecie = new PoliticaCalcoloSpecie();
				politicaCalcoloSpecie.setSpecie(beneAmmortizzabile.getSottoSpecie().getSpecie());
				politicaCalcoloSpecie.setPoliticaCalcoloCivilistica(creaNuovaPoliticaCalcoloCivilistica(null));
				politicaCalcoloSpecie.setPoliticaCalcoloFiscale(creaNuovaPoliticaCalcoloFiscale(null));
				politicaCalcoloSpecie.setIndicePolitica(indexPolitica);
				simulazione.addTopoliticheCalcolo(politicaCalcoloSpecie);
			}

			// se la sottospecie cambia creo la sua politica di calcolo e la
			// aggiungo alla simulazione
			if (beneAmmortizzabile.getSottoSpecie().getId() != idSottoSpecie) {
				// salvo l'id della nuova sottospecie
				idSottoSpecie = beneAmmortizzabile.getSottoSpecie().getId();
				PoliticaCalcoloSottoSpecie politicaCalcoloSottoSpecie = new PoliticaCalcoloSottoSpecie();
				politicaCalcoloSottoSpecie.setSottoSpecie(beneAmmortizzabile.getSottoSpecie());
				politicaCalcoloSottoSpecie.setPoliticaCalcoloCivilistica(creaNuovaPoliticaCalcoloCivilistica(null));
				politicaCalcoloSottoSpecie.setPoliticaCalcoloFiscale(creaNuovaPoliticaCalcoloFiscale(null));
				politicaCalcoloSottoSpecie.setIndicePolitica(indexPolitica);
				simulazione.addTopoliticheCalcolo(politicaCalcoloSottoSpecie);
			}

			PoliticaCalcoloBene politicaCalcoloBene = new PoliticaCalcoloBene();
			politicaCalcoloBene.setBene(beneAmmortizzabile);

			BigDecimal importoBene = beneAmmortizzabile.getImportoSoggettoAdAmmortamento();
			if (beneAmmortizzabile.getVariazioniBene() != null) {
				importoBene = importoBene.add(beneAmmortizzabile.getVariazioniBene());
			}
			if (beneAmmortizzabile.getVenditeBene() != null) {
				importoBene = importoBene.subtract(beneAmmortizzabile.getVenditeBene());
			}
			// politicaCalcoloBene.setImportoSoggettoAmmortamento(importoBene);
			operazioniBeneAmmortizzabileManager.initialize(simulazione);
			politicaCalcoloBene.setImportoSoggettoAmmortamento(operazioniBeneAmmortizzabileManager
					.getValoreBene(politicaCalcoloBene.getBene().getId()));
			politicaCalcoloBene.setPoliticaCalcoloCivilistica(creaNuovaPoliticaCalcoloCivilistica(beneAmmortizzabile));
			politicaCalcoloBene.setPoliticaCalcoloFiscale(creaNuovaPoliticaCalcoloFiscale(beneAmmortizzabile));
			politicaCalcoloBene.setIndicePolitica(indexPolitica);
			simulazione.addTopoliticheCalcolo(politicaCalcoloBene);
		}
		// aggiorna il valore della propriet� transient allowConsolida
		simulazione = updateAllowConsolida(simulazione);

		// setta di default la propriet� transient dirty a true delle politiche
		// di calcolo create alla simulazione
		updatePoliticheCalcoloDirtyTrue(simulazione);

		logger.debug("--> Exit creaSimulazione");
		return simulazione;
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * SimulazioneAmmortamentoManager#creaSimulazione(java.lang .String, java.util.Date,
	 * it.eurotn.panjea.beniammortizzabili2.domain.Simulazione, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Simulazione creaSimulazione(String descrizione, Date data, Simulazione simulazioneRiferimento,
			Integer idSimulazioneOld) {
		logger.debug("--> Enter creaSimulazione");
		Simulazione simulazione;
		try {
			simulazione = panjeaDAO.load(Simulazione.class, idSimulazioneOld);
			Query query = panjeaDAO.prepareNamedQuery("PoliticaCalcolo.caricaPoliticheSimulazione");
			query.setParameter("paramIdSimulazione", simulazione.getId());
			List<PoliticaCalcolo> list = query.getResultList();
			simulazione.setPoliticheCalcolo(list);

			simulazione = removeIds(simulazione);
			simulazione.setDescrizione(descrizione);
			simulazione.setData(data);
			simulazione = updateAllowConsolida(simulazione);

			// associo alla nuova simulazione la simulazione di riferimento
			simulazione.setSimulazioneRiferimento(simulazioneRiferimento);

			// setta di default la propriet� transient dirty a true delle
			// politiche di calcolo create alla simulazione
			updatePoliticheCalcoloDirtyTrue(simulazione);

			logger.debug("--> Exit creaSimulazione");
			return simulazione;
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore durante il caricamento della simulazione " + idSimulazioneOld, e);
			throw new RuntimeException("Errore durante il caricamento della simulazione " + idSimulazioneOld);
		}
	}

	/**
	 * Cancella le quote civilistiche e fiscali orphan quando viene calcolata(salvata) una simulazione viene ogni volta
	 * creata una nuova quota che � associata ad una politica di calcolo. Rimangono quindi delle quote che non sono
	 * associate ad alcuna politica; questo metodo elimina queste quote.
	 */
	private void deleteQuoteAmmortamentoOrphans() {
		logger.debug("--> Enter deleteQuoteAmmortamentoOrphans");
		Query query = panjeaDAO.prepareNamedQuery("QuotaAmmortamento.deleteOrphans");
		int result = -1;
		try {
			result = panjeaDAO.executeQuery(query);
			logger.debug("--> risultato dell'esecuzione della cancellazione delle quote orfane " + result);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dele quote ammortamento orfane", e);
			throw new RuntimeException("--> Errore durante la cancellazione dele quote ammortamento orfane", e);
		}
		logger.debug("--> Exit deleteQuoteAmmortamentoOrphans");
	}

	/**
	 * Dal session context recupera l'utente callerPrincipal e da qui restituisce l'azineda.
	 *
	 * @return L'azienda dell'utente corrente
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 * Esegue il lock di Simulazione.
	 *
	 * @param simulazione
	 *            simulazione
	 * @return lock
	 */
	private ILock lockSimulazione(Simulazione simulazione) {
		try {
			logger.debug("--> Enter lockSimulazione");
			ILock lock = lockingService.lock(simulazione.getDomainClassName(), simulazione.getId(),
					simulazione.getVersion());
			logger.debug("--> Exit lockSimulazione");
			return lock;
		} catch (Exception ex) {
			logger.error("--> errore, eseguire il lock", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Rilascia il lock di {@link Simulazione}.
	 *
	 * @param lock
	 *            lock
	 */
	private void releaseLockSimulazione(ILock lock) {
		try {
			logger.debug("--> Enter releaseLockSimulazione");
			if (lock == null) {
				logger.debug("--> lock non presente");
				return;
			}
			lockingService.release(lock);
			logger.debug("--> Exit releaseLockSimulazione");
		} catch (Exception ex) {
			logger.error("--> errore, impossibile rilasciare il lock creato ", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Rimuove il valore di <code>id</code> e <code>versione</code> dalla simulazione. Rimuove ilvalore di
	 * <code>id</code>, <code>versione</code>, <code>PoliticaCalcoloCivilistica</code>,
	 * <code>PoliticaCalcoloFiscale</code> dalle politiche di calcolo. Inoltre se la politica di calcolo risulta essere
	 * istanza di PoliticaCalcoloBene viene rimossa la quota civilistica e quella fiscale.
	 *
	 * @param simulazione
	 *            simulazione
	 * @return simulazione
	 */
	private Simulazione removeIds(Simulazione simulazione) {
		simulazione.setId(null);
		simulazione.setVersion(null);

		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {
			politicaCalcolo.setId(null);
			politicaCalcolo.setPoliticaCalcoloCivilistica(new PoliticaCalcoloCivilistica());
			politicaCalcolo.setPoliticaCalcoloFiscale(new PoliticaCalcoloFiscale());
			politicaCalcolo.setSimulazione(simulazione);

			if (politicaCalcolo instanceof PoliticaCalcoloBene) {
				((PoliticaCalcoloBene) politicaCalcolo).setQuotaCivilistica(null);
				((PoliticaCalcoloBene) politicaCalcolo).setQuotaFiscale(null);
			}
		}

		return simulazione;
	}

	@Override
	public void salvaPoliticaCalcolo(PoliticaCalcolo politicaCalcolo) {
		logger.debug("--> Enter salvaPoliticaCalcolo");

		try {
			panjeaDAO.save(politicaCalcolo);
		} catch (DAOException e) {
			logger.error("--> errore il salvataggio della politica di calcolo", e);
			throw new RuntimeException("errore il salvataggio della politica di calcolo", e);
		}

		logger.debug("--> Exit salvaPoliticaCalcolo");
	}

	@Override
	public Simulazione salvaSimulazione(Simulazione simulazione) throws AreeContabiliSimulazioneException {
		return salvaSimulazione(simulazione, false);
	}

	@Override
	public Simulazione salvaSimulazione(Simulazione simulazione, boolean forzaCancellazioneAC)
			throws AreeContabiliSimulazioneException {
		long nowEnter = Calendar.getInstance().getTimeInMillis();
		logger.info("--> Enter salvaSimulazione " + nowEnter);

		/*
		 * esegue il lock di simulazione, il lock dovr� essere poi rilasciato al'uscita del metodo
		 */
		ILock lock = null;
		if (simulazione.getId() != null) {
			lock = lockSimulazione(simulazione);
		}

		// cancello le simulazioni legate se non sto salvando una nuova
		// simulazione
		if (simulazione.getId() != null) {
			List<Simulazione> listSimLegate;
			try {
				listSimLegate = caricaSimulazioniCollegate(simulazione.getId());
			} catch (DAOException e) {
				logger.error("--> Errore durante il caricamento delle simulazioni collegate alla simulazione "
						+ simulazione.getId(), e);
				logger.debug("--> Sblocca simulazione prima di rilanciare l'eccezione ");
				/* rilascio il lock */
				releaseLockSimulazione(lock);
				throw new RuntimeException(
						"Errore durante il caricamento delle simulazioni collegate alla simulazione "
								+ simulazione.getId());
			}
			for (Simulazione simulazione2 : listSimLegate) {
				cancellaSimulazione(simulazione2);
			}

			// dato che stò modificando la simulazione controllo se cancellare le aree contaibli associate
			List<AreaContabile> areeContabili = beniAmmortizzabiliContabilitaManager
					.caricaAreeContabiliSimulazione(simulazione.getId());
			if (areeContabili != null && !areeContabili.isEmpty()) {
				if (forzaCancellazioneAC) {
					beniAmmortizzabiliContabilitaManager.cancellaAreeContabiliSimulazione(simulazione.getId());
					simulazione = caricaSimulazione(simulazione);
				} else {
					throw new AreeContabiliSimulazioneException(areeContabili);
				}
			}
		}
		Simulazione simulazioneSalvata = null;
		try {
			simulazioneSalvata = panjeaDAO.save(simulazione);
			logger.debug("--> Salvata la simulazione " + simulazioneSalvata.getId());
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio della simulazione.", e);
			logger.debug("--> Sblocca simulazione prima di rilanciare l'eccezione ");
			/* rilascio il lock */
			releaseLockSimulazione(lock);
			throw new RuntimeException("Errore durante il salvataggio della simulazione.");
		}

		simulazioneSalvata.setPoliticheCalcolo(simulazione.getPoliticheCalcolo());

		if (simulazioneSalvata.getPoliticheCalcolo() == null) {
			logger.debug("--> Exit salvaSimulazione");
			logger.debug("--> Sblocca simulazione salvata prima di restituirla al metodo chiamante ");
			/* rilascio il lock */
			releaseLockSimulazione(lock);
			return caricaSimulazione(simulazioneSalvata);
		}

		operazioniBeneAmmortizzabileManager.initialize(simulazione);
		for (PoliticaCalcolo politicaCalcolo : simulazioneSalvata.getPoliticheCalcolo()) {
			long nowPoliticaEnter = Calendar.getInstance().getTimeInMillis();
			if ((politicaCalcolo.getPoliticaCalcoloCivilistica().isAmmortamentoOrdinario())
					|| (politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoOrdinario())
					|| (politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoAccelerato())
					|| (politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoAnticipato())
					|| (politicaCalcolo.getPoliticaCalcoloFiscale().isAmmortamentoRidotto())) {
				logger.debug("--> Inizio salvataggio politica di calcolo: " + nowPoliticaEnter);
				politicaCalcolo.setSimulazione(simulazioneSalvata);

				if (politicaCalcolo instanceof PoliticaCalcoloBene) {
					logger.debug("--> La politica di calcolo � una politica calcolo bene.");
					QuotaAmmortamentoFiscale quotaAmmortamentoFiscale = calcolaQuotaAmmortamentoFiscale((PoliticaCalcoloBene) politicaCalcolo);

					try {
						QuotaAmmortamentoFiscale quotaAmmortamentoFiscaleSalvata = panjeaDAO
								.save(quotaAmmortamentoFiscale);
						// TODO da modificare quando verr� implementato il
						// calcolo della quota civilistica
						((PoliticaCalcoloBene) politicaCalcolo).setQuotaCivilistica(null);
						((PoliticaCalcoloBene) politicaCalcolo).setQuotaFiscale(quotaAmmortamentoFiscaleSalvata);
					} catch (Exception e) {
						logger.error("--> Errore durante il salvataggio delle quote della politica di calcolo bene "
								+ politicaCalcolo.getId(), e);
						logger.debug("--> Sblocca simulazione salvata prima di rilanciare l'eccezione ");
						/* rilascio il lock */
						releaseLockSimulazione(lock);
						throw new RuntimeException(
								"Errore durante il salvataggio delle quote della politica di calcolo bene "
										+ politicaCalcolo.getId());
					}
				}
				PoliticaCalcolo politicaCalcoloSalvata = null;
				try {
					politicaCalcoloSalvata = panjeaDAO.save(politicaCalcolo);
					logger.debug("--> Salvata la Politica di calcolo " + politicaCalcoloSalvata.getId());
					// cancella le quote ammortamento orfane dato che quando
					// viene salvata una simulazione pi�
					// volte abbiamo la creazione di pi� quote
					deleteQuoteAmmortamentoOrphans();
				} catch (Exception e) {
					logger.error(
							"--> Errore durante il salvataggio della politica di calcolo " + politicaCalcolo.getId(), e);
					logger.debug("--> Sblocca simulazione salvata prima di rilanciare l'eccezione ");
					/* rilascio il lock */
					releaseLockSimulazione(lock);
					throw new RuntimeException("Errore durante il salvataggio della politica di calcolo "
							+ politicaCalcolo.getId());
				}
				long nowPoliticaExit = Calendar.getInstance().getTimeInMillis();
				logger.info("--> Fine salvataggio politica di calcolo: " + nowPoliticaExit + " Tempo impiegato: "
						+ (nowPoliticaExit - nowPoliticaEnter) + " millisecondi.");
			}
		}
		logger.debug("--> fine calcolo ammortamento, ricarica la simulazione ");
		simulazioneSalvata = caricaSimulazione(simulazioneSalvata);
		/* rilascio il lock */
		releaseLockSimulazione(lock);

		long nowExit = Calendar.getInstance().getTimeInMillis();
		logger.debug("--> Exit salvaSimulazione " + nowExit + " Tempo impiegato: " + (nowExit - nowEnter)
				+ " millisecondi.");
		return simulazioneSalvata;
	}

	@Override
	public void salvaSimulazioneNoCheck(Simulazione simulazione) {
		logger.debug("--> Enter salvaSimulazioneNoCheck");

		try {
			panjeaDAO.save(simulazione);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio della simulazione", e);
			throw new RuntimeException("errore durante il salvataggio della simulazione", e);
		}

		logger.debug("--> Exit salvaSimulazioneNoCheck");
	}

	/**
	 * Metodo che valorizza l'attributo transient allowConsolida di Simulazione; verifica se l'anno dell'ultima
	 * simulazione consolidata è il precedente dell'anno della simulazione.
	 *
	 * @param simulazione
	 *            la simulazione di cui aggiornare l'attributo allowConsolida
	 * @return simulazione aggiornata
	 */
	private Simulazione updateAllowConsolida(Simulazione simulazione) {
		logger.debug("--> Enter updateAllowConsolida");
		Integer ultimoAnnoConsolidato = caricaAnnoUltimaSimulazioneConsolidata();
		simulazione.setAllowConsolida(false);

		if ((simulazione.getData() != null)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(simulazione.getData());
			if (((ultimoAnnoConsolidato == null)) || ((calendar.get(Calendar.YEAR) - 1) == ultimoAnnoConsolidato)) {
				simulazione.setAllowConsolida(true);
			}
		}
		logger.debug("--> Exit updateAllowConsolida");
		return simulazione;
	}

	/**
	 * Setta la proprietà transient dirty di politicheCalcolo a true per marcare tutte le politiche di calcolo per il
	 * salvataggio di una simulazione creata.
	 *
	 * @param simulazione
	 *            la simulazione che contiene le politiche di calcolo
	 */
	private void updatePoliticheCalcoloDirtyTrue(Simulazione simulazione) {
		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {
			politicaCalcolo.setDirty(true);
		}
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * SimulazioneAmmortamentoManager#verificaAnnoVenditaBene (java.lang.Integer)
	 */
	@Override
	public void verificaAnnoVenditaBene(Integer annoVendita) throws VenditaInAnnoConsolidatoException,
	VenditaInAnnoSimulatoException {
		logger.debug("--> Enter verificaAnnoVenditaBene");
		Integer annoUltimaSimulazioneConsolidata = caricaAnnoUltimaSimulazioneConsolidata();
		if ((annoUltimaSimulazioneConsolidata != null)
				&& (annoVendita.intValue() <= annoUltimaSimulazioneConsolidata.intValue())) {
			logger.warn("--> Non � possibile vendere negli anni gi� consolidati !");
			throw new VenditaInAnnoConsolidatoException();
		}
		Integer annoUltimaSimulazione = caricaAnnoUltimaSimulazione();
		if ((annoUltimaSimulazione != null) && (annoVendita.intValue() <= annoUltimaSimulazione.intValue())) {
			logger.warn("--> E' stata eseguita una vendita in una simulazione !");
			throw new VenditaInAnnoSimulatoException();
		}
	}

	/*
	 * @seeit.eurotn.panjea.beniammortizzabili2.manager.interfaces.
	 * SimulazioneAmmortamentoManager#verificaNuovaSimulazione (java.lang.Integer) TODO E' consigliabile gestire gli
	 * errori attraverso dell'eccezioni e non con valori integer
	 */
	@Override
	public Object verificaNuovaSimulazione(Integer anno) {
		logger.debug("--> Enter verificaNuovaSimulazione");

		// ricerca l'anno dell'ultima simulazione consolidata
		Integer annoUltimaConsolidata = caricaAnnoUltimaSimulazioneConsolidata();
		if (annoUltimaConsolidata == null) {
			// se non esiste nessuna simulazione consolidata nell'anno
			// precedente ritorno l'elenco delle simulazioni
			// dell'anno precedente, se non ne esite nessuna restituisce una
			// List vuota
			List<Simulazione> simulazioniPrecedenti = caricaSimulazioniNonConsolidateInAnnoPrecedente(anno);
			return simulazioniPrecedenti;
		} else {
			// se l'anno della nuova simulazione � minore dell'anno dell'ultima
			// simulazione consolidata
			// non posso creare la nuova simulazione e ritorno come errore un
			// new Integer(0)
			if (anno < annoUltimaConsolidata) {
				return new Integer(0);
			} else {
				// se l'anno scelto per la nuova simulazione � il seguente a
				// quello dell'ultima simulazione
				// consolidata posso crearla.
				if (annoUltimaConsolidata.compareTo(anno - 1) == 0) {
					return new ArrayList<Simulazione>();
				} else {
					// se l'anno scelto non � il seguente a quello dell'ultima
					// simulazione consolidata
					// verifico se ci sono simulazioni non consolidate prima
					// dell'anno scelto
					List<Simulazione> listSimPrec = caricaSimulazioniNonConsolidateInAnnoPrecedente(anno);
					// se non ci sono simulazioni non consolidate ritorno errore
					if (listSimPrec.size() == 0) {
						return new Integer(1);
					} else {
						// se ci sono delle simulazioni non consolidate prima
						// dell'anno scelto per la nuova
						// simulazione ritorno la lista trovata
						return listSimPrec;
					}
				}
			}

		}

	}
}
