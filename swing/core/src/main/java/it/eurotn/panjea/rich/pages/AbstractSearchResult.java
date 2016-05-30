package it.eurotn.panjea.rich.pages;

import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.support.JecGlobalCommandIds;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.settings.support.Memento;

import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.jidesoft.status.ButtonStatusBarItem;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.status.OvrInsStatusBarItem;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.status.TimeStatusBarItem;
import com.jidesoft.swing.JideBoxLayout;

/**
 * Editor astratto base per tutti gli editor che vengono aggiunti al SearchResultWorkspace, richiede la definizione di
 * un metodo executeSearch(Map params); e garantisce l'accesso a 2 commands di default searchResult.refreshCommand (di
 * default chiama il metodo search(Map)) e searchResult.deleteCommand, con la possibilita' di aggiungerli definendo nel
 * commands-context.xml la toolbar per quell'editor.<br>
 * <br>
 * &lt;bean id="mySearchResultEditor.editorToolBar"
 * class="org.springframework.richclient.command.CommandGroupFactoryBean"&gt;<br>
 * &lt;property name="members"&gt;<br>
 * &lt;list&gt;<br>
 * &lt;value&gt;searchResult.refreshCommand&lt;/value&gt;<br>
 * &lt;value&gt;searchResult.deleteCommand&lt;/value&gt;<br>
 * &lt;/list&gt;<br>
 * &lt;/property&gt;<br>
 * &lt;/bean&gt;<br>
 * 
 * @author Leonardo
 */
public abstract class AbstractSearchResult<T> extends AbstractEditor implements Memento {

    private class SearchResultDeleteCommand extends AbstractDeleteCommand {

        /**
         * Costruttore.
         * 
         * @param securityCommandId
         *            id del security command
         */
        public SearchResultDeleteCommand(final String securityCommandId) {
            super(securityCommandId);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(AbstractSearchResult.this.getId() + "." + "deleteCommand");
        }

        @Override
        public Object onDelete() {
            return delete();
        }
    }

    private static Logger logger = Logger.getLogger(AbstractSearchResult.class);
    private static final String REFRESH_COMMAND_ID = "refreshCommand";
    private AbstractCommand refreshCommand = null;
    private AbstractCommand deleteCommand = null;
    private Map<String, Object> searchParameters = null;
    private Timer timer;
    private final AbstractControlFactory factory = new AbstractControlFactory() {

        @Override
        public JComponent createControl() {
            return AbstractSearchResult.this.createControl();
        }
    };

    /**
     * Costruttore.
     */
    public AbstractSearchResult() {
        super();
        logger.debug("--> ## COSTRUISCO SEARCH RESULT " + getId() + " hashcode " + hashCode());
    }

    @Override
    public boolean canClose() {
        return true;
    }

    @Override
    public void componentFocusGained() {
        logger.debug("---> component focus gained della search result " + getId());
        this.getControl().requestFocusInWindow();
    }

    /**
     * @return controlli creati
     */
    public JComponent createControl() {
        BusyIndicator.showAt(getActiveWindow().getControl());
        logger.debug("---> Enter createControl");
        JComponent mainView;
        try {
            mainView = getComponentFactory().createPanel(new BorderLayout());
            JECCommandGroup toolBar = new JECCommandGroup();
            AbstractCommand[] abstractCommands = getCommand();
            if (abstractCommands != null) {
                for (AbstractCommand abstractCommand : abstractCommands) {
                    toolBar.add(abstractCommand);
                }
                mainView.add(toolBar.createToolBar(), BorderLayout.NORTH);
            }
            mainView.add(getSearchControl(), BorderLayout.CENTER);
        } catch (Exception e) {
            logger.error("--> Errore nel creare i controlli per la search result " + getId(), e);
            mainView = new JTextField("Errore nel creare i controlli per la search result " + getId());
        } finally {
            BusyIndicator.clearAt(getActiveWindow().getControl());
        }
        logger.debug("---> Exit createControl");
        mainView.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                componentFocusGained();
            }

