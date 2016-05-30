/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.test;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.StaleObjectStateException;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliSimulazioneException;
import it.eurotn.panjea.beniammortizzabili.exception.BeniAmmortizzabiliException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileConFigli;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.DatiCivilistici;
import it.eurotn.panjea.beniammortizzabili2.domain.DatiFiscali;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.panjea.beniammortizzabili2.service.interfaces.BeniAmmortizzabiliService;
import it.eurotn.panjea.beniammortizzabili2.util.SituazioneBene;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.naming.InitialContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

/**
 *
 * @author
 * @version 1.0, 05/ott/06
 *
 */
public class TestBeniAmmortizzabili extends TestCase {

	private static Logger logger = Logger.getLogger(TestBeniAmmortizzabili.class);
	private BeniAmmortizzabiliService beniAmmortizzabiliService;

	/**
	 * Crea un bene ammortizzabile in base ai valori specificati.
	 *
	 * @param anno
	 *            anno
	 * @param importoFatturatoAcquisto
	 *            importoFatturatoAcquisto
	 * @param importoSoggettoAdAmmortamento
	 *            importoSoggettoAdAmmortamento
	 * @param percentualeContributo
	 *            percentualeContributo
	 * @param percentualeUsoPromiscuo
	 *            percentualeUsoPromiscuo
	 * @param valoreAcquisto
	 *            valoreAcquisto
	 * @param percentualeAmmortamentoAcceleratoCivilistico
	 *            percentualeAmmortamentoAcceleratoCivilistico
	 * @param percentualeAmmortamentoAnticipatoCivilistico
	 *            percentualeAmmortamentoAnticipatoCivilistico
	 * @param percentualeAmmortamentoOrdinarioCivilistico
	 *            percentualeAmmortamentoOrdinarioCivilistico
	 * @param percentualeAmmortamentoAcceleratoFiscale
	 *            percentualeAmmortamentoAcceleratoFiscale
	 * @param percentualeAmmortamentoAnticipatoFiscale
	 *            percentualeAmmortamentoAnticipatoFiscale
	 * @param percentualeAmmortamentoOrdinarioFiscale
	 *            percentualeAmmortamentoOrdinarioFiscale
	 * @param sottoSpecieId
	 *            sottoSpecieId
	 * @return bene creato
	 * @throws RemoteException
	 *             RemoteException
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 * @throws ObjectNotFoundException
	 *             ObjectNotFoundException
	 */
	private BeneAmmortizzabile createBeneAmmortizzabile(int anno, double importoFatturatoAcquisto,
			double importoSoggettoAdAmmortamento, double percentualeContributo, double percentualeUsoPromiscuo,
			double valoreAcquisto, double percentualeAmmortamentoAcceleratoCivilistico,
			double percentualeAmmortamentoAnticipatoCivilistico, double percentualeAmmortamentoOrdinarioCivilistico,
			double percentualeAmmortamentoAcceleratoFiscale, double percentualeAmmortamentoAnticipatoFiscale,
			double percentualeAmmortamentoOrdinarioFiscale, int sottoSpecieId) throws RemoteException,
			BeniAmmortizzabiliException, ObjectNotFoundException {
		BeneAmmortizzabile beneAmmortizzabile = new BeneAmmortizzabile();
		beneAmmortizzabile.setId(null);
		beneAmmortizzabile.setVersion(0);
		beneAmmortizzabile.setAcquistatoUsato(false);
		beneAmmortizzabile.setAnnoAcquisto(anno);
		beneAmmortizzabile.setBeneDiProprieta(true);
		beneAmmortizzabile.setBeneInLeasing(false);
		beneAmmortizzabile.setBeneMateriale(false);
		beneAmmortizzabile.setDataInizioAmmortamento(new Date());
		beneAmmortizzabile.setDescrizione("bene ammortizzabile test");
		beneAmmortizzabile.setFabbricato(false);
		beneAmmortizzabile.setImportoFatturaAcquisto(BigDecimal.valueOf(importoFatturatoAcquisto));
		beneAmmortizzabile.setImportoSoggettoAdAmmortamentoSingolo(BigDecimal.valueOf(importoSoggettoAdAmmortamento));
		beneAmmortizzabile.setManutenzione(false);
		beneAmmortizzabile.setNumeroProtocolloAcquisto("12a");
		beneAmmortizzabile.setNumeroRegistro(12);
		beneAmmortizzabile.setPercentualeContributo(BigDecimal.valueOf(percentualeContributo));
		beneAmmortizzabile.setPercentualeUsoPromiscuo(BigDecimal.valueOf(percentualeUsoPromiscuo));
		beneAmmortizzabile.setSoggettoAContributo(false);
		beneAmmortizzabile.setStampaSuRegistriBeni(false);
		beneAmmortizzabile.setStampaSuRegistriInventari(false);
		beneAmmortizzabile.setValoreAcquisto(BigDecimal.valueOf(valoreAcquisto));

		DatiCivilistici datiCivilistici = new DatiCivilistici();
		datiCivilistici.setAmmortamentoInCorso(true);
		datiCivilistici.setPercentualeMaggioreUtilizzoBene(percentualeAmmortamentoAcceleratoCivilistico);
		datiCivilistici.setPercentualeMaggioreUtilizzoBene(percentualeAmmortamentoAnticipatoCivilistico);
		datiCivilistici.setPercentualeAmmortamentoOrdinario(percentualeAmmortamentoOrdinarioCivilistico);
		beneAmmortizzabile.setDatiCivilistici(datiCivilistici);

		DatiFiscali datiFiscali = new DatiFiscali();
		datiFiscali.setAmmortamentoInCorso(true);
		datiFiscali.setPercentualeAmmortamentoAccelerato(percentualeAmmortamentoAcceleratoFiscale);
		datiFiscali.setPercentualeAmmortamentoAnticipato(percentualeAmmortamentoAnticipatoFiscale);
		datiFiscali.setPercentualeAmmortamentoOrdinario(percentualeAmmortamentoOrdinarioFiscale);
		beneAmmortizzabile.setDatiFiscali(datiFiscali);

		SottoSpecie sottoSpecie = beniAmmortizzabiliService.caricaSottoSpecie(sottoSpecieId);

		beneAmmortizzabile.setSottoSpecie(sottoSpecie);
		beneAmmortizzabile.setFornitore(null);
		beneAmmortizzabile.setBenePadre(null);
		beneAmmortizzabile.setUbicazione(null);
		return beneAmmortizzabile;

	}

