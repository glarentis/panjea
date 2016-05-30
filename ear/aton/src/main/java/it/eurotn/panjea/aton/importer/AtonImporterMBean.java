/**
 *
 */
package it.eurotn.panjea.aton.importer;

import it.eurotn.panjea.aton.importer.service.interfaces.AtonImporterService;
import it.eurotn.panjea.service.interfaces.JpaUtils;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;

import java.io.Serializable;
import java.rmi.RemoteException;

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

/**
 * @author leonardo
 */
@Service(objectName = "panjea:service=AtonOnSales")
public class AtonImporterMBean implements AtonManagement, Serializable {

	private static final long serialVersionUID = -4338375679436824799L;

	private static Logger logger = Logger.getLogger(AtonImporterMBean.class);

	private static final String TIMER_NAME = "timerImportazioneOrdiniAtonOnSales";

	private long intervallo = 60000;

	@EJB
	private JpaUtils jpaUtils;

	@EJB
	private PanjeaMessage panjeaMessage;

	@EJB
	private AtonImporterService atonImporterService;

	@Resource
	private TimerService timerService;

	@EJB
	private SicurezzaService sicurezzaService;

	@Override
	public void create() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Create service atonImporterMBean");
		}
	}

	@Override
	public void destroy() {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Destroy service atonImporterMBean");
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
			panjeaMessage.send("ATTENZIONE, ERRORE LOGIN SERVIZIO IMPORTAZIONI ATON ONSALES",
					new String[] { "atonimportazione" }, 10, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			panjeaMessage.send("ATTENZIONE, ERRORE LOGIN SERVIZIO IMPORTAZIONI ATON ONSALES",
					new String[] { "atonimportazione" }, 10, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
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
		timerService.createTimer(60000, intervallo, TIMER_NAME);
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
	 * Lancio l'importazione degli ordini di Aton OnSales.
	 * 
	 * @param timer
	 *            timer che ha generato l'evento
	 */
	@Timeout
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void timeoutHandler(Timer timer) {
		try {
			for (String azienda : jpaUtils.getAziendeDeployate()) {
				if (azienda.equalsIgnoreCase("DOLCELIT")) {
					login(azienda);
					atonImporterService.importa();
				}
			}
		} catch (Exception e) {
			panjeaMessage
					.send("Errore nell'importazione dei file di Aton OnSales!\n Lanciare l'importazione manuale per verificare l'errore.",
							new String[] { "atonimportazione" }, 10, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			throw new RuntimeException(e);
		}
	}
}
