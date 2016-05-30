package it.eurotn.rich.dialog;

import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.DefaultMessageAreaPane;
import org.springframework.richclient.dialog.Messagable;
import org.springframework.richclient.dialog.MessagePane;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.SimpleValidationResultsReporter;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.layout.TableLayoutBuilder;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.Constraint;
import org.springframework.util.Assert;

import com.toedter.calendar.JDateChooser;

/**
 * @author leonardo
 */
public class InputApplicationDialog extends ApplicationDialog implements Messagable {

    private static class SelectAllBugFixer extends FocusAdapter {
        @Override
        public void focusGained(final FocusEvent evt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ((JFormattedTextField) evt.getComponent()).selectAll();
                }
            });
        }
    }

    private String inputLabelMessage = "dialog.input";

    private JComponent inputField;

    private Constraint inputConstraint;

    private Closure finishAction;

    private MessagePane reporter;

    private ValidatingFormModel formModel;

    /**
     * Costruttore.
     */
    public InputApplicationDialog() {
        this(null, null, CloseAction.DISPOSE);
    }

    /**
     * Costruttore.
     *
     * @param bean
     *            bean
     * @param propertyName
     *            property name
     */
    public InputApplicationDialog(final Object bean, final String propertyName) {
        this(bean, propertyName, true);
    }

    /**
     * Costruttore.
     *
     * @param bean
     *            bean
     * @param propertyName
     *            property name
     * @param bufferChanges
     *            buffer changes
     */
    public InputApplicationDialog(final Object bean, final String propertyName, final boolean bufferChanges) {
        this(FormModelHelper.createFormModel(bean, bufferChanges), propertyName);
    }

    /**
     * Costruttore.
     *
     * @param title
     *            titolo
     * @param parent
     *            parent window
     */
    public InputApplicationDialog(final String title, final Window parent) {
        this(title, parent, CloseAction.DISPOSE);
    }

    /**
     * Costruttore.
     *
     * @param title
     *            titolo
     * @param parent
     *            parent window
     * @param closeAction
     *            close action
     */
    public InputApplicationDialog(final String title, final Window parent, final CloseAction closeAction) {
        super(title, parent, closeAction);
        setResizable(true);
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     * @param propertyName
     *            property name
     */
    public InputApplicationDialog(final ValidatingFormModel formModel, final String propertyName) {
        this();
        this.formModel = formModel;
        setInputField(new SwingBindingFactory(formModel).createBinding(propertyName).getControl());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getMessagePane().addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getMessagePane().addPropertyChangeListener(propertyName, listener);
    }

    private boolean checkInputConstraint() {
        if (inputConstraint != null) {
            return inputConstraint.test(getInputValue());
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected JComponent createDialogContentPane() {
        TableLayoutBuilder layoutBuilder = new TableLayoutBuilder();

        if (this.inputField == null) {
            this.inputField = getComponentFactory().createTextField();
        }
        // work around for bug in JFormattedTextField text field for selectAll
        if (inputField instanceof JFormattedTextField) {
            SelectAllBugFixer selectAllBugFixer = new SelectAllBugFixer();
            inputField.addFocusListener(selectAllBugFixer);
        }

        layoutBuilder.cell(createInputLabel(), TableLayoutBuilder.DEFAULT_LABEL_ATTRIBUTES);
        layoutBuilder.labelGapCol();
        layoutBuilder.cell(inputField);

        layoutBuilder.unrelatedGapRow();
        layoutBuilder.cell(getMessagePane().getControl());

        layoutBuilder.relatedGapRow();
        layoutBuilder.separator("");
        return layoutBuilder.getPanel();
    }

    protected JComponent createInputLabel() {
        return getComponentFactory().createLabelFor(inputLabelMessage, getInputField());
    }

    protected MessagePane createMessagePane() {
        return new DefaultMessageAreaPane();
    }

    /**
     * @return formmodel
     */
    public ValidatingFormModel getFormModel() {
        return formModel;
    }

    /**
     * @return Returns the inputField.
     */
    public JComponent getInputField() {
        return inputField;
    }

    /**
     * @return input value
     */
    public Object getInputValue() {
        if (inputField instanceof JFormattedTextField) {
            return ((JFormattedTextField) inputField).getValue();
        } else if (inputField instanceof JTextComponent) {
            return ((JTextComponent) inputField).getText();
        } else if (inputField instanceof JDateChooser) {
            return ((JDateChooser) inputField).getDate();
        } else if (inputField instanceof JSpinner) {
            return ((JSpinner) inputField).getValue();
        } else {
            throw new IllegalStateException("Input field type not supported");
        }
    }

    private MessagePane getMessagePane() {
        if (reporter == null) {
            reporter = createMessagePane();

            if (this.formModel != null) {
                new SimpleValidationResultsReporter(formModel.getValidationResults(), reporter);
                FormGuard formGuard = new FormGuard(formModel);
                formGuard.addGuarded(this, FormGuard.FORMERROR_GUARDED);
            }
        }
        return reporter;
    }

    @Override
    protected boolean onFinish() {
        if (checkInputConstraint()) {
            onFinish(getInputValue());
            return true;
        }
        return false;
    }

    protected void onFinish(Object inputValue) {
        if (formModel != null) {
            formModel.commit();
        }
        if (finishAction != null) {
            finishAction.call(inputValue);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getMessagePane().removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getMessagePane().removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param procedure
     *            the finish action to set
     */
    public void setFinishAction(Closure procedure) {
        this.finishAction = procedure;
    }

    /**
     * @param constraint
     *            the constraint to set
     */
    public void setInputConstraint(Constraint constraint) {
        this.inputConstraint = constraint;
    }

    /**
     * @param field
     *            the input field to set
     */
    public void setInputField(JComponent field) {
        Assert.notNull(field);
        this.inputField = field;
    }

    /**
     * @param inputLabel
     *            the inputLabel to set
     */
    public void setInputLabelMessage(String inputLabel) {
        this.inputLabelMessage = inputLabel;
    }

    @Override
    public void setMessage(Message message) {
        getMessagePane().setMessage(message);
    }
}
