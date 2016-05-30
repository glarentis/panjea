package it.eurotn.panjea.cosaro;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

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

import it.eurotn.panjea.cosaro.evasione.EvasioneCosaroService;
import it.eurotn.panjea.cosaro.importazione.ImportazioneCosaroService;
import it.eurotn.panjea.cosaro.produzione.ProduzioneCosaroService;
import it.eurotn.panjea.mail.service.JbossMailService;
import it.eurotn.panjea.service.interfaces.JpaUtils;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;

@Service(objectName = "panjea:service=Cosaro")
public class CosaroImporterMBean implements CosaroManagement, Serializable {

    private static final long serialVersionUID = -321217549748427602L;

    private static final Logger LOGGER = Logger.getLogger(CosaroImporterMBean.class);

    private static final String TIMER_NAME = "timerImportazioneOrdiniCosaro";

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
    private ImportazioneCosaroService importazioneCosaroService;

    @EJB(beanName = "Panjea.Cosaro.Evasione")
    private EvasioneCosaroService evasioneCosaroService;

    @EJB(beanName = "Panjea.Cosaro.GammaMeatEvasione")
    private EvasioneCosaroService evasioneCosaroServiceGammaMeat;

    @EJB
    private ProduzioneCosaroService produzioneCosaroService;

    @EJB
    private JbossMailService jbossMailService;

    @EJB
    private CosaroSettings cosaroSettings;

    @Override
    public void create() throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Create service gulliverImporterMBean");
        }
    }

    @Override
    public void destroy() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-->Destroy service gulliverImporterMBean");
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
     * Viene lanciata l'importazione dei documenti con i file presenti nelle cartelle monitorate.
     */
    private void importFromFolders() {
        File folderEvasione = foldersToWatch.get(CosaroSettingsBean.COSARO_DIR_EVASIONE);

        if (folderEvasione != null && !cosaroSettings.isGammaMeatEnable()) {
            File fileSemaforo = new File(folderEvasione, "RESI.SEM");
            evasioneCosaroService.evadi(fileSemaforo);
        }

        if (folderEvasione != null && cosaroSettings.isGammaMeatEnable()) {
            evasioneCosaroServiceGammaMeat.evadi(folderEvasione);
        }

        File folderImportazione = foldersToWatch.get(CosaroSettingsBean.COSARO_DIR_IMPORT);
        if (folderImportazione != null) {
            for (File fileToImport : folderImportazione.listFiles()) {
                importazioneCosaroService.importa(fileToImport);
            }
        }

        File folderProduzione = foldersToWatch.get(CosaroSettingsBean.COSARO_DIR_PRODUZIONE);
        if (folderProduzione != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Controllo per importazione prod. di GammaMeat la cartella " + folderImportazione);
            }
            File[] files = folderProduzione.listFiles();
            if (files != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> Trovati num .file :" + files.length);
                }
                for (File fileToImport : files) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> File da importare trovato: " + fileToImport.getName());
                    }
                    produzioneCosaroService.importa(fileToImport);
                }
            }
        }
    }

    /**
     * Inizializza le cartelle di importazione per il plugin di cosaro, se non esistono manda un
     * messaggio.
     */
    private void initFoldersImportazione() {
        // VERIFICO E INIT DI COSARO_DIR_IMPORT, manda un messaggio se non esiste
        if (!foldersToWatch.containsKey(CosaroSettingsBean.COSARO_DIR_PRODUZIONE)) {
            foldersToWatch.put(CosaroSettingsBean.COSARO_DIR_PRODUZIONE,
                    cosaroSettings.getPreferenceDir(CosaroSettingsBean.COSARO_DIR_PRODUZIONE));
        }

        // VERIFICO E INIT DI COSARO_DIR_IMPORT, manda un messaggio se non esiste
        if (!foldersToWatch.containsKey(CosaroSettingsBean.COSARO_DIR_IMPORT)) {
            foldersToWatch.put(CosaroSettingsBean.COSARO_DIR_IMPORT,
                    cosaroSettings.getPreferenceDir(CosaroSettingsBean.COSARO_DIR_IMPORT));
        }

        // VERIFICO E INIT DI COSARO_DIR_EVASIONE, manda un messaggio se non esiste
        if (!foldersToWatch.containsKey(CosaroSettingsBean.COSARO_DIR_EVASIONE)) {
            foldersToWatch.put(CosaroSettingsBean.COSARO_DIR_EVASIONE,
                    cosaroSettings.getPreferenceDir(CosaroSettingsBean.COSARO_DIR_EVASIONE));
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
            panjeaMessage.send("ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI COSARO",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("ERRORE SERVIZIO IMPORTAZIONI",
                    "ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI COSARO", null);
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            panjeaMessage.send("ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI COSARO",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("ERRORE SERVIZIO IMPORTAZIONI",
                    "ATTENZIONE, ERRORE NEL LOGIN PER IL SERVIZIO IMPORTAZIONI COSARO", null);
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
        LOGGER.info("Attivazione del timer per l'importazione degli ordini di cosaro");
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
            }
        } catch (Exception e) {
            panjeaMessage.send("ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO IMPORTAZIONI COSARO",
                    PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
            jbossMailService.send("ERRORE SERVIZIO IMPORTAZIONI",
                    "ATTENZIONE, ERRORE IMPREVISTO NELL'AVVIO DEL SERVIZIO IMPORTAZIONI COSARO", null);
            throw new RuntimeException(e);
        }
    }
}
