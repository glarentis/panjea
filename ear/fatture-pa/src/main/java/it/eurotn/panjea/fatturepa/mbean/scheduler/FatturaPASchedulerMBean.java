package it.eurotn.panjea.fatturepa.mbean.scheduler;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.Service;
import org.jboss.security.auth.callback.UsernamePasswordHandler;

import it.eurotn.panjea.fatturepa.manager.interfaces.EmailNotificheSdICheckerManager;
import it.eurotn.panjea.fatturepa.mbean.scheduler.interfaces.FatturaPAScheduler;
import it.eurotn.panjea.fatturepa.service.interfaces.ConservazioneSostitutivaService;
import it.eurotn.panjea.fatturepa.service.interfaces.FatturePAService;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.service.interfaces.JpaUtils;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;

@Service(objectName = "panjea:service=FatturaPASchedulerMBean")
public class FatturaPASchedulerMBean implements FatturaPAScheduler, Serializable {

    private static final long serialVersionUID = -321217549748427602L;

    private static final Logger LOGGER = Logger.getLogger(FatturaPASchedulerMBean.class);

    private static final String TIMER_NAME = "timerFatturaPASchedulerMBean";

    private long intervallo = 1_800_000;

    @EJB
    private JpaUtils jpaUtils;

    @EJB
    private PanjeaMessage panjeaMessage;

    @Resource
    private TimerService timerService;

    @EJB
    private JbossMailService jbossMailService;

    @EJB
    private EmailNotificheSdICheckerManager emailNotificheSdICheckerManager;

    @EJB
    private FatturePAService fatturePAService;

    @EJB
    private ConservazioneSostitutivaService conservazioneSostitutivaService;

    @EJB
    private SicurezzaService sicurezzaService;

    @Override
    public void create() throws Exception {
        // Non utilizzato
    }

    @Override
    public void destroy() {
        // Non utilizzato
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
            panjeaMessage.send("Attenzione, Errore durante il login per il servizio di controllo della fatture PA",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("Errore servizio controllo della fattura PA",
                    "Attenzione, Errore durante il login per il servizio di controllo della fatture PA", null);
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            panjeaMessage.send("Attenzione, Errore durante il login per il servizio di controllo della fatture PA",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("Errore servizione controllo della fatture PA",
                    "Attenzione, Errore durante il login per il servizio di controllo della fatture PA", null);
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
            LOGGER.error("-->errore. Impossibile avviare il servizio.", e);
        }
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Attivazione del timer per il controllo della fatture PA");
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

        List<String> aziendeDeployate = null;
        try {
            aziendeDeployate = jpaUtils.getAziendeDeployate();
        } catch (Exception e) {
            panjeaMessage.send("Attenzione, Errore imprevisto nell'avvio del servizio controllo della fatture PA",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("ERRORE SERVIZIO CONTROLLO DELLE FATTURE PA",
                    "Attenzione, Errore imprevisto nell'avvio del servizio controllo della fatture PA", null);
            throw new RuntimeException(e);
        }

        for (String azienda : aziendeDeployate) {

            login(azienda);

            // Controlla i messaggi di posta sulla PEC per importare gli esiti.
            try {
                emailNotificheSdICheckerManager.checkMail(azienda);
            } catch (Exception e) {
                panjeaMessage
                        .send("Attenzione, Errore imprevisto durante il controllo delle notifiche della fatture PA per l'azienda "
                                + azienda, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                jbossMailService.send("ERRORE SERVIZIO CONTROLLO DELLE FATTURE PA",
                        "Attenzione, Errore imprevisto durante il controllo delle notifiche della fatture PA per l'azienda "
                                + azienda,
                        null);
                throw new RuntimeException(e);
            }

            try {
                // Verifica se ci sono nuovi esiti da importare da solutionDoc
                fatturePAService.checkEsitiFatturePA();

                // Esegue la conservazione sostitutiva
                conservazioneSostitutivaService.conservaXMLFatturePA();
            } catch (Exception e) {
                panjeaMessage
                        .send("Attenzione, Errore imprevisto relativo al servizio di conservazione sostitutiva delle fatture PA per l'azienda "
                                + azienda, PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
                jbossMailService.send("ERRORE SERVIZIO CONTROLLO DELLE FATTURE PA",
                        "Attenzione, Errore imprevisto relativo al servizio di conservazione sostitutiva delle fatture PA per l'azienda "
                                + azienda,
                        null);
                throw new RuntimeException(e);
            }
        }

    }
}
