package it.eurotn.panjea.rich;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.config.ApplicationWindowConfigurer;
import org.springframework.richclient.application.setup.SetupWizard;
import org.springframework.richclient.application.support.ApplicationWindowCommandManager;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.security.ApplicationSecurityManager;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.Authentication;

import com.jidesoft.spring.richclient.docking.JideApplicationLifecycleAdvisor;
import com.jidesoft.spring.richclient.docking.JideApplicationWindow;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.rich.login.ConnessioneUtente;
import it.eurotn.panjea.rich.statusBarItem.PanjeaFeedReader;
import it.eurotn.panjea.sicurezza.JecPrincipalSpring;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.DockingLayoutManager;
import it.eurotn.security.JecPrincipal;

/**
 * LifeCycle associato all'applicazione, viene legato il wizard iniziale, la conferma della chiusura
 * dell'applicazione e il login commmand.
 *
 * @author Leonardo
 */
public class PanjeaLifecycleAdvisor extends JideApplicationLifecycleAdvisor {

    /**
     * Simple extension to allow us to inject our special bean post-processors and control event
     * publishing.
     */
    private class CommandBarApplicationContext extends FileSystemXmlApplicationContext {

        /**
         * Constructor. Load bean definitions from the specified location.
         *
         * @param location
         *            of bean definitions
         */
        public CommandBarApplicationContext(final String location) {
            super(new String[] { location }, false, getApplication().getApplicationContext());
            refresh();
        }

        /**
         * Install our bean post-processors.
         *
         * @param beanFactory
         *            the bean factory used by the application context
         * @throws org.springframework.beans.BeansException
         *             in case of errors
         */
        @Override
        protected void postProcessBeanFactory(
                org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory)
                        throws BeansException {
            beanFactory.addBeanPostProcessor(new ApplicationWindowSetter(getOpeningWindow()));
        }

        /**
         * Publish an event in to this context. Since we are always getting notification from a
         * parent context, this overriden implementation does not dispatch up to the parent context,
         * thus avoiding an infinite loop.
         */
        @Override
        public void publishEvent(org.springframework.context.ApplicationEvent event) {
            // Temporarily disconnect our parent so the event publishing doesn't
            // result in an infinite loop.
            org.springframework.context.ApplicationContext parent = getParent();
            setParent(null);
            super.publishEvent(event);
            setParent(parent);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(PanjeaLifecycleAdvisor.class);

    private boolean isConfirmed = false;

    private String windowCommandBarDefinitions;

    private ConfigurableListableBeanFactory openingWindowCommandBarFactory;

    /**
     * Costruttore.
     *
     */
    public PanjeaLifecycleAdvisor() {
        super();
    }

    private void checkUnreadNews() {
        PanjeaFeedReader feedReader = RcpSupport.getBean("panjeaFeedReader");
        int unreadItem = feedReader.getUnReadItemCountForAllChannels();

        if (unreadItem > 0) {

            String newsMessage = feedReader.getNewsDescription();
            newsMessage = newsMessage.replace("<html>", "").replace("</html>", "");
            newsMessage = "<html><br>Esistono delle notizie ancora non lette, farlo ora? <br><br>" + newsMessage
                    + "</html>";

            ConfirmationDialog dialog = new ConfirmationDialog("Notizie non lette", newsMessage) {

                @Override
                protected JComponent createDialogContentPane() {
                    JScrollPane scrollPane = new JScrollPane(super.createDialogContentPane());
                    scrollPane.getViewport().setViewPosition(new Point(0, 0));
                    return scrollPane;
                }

                @Override
                protected void onConfirm() {
                    OpenEditorEvent event = new OpenEditorEvent("panjeaRssEditor");
                    Application.instance().getApplicationContext().publishEvent(event);
                }

            };
            dialog.setPreferredSize(new Dimension(500, 150));
            dialog.showDialog();
        }
    }

    @Override
    public ApplicationWindowCommandManager createWindowCommandManager() {
        initNewWindowCommandBarFactory();
        if (!getCommandBarFactory().containsBean("windowCommandManager")) {
            return new ApplicationWindowCommandManager();
        }
        return (ApplicationWindowCommandManager) getCommandBarFactory().getBean("windowCommandManager",
                ApplicationWindowCommandManager.class);
    }

    @Override
    protected ConfigurableListableBeanFactory getCommandBarFactory() {
        return this.openingWindowCommandBarFactory;
    }

    /**
     * Cerca nel message resource il valore data la chiave.
     *
     * @param keyMessage
     *            la chiave nel message resource dell'applicazione
     * @return il valore internazionalizzato
     */
    private String getMessage(String keyMessage) {
        MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                .getService(MessageSource.class);
        return messageSource.getMessage(keyMessage, null, keyMessage, Locale.getDefault());
    }

    @Override
    protected void initNewWindowCommandBarFactory() {
        // Install our own application context so we can register needed post-processors
        final CommandBarApplicationContext commandBarContext = new CommandBarApplicationContext(
                windowCommandBarDefinitions);
        addChildCommandContext(commandBarContext);
        this.openingWindowCommandBarFactory = commandBarContext.getBeanFactory();
    }

    /**
     * Alla creazione dei comandi dell'applicazione che vengono chiamati all'avvio della finestra
     * principale viene chiamato il commmand di login.
     *
     * @param window
     *            window
     */
    @Override
    public void onCommandsCreated(ApplicationWindow window) {
        // se sono in deug mode mi loggo direttamente
        SettingsManager settingsManager = (SettingsManager) ApplicationServicesLocator.services()
                .getService(SettingsManager.class);
        try {
            if (!settingsManager.getUserSettings().getBoolean("debugMode")) {
                ActionCommand command = (ActionCommand) window.getCommandManager().getCommand("loginCommand");
                command.execute();
            } else {
                PanjeaApplication application = (PanjeaApplication) Application.instance();
                String ultimaAziendaLoggata = application.getAziendaCommandLine();
                if (ultimaAziendaLoggata.isEmpty()) {
                    ultimaAziendaLoggata = settingsManager.getUserSettings().getString("ultimaAziendaLoggata");
                }
                JecPrincipal jecPrincipal = new JecPrincipalSpring("europa#" + ultimaAziendaLoggata + "#it");
                jecPrincipal.setCredentials("pnj_adm");
                ApplicationSecurityManager sm = (ApplicationSecurityManager) ApplicationServicesLocator.services()
                        .getService(ApplicationSecurityManager.class);
                sm.doLogin((Authentication) jecPrincipal);
            }
        } catch (Exception e) {
            LOGGER.error("--> errore nel leggere la chiave debugMode dai setting. Richiedo i dati di login", e);
            ActionCommand command = (ActionCommand) window.getCommandManager().getCommand("loginCommand");
            command.execute();
        }
    }

    @Override
    public void onPostStartup() {
        super.onPostStartup();

        PanjeaSwingUtil.traceFocusAndSelectAll();
    }

    @Override
    public void onPreStartup() {
        // non faccio niente
    }

    /**
     * Prima della chiusura dell' applicazione viene aperto un dialogo di conferma.
     *
     * @param window
     *            window
     * @return se ritorno true l'applicazione viene chiusa altrimenti l'azione viene annullata
     */
    @Override
    public boolean onPreWindowClose(ApplicationWindow window) {

        boolean closeConfirmed = false;

        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                PanjeaLifecycleApplicationEvent.CLOSE_APP, PanjeaLifecycleApplicationEvent.CLOSE_APP);
        Application.instance().getApplicationContext().publishEvent(event);

        String osName = System.getProperty("os.name");

        // verifico che il sistema operativo sia MAC OS
        if (!osName.toLowerCase().startsWith("windows") && !osName.toLowerCase().startsWith("linux")) {
            // se mi trovo sotto MAC OS non ho installato la system tray quindi
            // chiedo all'utente
            // se uscire o no dall'applicazione
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    getMessage("panjea.application.title.confirm.exit"),
                    getMessage("panjea.application.message.confirm.exit")) {
                @Override
                protected void onConfirm() {
                    isConfirmed = true;
                }
            };
            confirmationDialog.setPreferredSize(new Dimension(250, 50));
            confirmationDialog.setResizable(false);
            confirmationDialog.showDialog();
            closeConfirmed = isConfirmed;
        } else {
            // se non sono sotto MAC OS ritorno false perch� la sistem tray
            closeConfirmed = false;
        }

