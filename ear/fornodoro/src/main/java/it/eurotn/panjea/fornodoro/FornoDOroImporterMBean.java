package it.eurotn.panjea.fornodoro;

import it.eurotn.panjea.fornodoro.evasione.EvasioneFornodoroService;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.service.interfaces.JpaUtils;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.Service;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

@Service(objectName = "panjea:service=FornoDOro")
public class FornoDOroImporterMBean implements FornoDOroManagement, Serializable {

	private static final long serialVersionUID = -321217549748427602L;

	private static Logger logger = Logger.getLogger(FornoDOroImporterMBean.class);

	public static final String FORNODORO_DIR_TEMPLATE = "fornodoroDirTemplate";
	public static final String FORNODORO_DIR_EVASIONE = "fornodoroDirEvasione";

	public static final String FORNODORO_TIPO_DOC_CARICO = "fornodoroTipoDocCarico";
	public static final String FORNODORO_TIPI_ORD_CLIENTI_PRODUZIONE = "fornodoroTipiOrdClientiProduzione";
	public static final String FORNODORO_TIPI_ORD_PREVISIONALI = "fornodoroTipiOrdPrevisionali";
	public static final String FORNODORO_TIPI_ORD_CLIENTI = "fornodoroTipiOrdClienti";

	public static final String COSARO_TIPO_DOC_PRODUZIONE = "fornodoroTipoDocProduzione";

	private static final String TIMER_NAME = "timerImportazioneOrdiniFornodoro";

	private long intervallo = 10000;

	private Map<String, File> foldersToWatch = new HashMap<String, File>();

	@EJB
	private JpaUtils jpaUtils;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private TimerService timerService;

	@EJB
	private SicurezzaService sicurezzaService;

	@EJB
	private EvasioneFornodoroService evasioneFornodoroService;

	@EJB
	private JbossMailService jbossMailService;

	private void checkTipiOrdineConfigurati() {
		getPreferenceFor(FORNODORO_TIPI_ORD_CLIENTI_PRODUZIONE);
		getPreferenceFor(FORNODORO_TIPI_ORD_PREVISIONALI);
		getPreferenceFor(FORNODORO_TIPI_ORD_CLIENTI);
	}

	@Override
	public void create() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Create service gulliverImporterMBean");
		}
	}

	@Override
	public void destroy() {
		if (logger.isDebugEnabled()) {
			logger.debug("-->Destroy service gulliverImporterMBean");
		}
	}

	/**
	 * @return Returns the intervallo.
	 */
	@Override
	public long getIntervallo() {
		return intervallo;
	}

	/**
	 * La transaction è REQUIRED (se aperta, usa quella esistente, altrimenti ne crea una).
	 * 
	 * @param key
	 *            la chiave da ricercare nelle preference
	 * @return il valore associato alla chiave ricercata
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	@SuppressWarnings("unchecked")
	private String getPreferenceFor(String key) {
		String value = "";

		javax.persistence.Query query = panjeaDAO
				.prepareQuery("select p.valore from Preference p where p.chiave=:paramChiave ");
		query.setParameter("paramChiave", key);
		List<String> values = null;
		try {
			values = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore ricerca preference con key " + key, e);
			panjeaMessage.send("Errore nella ricerca delle preferenze del server per la chiave " + key,
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		}
		if (values.size() > 0) {
			value = values.get(0);
		} else {
			panjeaMessage.send("Manca nelle preferenze del server la chiave " + key,
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(key + " - " + value);
		}
		return value;
	}

	/**
	 * Viene lanciata l'importazione dei documenti con i file presenti nelle cartelle monitorate.
	 */
	private void importFromFolders() {
		File folderEvasione = foldersToWatch.get(FORNODORO_DIR_EVASIONE);

		if (folderEvasione != null) {
			File fileSemaforo = new File(folderEvasione, "RESI.SEM");
			evasioneFornodoroService.evadi(fileSemaforo);
		}
	}

	/**
	 * Inizializza le cartelle di importazione per il plugin di cosaro, se non esistono manda un messaggio.
	 */
	private void initFoldersImportazione() {
		// VERIFICO E INIT DI COSARO_DIR_EVASIONE, manda un messaggio se non esiste
		if (!foldersToWatch.containsKey(FORNODORO_DIR_EVASIONE)) {
			String foldersWatchPerEvasione = getPreferenceFor(FORNODORO_DIR_EVASIONE);
			if (foldersWatchPerEvasione.isEmpty()) {
				return;
			}
			foldersToWatch.put(FORNODORO_DIR_EVASIONE, new File(foldersWatchPerEvasione));
		}
		File folderWatchEvasione = foldersToWatch.get(FORNODORO_DIR_EVASIONE);
		if (!folderWatchEvasione.exists() || !folderWatchEvasione.isDirectory()) {
			// Errore e attesa;
			panjeaMessage.send("La cartella " + folderWatchEvasione.getAbsolutePath() + " non esiste",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			return;
		}
	}

	/**
	 * Effettua il login a panjea.
	 * 
	 * @param nomeAzienda
	 *            nome dell'azienda dove autenticarsi
	 */
	private void login(String nomeAzienda) {
		String username = new StringBuilder("internalAdmin#").append(nomeAzienda).append("#IT").toString();
		String credential = "internalEuropaSw";
		UsernamePasswordHandler passwordHandler = new UsernamePasswordHandler(username, credential);
		LoginContext loginContext;
		try {
			loginContext = new LoginContext("PanjeaLoginModule", passwordHandler);
			loginContext.login();
			sicurezzaService.login();
		} catch (LoginException e) {
			panjeaMessage.send("ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI FORNODORO",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			jbossMailService.send("ERRORE SERVIZIO IMPORTAZIONI",
					"ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI FORNODORO", null);
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			panjeaMessage.send("ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI FORNODORO",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			jbossMailService.send("ERRORE SERVIZIO IMPORTAZIONI",
					"ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI FORNODORO", null);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param intervallo
	 *            The intervallo to set.
	 */
	@Override
	public void setIntervallo(long intervallo) {
		this.intervallo = intervallo;
		stop();
		try {
			start();
		} catch (Exception e) {
			logger.error("-->errore. Impossibile avviare il servizio.", e);
		}
	}

	@Override
	public void start() throws Exception {
		logger.info("Attivazione del timer per l'importazione degli ordini di cosaro");
		timerService.createTimer(10000, intervallo, TIMER_NAME);
	}

	@Override
	public void stop() {
		for (Object obj : timerService.getTimers()) {
			Timer timer = (Timer) obj;
			if (TIMER_NAME.equals(timer.getInfo())) {
				timer.cancel();
			}
		}
	}

	/**
	 * Se trovo i file nella cartella configurata importo gli ordini.
	 * 
	 * @param timer
	 *            timer che ha generato l'evento
	 */
	@Timeout
	public void timeoutHandler(Timer timer) {
		try {
			for (String azienda : jpaUtils.getAziendeDeployate()) {
				login(azienda);
				initFoldersImportazione();
				importFromFolders();
				checkTipiOrdineConfigurati();
			}
		} catch (Exception e) {
			panjeaMessage.send("ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO IMPORTAZIONI FORNODORO",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			jbossMailService.send("ERRORE SERVIZIO IMPORTAZIONI",
					"ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO IMPORTAZIONI FORNODORO", null);
			throw new RuntimeException(e);
		}
	}
}
