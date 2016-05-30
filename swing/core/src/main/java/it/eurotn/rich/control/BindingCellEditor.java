/**
 * 
 */
package it.eurotn.rich.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;

import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationMessage;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;

/**
 * @author Leonardo
 * 
 */
public class BindingCellEditor extends AbstractCellEditor implements TableCellEditor, ValidationListener {

    protected class StopEditingAction extends AbstractAction {

        public static final String STOPEDITING_ID = "stopEditing";

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            BindingCellEditor.this.stopCellEditing();
        }

    }

    private static final long serialVersionUID = 1L;
    protected Binding binding;
    // protected PanjeaCellEditor delegate;
    private JComponent component;
    private String propertyPath;

    private Object emptyInstance;

    /**
     * An integer specifying the number of clicks needed to start editing. Even if <code>clickCountToStart</code> is
     * defined as zero, it will not initiate until a click occurs.
     */
    protected int clickCountToStart = 2;

    public BindingCellEditor(Binding binding, String propertyPath) {
        ((ValidatingFormModel) binding.getFormModel()).getValidationResults()
                .addValidationListener(binding.getProperty(), this);
        configureCellEditor(binding, propertyPath);
    }

    /**
     * 
     * @param component
     */
    protected void configureBindingComponent(JComponent paramComponent) {
        StopEditingAction stopEditingAction = new StopEditingAction();
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke tabKey = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        paramComponent.getInputMap().put(enterKey, StopEditingAction.STOPEDITING_ID);
        paramComponent.getInputMap().put(tabKey, StopEditingAction.STOPEDITING_ID);
        paramComponent.getActionMap().put(StopEditingAction.STOPEDITING_ID, stopEditingAction);
    }

    private void configureCellEditor(Binding paramBinding, String paramPropertyPath) {
        this.binding = paramBinding;
        this.propertyPath = paramPropertyPath;
        this.component = getBindingComponent();
        initializeEmptyInstance();
    }

    /**
     * Recupera e configura il control dal {@link Binding}
     * 
     * @return
     */
    protected JComponent getBindingComponent() {
        JComponent bindingComponent = binding.getControl();
        bindingComponent.setBorder(null);
        bindingComponent.setOpaque(false);
        configureBindingComponent(bindingComponent);
        return bindingComponent;
    }

    @Override
    public Object getCellEditorValue() {
        try {
            return binding.getFormModel().getValueModel(this.propertyPath).getValue();
        } catch (NullValueInNestedPathException e) {
            // recupera l'istanza vuota dal binding
            return emptyInstance;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        startingEditing();
        component.setBorder(BorderFactory.createEmptyBorder());
        // specificare altri casi qui, verificare genericamente un JPanel porta problemi devo quindi vedere
        // effettivamente l'identita' del component; in questo modo il component prende la dimensione della cella, se
        // arriva il panel invece non viene adattato il component correttamente
        return component;
    }

    /**
     * Inizializzazione della proprietà emptyInstance. <br>
     * Recupera l'instanza vuota dal Binding se questo è istanza di ComboBoxBinding o di SearchTextBinding.<br>
     * null negli altri casi
     * 
     */
    private void initializeEmptyInstance() {
        this.emptyInstance = null;
        if (this.binding instanceof ComboBoxBinding) {
            emptyInstance = ((ComboBoxBinding) binding).getEmptySelectionValue();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;

    }

    /**
     * @param emptyInstance
     *            The emptyInstance to set.
     */
    public void setEmptyInstance(Object emptyInstance) {
        this.emptyInstance = emptyInstance;
    }

    /**
     * Metodo da sovrascrivere per eseguire particolari operazioni all'inizio dell'editazione, ad esempio per chiamare
     * la lock, viene chiamato dall'omonimo metodo definito nella classe PanjeaCellEditor dove ha origine il ciclo di
     * vita della cella nella tabella.
     * 
     * @return
     */
    protected boolean startingEditing() {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void validationResultsChanged(ValidationResults results) {
        if (component != null) {
            if (results.getHasErrors()) {
                component.setBackground(new Color(255, 245, 245));
                component.setToolTipText(
                        ((ValidationMessage) results.getMessages(Severity.ERROR).iterator().next()).getMessage());
                component.setOpaque(true);
            } else {
                // component.setBackground(Color.RED);
                component.setToolTipText("");
                component.setOpaque(false);
            }
        }

    }

}
