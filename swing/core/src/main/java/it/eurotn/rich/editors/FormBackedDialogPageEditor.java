package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.validation.ValidationResultsModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.dialog.support.DialogPageUtils;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.ValidationResultsReporter;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;

import com.jidesoft.action.CommandBar;
import com.jidesoft.plaf.xerto.FrameBorder;
import com.toedter.calendar.JDateChooser;

import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaDialogPageUtils;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.command.GlueActionCommand;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.OpenAuditCommand;
import it.eurotn.rich.command.SeparatorActionCommand;
import it.eurotn.rich.editors.ToolbarPageEditor.SaveCommand;
import it.eurotn.rich.editors.controllers.DefaultController;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Form dialog per una singola entit? da editare, supporta l'aggiunta della toolbar grazie all'
 * implementazione di <code>IPageEditor</code> Per sincronizzare le azioni di inserimento e modifica
 * con la rispettiva search result viene lanciato un ApplicationEvent specifico a seconda
 * dell'azione eseguita.
 *
 * @author Aracno,Leonardo
 * @see it.eurotn.rich.editors.IPageEditor
 * @see it.eurotn.rich.editors.ToolbarPageEditor
 */
public abstract class FormBackedDialogPageEditor extends FormBackedDialogPage
        implements IFormPageEditor, IToolbarPageCommands {

    public class AuditCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(OpenAuditCommand.AUDIT_OBJECT,
                    FormBackedDialogPageEditor.this.getBackingFormPage().getFormObject());
            return true;
        }

    }

    public class ExternalCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand paramActionCommand) {
            paramActionCommand.addParameter(FORM_OBJECT_PROPERTY,
                    FormBackedDialogPageEditor.this.getBackingFormPage().getFormObject());
            return true;
        }

    }

    public enum ExternalCommandLayout {
        TOOLBAR_START, TOOLBAR_APPEND, TOOLBAR_LINE_END
    }

    public static final String FORM_OBJECT_PROPERTY = "formObjectProperty";

    private static final Logger LOGGER = Logger.getLogger(FormBackedDialogPageEditor.class);

    private String pageSecurityEditorId = null;

    protected JPanel errorBar;

    protected ToolbarPageEditor toolbarPageEditor = null;

    private boolean nuovoOggetto = true;// Indica se ho un nuovo oggetto

    protected boolean editOggetto = false;// Indica se sono in editazione di un //

    private String idNewCommand = null; // Utilizzato per caricare il newCommand

    private ExternalCommandInterceptor externalCommandInterceptor;
    private AbstractCommand newCommand = null;

    private JPanel panel;

    private AbstractCommand[] toolbarCommands = null;
    private boolean readonly = false;

    /**
     * Valore booleano che indica se includere la titlePane nella creazione dei controlli.
     */
    private boolean showTitlePane = false;
    /**
     * Controller che associa al form model collegato i diversi
     * {@link FormModelPropertyChangeListeners} che aggiungo via xml injection sulle proprieta'
     * specificate.<br>
     * Se dopo aver settato le proprieta' alla page, <code>defaultController</code> risulta essere
     * valorizzato, viene settato il form model ad esso;<br>
     * nella fase successiva, nel lifecycle della pagina,dopo aver settato il form object quando
     * viene chiamato il metodo <br>
     * {@link FormBackedDialogPageEditor.postSetFormObject(Object object)}, vengono registrati i
     * listener sul form model;<br>
     * da questo momento sono attivati i listener definiti.
     */
    protected DefaultController defaultController = null;

    private JComponent pageComponent;
    /**
     * AbstractCommand che possono' essere settati da fuori per integrare la toolbar standard.
     */
    private AbstractCommand[] externalCommandStart;
    private AbstractCommand[] externalCommandAppend;

    private AbstractCommand[] externalCommandLineEnd;
    private ValidationResultsReporter validationResultReporter;
    private JECCommandGroup toolBar;

    private JComponent toolbarComponent;

    private PanjeaFormGuard saveCommandFormGuarded;

    private OpenAuditCommand auditCommand;

    private AuditCommandInterceptor auditCommandInterceptor;

    /**
     * Crea una nuova form backed dialog page editor.
     *
     * @param parentPageId
     *            l'id della form backed
     * @param backingFormPage
     *            il backing form da integrare nella pagina
     */
    public FormBackedDialogPageEditor(final String parentPageId, final Form backingFormPage) {
        super(parentPageId, backingFormPage);
        backingFormPage.getFormModel().setReadOnly(true);
        // Risetto l'id della pagina perchè il comportamento di default è quello
        // di mettere idPagina.idForm
        setId(parentPageId, true);
        LOGGER.debug("--> COSTRUISCO LA PAGINA " + parentPageId + " hashcode " + hashCode());
        toolbarPageEditor = new ToolbarPageEditor(this);

        // Lego un eventual saveCommand al guarded del formModel
        SaveCommand sc = toolbarPageEditor.getSaveCommand();
        if (sc != null && getBackingFormPage().getFormModel() != null) {
            saveCommandFormGuarded = new PanjeaFormGuard(getBackingFormPage().getFormModel(), sc,
                    FormGuard.ON_ISDIRTY + FormGuard.ON_NOERRORS + FormGuard.ON_ENABLED);
        }
    }

    /**
     * Restituisce il primo componente a cui è possibile dare il focus.
     *
     * @param components
     *            lista di componenti
     * @return componente trovato
     */
    public static Component findComponentFocusable(Component[] components) {
        for (Component component : components) {
            LOGGER.debug("--> COMPONENT " + component);
            if (component instanceof JTextField) {
                return component;
            } else if (component instanceof SearchTextField) {
                return ((SearchTextField) component).getTextField();
            } else if (component instanceof JTextArea) {
                return component;
            } else if (component instanceof JEditorPane) {
                return component;
            } else if (component instanceof JTextPane) {
                return component;
            } else if (component instanceof JDateChooser) {
                return ((JDateChooser) component).getDateEditor().getUiComponent();
            } else if (component instanceof JComponent) {
                Component focusableComponent = findComponentFocusable(((JComponent) component).getComponents());
                if (focusableComponent != null) {
                    return focusableComponent;
                }
            }
        }
        return null;
    }

    /**
     * Verifico se l'id dell' oggetto contenuto nel form model ? un nuovo oggetto o uno gi?
     * esistente
     *
     * @param objFormModel
     */
    protected void checkFormObjectIfNew() {
        LOGGER.debug("-->ENTER checkFormObjectIfNew");
        FormModel formModel = getBackingFormPage().getFormModel();
        Object objFormModel = null;
        Integer id = null;

        if (formModel != null) {
            objFormModel = formModel.getFormObject();
        }

        if (objFormModel instanceof IDefProperty) {
            // Se il formObject deriva da IDefProperty vado direttamente sulla
            // propriet?
            // altrimenti vado di reflection
            id = ((IDefProperty) objFormModel).getId();
            LOGGER.debug("-->L'id trovato tramite IDefProperty risulta: " + id);
        } else {
            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(objFormModel);
                id = (Integer) wrapper.getPropertyValue("id");
                LOGGER.debug("-->L'id trovato tramite la reflection risulta: " + id);
            } catch (BeansException e) {
                LOGGER.warn("---> Non riesco a trovare tramite la reflection la propriet? id della classe  "
                        + objFormModel.getClass());
            }
        }
        getForm().setEnabled(true);
        if (id == null) {
            LOGGER.debug("---> Ricevuto un oggetto nuovo");
            nuovoOggetto = true;
            editOggetto = true; // se ? nuovo passo subito in edit
        } else if (id != null && id.intValue() == -1) {
            nuovoOggetto = false;
            editOggetto = false;
            getForm().setEnabled(false);
        } else {
            LOGGER.debug("---> Ricevuto un oggetto da modificare");
            nuovoOggetto = false;
            editOggetto = false;
        }
        LOGGER.debug("-->EXIT checkFormObjectIfNew");
    }

    /**
     * Crea questo editor inserendo la toolbar e il contenuto della pagina.
     *
     * @return JComponent
     */
    @Override
    public JComponent createControl() {
        if (panel == null) {
            PanjeaComponentFactory cf = (PanjeaComponentFactory) ApplicationServicesLocator.services()
                    .getService(ComponentFactory.class);
            pageComponent = super.createControl();
            panel = getComponentFactory().createPanel(new BorderLayout());

            JComponent toolbarPanel = createToobar();
            if (toolbarComponent != null) {
                panel.add(toolbarPanel, BorderLayout.PAGE_START);
            }

            GuiStandardUtils.attachBorder(pageComponent, BorderFactory.createEmptyBorder(0, 10, 10, 10));
            if (insertControlInScrollPane()) {
                panel.add(cf.createScrollPane(pageComponent), BorderLayout.CENTER);
            } else {
                panel.add(pageComponent, BorderLayout.CENTER);
            }

            if (defaultController != null) {
                // setto sempre il formModel , non ho problemi perche' e' sempre
                // lo stesso
                defaultController.setFormModel(this.getForm().getFormModel());
                defaultController.register();
            }
        }
        return panel;
    }

    /**
     * Crea un pannello messagable per stampare i messaggi e gli errori di validazione per il
     * formbacked.
     *
     * @return JComponent
     */
    protected JComponent createErrorBar() {
        errorBar = (JPanel) PanjeaDialogPageUtils.createFormBackedMessagablePanel(this);
        errorBar.setName("errorBar");
        return errorBar;
    }

    /**
     * Crea la toolbar in base ai comandi configurati.
     *
     * @return toolbar
     */
    protected JComponent createToobar() {
        // se ? stato sovrascritto il newCommand nella classe derivata
        // setto quest'ultimo come newCommand della toolbar
        AbstractCommand newCommandOverride = getNewCommand();
        LOGGER.debug("---> Il nuovo comando per la pagina vale " + newCommandOverride);
        if (newCommandOverride != null) {
            LOGGER.debug("--> sovrascrivo il nuovo comando default");
            toolbarPageEditor.setNewCommand(newCommandOverride);
        }

        toolBar = new JECCommandGroup();

        if (externalCommandStart != null) {
            for (AbstractCommand element : externalCommandStart) {
                if (element != null) {
                    if (element instanceof ActionCommand) {
                        ((ActionCommand) element).addCommandInterceptor(getExternalCommandInterceptor());
                    }
                    toolBar.add(element);
                }
            }
        }

        toolbarCommands = getCommand();
        if (toolbarCommands != null) {
            for (AbstractCommand toolbarCommand : toolbarCommands) {
                if (toolbarCommand instanceof GlueActionCommand) {
                    toolBar.addGlue();
                } else if (toolbarCommand instanceof SeparatorActionCommand) {
                    toolBar.addSeparator();
                } else {
                    if (toolbarCommand instanceof ActionCommand) {
                        ((ActionCommand) toolbarCommand).addCommandInterceptor(getExternalCommandInterceptor());
                    }
                    toolBar.add(toolbarCommand);
                }
            }

            if (externalCommandAppend != null) {
                for (AbstractCommand element : externalCommandAppend) {
                    if (element != null) {
                        if (element instanceof ActionCommand) {
                            ((ActionCommand) element).addCommandInterceptor(getExternalCommandInterceptor());
                        }
                        toolBar.add(element);
                    }
                }
            }
        }

        toolBar.addGlue();
        toolBar.add(getAuditCommand());
        if (externalCommandLineEnd != null) {
            for (AbstractCommand element : externalCommandLineEnd) {
                if (element != null) {
                    if (element instanceof ActionCommand) {
                        ((ActionCommand) element).addCommandInterceptor(getExternalCommandInterceptor());
                    }
                    toolBar.add(element);
                }
            }
        }

        toolbarComponent = toolBar.createToolBar();

        JPanel pannelloSuperiore = getComponentFactory().createPanel(new BorderLayout());
        pannelloSuperiore.add(toolbarComponent, BorderLayout.CENTER);

        if (showTitlePane) {
            TitlePane titlePane = DialogPageUtils.createTitlePane(this);
            JComponent titleComponent = titlePane.getControl();
            pannelloSuperiore.add(titleComponent, BorderLayout.NORTH);
        }

        JPanel headPanel = getComponentFactory().createPanel(new BorderLayout());
        headPanel.add(pannelloSuperiore, BorderLayout.PAGE_START);
        headPanel.add(createErrorBar(), BorderLayout.PAGE_END);
        headPanel.setBorder(new FrameBorder());

        headPanel.setName("toolBar");
        return headPanel;
    }

    /**
     * Esegue la dispose della pagina.
     */
    @Override
    public void dispose() {
        LOGGER.debug("--> Dispose formBackedDialogPage " + getId());

        if (saveCommandFormGuarded != null) {
            saveCommandFormGuarded.clear();
            saveCommandFormGuarded = null;
        }

        toolBar = null;
        if (toolbarComponent != null) {
            ((CommandBar) toolbarComponent).removeAll();
        }
        toolbarComponent = null;

        toolbarPageEditor.dispose();

        if (toolbarCommands != null) {
            for (int i = 0; i < toolbarCommands.length; i++) {
                if (toolbarCommands[i] instanceof ActionCommand) {
                    try {
                        ((ActionCommand) toolbarCommands[i]).removeCommandInterceptor(getExternalCommandInterceptor());
                    } catch (IllegalArgumentException e) {
                        LOGGER.debug("Errore nel rimuovere il command interceptor da " + getId());
                    }
                }
                toolbarCommands[i] = null;
            }
        }
        toolbarCommands = null;

        if (defaultController != null) {
            defaultController.unregistrer();
        }
        getForm().getFormModel().removeCommitListener(getForm());
        getForm().removeValidationResultsReporter(validationResultReporter);
        getForm().removeGuarded(this);

        validationResultReporter = null;

        if (getBackingFormPage() instanceof PanjeaAbstractForm) {
            ((PanjeaAbstractForm) getBackingFormPage()).dispose();
        } else {
            LOGGER.error("--> Attenzione, non viene chiamata la dispose del form " + getBackingFormPage().getId()
                    + " perchè non è un PanjeaAbstractForm.");
        }

        externalCommandAppend = null;
        if (externalCommandLineEnd != null) {
            for (int i = 0; i < externalCommandLineEnd.length; i++) {
                if (externalCommandLineEnd[i] instanceof ActionCommand) {
                    ((ActionCommand) externalCommandLineEnd[i])
                            .removeCommandInterceptor(getExternalCommandInterceptor());
                }
                externalCommandLineEnd[i] = null;
            }
        }
        externalCommandStart = null;

        if (auditCommandInterceptor != null) {
            auditCommand.removeCommandInterceptor(auditCommandInterceptor);
            auditCommandInterceptor = null;
            auditCommand = null;
        }
    }

    /**
     * Metodo da sovrascrivere per eseguire ulteriori funzioni sulla cancellazione dell'oggetto.
     * Ritorna l'oggetto cancellato.
     *
     * @return null di default
     */
    protected Object doDelete() {
        return null;
    }

    /**
     * Metodo da sovrascrivere se si vogliono eseguire ulteriori funzioni sul salvataggio del form
     * (commit), ad esempio salvataggio su DB, ecc. ritorna l'oggetto salvato
     *
     * @return null di default
     */
    protected Object doSave() {
        return null;
    }

    /**
     * Esegue il firePropertyChange della proprietà specificata.
     *
     * @param propertyName
     *            proprietà
     * @param oldValue
     *            vecchio valore
     * @param newValue
     *            nuovo valore
     */
    public void firePropertyChangeNew(String propertyName, Object oldValue, Object newValue) {
        firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Restituisce il comando per la visualizzazione dei dati di audit dell'oggetto gestito dalla
     * pagina.
     *
     * @return comando
     */
    public OpenAuditCommand getAuditCommand() {
        if (auditCommand == null) {
            auditCommand = new OpenAuditCommand();
            auditCommand.addCommandInterceptor(getAuditCommandInterceptor());
        }
        return auditCommand;
    }

    /**
     * @return Returns the auditCommandInterceptor.
     */
    public AuditCommandInterceptor getAuditCommandInterceptor() {
        if (auditCommandInterceptor == null) {
            auditCommandInterceptor = new AuditCommandInterceptor();
        }
        return auditCommandInterceptor;
    }

    /**
     * metodo da sovrascrivere nella classe derivata per attivare una toolbar nell'editor.
     *
     * @return AbstractCommand[]
     */
    protected AbstractCommand[] getCommand() {
        return null;
    }

    /**
     *
     * @return the defaultController
     */
    public DefaultController getDefaultController() {
        return defaultController;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return toolbarPageEditor.getDeleteCommand();
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return toolbarPageEditor.getLockCommand();
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        // Se sovrascrivo la getNewCommand con un comando "esterno"
        // sul costruttore viene comunque assegnato alla
        // toolbarPageEditor.getNewCommand()
        // tramite toolbarPageEditor.setNewCommand()
        return toolbarPageEditor.getNewCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return toolbarPageEditor.getSaveCommand();
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return toolbarPageEditor.getUndoCommand();
    }

    /**
     * @return barra contenente gli errori di validazione dell'oggetto gestito dalla pagina.
     */
    public JPanel getErrorBar() {
        return errorBar;
    }

    private ActionCommandInterceptor getExternalCommandInterceptor() {
        if (externalCommandInterceptor == null) {
            externalCommandInterceptor = new ExternalCommandInterceptor();

        }
        return externalCommandInterceptor;
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eurotn.rich.editors.IToolbarPageCommands#getExternalCommandStart()
     */
    @Override
    public AbstractCommand[] getExternalCommandStart() {
        return externalCommandStart;
    }

    /**
     * @return AbstractForm backing form page
     */
    @Override
    public PanjeaAbstractForm getForm() {
        return (PanjeaAbstractForm) getBackingFormPage();
    }

    /**
     * Metodo da implementare nel caso in cui si voglia utilizzare un command specifico per
     * istanziare il nuovo oggetto all'interno di questo editor.
     *
     * @return AbstractCommand
     */
    public AbstractCommand getNewCommand() {
        if ((newCommand == null) && (idNewCommand != null)) {
            newCommand = (AbstractCommand) getActiveWindow().getCommandManager().getCommand(idNewCommand);
            // Se la toolbar è gia stata creata risetto il new comma
            if (toolbarPageEditor != null) {
                toolbarPageEditor.setNewCommand(newCommand);
            }
        }
        return toolbarPageEditor.getNewCommand();
    }

    /**
     * Metodo di comodo per avere il form object e avere la possibilita' di scegliere se lanciare
     * sul nuovo command il property change che notifica agli ascoltatori che l'oggetto nel form e'
     * cambiato.
     *
     * @return Object il form object
     */
    protected Object getNewEditorObject() {
        return getBackingFormPage().getFormObject();
    }

    /**
     * @return page editor id
     */
    @Override
    public String getPageEditorId() {
        return getId();
    }

    @Override
    public Object getPageObject() {
        return getForm().getFormObject();
    }

    @Override
    public String getPageSecurityEditorId() {
        if (pageSecurityEditorId == null) {
            return getPageEditorId();
        } else {
            return pageSecurityEditorId;
        }
    }

    @Override
    public void grabFocus() {
        if (getForm() instanceof Focussable) {
            ((Focussable) getForm()).grabFocus();
        } else if (pageComponent != null) {
            PanjeaSwingUtil.giveFocusToComponent(pageComponent.getComponents());
        }
    }

    @Override
    protected void initPageValidationReporter() {
        validationResultReporter = getForm().newSingleLineResultsReporter(this);
        getForm().addGuarded(this);
    }

    /**
     * indica se inserire i controlli in una scrollPane. Di default mette la scrollPane. In alcuni
     * casi potrebbe dare problemi.
     *
     * @return true di default
     */
    protected boolean insertControlInScrollPane() {
        return true;
    }

    @Override
    public boolean isCommittable() {
        try {
            if (((PanjeaAbstractForm) getBackingFormPage()).getFormModel() != null) {
                ValidationResultsModel validationResults = ((PanjeaAbstractForm) getBackingFormPage()).getFormModel()
                        .getValidationResults();
                if (validationResults == null) {
                    LOGGER.error("Mail NPE: FormBackedDialogPageEditor " + getId() + " control created "
                            + isControlCreated() + " validation results null");
                }
                return ((PanjeaAbstractForm) getBackingFormPage()).getFormModel().isCommittable();
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("-->errore nel richiamare la isCommitable.ERRORE non visualizzato dall'utente", e);
        }
        return true;
    }

    @Override
    public boolean isDirty() {
        try {
            PanjeaAbstractForm abstractForm = (PanjeaAbstractForm) getBackingFormPage();
            // NPE Mail
            if (abstractForm != null && abstractForm.getFormModel() != null) {
                return abstractForm.getFormModel().isDirty();
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("-->errore nel richiamare la isDirty.ERRORE non visualizzato dall'utente", e);
        }
        return false;
    }

    /**
     * @return Returns the editEnabled.
     */
    protected boolean isEditEnabled() {
        return editOggetto;
    }

    @Override
    public boolean isLocked() {
        return toolbarPageEditor.getLock() != null;
    }

    /**
     * @return true se l'oggetto gestito dalla pagina è in inserimento.
     */
    public boolean isNuovoOggetto() {
        return nuovoOggetto;
    }

    /**
     * @return true se la pagina è in readonly
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * @return Returns the showTitlePane.
     */
    protected boolean isShowTitlePane() {
        return showTitlePane;
    }

    @Override
    public Object onDelete() {
        return doDelete();
    }

    /**
     * Metodo chiamato dal command lock della toolbar, esegue il lock dell'oggetto presente nel form
     * model.
     *
     * @return lock
     */
    @Override
    public ILock onLock() {
        toolbarPageEditor.lock();
        editOggetto = true;
        updateCommands();
        // per dare il focus sulla modifica nel caso in cui si ha una
        // AbstractTableDetailDialogPageEditor
        PanjeaSwingUtil.giveFocusToComponent(getBackingFormPage().getControl().getComponents());
        return toolbarPageEditor.getLock();
    }

    /**
     * Metodo chiamato dal command new della toolbar esegue la chiamata alla funzione newFormObject
     * del form.
     */
    @Override
    public void onNew() {
        ((PanjeaAbstractForm) getBackingFormPage()).getNewFormObjectCommand().execute();
        nuovoOggetto = true;
        editOggetto = true;
        updateCommands();

        Object newObjectRaised = getNewEditorObject();
        if (newObjectRaised != null) {
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, newObjectRaised);
        }

        // per dare il focus sulla modifica nel caso in cui si ha una
        // AbstractTableDetailDialogPageEditor
        PanjeaSwingUtil.giveFocusToComponent(getBackingFormPage().getControl().getComponents());
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    /**
     * Metodo chiamato dal command save della toolbar, rilascia il lock dall'oggetto modificato,
     * esegue il commit e il salvataggio, lancia l'application event necessario per notificare la
     * modifica o inserimento dell'oggetto, intercettato principalmente dalla
     * AbstractTableSearchResult.
     *
     * @return true o false
     */
    @Override
    public boolean onSave() {
        LOGGER.debug("-->ENTER onSave");
        boolean result = true;

        // tengo un riferimento del vecchio oggetto per verificare se sono in
        // modifica di un oggetto esistente o in creazione di uno nuovo
        Integer oldId = null;
        Object oldObj = getBackingFormPage().getFormObject();

        if (oldObj instanceof IDefProperty) {
            oldId = ((IDefProperty) getBackingFormPage().getFormObject()).getId();
        }
        // eseguo il commit e quindi salvo l'oggetto (doSave())
        getBackingFormPage().commit();
        Object resultObject = null;
        try {
            resultObject = doSave();
            // la release lock aggiorna i commands e rende il form readOnly
            if (resultObject != null) {
                toolbarPageEditor.releaseLock();
            }
        } catch (RuntimeException e) {
            // se il salvataggio genera una exc devo lasciare il form nella
            // situazione precedente allo stesso
            // e quindi rilanciare al client l'exc.
            editOggetto = true;
            updateCommands();
            throw e;
        }
        if (resultObject != null) {
            // se l'id di oldObject e' null ho a che fare con l'inserimento di
            // un nuovo oggetto
            // altrimenti ho a che fare con la modifica di un oggetto esistente
            // il lancio dell'evento lo eseguo specificatamente per avere
            // sincronizzata la searchResult con
            // l'entita' su cui sto lavorando nell'editor.
            if (oldId == null || oldId == -1) {
                publishCreateEvent(resultObject);
            } else {
                publishModifyEvent(resultObject);
            }
            // HACK bug 396:clono l'oggetto in modo da trovarlo inalterato per il lancio eventi;
            // in caso di search text in dialog mi ritrovo a null la property
            // linkata che viene presentata nella searchtextfield se dopo
            // selezione record da
            // popupsearhctext premo invio
            setFormObject(PanjeaSwingUtil.cloneObject(resultObject));
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, resultObject);
            nuovoOggetto = false; // ho salvato quindi non e' piu' nuovo
            editOggetto = false;
            updateCommands();
            result = true;
        } else {
            LOGGER.error("--> Attenzione! La doSave ritorna un valore a null");
            // gestisco il ritorno di null come una exception avvenuta durante
            // il salvataggio
            editOggetto = true;
            updateCommands();
        }
        LOGGER.debug("-->EXIT onSave con risultato " + result);
        return result;
    }

    /**
     * Metodo chiamato dal command undo della toolbar rilascia il lock dell'oggetto modificato ed
     * esegue il revert del form.
     *
     * @return true o false
     */
    @Override
    public boolean onUndo() {
        getBackingFormPage().revert();
        toolbarPageEditor.releaseLock();
        editOggetto = false;
        updateCommands();
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    /**
     * Pubblica un evento per l'applicazione intercettato da ogni oggetto che implementa
     * ApplicationListener, serve alla AbstractTableSearchResult per monitorare le azioni di
     * inserimento, modifica e cancellazione eseguite negli editor.
     *
     * @param e
     *            la stringa che identifica il tipo di evento (azione) da lanciare
     * @param obj
     *            l'oggetto che deve essere inglobato nell'evento associato all'azione eseguita
     */
    protected void publishApplicationEvent(String eventPublish, Object obj) {
        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(eventPublish, obj);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * Notifica l'aggiunta dell'oggetto.
     *
     * @param obj
     *            oggetto creato da lanciare
     */
    public void publishCreateEvent(Object obj) {
        publishApplicationEvent(LifecycleApplicationEvent.CREATED, obj);
    }

    /**
     * Notifica la cancellazione dell'oggetto.
     *
     * @param obj
     *            oggetto cancellato da lanciare
     */
    public void publishDeleteEvent(Object obj) {
        publishApplicationEvent(LifecycleApplicationEvent.DELETED, obj);
    }

    /**
     * Notifica la modifica dell'oggetto.
     *
     * @param obj
     *            oggetto aggiornato da lanciare
     */
    public void publishModifyEvent(Object obj) {
        publishApplicationEvent(LifecycleApplicationEvent.MODIFIED, obj);
    }

    /**
     * Restore state dell' interface Memento di default sulla formbacked non fa nulla.
     *
     * @param settings
     *            settings
     */
    @Override
    public void restoreState(Settings settings) {
    }

    /**
     * Save state dell' interface Memento di default sulla formbacked non fa nulla.
     *
     * @param settings
     *            settings
     */
    @Override
    public void saveState(Settings settings) {
    }

    /**
     *
     * @param abstractCommand
     *            auditCommand settato dall'esterno.
     */
    public void setAuditCommand(OpenAuditCommand abstractCommand) {
        this.auditCommand = abstractCommand;
    }

    /**
     *
     * @param auditCommandInterceptor
     *            interceptor per l'audit Command
     */
    public void setAuditCommandInterceptor(AuditCommandInterceptor auditCommandInterceptor) {
        this.auditCommandInterceptor = auditCommandInterceptor;
    }

    /**
     *
     * @param defaultController
     *            setter of defaultController
     */
    public void setDefaultController(DefaultController defaultController) {
        this.defaultController = defaultController;
    }

    /**
     * @param deleteCommand
     *            delete command da utilizzare nel form al posto del predefinito
     */
    public void setDeleteCommand(AbstractCommand deleteCommand) {
        // Se la toolbar è gia stata creata risetto il new command
        if (toolbarPageEditor != null) {
            toolbarPageEditor.setDeleteCommand(deleteCommand);
        }
    }

    @Override
    public void setExternalCommandAppend(AbstractCommand[] externalCommandAppend) {
        this.externalCommandAppend = externalCommandAppend;
    }

    @Override
    public void setExternalCommandLineEnd(AbstractCommand[] externalCommandLineEnd) {
        this.externalCommandLineEnd = externalCommandLineEnd;
    }

    @Override
    public void setExternalCommandStart(AbstractCommand[] externalCommandStart) {
        this.externalCommandStart = externalCommandStart;
    }

    @Override
    public void setFormObject(Object object) {
        boolean visible = PanjeaSwingUtil.hasPermission("visualizzaAudit");
        if (object != null) {
            visible = visible && (object.getClass().getAnnotation(Audited.class) != null
                    || object.getClass().getAnnotation(AuditableProperties.class) != null);
        }
        getAuditCommand().setVisible(visible);
        getBackingFormPage().getFormModel().setReadOnly(true);
        getBackingFormPage().setFormObject(object);
        LOGGER.debug("-->SETTATO il formObject");
        editOggetto = false;

        // Se la pagina non e' readonly riattivo attivo e disattivo i pulsanti
        // controllando
        // l'oggetto della pagina.
        if (!this.isReadonly()) {
            checkFormObjectIfNew();
            updateCommands();
        }
        LOGGER.debug("-->EXIT setFormObject");
    }

    /**
     *
     * @param idNewCommand
     *            id del new command
     */
    public void setIdNewCommand(String idNewCommand) {
        this.idNewCommand = idNewCommand;
    }

    /**
     * @param newCommand
     *            The newCommand to set.
     */
    public void setNewCommand(AbstractCommand newCommand) {
        this.newCommand = newCommand;
        // Se la toolbar è gia stata creata risetto il new command
        if (toolbarPageEditor != null) {
            toolbarPageEditor.setNewCommand(newCommand);
        }
    }

    /**
     * @param pageSecurityEditorId
     *            setter of pageSecurityEditorId
     */
    public void setPageSecurityEditorId(String pageSecurityEditorId) {
        this.pageSecurityEditorId = pageSecurityEditorId;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readonly = readOnly;
        // se cambio lo stato readonly della pagina non devo ricontrollare
        // l'oggetto contenuto nella page altrimenti
        // perdo il controllo sul readOnly
        if (toolbarCommands != null) {
            for (AbstractCommand abstractCommand : toolbarCommands) {
                if (abstractCommand != toolbarPageEditor.getSaveCommand()) {
                    abstractCommand.setEnabled(!readOnly);
                }
            }
            getForm().getFormModel().setReadOnly(readOnly);
            if (!readOnly) {
                checkFormObjectIfNew();
                updateCommands();
            }
        }
    }

    /**
     * @param showTitlePane
     *            The showTitlePane to set.
     */
    public void setShowTitlePane(boolean showTitlePane) {
        this.showTitlePane = showTitlePane;
    }

    @Override
    public void unLock() {
        toolbarPageEditor.releaseLock();
        editOggetto = false;
        updateCommands();
    }

    /**
     * Aggiorno lo stato dei pulsanti della toolbar a seconda delle variabili che definiscono quale
     * funzione è in esecuzione (se il lock è abilitato o se è stato creato un nuovo oggetto nel
     * form).
     */
    public void updateCommands() {
        LOGGER.debug("---> Enter updateCommands");
        LOGGER.debug("-->edit oggetto :" + editOggetto);
        LOGGER.debug("-->new oggetto :" + nuovoOggetto);
        getForm().getFormModel().setReadOnly(!editOggetto);
        if (toolbarPageEditor.getPageEditor() != null) {
            toolbarPageEditor.getLockCommand().setEnabled(!editOggetto);
            toolbarPageEditor.getDeleteCommand().setEnabled(!nuovoOggetto && !editOggetto);
            toolbarPageEditor.getUndoCommand().setEnabled(editOggetto);
        }
        LOGGER.debug("---> Exit updateCommands");
    }
}
