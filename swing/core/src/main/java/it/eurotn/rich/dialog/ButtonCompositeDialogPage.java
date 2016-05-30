package it.eurotn.rich.dialog;

import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.swing.JideSwingUtilities;

/**
 * Visualizza una sola DialogPageEditor alla volta con la possibilit� di passare. ad un'altra con una barra laterale
 * 
 * @author giangi
 * 
 */
public class ButtonCompositeDialogPage extends JecCompositeDialogPage {

    private class DialogPageCommand extends ActionCommand {

        private final class PagePropertyChange implements PropertyChangeListener {
            /**
             * @param evt
             *            .
             */
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                logger.debug("--> PROPERTY PAGINA: " + evt.getPropertyName());

                // propertychange su pageValid per cambiare il face del command
                // nel caso in cui il valore sia false
                if ("pageValid".equals(evt.getPropertyName())) {
                    Boolean valid = (Boolean) evt.getNewValue();
                    if (valid) {
                        setFaceDescriptor(descriptorEnable);
                    } else {
                        setFaceDescriptor(descriptorNotEnable);
                    }
                }

                if (PAGE_VISIBLE.equals(evt.getPropertyName())) {
                    logger.debug("--> PAGE_VISIBLE " + evt.getNewValue());
                    DialogPageCommand.this.setEnabled((Boolean) evt.getNewValue());
                }

                if (SHOW_INIT_PAGE.equals(evt.getPropertyName())) {
                    logger.debug("--> propertyName SHOW_INIT_PAGE ");
                    ButtonCompositeDialogPage.this.showInitialPage();
                }
            }
        }

        private final DialogPage page;
        private final CommandFaceDescriptor descriptorEnable;
        private final CommandFaceDescriptor descriptorNotEnable;
        private PagePropertyChange pagePropertyChange = null;

        private AbstractButton button = null;

        /**
         * 
         * 
         * @param page
         *            .
         */
        public DialogPageCommand(final DialogPage page) {
            super(ButtonCompositeDialogPage.this.getId() + "." + page.getId() + "Command");
            this.page = page;
            logger.debug("-->Creo il DialogPageCommand :" + this.hashCode());
            getCommandConfigurer().configure(this);
            descriptorEnable = getFaceDescriptor();
            descriptorNotEnable = new CommandFaceDescriptor(descriptorEnable.getText(),
                    getIconSource().getIcon("command.notEnable.image"), null);
            pagePropertyChange = new PagePropertyChange();
            this.page.addPropertyChangeListener(pagePropertyChange);
        }

        /**
         * chiamata per pulire i vari riferimenti alla dialogPage.
         */
        public void dispose() {
            page.removePropertyChangeListener(pagePropertyChange);
        }

        @Override
        protected void doExecuteCommand() {
            showPage(page);
            // richiamo il componentocusGained
            ButtonCompositeDialogPage.this.componentFocusGained();
        }

        /**
         * 
         * @return commandConfigurer.
         */
        private CommandConfigurer getCommandConfigurer() {
            return (CommandConfigurer) getService(CommandConfigurer.class);
        }

        @Override
        public CommandFaceDescriptor getFaceDescriptor() {
            return super.getFaceDescriptor();
        }

        /**
         * 
         * @return PageId(String).
         */
        public String getPageId() {
            return this.page.getId();
        }

        @Override
        public String getSecurityControllerId() {
            return this.getId();
        }

        /**
         * Sovrascrivo questo metodo per accedere al controllo di this, quando viene attaccato il button a this creo un
         * overlay e lo applico al control del button inizializzandolo invisibile.
         * 
         * @param paramButton
         *            .
         */
        @Override
        protected void onButtonAttached(AbstractButton paramButton) {
            super.onButtonAttached(paramButton);
            this.button = paramButton;
            this.button.setName(ButtonCompositeDialogPage.this.getId() + "." + page.getId() + "Command");
        }