            @Override
            public void focusLost(FocusEvent e) {
                // TODO Auto-generated method stub

            }
        });
        return mainView;
    }

    /**
     * Create the context popup menu, if any, for this table. The default operation is to create the popup from the
     * command group if one has been specified. If not, then null is returned.
     * 
     * @return popup menu to show, or null if none
     */
    protected JPopupMenu createPopupContextMenu() {
        return (getPopupCommandGroup() != null) ? getPopupCommandGroup().createPopupMenu() : null;
    }

    /**
     * Crea la statusBar per la search result.
     * 
     * @return StatusBar
     */
    protected StatusBar createStatusBar() {
        // setup status bar
        StatusBar statusBar = new StatusBar();
        final ProgressStatusBarItem progress = new ProgressStatusBarItem();
        progress.setCancelCallback(new ProgressStatusBarItem.CancelCallback() {

            @Override
            public void cancelPerformed() {
                timer.stop();
                timer = null;
                progress.setStatus("Cancelled");
                progress.showStatus();
            }
        });
        statusBar.add(progress, JideBoxLayout.VARY);
        ButtonStatusBarItem button = new ButtonStatusBarItem("READ-ONLY");
        button.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.BLANK));
        button.setPreferredWidth(20);
        statusBar.add(button, JideBoxLayout.FLEXIBLE);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null && timer.isRunning()) {
                    return;
                }
                timer = new Timer(100, new ActionListener() {

                    private int i = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (i == 0) {
                            progress.setProgressStatus("Initializing ......");
                        }
                        if (i == 10) {
                            progress.setProgressStatus("Running ......");
                        }
                        if (i == 90) {
                            progress.setProgressStatus("Completing ......");
                        }
                        progress.setProgress(i++);
                        if (i > 100) {
                            timer.stop();
                        }
                    }
                });
                timer.start();
            }
        });

        final LabelStatusBarItem label = new LabelStatusBarItem("Line");
        label.setText("100:42");
        label.setAlignment(SwingConstants.CENTER);
        statusBar.add(label, JideBoxLayout.FLEXIBLE);

        final OvrInsStatusBarItem ovr = new OvrInsStatusBarItem();
        ovr.setPreferredWidth(100);
        ovr.setAlignment(SwingConstants.CENTER);
        statusBar.add(ovr, JideBoxLayout.FLEXIBLE);

        final TimeStatusBarItem time = new TimeStatusBarItem();
        statusBar.add(time, JideBoxLayout.FLEXIBLE);
        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
        statusBar.add(gc, JideBoxLayout.FLEXIBLE);

        return statusBar;
    }

    /**
     * Metodo da implementare per personalizzare la deleteCommand sulla searchResult derivata.
     * 
     * @return oggetto cancellato
     */
    protected Object delete() {
        return null;
    }

    @Override
    public void dispose() {
        logger.debug("--> ## DISPOSE SEARCH RESULT " + getId() + " hashcode " + hashCode());
        refreshCommand = null;
        deleteCommand = null;
        searchParameters = null;
        timer = null;
        super.dispose();
    }

    /**
     * Metodo chiamato dalla search(Map).
     * 
     * @param parameters
     *            parametri
     */
    protected abstract void executeSearch(Map<String, Object> parameters);

    /**
     * getter di ApplicationEventMulticaster.
     * 
     * @return ApplicationEventMulticaster
     */
    protected ApplicationEventMulticaster getApplicationEvent() {
        String beanName = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;
        if (getApplicationContext().containsBean(beanName)) {
            return (ApplicationEventMulticaster) getApplicationContext().getBean(beanName);
        }
        return null;
    }

    /**
     * @return comandi da inserire nella toolbar
     */
    protected AbstractCommand[] getCommand() {
        return null;
    }

    @Override
    public JComponent getControl() {
        return factory.getControl();
    }

    /**
     * @return comandi di default ( refresh e delete )
     **/
    protected AbstractCommand[] getDefaultCommand() {
        return new AbstractCommand[] { getRefreshCommand(), getDeleteCommand() };
    }

    /**
     * @return deleteCommand
     */
    protected AbstractCommand getDeleteCommand() {
        if (deleteCommand == null) {
            deleteCommand = new SearchResultDeleteCommand(getId() + ".deleteCommand");
        }
        return deleteCommand;
    }

    @Override
    public Object getEditorInput() {
        return null;
    }

    /**
     * @return the popupCommandGroup
     */
    protected abstract CommandGroup getPopupCommandGroup();

    /**
     * @return refreshCommand
     */
    protected AbstractCommand getRefreshCommand() {
        if (refreshCommand == null) {
            refreshCommand = new ActionCommand(REFRESH_COMMAND_ID) {

                @Override
                protected void doExecuteCommand() {
                    refresh();
                }

                @Override
                protected void onButtonAttached(AbstractButton button) {
                    super.onButtonAttached(button);
                    button.setName(AbstractSearchResult.this.getId() + "." + REFRESH_COMMAND_ID);
                }
            };
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            c.configure(refreshCommand);
        }
        return refreshCommand;
    }

    /**
     * Metodo da implementare per aggiungere il contenuto all'editor derivato.
     * 
     * @return JComponent controlli creati
     */
    protected abstract JComponent getSearchControl();

    @Override
    public void initialize(Object editorObject) {
        // metodo derivato da AbstractEditor implementato vuoto
    }

    /**
     * Metodo da implementare per personalizzare la refreshCommand sulla searchResult derivata. Di default chiama il
     * metodo search con i parametri salvati.
     */
    protected void refresh() {
        logger.debug("---> Enter refresh");
        search(searchParameters);
    }

    /**
     * registra deleteCommand e refreshCommand, inoltre annulla la registrazione di saveCommand e saveAsCommand,
     * registrati dalla classe base (AbstractEditor).
     * 
     * @param context
     *            context
     */
    @Override
    protected final void registerLocalCommandExecutors(PageComponentContext context) {
        logger.debug("---> AbstractSearchResult: Enter registerLocalCommandExecutors");
        // tolgo save e saveas, commands registrati di default sull'editor
        context.register(JecGlobalCommandIds.SAVE, null);
        context.register(JecGlobalCommandIds.SAVE_AS, null);
        // chiama il metodo protected per aggiungere altri executors oltre a
        // quelli registrari di default sulla SearchResult
        registerSearchResultCommandsExecutors(context);
    }

    /**
     * Metodo da implementare per registrare commandsExecutors personalizzati sulla searchResult derivata questi sono
     * aggiunti ai commands di default che sono refresh e delete.
     * 
     * @param context
     *            context
     */
    protected void registerSearchResultCommandsExecutors(PageComponentContext context) {
    }

    @Override
    public void save(ProgressMonitor saveProgressTracker) {
    }

    /**
     * Metodo che aggiorna il contenuto del searchResultEditor questo metodo viene chiamato dalla openResultView() della
     * classe PanjeaDockingApplicationPage.
     * 
     * @param parameters
     *            i parametri usati per la ricerca
     */
    public void search(Map<String, Object> parameters) {
        // salvo i parametri
        searchParameters = parameters;
        // chiamo il metodo della classe estesa
        executeSearch(parameters);
    }

    @Override
    public void setEditorInput(Object input) {
    }

    /**
     * @param results
     *            results
     */
    public abstract void viewResults(Collection<T> results);
}