        DockingLayoutManager.saveLayout(((JideApplicationWindow) window).getDockingManager(), window.getPage().getId());
        // mi scollego dal server
        ConnessioneUtente connessioneUtente = RcpSupport.getBean(ConnessioneUtente.BEAN_ID);
        connessioneUtente.logout();
        return closeConfirmed;
    }

    /**
     * Prima dell'apertura della finestra dell'applicazione principale viene presentato un wizard
     * per l'informativa della licenza software gestire le eccezioni su onPreWindowOpen.
     *
     * @param configurer
     *            window
     */
    @Override
    public void onPreWindowOpen(ApplicationWindowConfigurer configurer) {
        new LogSender();
        super.onPreWindowOpen(configurer);
        SettingsManager manager = (SettingsManager) ApplicationServicesLocator.services()
                .getService(SettingsManager.class);
        try {
            if (manager.getUserSettings().getBoolean("ShowLicence")) {
                SetupWizard setupWizard = (SetupWizard) getApplication().getApplicationContext().getBean("setupWizard",
                        SetupWizard.class);
                setupWizard.execute();
                manager.getUserSettings().setBoolean("ShowLicence", false);
                manager.getUserSettings().save();
            }
        } catch (BeansException e) {
            LOGGER.error("--> non trovo il wizard per la licenza", e);
        } catch (SettingsException e) {
            LOGGER.error("--> non riesco a leggere il settings per il flag della licenza", e);
        } catch (IOException e) {
            LOGGER.error("--> Non riesco ad aprire il file per la licenza", e);
        }
    }

    @Override
    public void onWindowCreated(ApplicationWindow window) {
        super.onWindowCreated(window);

        String osName = System.getProperty("os.name");

        if (osName.toLowerCase().contains("windows 7")) {
            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            window.getControl().setLocation(0, 0);
            window.getControl().setSize(screensize.width, screensize.height - 50);
        } else {
            // apro l'applicazione a schermo intero
            window.getControl().setExtendedState(Frame.MAXIMIZED_BOTH);
        }
    }

    @Override
    public void onWindowOpened(ApplicationWindow applicationWindow) {

        String osName = System.getProperty("os.name");

        // verifico che il sistema operativo non sia MAC OS
        if ((osName.toLowerCase().contains("windows")) || (osName.toLowerCase().contains("linux"))) {
            // invece di chiudere l'applicazione la nascondo
            applicationWindow.getControl().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            // installo la system tray
            new PanjeaSystemTray(applicationWindow);
        }

        DockingLayoutManager.restoreLayout(((JideApplicationWindow) applicationWindow).getDockingManager(),
                applicationWindow.getPage().getId());

        checkUnreadNews();

        super.onWindowOpened(applicationWindow);
    }

    @Override
    public void setWindowCommandBarDefinitions(String commandBarDefinitionLocation) {
        this.windowCommandBarDefinitions = commandBarDefinitionLocation;
    }
}