        /**
         * @param selected
         *            selected
         */
        public void setSelected(boolean selected) {
            if (button != null) {
                Font f = button.getFont();
                button.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
                if (selected) {
                    button.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
                }
            }
        }

    }

    private final Logger logger = Logger.getLogger(ButtonCompositeDialogPage.class);

    public static final String PAGE_VISIBLE = "pageVisible";

    private CardLayout cardLayout;
    private JPanel pagesPanel = new JPanel();
    // Pannello contentente i controlli
    // delle
    // pagine con un
    // cardLayout
    private JPanel mainPanel;
    private static final String GRUPPODEFAULT = "default";
    private static final int BUTTONSTACKWIDTH = 135;

    // barra contenente la lista di listaDialogPageCommand
    private CollapsiblePanes buttonBar;
    private List<DialogPageCommand> listDialogPageCommand;
    protected Map<String, List<DialogPage>> gruppi;
    // Tiene l'ordinamento dei gruppi come viene inserito nell'xml
    private List<String> gruppiOrdinati;

    // Quando chiamo la addPage carico la pagina nel gruppoCorrente,
    // se è un group: lo setto con il nome del gruppo
    private String gruppoCorrente;
    private CollapsiblePane pannello;

    // defenisce la collocazione del panel dei command all'interno della
    // SplitPane
    private boolean commandsPanelOnLeft = true;

    /**
     * 
     * @param pageId
     *            .
     */
    public ButtonCompositeDialogPage(final String pageId) {
        super(pageId);
    }

    /**
     * 
     * @param pageId
     *            .
     * @param enabledOnOpen
     *            .
     */
    public ButtonCompositeDialogPage(final String pageId, final boolean enabledOnOpen) {
        super(pageId);
    }

    /*
     * considero anche i gruppi
     * 
     * @see it.eurotn.rich.dialog.JecCompositeDialogPage#addPage(java.lang.String)
     */
    @Override
    public void addPage(String idPage) {

        if (gruppiOrdinati == null) {
            // Creo la mappa e setto il gruppo default
            gruppiOrdinati = new ArrayList<String>();
            gruppi = new HashMap<String, List<DialogPage>>();

            // Creo il gruppo di default
            this.gruppi.put(GRUPPODEFAULT, new ArrayList<DialogPage>());
            this.gruppiOrdinati.add(GRUPPODEFAULT);

            // E' la prima pagina che inserisco e non ho un gruppo settato
            // setto il default
            this.gruppi.put(GRUPPODEFAULT, new ArrayList<DialogPage>());
            this.gruppiOrdinati.add(GRUPPODEFAULT);
            gruppoCorrente = GRUPPODEFAULT;
        }

        if (idPage.startsWith("group:")) {
            String gruppo = idPage.substring("group:".length());
            if (!gruppi.containsKey(gruppo)) {
                // Se il gruppo non è nella mappa (quindi è un nuovo gruppo)
                // lo inserisco
                this.gruppi.put(gruppo, new ArrayList<DialogPage>());
                gruppiOrdinati.add(gruppo);
            }
            gruppoCorrente = gruppo;
        } else {
            DialogPage pagina = (DialogPage) Application.instance().getApplicationContext().getBean(idPage);
            if (pagina != null) {
                logger.debug("-->Inserisco la pagina " + idPage + " newl gruppo " + gruppoCorrente);
                addPage(pagina);
                gruppi.get(gruppoCorrente).add(pagina);
            } else {
                logger.error("-->Non posso inserire la pagina " + idPage + " perchè il bean non esiste.");
                throw new RuntimeException(idPage + " non esiste");
            }
        }

    }

    @Override
    protected JComponent createPageControl() {
        logger.debug("--> Enter createPageControl");
        cardLayout = new CardLayout();
        pagesPanel = new JPanel(cardLayout);
        mainPanel = new JPanel(new BorderLayout());

        if (getPages().size() == 1) {
            // Creo solamente il cardLayout,
            // la buttonbar non serve
            mainPanel.add(pagesPanel, BorderLayout.CENTER);
            showPage((DialogPage) getPages().get(0));
        } else {
            // creo la buttonBar per organizzare le pagine
            buttonBar = new CollapsiblePanes();
            buttonBar.setFocusable(false);

            buttonBar.setAutoscrolls(true);
            // Scorro i gruppi ed inserisco i pulsanti
            for (String gruppo : gruppiOrdinati) {
                pannello = new CollapsiblePane();
                pannello.setFocusable(false);
                pannello.setContentAreaFilled(true);
                pannello.setEmphasized(true);
                pannello.setShowTitleBar(true);
                pannello.setStyle(CollapsiblePane.DROPDOWN_STYLE);
                pannello.setShowExpandButton(false);
                pannello.setOpaque(false);
                if (!GRUPPODEFAULT.equals(gruppo)) {
                    // Setto l'icona del gruppo
                    Icon icon = getIconSource().getIcon("group." + gruppo + ".icon");
                    if (icon != null) {
                        pannello.setTitleIcon(icon);
                    } else {
                        logger.warn("-->Non riesco a trovare l'icona per il pulsante del gruppo. Id cercato: "
                                + "group." + gruppo + ".icon");
                    }

                    // Setto la caption del gruppo
                    String caption = getMessage("group." + gruppo + ".caption");
                    if ("".equals(caption)) {
                        caption = "group." + gruppo + ".caption";
                    } else {
                        // la caption deve essere lunga esattamente 16 caratteri
                        // la taglio se � maggiore
                        if (caption.length() > 16) {
                            caption = caption.substring(0, 16);
                        }

                        // La riempio se � minore
                        if (caption.length() < 16) {
                            char[] output = new char[15];
                            Arrays.fill(output, ' ');
                            System.arraycopy(caption.toCharArray(), 0, output, 0, caption.length());
                            caption = new String(output);
                        }
                        pannello.setTitle(caption);
                    }
                }
                // Per ogni pagina del gruppo
                if (listDialogPageCommand == null) {
                    listDialogPageCommand = new ArrayList<DialogPageCommand>();
                }

                JECCommandGroup commandGroup = new JECCommandGroup();
                // CommandGroup commandGroup=new CommandGroup();
                Integer i = 0;
                for (DialogPage page : gruppi.get(gruppo)) {
                    DialogPageCommand command = new DialogPageCommand(page);
                    i++;
                    listDialogPageCommand.add(command);
                    commandGroup.add(command);
                }

                // Se nel gruppo non ho pagine non lo inserisco
                if (gruppi.get(gruppo).size() > 0) {
                    JComponent c = commandGroup.createButtonStack();
                    pannello.setContentPane(c);
                    pannello.setAutoscrolls(true);
                    JideSwingUtilities.setOpaqueRecursively(pannello, false);
                    buttonBar.add(pannello);
                }
            }

            // Creo il pannello che va a sinistra contenente la ButtonStack
            buttonBar.setOpaque(false);
            buttonBar.addExpansion();
            buttonBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

            JPanel commandPanel = getComponentFactory().createPanel(new BorderLayout());
            JScrollPane scrollPane = getComponentFactory().createScrollPane(buttonBar);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setOpaque(false);
            commandPanel.add(scrollPane, BorderLayout.CENTER);

            scrollPane.setMinimumSize(new Dimension(BUTTONSTACKWIDTH, 400));
            scrollPane.setPreferredSize(new Dimension(BUTTONSTACKWIDTH, 400));
            commandPanel.setMinimumSize(new Dimension(BUTTONSTACKWIDTH, 400));
            commandPanel.setPreferredSize(new Dimension(BUTTONSTACKWIDTH, 400));
            JideSwingUtilities.setOpaqueRecursively(commandPanel, false);

            JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            if (commandsPanelOnLeft) {
                jSplitPane.setLeftComponent(commandPanel);
                // jSplitPane.setRightComponent(getComponentFactory().createScrollPane(pagesPanel));
                jSplitPane.setRightComponent(pagesPanel);
            } else {
                // jSplitPane.setLeftComponent(getComponentFactory().createScrollPane(pagesPanel));
                jSplitPane.setLeftComponent(pagesPanel);
                jSplitPane.setRightComponent(commandPanel);
            }

            // jSplitPane.setUI(new BasicSplitPaneUI() {
            // @Override
            // public BasicSplitPaneDivider createDefaultDivider() {
            // return new BasicSplitPaneDivider(this) {
            // /**
            // * Comment for <code>serialVersionUID</code>
            // */
            // private static final long serialVersionUID = 1L;
            //
            // @Override
            // public void setBorder(Border b) {
            // super.setBorder(b);
            // }
            //
            // };
            // }
            // });
            jSplitPane.setDividerSize(1);
            jSplitPane.setBorder(null);
            mainPanel.add(jSplitPane, BorderLayout.CENTER);
            mainPanel.setOpaque(false);

            // Visualizzo la prima pagina
            showPage((DialogPage) getPages().get(0));
        }
        return mainPanel;
    }

    @Override
    public void dispose() {
        super.dispose();
        // Per ogni command caricato
        // chiamo la sua dispose
        if (listDialogPageCommand != null) {
            for (DialogPageCommand pageCommand : listDialogPageCommand) {
                pageCommand.dispose();
            }
        }

        for (DialogPage page : getDialogPages()) {
            if (isPageLoaded(page.getId())) {
                ((IPageLifecycleAdvisor) page).dispose();
            }
        }
        if (pagesPanel != null) {
            pagesPanel.removeAll();
        }
        if (mainPanel != null) {
            mainPanel.removeAll();
        }
    }

    /**
     * Sovrascrivo a causa del fatto che per cambiare pagina attiva su una button composite non � sufficiente chiamare
     * il metodo setActivePage, � invece utile usare il command specifico associato alla dialogPage.
     * 
     * @param idDialogPage
     *            .
     */
    @Override
    protected void handleChangeActivePage(String idDialogPage) {
        showPage(getPage(idDialogPage));
        ButtonCompositeDialogPage.this.componentFocusGained();
    }

    /**
     * Il cambio della pagina attiva avviene solo dal primo all'ultimo elemento senza chiudere il ciclo tra le pagine
     * aggiunte dall'editor nella compositeDialogPage.
     * 
     * @return boolean.
     */
    @Override
    protected boolean isCycleLoopActive() {
        return false;
    }

    /**
     * Oltre a caricare la pagina normalmente inserisce i suoi controlli nel cardLayout.
     * 
     * @param page
     *            .
     */
    @Override
    protected void loadPage(DialogPage page) {
        // Prima di chiamare la super.loadPage
        // devo creare i controlli perch� nella super
        // chiamo la loadData
        pagesPanel.add(page.getControl(), page.getId());
        super.loadPage(page);
    }

    /**
     * Metto a dirty tutte le pagine (esclusa quella che ha generato l'evento).
     * 
     * @param domainObject
     *            .
     * @param pageSource
     *            .
     */
    @Override
    protected void objectChange(Object domainObject, DialogPage pageSource) {
        logger.debug("--> Enter objectChange");
        for (DialogPage page : getDialogPages()) {
            if (pageSource == null || !page.getId().equals(pageSource.getId())) {
                pagesDirty.put(page.getId(), true);
            }
        }
        if (pageSource == null) {
            pagesLoaded.clear();
            showPage(getPage(getPagesLinkedList().get(0)));
        }
        logger.debug("--> Exit objectChange");
    }

    @Override
    public void restoreState(Settings settings) {
        super.restoreState(settings);
        // devo ripristinare la prima pagina attiva della buttonComposite dato
        // che
        // quando viene chiamata la loadPage all'apertura dell'editor, il
        // settings �
        // ancora a null, il restore sulla pagina attiva devo farlo a mano
        if (getActivePage() instanceof Memento) {
            ((Memento) getActivePage()).restoreState(settings);
        }
    }

    /**
     * Rispetto all'implementazione base aggiungo l'overlay alla pagina che verra' settata come corrente e lo rimuovo da
     * quella che verra' disabilitata.
     * 
     * @param activePage
     *            .
     */
    @Override
    public void setActivePage(DialogPage activePage) {
        // Eseguo solamente se ho più pagine nell'editor
        // gli editor con una pagina solamente non creano la lista di
        // DialogPageCommand
        if (getPages().size() > 1) {
            if (listDialogPageCommand != null) {
                for (DialogPageCommand command : listDialogPageCommand) {
                    // disabilito il pulsante della nuova pagina che si andra' a
                    // visualizzare
                    if (activePage.getId().equals(command.getPageId())) {
                        command.setSelected(false);
                    } else {
                        // se esisteva, riabilito il pulsante della pagina che si
                        // stava visualizzando
                        if ((getActivePage() != null) && (getActivePage().getId().equals(command.getPageId()))) {
                            command.setSelected(true);
                        }
                    }
                }
            }
        }
        // if (!pagesLoaded.containsKey(activePage.getId())) {
        // loadPage(activePage);
        // }
        super.setActivePage(activePage);
    }

    /**
     * @param commandsPanelOnLeft
     *            The commandsPanelOnLeft to set.
     */
    public void setCommandsPanelOnLeft(boolean commandsPanelOnLeft) {
        this.commandsPanelOnLeft = commandsPanelOnLeft;
    }

    @Override
    protected void showComponentPage(DialogPage page) {
        cardLayout.show(pagesPanel, page.getId());
    }
}