	/**
	 * Carica tutti i beni ammortizzabili.
	 *
	 * @return beni caricati
	 * @throws RemoteException
	 *             RemoteException
	 * @throws AnagraficaServiceException
	 *             AnagraficaServiceException
	 */
	private List<BeneAmmortizzabileLite> getBeniAmmortizzabili() throws RemoteException, AnagraficaServiceException {
		logger.debug("--> Enter getBeniAmmortizzabili");
		Map<String, Object> map = new TreeMap<String, Object>();
		List<BeneAmmortizzabileLite> list = beniAmmortizzabiliService.ricercaBeniAmmortizzabili(map);
		logger.debug("--> Exit getBeniAmmortizzabili #BeneAmmortizzabile " + list.size());
		return list;
	}

	/**
	 * Carica le vendite del bene ammortizzabile.
	 *
	 * @param beneAmmortizzabileId
	 *            id del bene di riferimento
	 * @return vendite caricate
	 * @throws Exception
	 *             Exception
	 */
	private List<VenditaBene> getVenditeBene(int beneAmmortizzabileId) throws Exception {
		logger.debug("--> Enter getVenditeBene");
		BeneAmmortizzabile beneAmmortizzabile = new BeneAmmortizzabile();
		beneAmmortizzabile.setId(beneAmmortizzabileId);
		List<VenditaBene> list = beniAmmortizzabiliService.caricaVenditeBene(beneAmmortizzabile);

		logger.debug("--> Exit getVenditeBene");
		return list;
	}

