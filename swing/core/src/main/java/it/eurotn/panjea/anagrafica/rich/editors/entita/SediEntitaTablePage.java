package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Gestione delle sedi entita'.
 *
 * @author Leonardo
 */
public class SediEntitaTablePage extends AbstractTablePageEditor<SedeEntita> {

    private class DeleteSedeActionCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand actioncommand) {
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            SedeEntita sedeEntita = getTable().getSelectedObject();
            actioncommand.addParameter(DeleteSedeEntitaCommand.PARAM_SEDE_ENTITA, sedeEntita);
            if (sedeEntita.getTipoSede().isSedePrincipale()) {
                // blocco la cancellazione della sede se e' una sede
                // entita principale
                showAlertSedePrincipaleDialog();
                return false;
            }
            return true;
        }
    }

    private class NewSedeEntitaCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public NewSedeEntitaCommand() {
            super(NEW_SEDE_ENTITA_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            SedeEntita nuovaSedeEntita = new SedeEntita();
            AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            nuovaSedeEntita.getSede().getDatiGeografici().setNazione(aziendaCorrente.getNazione());
            nuovaSedeEntita.setEntita(entita);
            sedeEntitaCompositePage.update(null, nuovaSedeEntita);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(NEW_SEDE_ENTITA_COMMAND);
        }

    }

    /**
     * Comando per creare una nuova sede entita selezionando prima una sede anagrafica esistente.
     *
     * @author Aracno
     * @version 1.0, 28/lug/06
     */
    private class NewSelectionSedeAnagraficaCommad extends ActionCommand {

        /**
         * Costruttore.
         */
        public NewSelectionSedeAnagraficaCommad() {
            super(NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND);
            setSecurityControllerId(PAGE_ID + "." + NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("selezioneSedeAnagraficaDialog.title", new Object[] {},
                    Locale.getDefault());

            List<SedeAnagrafica> listSediAnagrafiche = anagraficaBD.caricaSediAnagrafica(entita.getAnagrafica());
            ListSelectionDialog selectionDialog = new ListSelectionDialog(titolo, (Window) null, listSediAnagrafiche) {

                @Override
                protected void onSelect(Object selection) {
                    SedeEntita nuovaSedeEntita = new SedeEntita();
                    nuovaSedeEntita.setSede((SedeAnagrafica) selection);
                    nuovaSedeEntita.setEntita(entita);
                    sedeEntitaCompositePage.update(null, nuovaSedeEntita);
                }
            };

            selectionDialog.setRenderer(new DefaultListCellRenderer() {

                /**
                 * Comment for <code>serialVersionUID</code>
                 */
                private static final long serialVersionUID = -6600465552132434943L;

                @Override
                public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
                        boolean arg4) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
                    label.setText(
                            ((SedeAnagrafica) arg1).getDescrizione() + " - " + ((SedeAnagrafica) arg1).getIndirizzo()
                                    + " - " + ((SedeAnagrafica) arg1).getDatiGeografici().getDescrizioneLocalita());
                    return label;
                }
            });
            selectionDialog.showDialog();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND);
        }
    }

    public static final String PAGE_ID = "sediEntitaTablePage";

    private static final String NEW_SELECTION_SEDE_ANAGRAFICA_COMMAND = "newSelectionSedeAnagraficaCommand";
    private static final String NEW_SEDE_ENTITA_COMMAND = "newCommand";
    private IAnagraficaBD anagraficaBD;

    private Entita entita;

    private SedeEntitaCompositePage sedeEntitaCompositePage;

    private DeleteSedeEntitaCommand deleteSedeEntitaCommand;

    private CommandGroup nuovoCommandGroup;
    private NewSelectionSedeAnagraficaCommad newSelectionSedeAnagraficaCommad = new NewSelectionSedeAnagraficaCommad();
    private NewSedeEntitaCommand newSedeEntitaCommand = new NewSedeEntitaCommand();

    private DeleteSedeActionCommandInterceptor deleteSedeActionCommandInterceptor;

    private SedeEntita sedeToSelect = null;

    /**
     * Costruttore.
     */
    protected SediEntitaTablePage() {
        super(PAGE_ID, new String[] { "abilitato", "sede.descrizione", "sede.datiGeografici.localita", "sede.indirizzo",
                "tipoSede.descrizione", "sedeCollegata", "codice", "predefinita" }, SedeEntita.class);
        getTable().setTableType(TableType.GROUP);
    }

    @Override
    protected JComponent createControl() {
        final JSplitPane panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, super.createControl(),
                getComponentFactory().createScrollPane(sedeEntitaCompositePage.getControl()));

        JPanel rootPanel = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = -9037056275500327799L;
            private boolean painted;

            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);

                if (!painted) {
                    painted = true;
                    panel.setDividerLocation(0.25);
                }
            }
        };
        rootPanel.add(panel, BorderLayout.CENTER);

        // panel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        // panel.add(super.createControl());
        getTable().addSelectionObserver(sedeEntitaCompositePage);

        // panel.add(getComponentFactory().createScrollPane(sedeEntitaCompositePage.getControl()));
        return rootPanel;
    }

    /**
     * Command group per creare una nuova sede entita' o una sede da una sede anagrafica esistente.
     *
     * @return {@link CommandGroup}
     */
    private CommandGroup createNuovoCommandGroup() {
        nuovoCommandGroup = new JECCommandGroup("buttonNuovaSedeEntita");
        final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        nuovoCommandGroup.setSecurityControllerId(getPageSecurityEditorId() + ".controller");
        c.configure(nuovoCommandGroup);

        nuovoCommandGroup.add(newSedeEntitaCommand);
        nuovoCommandGroup.add(newSelectionSedeAnagraficaCommad);

        return nuovoCommandGroup;
    }

    @Override
    public void dispose() {
        deleteSedeEntitaCommand.removeCommandInterceptor(getDeleteSedeActionCommandInterceptor());
        nuovoCommandGroup.remove(newSedeEntitaCommand);
        nuovoCommandGroup.remove(newSelectionSedeAnagraficaCommad);
        newSedeEntitaCommand = null;
        newSelectionSedeAnagraficaCommad = null;
        nuovoCommandGroup = null;

        super.dispose();
    }

    @Override
    public AbstractCommand[] getCommands() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { createNuovoCommandGroup(),
                getDeleteSedeEntitaCommand(), getRefreshCommand() };
        return abstractCommands;
    }

    /**
     * @return deleteSedeActionCommandInterceptor
     */
    private DeleteSedeActionCommandInterceptor getDeleteSedeActionCommandInterceptor() {
        if (deleteSedeActionCommandInterceptor == null) {
            deleteSedeActionCommandInterceptor = new DeleteSedeActionCommandInterceptor();
        }
        return deleteSedeActionCommandInterceptor;
    }

    /**
     * @return the deleteSedeEntitaCommand
     */
    public DeleteSedeEntitaCommand getDeleteSedeEntitaCommand() {
        if (deleteSedeEntitaCommand == null) {
            deleteSedeEntitaCommand = new DeleteSedeEntitaCommand(this.anagraficaBD, this);
            deleteSedeEntitaCommand.addCommandInterceptor(getDeleteSedeActionCommandInterceptor());
        }
        return deleteSedeEntitaCommand;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return getDeleteSedeEntitaCommand();
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        if (sedeEntitaCompositePage.getActivePage() == null) {
            return null;
        }
        return ((IEditorCommands) sedeEntitaCompositePage.getActivePage()).getEditorLockCommand();
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return newSedeEntitaCommand;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        if (sedeEntitaCompositePage.getActivePage() == null) {
            return null;
        }
        return ((IEditorCommands) sedeEntitaCompositePage.getActivePage()).getEditorSaveCommand();
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        if (sedeEntitaCompositePage.getActivePage() == null) {
            return null;
        }
        return ((IEditorCommands) sedeEntitaCompositePage.getActivePage()).getEditorUndoCommand();
    }

    @Override
    public boolean isLocked() {
        boolean isLocked = super.isLocked();
        return isLocked || sedeEntitaCompositePage.isLocked();
    }

    @Override
    public List<SedeEntita> loadTableData() {

        List<SedeEntita> sedi = new ArrayList<SedeEntita>();
        // NPE MAIL: id entità null, ma per sicurezza controllo anche l'entità
        if (entita != null) {
            sedi = anagraficaBD.caricaSediEntita(null, entita.getId(), CaricamentoSediEntita.TUTTE, Boolean.TRUE);
        }

        return sedi;
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        super.onEditorEvent(event);

        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        if (panjeaEvent.getSource() instanceof SedeEntita
                && (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.CREATED)
                        || panjeaEvent.getEventType().equals(LifecycleApplicationEvent.MODIFIED))) {

            SedeEntita sedeEntita = (SedeEntita) panjeaEvent.getSource();

            getTable().replaceOrAddRowObject(sedeEntita, sedeEntita, sedeEntitaCompositePage);

            // se la sede è principale o predefinita vado a ricaricare tutto perchè potrebbero essere cambiate le
            // rispettive proprietà di altre sedi ( esiste solo 1 principale e 1 predefinita )
            if (sedeEntita.getTipoSede().isSedePrincipale() || sedeEntita.isPredefinita()) {
                // salvo la sede attualmente selezionata per poi riselezionarla a fine caricamento sulla
                // processtabledata
                sedeToSelect = getTable().getSelectedObject();

                loadData();
                if (entita != null) {
                    Entita entitaPrincipale = anagraficaBD.caricaEntita(entita.getEntitaLite(), false);
                    firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, entitaPrincipale);
                } else {
                    // MAIL NPE verifico sede e entita
                    logger.error("--> Errore NPE MAIL detail: sedeEntita: " + sedeEntita + ", entita: " + entita);
                }
            }
        }
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (entita.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public void processTableData(Collection<SedeEntita> results) {
        super.processTableData(results);
        sedeEntitaCompositePage.update(null, getTable().getSelectedObject());

        // NPE MAIL: entitaPanel null, i controlli non sono ancora creati o c'è
        // un errore alla chiusura dell'editor
        // forse ???
        if (isControlCreated()) {

            if (sedeToSelect != null) {
                getTable().selectRowObject(sedeToSelect, null);
                sedeToSelect = null;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        if (JecCompositeDialogPage.PAGE_ACTIVE_PROPERTY.equals(event.getPropertyName())) {
            firePropertyChange(JecCompositeDialogPage.GLOBAL_COMMAND_PROPERTY, null, this);
        }
    }

    @Override
    public List<SedeEntita> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
        super.restoreState(settings);
        sedeEntitaCompositePage.restoreState(settings);
    }

    @Override
    public void saveState(Settings settings) {
        super.saveState(settings);
        sedeEntitaCompositePage.saveState(settings);
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        this.entita = (Entita) object;
    }

    /**
     * @param sedeEntitaCompositePage
     *            the sedeEntitaCompositePage to set
     */
    public void setSedeEntitaCompositePage(SedeEntitaCompositePage sedeEntitaCompositePage) {
        this.sedeEntitaCompositePage = sedeEntitaCompositePage;
        // Quando cambia la activePage rilancio la mia activePage per
        // riassociare gli shortcut
        this.sedeEntitaCompositePage.addPropertyChangeListener(this);
    }

    /**
     * showAlertSedePrincipaleDialog.
     */
    private void showAlertSedePrincipaleDialog() {
        MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                .getService(MessageSourceAccessor.class);
        Object[] parameters = new Object[] {
                messageSourceAccessor.getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) };
        String titolo = messageSourceAccessor.getMessage("sedeEntita.deleteSedePrincipale.confirm.title",
                new Object[] {}, Locale.getDefault());
        String messaggio = messageSourceAccessor.getMessage("sedeEntita.deleteSedePrincipale.confirm.message",
                parameters, Locale.getDefault());
        MessageDialog dialog = new MessageDialog(titolo, messaggio);
        dialog.showDialog();
    }

    @Override
    public void unLock() {
        sedeEntitaCompositePage.unLock();
        super.unLock();
    }
}
