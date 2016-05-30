package it.eurotn.panjea.aton.exporter.mbean;

import it.eurotn.panjea.aton.exporter.service.interfaces.AtonExporterService;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.service.interfaces.JpaUtils;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

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
import org.joda.time.DateTime;

@Service(objectName = "panjea:service=AtonOnSaleExporter")
public class AtonExporterMBean implements AtonExporterManagement, Serializable {

	private static final long serialVersionUID = -321217549748427602L;

	private static Logger logger = Logger.getLogger(AtonExporterMBean.class);

	private static final String TIMER_NAME = "timerEsportazioneAtonOnsale";

	private long intervallo = 10000;

	private int ora1 = 12;
	private int ora2 = 21;

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
	private JbossMailService jbossMailService;

	@EJB
	private AtonExporterService atonExporterService;

	private boolean checkOraConfigurata() {

		DateTime dataInizio = new DateTime();
		DateTime dataFine = dataInizio.plusMillis(new Long(intervallo).intValue());
		DateTime data1 = new DateTime().withHourOfDay(ora1).withMinuteOfHour(0).withSecondOfMinute(0)
				.withMillisOfSecond(0);
		DateTime data2 = new DateTime().withHourOfDay(ora2).withMinuteOfHour(0).withSecondOfMinute(0)
				.withMillisOfSecond(0);

		// controllo se almeno una delle 2 date è nel range
		boolean data1Valid = isBetween(data1, dataInizio, dataFine);
		boolean data2Valid = isBetween(data2, dataInizio, dataFine);

		return data1Valid || data2Valid;
	}

	@Override
	public void create() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Create service AtonOnSaleExporter");
		}
	}

	@Override
	public void destroy() {
		if (logger.isDebugEnabled()) {
			logger.debug("-->Destroy service AtonOnSaleExporter");
		}
	}

	/**
	 * @return Returns the intervallo.
	 */
	@Override
	public long getIntervallo() {
		return intervallo;
	}

	@Override
	public int getOra1() {
		return ora1;
	}

	@Override
	public int getOra2() {
		return ora2;
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
	 * Verifica che siano configurate le directory necessare all'esportazione. Se non esistono viene mandato un
	 * messaggio.
	 */
	private void initFoldersEsportazione() {

		String folderExport = getPreferenceFor("atonDirExport");
		if (folderExport.isEmpty()) {
			return;
		}

		String folderTemplate = getPreferenceFor("atonDirTemplate");
		if (folderTemplate.isEmpty()) {
			return;
		}

	}

	/**
	 * Verifica se la data è compresa fra le altre 2.
	 * 
	 * @param data
	 *            data da testare
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return <code>true</code> se la data è compresa
	 */
	private boolean isBetween(DateTime data, DateTime dataInizio, DateTime dataFine) {
		return dataInizio.getMillis() <= data.getMillis() && dataFine.getMillis() >= data.getMillis();
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
			panjeaMessage.send("ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO ESPORTAZIONE ATON ONSALE",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			jbossMailService.send("ERRORE SERVIZIO ESPORTAZIONI",
					"ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO ESPORTAZIONE ATON ONSALE", null);
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			panjeaMessage.send("ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO ESPORTAZIONE ATON ONSALE",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			jbossMailService.send("ERRORE SERVIZIO ESPORTAZIONI",
					"ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO ESPORTAZIONE ATON ONSALE", null);
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
	public void setOra1(int ora) {
		ora1 = ora;
	}

	@Override
	public void setOra2(int ora) {
		ora2 = ora;
	}

	@Override
	public void start() throws Exception {
		logger.info("Attivazione del timer per l'esportazione di Aton onSale");
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

		if (!checkOraConfigurata()) {
			return;
		}

		try {
			for (String azienda : jpaUtils.getAziendeDeployate()) {
				login(azienda);
				initFoldersEsportazione();
				atonExporterService.esporta();
			}
		} catch (Exception e) {
			panjeaMessage.send("ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO ESPORTAZIONE ATON ONSALE",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			jbossMailService.send("ERRORE SERVIZIO ESPORTAZIONI",
					"ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO ESPORTAZIONE ATON ONSALE", null);
			throw new RuntimeException(e);
		}
	}
}
