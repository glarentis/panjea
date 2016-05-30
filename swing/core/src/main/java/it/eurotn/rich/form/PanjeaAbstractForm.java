package it.eurotn.rich.form;

import java.awt.Component;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.springframework.binding.form.CommitListener;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResultsModel;
import org.springframework.binding.validation.support.DefaultValidationResultsModel;
import org.springframework.binding.value.IndexAdapter;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ObservableList;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.dialog.Messagable;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.SimpleValidationResultsReporter;
import org.springframework.richclient.form.ValidationResultsReporter;
import org.springframework.richclient.form.binding.BindingFactory;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public abstract class PanjeaAbstractForm extends AbstractControlFactory implements Form, CommitListener {

    private class EditingFormObjectSetter implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            int selectionIndex = getEditingFormObjectIndex();
            if (selectionIndex == -1) {
                // FIXME: why do we need this
                // getFormModel().reset();
                setEnabled(false);
            } else {
                if (selectionIndex < editableFormObjects.size()) {
                    // If we were editing a "new" object, we need to clear
                    // that flag since a new object has been selected
                    setEditingNewFormObject(false);
                    setFormObject(getEditableFormObject(selectionIndex));
                    setEnabled(true);
                }
            }
        }
    }

    private class FormEnabledPropertyChangeHandler implements PropertyChangeListener {
        public FormEnabledPropertyChangeHandler() {
            handleEnabledChange(getFormModel().isEnabled());
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            handleEnabledChange(getFormModel().isEnabled());
        }
    }

    private class FormObjectChangeHandler implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setFormModelDefaultEnabledState();
        }
    }

    private final FormObjectChangeHandler formObjectChangeHandler = new FormObjectChangeHandler();

    private String formId;

    private ValidatingFormModel formModel;

    private HierarchicalFormModel parentFormModel;

    private FormGuard formGuard;

    private JButton lastDefaultButton;

    private PropertyChangeListener formEnabledChangeHandler;

    private ActionCommand newFormObjectCommand;

    private ActionCommand commitCommand;

    private ActionCommand revertCommand;

    private boolean editingNewFormObject;

    private boolean clearFormOnCommit = false;

    private ObservableList editableFormObjects;

    private ValueModel editingFormObjectIndexHolder;

    private PropertyChangeListener editingFormObjectSetter;

    private BindingFactory bindingFactory;

    private Map childForms = new HashMap();

    private List validationResultsReporters = new ArrayList();

    private SimpleValidationResultsReporter reporter;

    /**
     * Default constructor will use the uncapitalized simplename of the class to construct its id.
     */
    protected PanjeaAbstractForm() {
        setId(StringUtils.uncapitalize(getClass().getSimpleName()));
        init();
    }

    /**
     * Create an AbstractForm with the given {@link FormModel}. Use the formModel's id to configure the Form.
     *
     * @see #AbstractForm(FormModel, String)
     */
    protected PanjeaAbstractForm(FormModel formModel) {
        this(formModel, formModel.getId());
    }

    /**
     * Create an AbstractForm.
     */
    protected PanjeaAbstractForm(FormModel formModel, String formId) {
        setId(formId);
        if (formModel instanceof ValidatingFormModel) {
            setFormModel((ValidatingFormModel) formModel);
        } else {
            throw new IllegalArgumentException("Unsupported form model implementation " + formModel);
        }
        init();
    }

    /**
     * Create a Form with a FormModel that has a child-parent relation with the provided parentFormModel.
     *
     * @param parentFormModel
     *            the parent formModel.
     * @param formId
     *            id used for this Form's configuration.
     * @param childFormObjectPropertyPath
     *            the path relative to the parentFormModel's formObject that leads to the child formObject that will be
     *            handled by this Form.
     * @see FormModelHelper#createChildPageFormModel(HierarchicalFormModel, String, String)
     */
    protected PanjeaAbstractForm(HierarchicalFormModel parentFormModel, String formId,
            String childFormObjectPropertyPath) {
        setId(formId);
        this.parentFormModel = parentFormModel;
        setFormModel(FormModelHelper.createChildPageFormModel(parentFormModel, formId, childFormObjectPropertyPath));
        init();
    }

    /**
     * Create a Form with a FormModel that has a child-parent relation with the provided parentFormModel.
     *
     * @param parentFormModel
     *            the parent formModel.
     * @param formId
     *            id used for this Form's configuration.
     * @param childFormObjectHolder
     *            the valueModel of the parentFormModel that holds the child formObject that will be handled by this
     *            Form.
     * @see FormModelHelper#createChildPageFormModel(HierarchicalFormModel, String, ValueModel)
     */
    protected PanjeaAbstractForm(HierarchicalFormModel parentFormModel, String formId,
            ValueModel childFormObjectHolder) {
        setId(formId);
        this.parentFormModel = parentFormModel;
        setFormModel(FormModelHelper.createChildPageFormModel(parentFormModel, formId, childFormObjectHolder));
        init();
    }

    /**
     * Id configurable constructor.
     */
    protected PanjeaAbstractForm(String formId) {
        setId(formId);
        init();
    }

    /**
     * Add a child (or sub) form to this form. Child forms will be tied in to the same validation results reporter as
     * this form and they will be configured to control the same guarded object as this form.
     * <p>
     * Validation listeners are unique to a form, so calling {@link #addValidationListener(ValidationListener)} will
     * only add a listener to this form. If you want to listen to the child forms, you will need to add a validation
     * listener on each child form of interest.
     * <p>
     * <em>Note:</em> It is very important that the child form provided be created using a form model that is a child
     * model of this form's form model. If this is not done, then commit and revert operations will not be properly
     * delegated to the child form models.
     *
     * @param childForm
     *            to add
     */
    @Override
    public void addChildForm(Form childForm) {
        childForms.put(childForm.getId(), childForm);
        getFormModel().addChild(childForm.getFormModel());
    }

    public void addFormObjectChangeListener(PropertyChangeListener listener) {
        formModel.getFormObjectHolder().addValueChangeListener(listener);
    }

    public void addFormValueChangeListener(String formPropertyPath, PropertyChangeListener listener) {
        getFormModel().getValueModel(formPropertyPath).addValueChangeListener(listener);
    }

    @Override
    public void addGuarded(Guarded guarded) {
        formGuard.addGuarded(guarded, FormGuard.FORMERROR_GUARDED);
    }

    @Override
    public void addGuarded(Guarded guarded, int mask) {
        formGuard.addGuarded(guarded, mask);
    }

    @Override
    public void addValidationListener(ValidationListener listener) {
        formModel.getValidationResults().addValidationListener(listener);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addValidationResultsReporter(ValidationResultsReporter paramReporter) {
        this.validationResultsReporters.add(paramReporter);
    }

    protected void attachFormErrorGuard(Guarded guarded) {
        attachFormGuard(guarded, FormGuard.FORMERROR_GUARDED);
    }

    protected void attachFormGuard(Guarded guarded, int mask) {
        this.formGuard.addGuarded(guarded, mask);
    }

    @Override
    public void commit() {
        // npe mail
        if (formModel != null && formModel.isCommittable()) {
            formModel.commit();
        }
    }

    /**
     * Construct a default security controller Id for a given command face id. The id will be a combination of the form
     * model id, if any, and the face id.
     * <p>
     * <code>[formModel.id] + "." + [commandFaceId]</code> if the form model id is not null.
     * <p>
     * <code>[commandFaceId]</code> if the form model is null.
     * <p>
     * <code>null</code> if the commandFaceId is null.
     *
     * @param commandFaceId
     * @return default security controller id
     */
    protected String constructSecurityControllerId(String commandFaceId) {
        String id = null;
        String formModelId = getFormModel().getId();

        if (commandFaceId != null) {
            id = (formModelId != null) ? formModelId + "." + commandFaceId : commandFaceId;
        }
        return id;
    }

    protected final JButton createCommitButton() {
        Assert.state(commitCommand != null, "Commit command has not been created!");
        return (JButton) commitCommand.createButton();
    }

    /**
     * Returns a command wrapping the commit behavior of the {@link FormModel}. This command has the guarded and
     * security aspects.
     */
    private final ActionCommand createCommitCommand() {
        String commandId = getCommitCommandFaceDescriptorId();
        if (!StringUtils.hasText(commandId)) {
            return null;
        }
        ActionCommand commitCmd = new ActionCommand(commandId) {
            @Override
            protected void doExecuteCommand() {
                commit();
            }
        };
        commitCmd.setSecurityControllerId(getCommitSecurityControllerId());
        attachFormGuard(commitCmd, FormGuard.LIKE_COMMITCOMMAND);
        return (ActionCommand) getCommandConfigurer().configure(commitCmd);
    }

    @Override
    protected final JComponent createControl() {
        Assert.state(getFormModel() != null,
                "This form's FormModel cannot be null once control creation is triggered!");
        initStandardLocalFormCommands();
        JComponent formControl = createFormControl();
        this.formEnabledChangeHandler = new FormEnabledPropertyChangeHandler();
        getFormModel().addPropertyChangeListener(FormModel.ENABLED_PROPERTY, formEnabledChangeHandler);
        addFormObjectChangeListener(formObjectChangeHandler);
        if (getCommitCommand() != null) {
            getFormModel().addCommitListener(this);
        }
        return formControl;
    }

    protected abstract JComponent createFormControl();

    protected final JButton createNewFormObjectButton() {
        Assert.state(newFormObjectCommand != null, "New form object command has not been created!");
        return (JButton) newFormObjectCommand.createButton();
    }

    private ActionCommand createNewFormObjectCommand() {
        String commandId = getNewFormObjectCommandId();
        if (!StringUtils.hasText(commandId)) {
            return null;
        }
        ActionCommand newFormObjectCmd = new ActionCommand(commandId) {
            @Override
            protected void doExecuteCommand() {
                getFormModel().setFormObject(createNewObject());
                getFormModel().setEnabled(true);
                editingNewFormObject = true;
                if (isEditingFormObjectSelected()) {
                    setEditingFormObjectIndexSilently(-1);
                }
            }
        };
        newFormObjectCmd.setSecurityControllerId(getNewFormObjectSecurityControllerId());
        attachFormGuard(newFormObjectCmd, FormGuard.LIKE_NEWFORMOBJCOMMAND);
        return (ActionCommand) getCommandConfigurer().configure(newFormObjectCmd);
    }

    /**
     * Create a new object to install into the form. By default, this simply returns null. This will cause the form
     * model to instantiate a new copy of the model object class. Subclasses should override this method if they need
     * more control over how new objects are constructed.
     *
     * @return new object for editing
     */
    protected Object createNewObject() {
        return null;
    }

    private final ActionCommand createRevertCommand() {
        String commandId = getRevertCommandFaceDescriptorId();
        if (!StringUtils.hasText(commandId)) {
            return null;
        }
        ActionCommand revertCmd = new ActionCommand(commandId) {
            @Override
            protected void doExecuteCommand() {
                revert();
            }
        };
        attachFormGuard(revertCmd, FormGuard.LIKE_REVERTCOMMAND);
        return (ActionCommand) getCommandConfigurer().configure(revertCmd);
    }

    protected void detachFormGuard(Guarded guarded) {
        this.formGuard.removeGuarded(guarded);
    }

    /**
     * Esegue il dispose del form.
     */
    public void dispose() {

        bindingFactory = null;

        if (isControlCreated()) {
            resetComponentDocument(getControl());
        }

        getFormModel().setValidating(false);

        @SuppressWarnings("unchecked")
        List<ValidationResultsReporter> validationReporter = getValidationResultsReporters();

        ValidationResultsModel validationModel = (getFormModel()).getValidationResults();
        if (validationModel != null) {
            ((DefaultValidationResultsModel) validationModel).clearAllValidationResults();
            validationModel.remove(validationModel);
        }

        for (ValidationResultsReporter validationResultsReporter : validationReporter) {
            removeValidationResultsReporter(validationResultsReporter);
            validationResultsReporter = null;
        }

        PropertyChangeListener[] propertyChangeListener = ((DefaultFormModel) getFormModel())
                .getPropertyChangeListeners();
        for (PropertyChangeListener listener : propertyChangeListener) {
            ((DefaultFormModel) getFormModel()).removePropertyChangeListener(listener);
        }

        getFormModel().removePropertyChangeListener("enabled", formEnabledChangeHandler);
        removeFormObjectChangeListener(formObjectChangeHandler);
        formEnabledChangeHandler = null;

        formGuard = null;

        Field field;
        try {
            field = getFormModel().getClass().getDeclaredField("validationResultsModel");
            field.setAccessible(true);
            field.set(getFormModel(), null);
        } catch (Exception e) {
            logger.error("-->errore durante la dispose del form nella validationResultsModel");
        }

        try {
            // è come ci fosse un solo reporter per ogni form che condivide lo
            // stesso form model;
            // sul secondo form trovo reporter a null
            if (reporter != null) {
                field = reporter.getClass().getDeclaredField("messageReceiver");
                field.setAccessible(true);
                field.set(reporter, null);
            }
        } catch (Exception e) {
            logger.error("-->errore durante la dispose del form", e);
        }

    }

    /**
     * @return Returns a {@link BindingFactory} bound to the inner {@link FormModel} to provide binding support.
     */
    public BindingFactory getBindingFactory() {
        if (bindingFactory == null) {
            bindingFactory = ((BindingFactoryProvider) getService(BindingFactoryProvider.class))
                    .getBindingFactory(formModel);
        }
        return bindingFactory;
    }

    /**
     * Return a child form of this form with the given form id.
     *
     * @param id
     *            of child form
     * @return child form, null if no child form with the given id has been registered
     */
    protected Form getChildForm(String id) {
        return (Form) childForms.get(id);
    }

    public ActionCommand getCommitCommand() {
        if (this.commitCommand == null) {
            this.commitCommand = createCommitCommand();
        }
        return commitCommand;
    }

    protected String getCommitCommandFaceDescriptorId() {
        return null;
    }

    /**
     * Subclasses may override to return a security controller id to be attached to the commit command. The default is
     * The default is <code>[formModel.id] + "." + [getCommitCommandFaceDescriptorId()]</code>.
     * <p>
     * This id can be mapped to a specific security controller using the SecurityControllerManager service.
     *
     * @return security controller id, may be null if the face id is null
     * @see org.springframework.richclient.security.SecurityControllerManager
     */
    protected String getCommitSecurityControllerId() {
        return constructSecurityControllerId(getCommitCommandFaceDescriptorId());
    }

    protected JButton getDefaultButton() {
        if (isControlCreated()) {
            JRootPane rootPane = SwingUtilities.getRootPane(getControl());
            return rootPane == null ? null : rootPane.getDefaultButton();
        }
        return null;
    }

    protected Object getEditableFormObject(int selectionIndex) {
        return editableFormObjects.get(selectionIndex);
    }

    protected int getEditingFormObjectIndex() {
        return ((Integer) editingFormObjectIndexHolder.getValue()).intValue();
    }

    @Override
    public ValidatingFormModel getFormModel() {
        return formModel;
    }

    @Override
    public Object getFormObject() {
        return formModel.getFormObject();
    }

    @Override
    public String getId() {
        return formId;
    }

    public ActionCommand getNewFormObjectCommand() {
        if (this.newFormObjectCommand == null) {
            this.newFormObjectCommand = createNewFormObjectCommand();
        }
        return newFormObjectCommand;
    }

    protected String getNewFormObjectCommandId() {
        return "new" + StringUtils
                .capitalize(ClassUtils.getShortName(getFormModel().getFormObject().getClass() + "Command"));
    }

    /**
     * Subclasses may override to return a security controller id to be attached to the newFormObject command. The
     * default is <code>[formModel.id] + "." + [getNewFormObjectCommandId()]</code>.
     * <p>
     * This id can be mapped to a specific security controller using the SecurityControllerManager service.
     *
     * @return security controller id, may be null if the face id is null
     * @see org.springframework.richclient.security.SecurityControllerManager
     */
    protected String getNewFormObjectSecurityControllerId() {
        return constructSecurityControllerId(getNewFormObjectCommandId());
    }

    /**
     * Returns the parent of this Form's FormModel or <code>null</code>.
     */
    protected HierarchicalFormModel getParent() {
        return this.parentFormModel;
    }

    public ActionCommand getRevertCommand() {
        if (this.revertCommand == null) {
            this.revertCommand = createRevertCommand();
        }
        return revertCommand;
    }

    protected String getRevertCommandFaceDescriptorId() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List getValidationResultsReporters() {
        return validationResultsReporters;
    }

    @Override
    public Object getValue(String formProperty) {
        return formModel.getValueModel(formProperty).getValue();
    }

    @Override
    public ValueModel getValueModel(String formProperty) {
        ValueModel valueModel = formModel.getValueModel(formProperty);
        if (valueModel == null) {
            logger.warn("A value model for property '" + formProperty + "' could not be found.  Typo?");
        }
        return valueModel;
    }

    protected void handleEnabledChange(boolean enabled) {
        if (enabled) {
            if (getCommitCommand() != null) {
                if (lastDefaultButton == null) {
                    lastDefaultButton = getDefaultButton();
                }
                getCommitCommand().setDefaultButton();
            }
        } else {
            if (getCommitCommand() != null) {
                getCommitCommand().setEnabled(false);
            }
            // set previous default button
            if (lastDefaultButton != null) {
                setDefaultButton(lastDefaultButton);
            }
        }
    }

    @Override
    public boolean hasErrors() {
        return formModel.getValidationResults().getHasErrors();
    }

    /**
     * Hook called when constructing the Form.
     */
    protected void init() {

    }

    private void initStandardLocalFormCommands() {
        getNewFormObjectCommand();
        getCommitCommand();
        getRevertCommand();
    }

    public boolean isDirty() {
        return formModel.isDirty();
    }

    private boolean isEditingFormObjectSelected() {
        if (editingFormObjectIndexHolder == null) {
            return false;
        }
        int value = ((Integer) editingFormObjectIndexHolder.getValue()).intValue();
        return value != -1;
    }

    public boolean isEditingNewFormObject() {
        return editingNewFormObject;
    }

    public boolean isEnabled() {
        return this.formModel.isEnabled();
    }

    /**
     * Construct the validation results reporter for this form and attach it to the provided Guarded object. An instance
     * of {@link SimpleValidationResultsReporter} will be constructed and returned. All registered child forms will be
     * attached to the same <code>guarded</code> and <code>messageReceiver</code> as this form.
     */
    @Override
    public ValidationResultsReporter newSingleLineResultsReporter(Messagable messageReceiver) {

        reporter = new SimpleValidationResultsReporter(formModel.getValidationResults(), messageReceiver);

        return reporter;
    }

    @Override
    public void postCommit(FormModel model) {
        if (editableFormObjects != null) {
            if (editingNewFormObject) {
                editableFormObjects.add(model.getFormObject());
                setEditingFormObjectIndexSilently(editableFormObjects.size() - 1);
            } else {
                int index = getEditingFormObjectIndex();
                // Avoid updating unless we have actually selected an object for
                // edit
                if (index >= 0) {
                    IndexAdapter adapter = editableFormObjects.getIndexAdapter(index);
                    adapter.setValue(model.getFormObject());
                    adapter.fireIndexedObjectChanged();
                }
            }
        }
        if (clearFormOnCommit) {
            setFormObject(null);
        }
        editingNewFormObject = false;
    }

    @Override
    public void preCommit(FormModel model) {
    }

    /**
     * @inheritDoc
     */
    @Override
    public void removeChildForm(Form childForm) {
        getFormModel().removeChild(childForm.getFormModel());
        childForms.remove(childForm.getId());
    }

    public void removeFormObjectChangeListener(PropertyChangeListener listener) {
        formModel.getFormObjectHolder().removeValueChangeListener(listener);
    }

    public void removeFormValueChangeListener(String formPropertyPath, PropertyChangeListener listener) {
        getFormModel().getValueModel(formPropertyPath).removeValueChangeListener(listener);
    }

    @Override
    public void removeGuarded(Guarded guarded) {
        if (formGuard != null) {
            formGuard.removeGuarded(guarded);
        }
    }

    @Override
    public void removeValidationListener(ValidationListener listener) {
        formModel.getValidationResults().removeValidationListener(listener);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void removeValidationResultsReporter(ValidationResultsReporter paramReporter) {
        this.validationResultsReporters.remove(paramReporter);
    }

    @Override
    public void reset() {
        getFormModel().reset();
    }

    /**
     * Rimuove dal componente e suoi figli, se si tratta di un {@link JTextComponent}, il document. Pulisce anche la
     * client property "binding" per evitare memory leak.
     *
     * @param rootComponent
     *            componente di riferimento
     */
    private void resetComponentDocument(JComponent rootComponent) {

        for (Component component : rootComponent.getComponents()) {

            if (component instanceof JComponent) {
                resetComponentDocument(((JComponent) component));

                ((JComponent) component).putClientProperty("binding", null);
                if (component instanceof JTextComponent) {

                    // ((JTextComponent) component).setDocument(new
                    // HTMLDocument());
                    ContainerListener[] list = component.getListeners(ContainerListener.class);
                    for (int i = 0; i < list.length; i++) {
                        ((JTextComponent) component).removeContainerListener(list[i]);
                    }

                    FocusListener[] focusList = component.getListeners(FocusListener.class);
                    for (int i = 0; i < focusList.length; i++) {
                        ((JTextComponent) component).removeFocusListener(focusList[i]);
                    }
                }

                for (HierarchyListener listener : component.getHierarchyListeners()) {
                    component.removeHierarchyListener(listener);
                }

                for (HierarchyBoundsListener listener : component.getHierarchyBoundsListeners()) {
                    component.removeHierarchyBoundsListener(listener);
                }

                for (ComponentListener listener : component.getComponentListeners()) {
                    component.removeComponentListener(listener);
                }

                component = null;
            }
        }

    }

    @Override
    public void revert() {
        formModel.revert();
    }

    public void setClearFormOnCommit(boolean clearFormOnCommit) {
        this.clearFormOnCommit = clearFormOnCommit;
    }

    protected void setDefaultButton(JButton button) {
        JRootPane rootPane = SwingUtilities.getRootPane(getControl());
        if (rootPane != null) {
            rootPane.setDefaultButton(button);
        }
    }

    protected void setEditableFormObjects(ObservableList editableFormObjects) {
        this.editableFormObjects = editableFormObjects;
    }

    protected void setEditingFormObjectIndexHolder(ValueModel valueModel) {
        this.editingFormObjectIndexHolder = valueModel;
        this.editingFormObjectSetter = new EditingFormObjectSetter();
        this.editingFormObjectIndexHolder.addValueChangeListener(editingFormObjectSetter);
    }

    protected void setEditingFormObjectIndexSilently(int index) {
        editingFormObjectIndexHolder.removeValueChangeListener(editingFormObjectSetter);
        editingFormObjectIndexHolder.setValue(new Integer(index));
        editingFormObjectIndexHolder.addValueChangeListener(editingFormObjectSetter);
    }

    /**
     * Set the "editing new form object" state as indicated.
     *
     * @param editingNewFormOject
     */
    protected void setEditingNewFormObject(boolean editingNewFormOject) {
        this.editingNewFormObject = editingNewFormOject;
    }

    public void setEnabled(boolean enabled) {
        this.formModel.setEnabled(enabled);
    }

    /**
     * Set the {@link FormModel} for this {@link Form}. Normally a Form won't change it's FormModel as this may lead to
     * an inconsistent state. Only use this when the formModel isn't set yet.
     *
     * TODO check why we do allow setting when no control is created. ValueModels might exist already leading to an
     * inconsistent state.
     *
     * @param formModel
     */
    public void setFormModel(ValidatingFormModel formModel) {
        Assert.notNull(formModel);
        if (this.formModel != null && isControlCreated()) {
            throw new UnsupportedOperationException("Cannot reset form model once form control has been created");
        }
        if (this.formModel != null) {
            this.formModel.removeCommitListener(this);
        }
        this.formModel = formModel;
        this.formGuard = new FormGuard(formModel);
        this.formModel.addCommitListener(this);
        setFormModelDefaultEnabledState();
    }

    /**
     * Set the form's enabled state based on a default policy--specifically, disable if the form object is null or the
     * form object is guarded and is marked as disabled.
     */
    protected void setFormModelDefaultEnabledState() {
        if (getFormObject() == null) {
            getFormModel().setEnabled(false);
        } else {
            if (getFormObject() instanceof Guarded) {
                setEnabled(((Guarded) getFormObject()).isEnabled());
            }
        }
    }

    @Override
    public void setFormObject(Object formObject) {
        formModel.setFormObject(formObject);
    }

    /**
     * Set the id used to configure this Form.
     *
     * @param id
     *            id
     */
    protected void setId(String id) {
        this.formId = id;
    }
}
