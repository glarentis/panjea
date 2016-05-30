/**
 * 
 */
package it.eurotn.panjea.rich.factory.table;

import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;

import com.toedter.calendar.JDateChooser;

/**
 * CellEditor per personalizzare il pattern di {@link JDateChooser}.
 * 
 * @author Leonardo
 */
public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {

    private class StopEditingAction extends AbstractAction {

        public static final String STOPEDITING_ID = "stopEditing";
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            DateCellEditor.this.fireEditingStopped();
        }
    }

    private static final long serialVersionUID = -3662888151080598097L;

    private JDateChooser dateChooser = null;

    /**
     * Costruttore.
     * 
     */
    public DateCellEditor() {
        super();
        initialize();
    }

    @Override
    public void cancelCellEditing() {
        DateCellEditor.this.fireEditingStopped();
    }

    protected void configureComponent(JComponent component) {
        StopEditingAction stopEditingAction = new StopEditingAction();
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        KeyStroke tabKey = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        component.getInputMap().put(enterKey, StopEditingAction.STOPEDITING_ID);
        component.getInputMap().put(tabKey, StopEditingAction.STOPEDITING_ID);
        component.getActionMap().put(StopEditingAction.STOPEDITING_ID, stopEditingAction);
    }

    @Override
    public Object getCellEditorValue() {
        return dateChooser.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Date date = null;
        if (value instanceof Date) {
            date = (Date) value;
        }
        dateChooser.setDate(date);
        return dateChooser;
    }

    private void initialize() {
        PanjeaComponentFactory componentFactory = (PanjeaComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        dateChooser = componentFactory.createDateChooser(new PanjeaTextFieldDateEditor("dd/MM/yy", "##/##/##", '_'));
        dateChooser.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent arg0) {
                JComponent component = dateChooser.getDateEditor().getUiComponent();
                component.requestFocusInWindow();
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                DateCellEditor.this.fireEditingStopped();
            }

        });
        configureComponent(dateChooser.getDateEditor().getUiComponent());
    }

    @Override
    public boolean stopCellEditing() {
        DateCellEditor.this.fireEditingStopped();
        return true;
    }
}