	@Override
	protected void setUp() throws Exception {
		System.setProperty("java.security.auth.login.config", TestBeniAmmortizzabili.class.getClassLoader()
				.getResource("auth.conf").getPath());

		String username = "europa#TECHNOB#Panjea#it";
		String credential = "europasw";
		char[] password = credential.toCharArray();
		BasicConfigurator.configure();
		FileInputStream is = new FileInputStream(TestBeniAmmortizzabili.class.getClassLoader()
				.getResource("beniLog4j.properties").getPath());
		Properties logProperties = new Properties();
		logProperties.load(is);
		BasicConfigurator.resetConfiguration();
		PropertyConfigurator.configure(logProperties);
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, password);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("other", passwordHandler);
			loginContext.login();
		} catch (LoginException e) {
			fail(e.getMessage());
		}

		is = new FileInputStream(TestBeniAmmortizzabili.class.getClassLoader().getResource("jndi.properties").getPath());
		Properties jndiProperties = new Properties();
		jndiProperties.load(is);
		InitialContext ic = new InitialContext(jndiProperties);
		beniAmmortizzabiliService = (BeniAmmortizzabiliService) ic.lookup("Panjea.BeniAmmortizzabiliService");
	}

	/**
	 * Test per l'aggiornamento dei beni che sono in ammortamento.
	 */
	public void testAggiornaBeniInAmmortamento() {
		beniAmmortizzabiliService.aggiornaBeniInAmmortamento();
	}

	/**
	 * Test per la cancellazione di una simulazione.
	 *
	 * @throws RemoteException
	 *             Remote
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 */
	public void testCancellaSimulazione() throws RemoteException, BeniAmmortizzabiliException {
		long nowEnter = Calendar.getInstance().getTimeInMillis();
		List<Simulazione> simulazioni = beniAmmortizzabiliService.caricaSimulazioni();
		assertTrue("Non ho simulazioni da cancellare", simulazioni.size() > 0);
		Simulazione simulazione = simulazioni.get(0);
		logger.debug("--> Cancello la simulaziione con id " + simulazione.getId());
		beniAmmortizzabiliService.cancellaSimulazione(simulazione);
		logger.debug("--> Exit testCancellaSimulazione : Tempo impiegato: "
				+ (Calendar.getInstance().getTimeInMillis() - nowEnter) + " millisecondi.");
	}

	/**
	 * Carica la situazione dei beni.
	 */
	public void testCaricaSituazioneBeni() {

		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("fornitore", -1);
		params.put("azienda", "AESSE");
		params.put("visualizzafigli", true);

		List<SituazioneBene> list = beniAmmortizzabiliService.caricaSituazioneBeni(params);

		System.out.println("Numero beni: " + list.size());
		for (SituazioneBene situazioneBene : list) {
			System.out.println(situazioneBene.getBene().getSottoSpecie().getSpecie().getCodice() + "/"
					+ situazioneBene.getBene().getSottoSpecie().getCodice() + " - "
					+ situazioneBene.getBene().getCodice() + " - " + situazioneBene.getBene().getDescrizione() + " - "
					+ situazioneBene.getImportoSoggettoAdAmmortamento());

			for (SituazioneBene situazioneBeneFiglio : situazioneBene.getBeniFigli()) {
				System.out.println("        Bene figlio: " + situazioneBeneFiglio.getBene().getCodice() + " - "
						+ situazioneBeneFiglio.getBene().getDescrizione());
			}
		}
	}

	/**
	 * Carica tutte le specie del gruppo assegnato all'azienda.
	 */
	public void testCaricaSpecieAzienda() {
		logger.debug("--> Enter testCaricaSpecieAzienda");
		/* carica i dati azienda */
		Gruppo gruppo = null;
		try {
			gruppo = beniAmmortizzabiliService.caricaGruppoAzienda();
		} catch (Exception e) {
			logger.error("--> errore ", e);
			fail(e.getMessage());
		}
		List<Specie> speci = null;
		try {
			speci = beniAmmortizzabiliService.caricaSpeci("codice", null);
			for (Specie specie : speci) {
				/* controllo della corrispondenza del codice azienda */
				assertEquals(specie.getGruppo(), gruppo);
			}
		} catch (Exception e) {
			logger.error("--> errore ", e);
			fail(e.getMessage());
		}
		logger.debug("--> Exit testCaricaSpecieAzienda");
	}

	/**
	 * Test per la creazione e salvataggio di una simulazione.
	 *
	 * @throws RemoteException
	 *             RemoteException
	 */
	public void testCreaESalvaSimulazione() throws RemoteException {
		long nowEnter = Calendar.getInstance().getTimeInMillis();
		logger.debug("--> Enter testSalvaSimulazione " + nowEnter);

		Simulazione simulazione = beniAmmortizzabiliService.creaSimulazione("Prova", Calendar.getInstance().getTime(),
				null);

		logger.debug("--> Politiche di calcolo da elaborare: " + simulazione.getPoliticheCalcolo().size());
		// int index = 0;
		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {
			// index++;
			// if (index == 101) break;
			politicaCalcolo.setDirty(true);

			if (politicaCalcolo instanceof PoliticaCalcoloBene) {
				((PoliticaCalcoloBene) politicaCalcolo).getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
				((PoliticaCalcoloBene) politicaCalcolo).getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(12.0);
			}
		}

		try {
			long nowSalvaEnter = Calendar.getInstance().getTimeInMillis();
			logger.error("--> Inizio salvataggio simulazione " + nowSalvaEnter);
			simulazione = beniAmmortizzabiliService.salvaSimulazione(simulazione);
			logger.debug("--> Salvata " + simulazione.getId());
			long nowSalvaExit = Calendar.getInstance().getTimeInMillis();
			logger.error("--> Fine salvataggio simulazione " + nowSalvaExit + " Tempo impiegato: "
					+ (nowSalvaExit - nowSalvaEnter) + " millisecondi.");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		long nowExit = Calendar.getInstance().getTimeInMillis();
		logger.error("--> Exit testSalvaSimulazione " + nowExit + " Tempo impiegato: " + (nowExit - nowEnter)
				+ " millisecondi.");
	}

	/**
	 * Test per la creazione e salvataggio di una simulazione.
	 *
	 * @throws RemoteException
	 *             RemoteException
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 * @throws ObjectNotFoundException
	 *             ObjectNotFoundException
	 */
	public void testCreaESalvaSimulazioneDaSimulazioneEsistente() throws RemoteException, BeniAmmortizzabiliException,
	ObjectNotFoundException {
		long nowEnter = Calendar.getInstance().getTimeInMillis();
		logger.debug("--> Enter testCreaESalvaSimulazioneDaSimulazioneEsistente " + nowEnter);

		Simulazione simulazioneBase = new Simulazione();
		simulazioneBase.setId(43);
		Simulazione simulazioneNuova = beniAmmortizzabiliService.creaSimulazione("Prova2", Calendar.getInstance()
				.getTime(), simulazioneBase);

		int index = 0;
		for (PoliticaCalcolo politicaCalcolo : simulazioneNuova.getPoliticheCalcolo()) {
			index++;
			if (index == 101) {
				break;
			}
			politicaCalcolo.setDirty(true);

			if (politicaCalcolo instanceof PoliticaCalcoloBene) {
				((PoliticaCalcoloBene) politicaCalcolo).getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
				((PoliticaCalcoloBene) politicaCalcolo).getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(12.0);
			}
		}

		try {
			long nowSalvaEnter = Calendar.getInstance().getTimeInMillis();
			logger.error("--> Inizio salvataggio simulazione " + nowSalvaEnter);
			Simulazione simulazione;
			simulazione = beniAmmortizzabiliService.salvaSimulazione(simulazioneNuova);
			logger.debug("--> Salvata " + simulazione.getId());
			long nowSalvaExit = Calendar.getInstance().getTimeInMillis();
			logger.error("--> Fine salvataggio simulazione " + nowSalvaExit + " Tempo impiegato: "
					+ (nowSalvaExit - nowSalvaEnter) + " millisecondi.");
		} catch (Exception e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

		long nowExit = Calendar.getInstance().getTimeInMillis();
		logger.debug("--> Exit testCreaESalvaSimulazioneDaSimulazioneEsistente " + nowExit + " Tempo impiegato: "
				+ (nowExit - nowEnter) + " millisecondi.");
	}

	/**
	 * Test per la creazione del gruppo.
	 */
	public void testCreaGruppo() {
		Gruppo gruppo = new Gruppo();
		gruppo.setCodice("G1");
		gruppo.setDescrizione("Gruppo 01");

		Gruppo gruppoSalvato = null;
		gruppoSalvato = beniAmmortizzabiliService.salvaGruppo(gruppo);
		if (gruppoSalvato.isNew()) {
			fail("gruppo non salvato");
		}
	}

	/**
	 * Test per la creazione di una nuova simulazione.
	 */
	public void testCreaNuovaSimulazione() {
		logger.debug("--> Enter testCreaNuovaSimulazione");

		try {
			beniAmmortizzabiliService.creaSimulazione("Prova", Calendar.getInstance().getTime(), null);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		logger.debug("--> Exit testCreaNuovaSimulazione");
	}

	/**
	 * Esegue il caricamento dei beni di default per i test.
	 *
	 * @throws RemoteException
	 *             RemoteException
	 * @throws AnagraficaServiceException
	 *             AnagraficaServiceException
	 * @throws DAOException
	 *             DAOException
	 */
	public void testFillDataBaseBeniAmmortamento() throws RemoteException, AnagraficaServiceException, DAOException {
		logger.debug("--> Enter testFillDataBaseBeniAmmortamento");

		beniAmmortizzabiliService.fillDataBaseBeniAmmortamento();

		logger.debug("--> Exit testFillDataBaseBeniAmmortamento");
	}

	/**
	 * Esegue il ricalcolo della simulazione in modo asincrono mediante JMS, eseguendo una modifica alla vendita e
	 * forzandone il ricalcolo.
	 *
	 * @throws Exception
	 *             Exception
	 */
	public void testRicalcoloSimulazione() throws Exception {
		logger.debug("--> Enter testRicalcoloSimulazione");
		List<BeneAmmortizzabileLite> beni = getBeniAmmortizzabili();
		BeneAmmortizzabileLite beneAmmortizzabile = beni.get(0);
		VenditaBene vendita = getVenditeBene(beneAmmortizzabile.getId()).get(0);
		vendita = beniAmmortizzabiliService.salvaVenditaBene(vendita, true);
		assertTrue(true);
		logger.debug("--> Exit testRicalcoloSimulazione");
	}

	/**
	 * Test per la ricerca delle quote di ammortamento fiscali.
	 */
	public void testRicercaAmmortamentiFiscali() {
		logger.debug("--> Enter createBeneAmmortizzabile");
		CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili = new CriteriaRicercaBeniAmmortizzabili();
		criteriaRicercaBeniAmmortizzabili.setAnnoAmmortamento(2005);
		Specie specieIniziale = new Specie();
		specieIniziale.setId(new Integer(1009));
		specieIniziale.setCodice("1");
		criteriaRicercaBeniAmmortizzabili.setSpecieIniziale(specieIniziale);
		Specie specieFinale = new Specie();
		specieFinale.setId(new Integer(1010));
		specieFinale.setCodice("2");
		criteriaRicercaBeniAmmortizzabili.setSpecieFinale(specieFinale);
		List<QuotaAmmortamentoFiscale> list = null;
		try {
			list = beniAmmortizzabiliService.ricercaQuoteAmmortamento(criteriaRicercaBeniAmmortizzabili);
			if (list.size() == 0) {
				fail("Nessun risultato ottenuto");
			}
		} catch (Exception e) {
			logger.error("--> errore ", e);
			fail(e.getMessage());
		}
		for (QuotaAmmortamentoFiscale quotaAmmortamento : list) {

			assertEquals("Anno errato", criteriaRicercaBeniAmmortizzabili.getAnnoAmmortamento().intValue(), 2006);
			assertTrue("Intervallo non rispettato  "
					+ quotaAmmortamento.getBeneAmmortizzabile().getSottoSpecie().getSpecie().getCodice() + " - "
					+ specieIniziale.getCodice(), quotaAmmortamento.getBeneAmmortizzabile().getSottoSpecie()
					.getSpecie().getCodice().compareToIgnoreCase(specieIniziale.getCodice()) >= 0);
			assertTrue("Intervallo non rispettato "
					+ quotaAmmortamento.getBeneAmmortizzabile().getSottoSpecie().getSpecie().getCodice() + " - "
					+ specieFinale.getCodice(), quotaAmmortamento.getBeneAmmortizzabile().getSottoSpecie().getSpecie()
					.getCodice().compareToIgnoreCase(specieFinale.getCodice()) <= 0);
		}
		logger.debug("--> Exit testRicercaAmmortamentiFiscali");
	}

	/**
	 * Test per la ricerca dei beni ammoritzzabili.
	 *
	 * @throws RemoteException
	 *             RemoteException
	 * @throws AnagraficaServiceException
	 *             AnagraficaServiceException
	 */
	public void testRicercaBeniAmmortizzabili() throws RemoteException, AnagraficaServiceException {
		logger.debug("--> Enter testRicercaBeniAmmortizzabili");
		Map<String, Object> map = new HashMap<String, Object>();
		List<BeneAmmortizzabileLite> list = beniAmmortizzabiliService.ricercaBeniAmmortizzabili(map);
		assertTrue("ricerca di tutti i beni fallita", list.size() != 0);
		for (BeneAmmortizzabileLite lite : list) {
			map.put(BeneAmmortizzabileLite.PROP_CODICE, lite.getCodice());
			map.put(BeneAmmortizzabileLite.PROP_DESCRIZIONE, lite.getDescrizione());
			map.put(BeneAmmortizzabileLite.PROP_BENE_DI_PROPRIETA, lite.isBeneDiProprieta());
			map.put(BeneAmmortizzabileLite.PROP_BENE_IN_LEASING, lite.isBeneInLeasing());
			List<BeneAmmortizzabileLite> list2 = beniAmmortizzabiliService.ricercaBeniAmmortizzabili(map);
			assertEquals("Ricerca del singolo bene fallita", list2.size(), 1);
			BeneAmmortizzabileLite ammortizzabileLite = list2.get(0);
			assertEquals("Corrispondenza del bene ricercato fallita ", lite, ammortizzabileLite);
			break;
		}
	}

	/**
	 * Test per la ricerca dei beni nella rubrica dei beni.
	 */
	public void testRicercaBeniAmmortizzabiliPerStampe() {
		logger.debug("--> Enter testRicercaBeniAmmortizzabili");
		CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili = new CriteriaRicercaBeniAmmortizzabili();
		criteriaRicercaBeniAmmortizzabili.setAnno(2006);
		criteriaRicercaBeniAmmortizzabili.setAmmortamentoInCorso(true);
		criteriaRicercaBeniAmmortizzabili.setBeniEliminati(false);
		Ubicazione ubicazione = new Ubicazione();
		ubicazione.setId(1);
		criteriaRicercaBeniAmmortizzabili.setUbicazione(ubicazione);
		FornitoreLite fornitoreLite = new FornitoreLite();
		fornitoreLite.setId(2);
		criteriaRicercaBeniAmmortizzabili.setFornitore(fornitoreLite);
		SottoSpecie sottoSpecieIniziale = new SottoSpecie();
		sottoSpecieIniziale.setId(1);
		sottoSpecieIniziale.setCodice("1");
		SottoSpecie sottoSpecieFinale = new SottoSpecie();
		sottoSpecieFinale.setId(4);
		sottoSpecieFinale.setCodice("2");
		criteriaRicercaBeniAmmortizzabili.setSottoSpecieIniziale(sottoSpecieIniziale);
		criteriaRicercaBeniAmmortizzabili.setSottoSpecieFinale(sottoSpecieFinale);
		Specie specieIniziale = new Specie();
		specieIniziale.setId(1);
		specieIniziale.setCodice("1");
		criteriaRicercaBeniAmmortizzabili.setSpecieIniziale(specieIniziale);
		Specie specieFinale = new Specie();
		specieFinale.setId(4);
		specieFinale.setCodice("2");
		criteriaRicercaBeniAmmortizzabili.setSpecieFinale(specieFinale);
		List<BeneAmmortizzabileConFigli> list = null;
		try {
			list = beniAmmortizzabiliService.ricercaRubricaBeni(criteriaRicercaBeniAmmortizzabili);
		} catch (Exception e) {
			logger.error("--> ", e);
			fail(e.getMessage());
		}
		for (BeneAmmortizzabileConFigli beneAmmortizzabile : list) {
			assertEquals("Anno errato", beneAmmortizzabile.getBeneAmmortizzabile().getAnnoAcquisto().intValue(), 2006);
			assertEquals("Ammortamento Civilistico errato", beneAmmortizzabile.getBeneAmmortizzabile()
					.getDatiCivilistici().isAmmortamentoInCorso(), true);
			assertEquals("Ammortamento Fiscale errato", beneAmmortizzabile.getBeneAmmortizzabile().getDatiFiscali()
					.isAmmortamentoInCorso(), true);
			assertEquals("Fornitore errato",
					beneAmmortizzabile.getBeneAmmortizzabile().getFornitore().getId().equals(fornitoreLite.getId()));
			assertEquals("Ubicazione errata", beneAmmortizzabile.getBeneAmmortizzabile().getUbicazione().getId()
					.equals(ubicazione.getId()));
			assertTrue("Intervallo non rispettato  "
					+ beneAmmortizzabile.getBeneAmmortizzabile().getSottoSpecie().getSpecie().getCodice() + " - "
					+ specieIniziale.getCodice(), beneAmmortizzabile.getBeneAmmortizzabile().getSottoSpecie()
					.getSpecie().getCodice().compareToIgnoreCase(specieIniziale.getCodice()) >= 0);
			assertTrue("Intervallo non rispettato "
					+ beneAmmortizzabile.getBeneAmmortizzabile().getSottoSpecie().getSpecie().getCodice() + " - "
					+ specieFinale.getCodice(), beneAmmortizzabile.getBeneAmmortizzabile().getSottoSpecie().getSpecie()
					.getCodice().compareToIgnoreCase(specieFinale.getCodice()) <= 0);
		}
		logger.debug("--> Exit testRicercaBeniAmmortizzabiliPerStampe");
	}

	/**
	 * Test per la ricerca delle vendite del bene.
	 */
	public void testRicercaVenditaBene() {
		logger.debug("--> Enter testRicercaVenditaBene");
		CriteriaRicercaBeniAmmortizzabili criteriaRicercaBeniAmmortizzabili = new CriteriaRicercaBeniAmmortizzabili();
		criteriaRicercaBeniAmmortizzabili.setAnno(2006);
		criteriaRicercaBeniAmmortizzabili.setAmmortamentoInCorso(true);
		criteriaRicercaBeniAmmortizzabili.setBeniEliminati(false);
		criteriaRicercaBeniAmmortizzabili.setFornitore(null);
		criteriaRicercaBeniAmmortizzabili.setSottoSpecieIniziale(null);
		criteriaRicercaBeniAmmortizzabili.setSottoSpecieFinale(null);
		Specie specieIniziale = new Specie();
		specieIniziale.setId(new Integer(1009));
		specieIniziale.setCodice("1");
		criteriaRicercaBeniAmmortizzabili.setSpecieIniziale(specieIniziale);
		Specie specieFinale = new Specie();
		specieFinale.setId(new Integer(1010));
		specieFinale.setCodice("2");
		criteriaRicercaBeniAmmortizzabili.setSpecieFinale(specieFinale);
		List<VenditaBene> list = null;
		try {
			list = beniAmmortizzabiliService.ricercaVenditeBeni(criteriaRicercaBeniAmmortizzabili);
			if (list.size() == 0) {
				fail("Nessun risultato ottenuto");
			}
		} catch (Exception e) {
			logger.error("--> errore ", e);
			fail(e.getMessage());
		}
		for (VenditaBene vendita : list) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vendita.getDataVendita());
			int anno = calendar.get(Calendar.YEAR);

			assertEquals("Anno errato", anno, 2006);
			assertTrue("Intervallo non rispettato  " + vendita.getBene().getSottoSpecie().getSpecie().getCodice()
					+ " - " + specieIniziale.getCodice(), vendita.getBene().getSottoSpecie().getSpecie().getCodice()
					.compareToIgnoreCase(specieIniziale.getCodice()) >= 0);
			assertTrue("Intervallo non rispettato " + vendita.getBene().getSottoSpecie().getSpecie().getCodice()
					+ " - " + specieFinale.getCodice(), vendita.getBene().getSottoSpecie().getSpecie().getCodice()
					.compareToIgnoreCase(specieFinale.getCodice()) <= 0);
		}

		logger.debug("--> Exit testRicercaVenditaBene");

	}

	/**
	 * Test per il salvataggio del bene ammortizzabile.
	 */
	public void testSalvaBeneAmmortizzabile() {
		logger.debug("--> Enter testSalvaBeneAmmortizzabile");

		BeneAmmortizzabile beneAmmortizzabile = new BeneAmmortizzabile();
		beneAmmortizzabile.setAcquistatoUsato(false);
		beneAmmortizzabile.setAnnoAcquisto(2006);
		beneAmmortizzabile.setBeneDiProprieta(false);
		beneAmmortizzabile.setBeneInLeasing(false);
		beneAmmortizzabile.setBeneMateriale(false);
		beneAmmortizzabile.setDataInizioAmmortamento(new Date());
		beneAmmortizzabile.setDescrizione("prova inserimento bene");
		beneAmmortizzabile.setFabbricato(false);
		beneAmmortizzabile.setImportoFatturaAcquisto(BigDecimal.valueOf(10.1));
		beneAmmortizzabile.setImportoSoggettoAdAmmortamentoSingolo(BigDecimal.valueOf(10.1));
		beneAmmortizzabile.setManutenzione(false);
		beneAmmortizzabile.setNumeroProtocolloAcquisto("12a");
		beneAmmortizzabile.setNumeroRegistro(12);
		beneAmmortizzabile.setPercentualeContributo(BigDecimal.valueOf(10.1));
		beneAmmortizzabile.setPercentualeUsoPromiscuo(BigDecimal.valueOf(10.1));
		beneAmmortizzabile.setSoggettoAContributo(false);
		beneAmmortizzabile.setStampaSuRegistriBeni(false);
		beneAmmortizzabile.setStampaSuRegistriInventari(false);
		beneAmmortizzabile.setValoreAcquisto(BigDecimal.valueOf(10.1));

		DatiCivilistici datiCivilistici = new DatiCivilistici();
		datiCivilistici.setAmmortamentoInCorso(true);
		datiCivilistici.setPercentualeMaggioreUtilizzoBene(10.1);
		datiCivilistici.setPercentualeMinoreUtilizzoBene(10.1);
		datiCivilistici.setPercentualeAmmortamentoOrdinario(10.1);
		beneAmmortizzabile.setDatiCivilistici(datiCivilistici);

		DatiFiscali datiFiscali = new DatiFiscali();
		datiFiscali.setAmmortamentoInCorso(true);
		datiFiscali.setPercentualeAmmortamentoAccelerato(11.0);
		datiFiscali.setPercentualeAmmortamentoAnticipato(11.0);
		datiFiscali.setPercentualeAmmortamentoOrdinario(11.0);
		beneAmmortizzabile.setDatiFiscali(datiFiscali);

		beneAmmortizzabile.setSottoSpecie(null);
		beneAmmortizzabile.setFornitore(null);
		beneAmmortizzabile.setBenePadre(null);
		beneAmmortizzabile.setUbicazione(null);

		BeneAmmortizzabile beneAmmortizzabileSalvato = null;
		try {
			beneAmmortizzabileSalvato = beniAmmortizzabiliService.salvaBeneAmmortizzabile(beneAmmortizzabile);
			assertNotNull("Bene ammortizzabile nullo.", beneAmmortizzabileSalvato);
			assertNotNull("ID del bene ammortizzabile nullo.", beneAmmortizzabileSalvato.getId());
			assertNotNull("Versione del bene nulla", beneAmmortizzabileSalvato.getVersion());
			assertNotNull("Codice azienda non avvalorato", beneAmmortizzabile.getCodiceAzienda());
		} catch (Exception e) {
			logger.error("--> RemoteException ", e);
			fail(e.getMessage());
		}

		logger.debug("--> Exit testSalvaBeneAmmortizzabile");
	}

	/**
	 * Test per il salvataggio di una vendita del bene.
	 *
	 * @throws Exception
	 *             Exception
	 */
	public void testSalvaVenditaBene() throws Exception {
		logger.debug("--> Enter testVenditaBene");
		List<BeneAmmortizzabileLite> beni = getBeniAmmortizzabili();
		BeneAmmortizzabileLite beneAmmortizzabile = beni.get(0);
		List<VenditaBene> vendite = getVenditeBene(beneAmmortizzabile.getId());
		for (VenditaBene vendita : vendite) {
			/* modifica alcuni dati delle vendite e esegue il salvataggio poi vediamo quel che succede */
			ClienteLite cliente = new ClienteLite();
			cliente.setId(200);
			vendita.setCliente(cliente);
			VenditaBene venditaBene = beniAmmortizzabiliService.salvaVenditaBene(vendita, true);
			assertEquals(vendita.getCliente(), venditaBene.getCliente());

		}
		logger.debug("--> Exit testVenditaBene");
	}

	/**
	 * Test di simulazione di un piano di ammortamento così strutturato :
	 *
	 * Bene Ammortizzabile valoreBene = 1000 percentuale ammortamento ordinario = 20 % percentuale ammortamento
	 * anticipato =0 %.
	 *
	 *
	 * @throws RemoteException
	 *             RemoteException
	 * @throws ObjectNotFoundException
	 *             ObjectNotFoundException
	 * @throws StaleObjectStateException
	 *             StaleObjectStateException
	 * @throws DuplicateKeyObjectException
	 *             DuplicateKeyObjectException
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 * @throws AreeContabiliSimulazioneException
	 */
	public void testSimulazioneCasoDue() throws RemoteException, ObjectNotFoundException, StaleObjectStateException,
	DuplicateKeyObjectException, BeniAmmortizzabiliException, AreeContabiliSimulazioneException {
		logger.debug("--> Enter testSimulazione");
		/* creazione bene ammortizzabile */
		int annoAmmortamento = 2006;
		double valoreBene = 1000;
		double percentualeOrdinario = 20;
		double percentualeAnticipato = 0;
		double percentualeAccelerato = 0;
		int sottoSpecieId = 17233;
		BeneAmmortizzabile beneAmmortizzabile = createBeneAmmortizzabile(annoAmmortamento, valoreBene, valoreBene, 10,
				10, valoreBene, percentualeAccelerato, percentualeAnticipato, percentualeOrdinario,
				percentualeAccelerato, percentualeAnticipato, percentualeOrdinario, sottoSpecieId);
		beneAmmortizzabile = beniAmmortizzabiliService.salvaBeneAmmortizzabile(beneAmmortizzabile);
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		Calendar calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		/*
		 * Anno Simulazione 2006 risultati attesi ammortamento ordinario 100 ammortamento anticipato 0
		 */
		Simulazione simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 "
				+ annoAmmortamento, calendar.getTime(), null);
		List<PoliticaCalcolo> politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		Simulazione simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		List<PoliticaCalcolo> politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 100d);
				}
			}
		}

		/*
		 * Anno Simulazione 2007 risultati attesi ammortamento ordinario 200 ammortamento anticipato 0
		 */
		annoAmmortamento = 2007;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2008 risultati attesi ammortamento ordinario 200 ammortamento anticipato 0
		 */
		annoAmmortamento = 2008;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2009 risultati attesi ammortamento ordinario 200 ammortamento anticipato 0
		 */
		annoAmmortamento = 2009;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2010 risultati attesi ammortamento ordinario 200 ammortamento anticipato 0
		 */
		annoAmmortamento = 2010;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2011 risultati attesi ammortamento ordinario 100 ammortamento anticipato 0
		 */
		annoAmmortamento = 2011;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 100d);
				}
			}
		}

		/*
		 * Anno Simulazione 2011 risultati attesi ammortamento ordinario 0 ammortamento anticipato 0
		 */
		annoAmmortamento = 2012;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 0d);
				}
			}
		}

		logger.debug("--> Exit testSimulazione");
	}

	/**
	 * Test di simulazione di un piano di ammortamento così strutturato :
	 *
	 * Bene Ammortizzabile valoreBene = 1000 percentuale ammortamento ordinario = 20 % percentuale ammortamento
	 * anticipato =20 % vendita parziale al 3° anno del 40%.
	 *
	 *
	 * @throws RemoteException
	 *             RemoteException
	 * @throws ObjectNotFoundException
	 *             ObjectNotFoundException
	 * @throws StaleObjectStateException
	 *             StaleObjectStateException
	 * @throws DuplicateKeyObjectException
	 *             DuplicateKeyObjectException
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 * @throws AreeContabiliSimulazioneException
	 */
	public void testSimulazioneCasoTre() throws RemoteException, ObjectNotFoundException, StaleObjectStateException,
	DuplicateKeyObjectException, BeniAmmortizzabiliException, AreeContabiliSimulazioneException {
		logger.debug("--> Enter testSimulazione");
		/* creazione bene ammortizzabile */
		int annoAmmortamento = 2006;
		double valoreBene = 1000;
		double percentualeOrdinario = 20;
		double percentualeAnticipato = 20;
		double percentualeAccelerato = 0;
		int sottoSpecieId = 17233;
		BeneAmmortizzabile beneAmmortizzabile = createBeneAmmortizzabile(annoAmmortamento, valoreBene, valoreBene, 10,
				10, valoreBene, percentualeAccelerato, percentualeAnticipato, percentualeOrdinario,
				percentualeAccelerato, percentualeAnticipato, percentualeOrdinario, sottoSpecieId);
		beneAmmortizzabile = beniAmmortizzabiliService.salvaBeneAmmortizzabile(beneAmmortizzabile);
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		Calendar calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		/*
		 * Anno Simulazione 2006 risultati attesi ammortamento ordinario 100 ammortamento anticipato 100
		 */
		Simulazione simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 "
				+ annoAmmortamento, calendar.getTime(), null);
		List<PoliticaCalcolo> politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		Simulazione simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		List<PoliticaCalcolo> politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 100d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 100d);
				}
			}
		}

		/*
		 * Anno Simulazione 2007 risultati attesi ammortamento ordinario 200 ammortamento anticipato 200
		 */
		annoAmmortamento = 2007;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 200d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2008 Vendita Bene risultati attesi ammortamento ordinario 200 ammortamento anticipato 200
		 */

		annoAmmortamento = 2008;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 200d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2009 risultati attesi ammortamento ordinario 200 ammortamento anticipato 0
		 */
		annoAmmortamento = 2009;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2010 risultati attesi ammortamento ordinario 200 ammortamento anticipato 0
		 */
		annoAmmortamento = 2010;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2011 risultati attesi ammortamento ordinario 100 ammortamento anticipato 0
		 */
		annoAmmortamento = 2011;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 100d);
				}
			}
		}

		/*
		 * Anno Simulazione 2011 risultati attesi ammortamento ordinario 0 ammortamento anticipato 0
		 */
		annoAmmortamento = 2012;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazioneCalcolata);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 0d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 0d);
				}
			}
		}

		logger.debug("--> Exit testSimulazione");
	}

	/**
	 * Test di simulazione di un piano di ammortamento così strutturato :
	 *
	 * Bene Ammortizzabile valoreBene = 1000 percentuale ammortamento ordinario = 20 % percentuale ammortamento
	 * anticipato = 20 %.
	 *
	 *
	 * @throws RemoteException
	 *             RemoteException
	 * @throws ObjectNotFoundException
	 *             ObjectNotFoundException
	 * @throws StaleObjectStateException
	 *             StaleObjectStateException
	 * @throws DuplicateKeyObjectException
	 *             DuplicateKeyObjectException
	 * @throws BeniAmmortizzabiliException
	 *             BeniAmmortizzabiliException
	 * @throws AreeContabiliSimulazioneException
	 */
	public void testSimulazioneCasoUno() throws RemoteException, ObjectNotFoundException, StaleObjectStateException,
	DuplicateKeyObjectException, BeniAmmortizzabiliException, AreeContabiliSimulazioneException {
		logger.debug("--> Enter testSimulazione");
		/* creazione bene ammortizzabile */
		int annoAmmortamento = 2006;
		double valoreBene = 1000;
		double percentualeOrdinario = 20;
		double percentualeAnticipato = 20;
		double percentualeAccelerato = 20;
		int sottoSpecieId = 17233;
		BeneAmmortizzabile beneAmmortizzabile = createBeneAmmortizzabile(annoAmmortamento, valoreBene, valoreBene, 10,
				10, valoreBene, percentualeAccelerato, percentualeAnticipato, percentualeOrdinario,
				percentualeAccelerato, percentualeAnticipato, percentualeOrdinario, sottoSpecieId);
		beneAmmortizzabile = beniAmmortizzabiliService.salvaBeneAmmortizzabile(beneAmmortizzabile);
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		Calendar calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		/*
		 * Anno Simulazione 2006 risultati attesi ammortamento ordinario 100 ammortamento anticipato 100
		 */
		Simulazione simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 "
				+ annoAmmortamento, calendar.getTime(), null);
		List<PoliticaCalcolo> politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		Simulazione simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		List<PoliticaCalcolo> politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 100d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 100d);
				}
			}
		}

		/*
		 * Anno Simulazione 2007 risultati attesi ammortamento ordinario 200 ammortamento anticipato 200
		 */
		annoAmmortamento = 2007;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazione);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 200d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}

		/*
		 * Anno Simulazione 2008 risultati attesi ammortamento ordinario 200 ammortamento anticipato 200
		 */
		annoAmmortamento = 2008;
		logger.debug("--> creazione simulazione " + annoAmmortamento);
		calendar = Calendar.getInstance();
		calendar.set(annoAmmortamento, Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		simulazione = beniAmmortizzabiliService.creaSimulazione("Simulazione per Test #1 " + annoAmmortamento,
				calendar.getTime(), simulazione);
		politicheCalcolo = simulazione.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcolo) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					calcoloBene.setDirty(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoOrdinario(true);
					calcoloBene.getPoliticaCalcoloFiscale().setAmmortamentoAnticipato(true);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoOrdinario(percentualeOrdinario);
					calcoloBene.getPoliticaCalcoloFiscale().setPercAmmortamentoAnticipato(percentualeAnticipato);
				}
			}

			logger.debug("--> calcolo" + calcolo);
		}
		simulazioneCalcolata = beniAmmortizzabiliService.salvaSimulazione(simulazione);
		logger.debug("--> controllo valori calcolati nell'anno " + annoAmmortamento);
		politicheCalcoloRisultati = simulazioneCalcolata.getPoliticheCalcolo();
		for (PoliticaCalcolo calcolo : politicheCalcoloRisultati) {
			if (calcolo instanceof PoliticaCalcoloBene) {
				PoliticaCalcoloBene calcoloBene = (PoliticaCalcoloBene) calcolo;
				logger.debug("--> cerca bene corrente " + calcoloBene.getBene().getId() + " "
						+ beneAmmortizzabile.getId());
				if (calcoloBene.getBene().getId().equals(beneAmmortizzabile.getId())) {
					logger.debug("--> verifica simulazione anno " + annoAmmortamento);
					assertEquals("Calcolo totale anticipato errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleAnticipato().doubleValue(), 200d);
					assertEquals("Calcolo totale ordinario errato ", calcolo.getPoliticaCalcoloFiscale()
							.getTotaleOrdinario().doubleValue(), 200d);
				}
			}
		}
		logger.debug("--> Exit testSimulazione");
	}

}
