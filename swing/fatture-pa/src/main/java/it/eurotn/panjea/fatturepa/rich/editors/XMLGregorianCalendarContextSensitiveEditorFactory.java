package it.eurotn.panjea.fatturepa.rich.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

import javax.swing.CellEditor;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.control.table.editor.AbstractCellEditorFactory;

public class XMLGregorianCalendarContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    private class DateCellEditor extends ContextSensitiveCellEditor {
        private static final long serialVersionUID = -6469527574524217974L;

        private JDateChooser dateChooser;

        /**
         * Costruttore.
         *
         * @param type
         *            tipo
         */
        public DateCellEditor(final Class<?> type) {
            super();
            setType(type);
        }

        @Override
        public Object getCellEditorValue() {
            if (dateChooser.getDate() != null) {
                return FatturazionePAUtils.getXMLGregorianCalendar(dateChooser.getDate());
            } else {
                return null;
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {

            String mask = "dd/MM/yy";
            boolean manageTime = false;
            if (getEditorContext() != null) {
                mask = (String) getEditorContext().getUserObject();
                manageTime = true;
            }
            PanjeaTextFieldDateEditor textFieldDateEditor = new PanjeaTextFieldDateEditor(mask, mask, '_');
            textFieldDateEditor.setManageTime(manageTime);
            dateChooser = new JDateChooser(textFieldDateEditor);
            /*
             * aggiungo un focusListeners per assegnare il focus al componente UI del suo DefaultTextEditor
             */
            dateChooser.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent arg0) {
                    JComponent component = dateChooser.getDateEditor().getUiComponent();
                    component.requestFocusInWindow();
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                    // nothing to do
                }

            });
            dateChooser.cleanup();
            Date date = null;
            if (value != null) {
                date = ((XMLGregorianCalendar) value).toGregorianCalendar().getTime();
            }
            dateChooser.setDate(date);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            final JComponent uicomp = dateChooser.getDateEditor().getUiComponent();
            uicomp.setBorder(null);
            uicomp.setOpaque(false);
            return uicomp;
        }

    }

    @Override
    public CellEditor create() {
        return new DateCellEditor(getType());
    }

}
