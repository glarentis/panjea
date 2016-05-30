package it.eurotn.panjea.magazzino.rich.editors.articolo;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;
import it.eurotn.panjea.magazzino.rich.forms.articolo.ArticoloAttributiForm;
import it.eurotn.panjea.magazzino.rich.forms.articolo.ArticoloForm;
import it.eurotn.panjea.magazzino.rich.forms.articolo.ArticoloNoteForm;
import it.eurotn.panjea.magazzino.rich.forms.articolo.DescrizioniEsteseArticoloForm;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Gestisce la visualizzazione dell'anagrafica di un articolo.
 *
 * @author adriano
 * @version 1.0, 16/mag/08
 */
public class ArticoloPage extends FormsBackedTabbedDialogPageEditor implements InitializingBean {

    private class ReloadArticoloTRiggerChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Articolo articolo = magazzinoAnagraficaBD.caricaArticolo((Articolo) getBackingFormPage().getFormObject(),
                    true);
            ArticoloCategoriaDTO artCat = new ArticoloCategoriaDTO(articolo, null);
            preSetFormObject(artCat);
            setFormObject(artCat);
            postSetFormObject(artCat);
            refreshData();
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, artCat);
        }

    }

    private final class TabbedChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent event) {
            if (getCurrentPage() != ArticoloPage.this) {
                if (!((AbstractDialogPage) getCurrentPage()).isControlCreated()) {
                    JideTabbedPane pane = (JideTabbedPane) event.getSource();
                    JPanel panel = (JPanel) pane.getComponentAt(pane.getSelectedIndex());
                    panel.removeAll();
                    panel.add(getCurrentPage().getControl(), BorderLayout.CENTER);
                    ((IPageLifecycleAdvisor) getCurrentPage()).restoreState(settings);
                }
                ((IPageLifecycleAdvisor) getCurrentPage()).preSetFormObject(getForm().getFormObject());
                ((IPageLifecycleAdvisor) getCurrentPage()).setFormObject(getForm().getFormObject());
                ((IPageLifecycleAdvisor) getCurrentPage()).postSetFormObject(getForm().getFormObject());

                ((IPageLifecycleAdvisor) getCurrentPage()).refreshData();
            }
            firePropertyChange(JecCompositeDialogPage.GLOBAL_COMMAND_PROPERTY, null, ArticoloPage.this);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ArticoloPage.class);

    public static final String PAGE_ID = "articoloPage";

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
    private IAnagraficaTabelleBD anagraficaTabelleBD;
    private DescrizioniEsteseArticoloForm descrizioniEsteseArticoloForm;
    private Categoria categoria;

    private boolean isArticoloChanged;
    private AziendaCorrente aziendaCorrente;

    private ArticoloNoteForm articoloNoteForm;
    private ArticoloMarchiCECommand articoloMarchiCECommand;
    private JideTabbedPane tabbedPane;

    private List<DialogPage> pagineCollegate;

    private TabbedChangeListener tabbedChangeListener;

    private CopiaArticoloCommand copiaArticoloCommand;

    private ReloadArticoloTRiggerChangeListener reloadArticoloTRiggerChangeListener;

    private Articolo articoloCorrente;

    private Settings settings;

    private ArticoloAttributiForm attributiForm;

    /**
     * costruttore.
     */
    public ArticoloPage() {
        super(PAGE_ID, new ArticoloForm());
    }

    @Override
    public void addForms() {
        LOGGER.debug("--> Enter addForms");
        attributiForm = new ArticoloAttributiForm(getForm().getFormModel());
        addForm(attributiForm);

        descrizioniEsteseArticoloForm = new DescrizioniEsteseArticoloForm(getForm().getFormModel());
        addForm(descrizioniEsteseArticoloForm);

        articoloNoteForm = new ArticoloNoteForm(getForm().getFormModel());
        addForm(articoloNoteForm);
        LOGGER.debug("--> Exit addForms");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ((ArticoloForm) getForm()).setMagazzinoAnagraficaBD(magazzinoAnagraficaBD);
    }

    /**
     * @return controlli della pagina
     */
    @Override
    public JComponent createControl() {
        tabbedPane = (JideTabbedPane) getComponentFactory().createTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_VSNET);
        tabbedPane.setTabPlacement(SwingConstants.TOP);
        tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        JComponent pageControl = super.createControl();
        tabbedPane.addTab(getTitle(), pageControl);
        tabbedChangeListener = new TabbedChangeListener();
        tabbedPane.addChangeListener(tabbedChangeListener);

        for (DialogPage pageEditor : pagineCollegate) {
            tabbedPane.addTab(pageEditor.getTitle(), new JPanel(new BorderLayout()));
        }
        return tabbedPane;
    }

    @Override
    public void dispose() {
        tabbedPane.removeChangeListener(tabbedChangeListener);
        for (DialogPage page : pagineCollegate) {
            ((IPageLifecycleAdvisor) page).dispose();
        }
        super.dispose();
    }

    @Override
    public Object doDelete() {
        Articolo articolo = (Articolo) getForm().getFormObject();
        magazzinoAnagraficaBD.cancellaArticolo(articolo);
        // lancio anche l'evento per l'articoloLite
        publishDeleteEvent(articolo.getArticoloRicerca());
        return null;
    }

    @Override
    protected Object doSave() {
        Articolo articolo = (Articolo) getForm().getFormObject();
        articolo = magazzinoAnagraficaBD.salvaArticolo(articolo);
        // ArticoloCategoriaDTO non è un iDefProperty quindi il formBacked non
        // rimuove i valori a null
        return new ArticoloCategoriaDTO(articolo, null);
    }

    /**
     * @return Returns the anagraficaTabelleBD.
     */
    public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
        return anagraficaTabelleBD;
    }

    /**
     * @return the articoloMarchiCECommand
     */
    public ArticoloMarchiCECommand getArticoloMarchiCECommand() {
        if (articoloMarchiCECommand == null) {
            articoloMarchiCECommand = new ArticoloMarchiCECommand();
            articoloMarchiCECommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand arg0) {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("postExecution");
                    }
                }

                @Override
                public boolean preExecution(ActionCommand actionCommand) {

                    actionCommand.addParameter(ArticoloMarchiCECommand.PARAM_ARTICOLO,
                            ArticoloPage.this.getBackingFormPage().getFormObject());
                    return true;
                }
            });
        }

        return articoloMarchiCECommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        LOGGER.debug("--> Enter getCommand");
        AbstractCommand[] defaultCommands = toolbarPageEditor.getDefaultCommand(true);
        AbstractCommand[] commands = Arrays.copyOf(defaultCommands, defaultCommands.length + 2);
        commands[commands.length - 2] = getCopiaArticoloCommand();
        commands[commands.length - 1] = getArticoloMarchiCECommand();
        return commands;
    }

    /**
     *
     * @return command per copiare un articolo
     */
    private CopiaArticoloCommand getCopiaArticoloCommand() {
        if (copiaArticoloCommand == null) {
            copiaArticoloCommand = new CopiaArticoloCommand(magazzinoAnagraficaBD);
            copiaArticoloCommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand actionCommand) {
                    Articolo articoloCopia = ((CopiaArticoloCommand) actionCommand).getArticoloCopiato();
                    ArticoloCategoriaDTO articoloCategoriaDTO = new ArticoloCategoriaDTO(articoloCopia, null);
                    // Lancio due volte il proeprty change altrimenti non mi seleziona
                    // correttametne.
                    firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, articoloCategoriaDTO);
                    firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, articoloCategoriaDTO);
                    preSetFormObject(articoloCategoriaDTO);
                    setFormObject(articoloCategoriaDTO);
                    postSetFormObject(articoloCategoriaDTO);
                    refreshData();
                }

                @Override
                public boolean preExecution(ActionCommand actionCommand) {
                    actionCommand.addParameter(CopiaArticoloCommand.PARAM_ARTICOLO,
                            ArticoloPage.this.getBackingFormPage().getFormObject());
                    return true;
                }
            });
        }
        return copiaArticoloCommand;
    }

    /**
     *
     * @return pagina corrente rispetto al tab selezionato
     */
    private DialogPage getCurrentPage() {
        DialogPage currentPage = null;
        if (!isControlCreated()) {
            getControl();
        }
        if (tabbedPane.getSelectedIndex() == 0) {
            currentPage = this;
        } else {
            int index = tabbedPane.getSelectedIndex() - 1;
            currentPage = pagineCollegate.get(index);
        }
        return currentPage;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        if (getCurrentPage() == this) {
            return super.getEditorDeleteCommand();
        } else if (getCurrentPage() instanceof IEditorCommands) {
            return ((IEditorCommands) getCurrentPage()).getEditorDeleteCommand();
        }
        return null;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        if (getCurrentPage() == this) {
            return super.getEditorLockCommand();
        } else if (getCurrentPage() instanceof IEditorCommands) {
            return ((IEditorCommands) getCurrentPage()).getEditorLockCommand();
        }
        return null;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        if (getCurrentPage() == this) {
            return super.getEditorNewCommand();
        } else if (getCurrentPage() instanceof IEditorCommands) {
            return ((IEditorCommands) getCurrentPage()).getEditorNewCommand();
        }
        return null;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        if (getCurrentPage() == this) {
            return super.getEditorSaveCommand();
        } else if (getCurrentPage() instanceof IEditorCommands) {
            return ((IEditorCommands) getCurrentPage()).getEditorSaveCommand();
        }
        return null;
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        if (getCurrentPage() == this) {
            return super.getEditorUndoCommand();
        } else if (getCurrentPage() instanceof IEditorCommands) {
            return ((IEditorCommands) getCurrentPage()).getEditorUndoCommand();
        }
        return null;
    }

    /**
     *
     * @return anagraficaBD
     */
    public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
        return magazzinoAnagraficaBD;
    }

    @Override
    protected Object getNewEditorObject() {
        Articolo articolo = (Articolo) getBackingFormPage().getFormObject();
        return new ArticoloCategoriaDTO(articolo, null);
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return true;
    }

    @Override
    public void loadData() {
        if (tabbedPane.getSelectedIndex() > 0) {
            int index = tabbedPane.getSelectedIndex() - 1;
            AbstractDialogPage paginaCollegata = (AbstractDialogPage) pagineCollegate.get(index);
            if (paginaCollegata.isControlCreated()) {
                ((IPageLifecycleAdvisor) paginaCollegata).loadData();
            }
        }
        updateTabsVisibility();
    }

    @Override
    public ILock onLock() {

        // mi salvo l'articolo che era prima nella pagina perchè in caso di annulla lo devo
        // ripristinare
        articoloCorrente = (Articolo) getForm().getFormObject();

        // disabilito le altre pagine finchè sono in modifica
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }

        return super.onLock();
    }

    @Override
    public void onNew() {

        // mi salvo l'articolo che era prima nella pagina perchè in caso di annulla lo devo
        // ripristinare
        articoloCorrente = (Articolo) getForm().getFormObject();

        super.onNew();
        Articolo articolo = ArticoloPage.this.magazzinoAnagraficaBD.creaArticolo(categoria);
        getForm().setFormObject(articolo);

        for (DialogPage page : pagineCollegate) {
            if (!((AbstractDialogPage) page).getId().equals(PAGE_ID)) {
                int index = tabbedPane.indexOfTab(page.getTitle());
                // AIOOB exception MAIL non sono riuscito a riprodurla, ma controllo il -1 per
                // evitare l'errore
                if (index != -1) {
                    tabbedPane.setEnabledAt(index, false);
                }
            }
        }
    }

    @Override
    public void onPostPageOpen() {
        LOGGER.debug("--> Enter onPostPageOpen");
        LOGGER.debug("--> Exit onPostPageOpen");
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onSave() {
        boolean save = super.onSave();

        updateTabsVisibility();

        return save;
    }

    @Override
    public boolean onUndo() {
        boolean undo = super.onUndo();

        setFormObject(new ArticoloCategoriaDTO(articoloCorrente, null));

        updateTabsVisibility();

        return undo;
    }

    @Override
    public void postSetFormObject(Object object) {
        super.postSetFormObject(object);
        ((ArticoloForm) getForm()).activatePropertyChangeListeners();
        ArticoloCategoriaDTO articoloCategoriaDTO = (ArticoloCategoriaDTO) object;
        if (articoloCategoriaDTO.getArticolo() != null) {
            for (DialogPage dialogPage : pagineCollegate) {
                ((IPageLifecycleAdvisor) dialogPage).postSetFormObject(articoloCategoriaDTO.getArticolo());
            }
        }
    }

    @Override
    public void preSetFormObject(Object object) {
        ((ArticoloForm) getForm()).deactivatePropertyChangeListeners();
        ArticoloCategoriaDTO articoloCategoriaDTO = (ArticoloCategoriaDTO) object;
        if (articoloCategoriaDTO.getArticolo() != null) {
            articoloCategoriaDTO.getArticolo().setCodiceLinguaAzienda(aziendaCorrente.getLingua());
            isArticoloChanged = !articoloCategoriaDTO.getArticolo().equals(getForm().getFormObject());
            super.preSetFormObject(object);

            for (DialogPage dialogPage : pagineCollegate) {
                ((IPageLifecycleAdvisor) dialogPage).preSetFormObject(articoloCategoriaDTO.getArticolo());
            }
        }
    }

    @Override
    public void refreshData() {
        if (isArticoloChanged) {
            loadData();
        }
    }

    @Override
    public void restoreState(Settings paramSettings) {
        super.restoreState(settings);
        this.settings = paramSettings;
    }

    @Override
    public void saveState(Settings paramSettings) {
        super.saveState(settings);
        for (DialogPage dialogPage : pagineCollegate) {
            if (((AbstractDialogPage) dialogPage).isControlCreated() && (dialogPage instanceof Memento)) {
                ((Memento) dialogPage).saveState(paramSettings);
            }
        }
    }

    /**
     * @param anagraficaTabelleBD
     *            The anagraficaTabelleBD to set.
     */
    public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
        this.anagraficaTabelleBD = anagraficaTabelleBD;
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setFormObject(Object object) {
        ArticoloCategoriaDTO articoloCategoriaDTO = (ArticoloCategoriaDTO) object;
        setReadOnly(false);
        tabbedPane.setEnabled(true);
        if (articoloCategoriaDTO.getArticolo() != null) {
            categoria = articoloCategoriaDTO.getArticolo().getCategoria();
            for (DialogPage dialogPage : pagineCollegate) {
                ((IPageLifecycleAdvisor) dialogPage).setFormObject(articoloCategoriaDTO.getArticolo());
            }
            super.setFormObject(articoloCategoriaDTO.getArticolo());
        }

        if (articoloCategoriaDTO.getCategoria() != null && !articoloCategoriaDTO.getCategoria().isNew()) {
            categoria = articoloCategoriaDTO.getCategoria();
            if (articoloCategoriaDTO.getArticolo() == null) {
                Articolo articolo = magazzinoAnagraficaBD.creaArticolo(categoria);
                getForm().setFormObject(articolo);
                // se il DTO contiene la categoria ma non l'artico, abilito solo il tab "dati
                // articolo" e lo seleziono
                // per far si che l'utente possa inserirne uno
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    tabbedPane.setEnabledAt(i, false);
                }
                tabbedPane.setEnabledAt(0, true);
                tabbedPane.setSelectedIndex(0);
            }
        }

        // se l'editor e' nella cache, nel form l'articolo ha id avvalorato; devo mantenere
        // abilitate le tabbed
        Articolo articoloInForm = (Articolo) getBackingFormPage().getFormObject();
        boolean articoloPresenteInForm = articoloInForm.getId() != null;
        if (!articoloPresenteInForm && (articoloCategoriaDTO.getCategoria() == null
                && articoloCategoriaDTO.getArticolo() == null
                || (articoloCategoriaDTO.getCategoria() != null && articoloCategoriaDTO.getCategoria().isNew()))) {

            setReadOnly(true);
            tabbedPane.setEnabled(false);
        }
    }

    /**
     *
     * @param magazzinoAnagraficaBD
     *            anagraficaBD
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    /**
     * @param pagineCollegate
     *            The pagineCollegate to set.
     */
    public void setPagineCollegate(List<DialogPage> pagineCollegate) {
        this.pagineCollegate = pagineCollegate;
    }

    @Override
    public void updateCommands() {
        if (toolbarPageEditor.getPageEditor() != null) {
            getArticoloMarchiCECommand().setEnabled(!editOggetto);
        }
        super.updateCommands();

        // il comando di copia deve comportarsi come quello di modifica
        getCopiaArticoloCommand().setEnabled(toolbarPageEditor.getLockCommand().isEnabled());
    }

    /**
     * Aggiorna lo stato enabled delle tabbed pages.
     */
    private void updateTabsVisibility() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, true);
        }

    }
}