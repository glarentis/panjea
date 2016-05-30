package it.eurotn.rich.control.table.editor;

import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextBinder;
import it.eurotn.rich.binding.searchtext.SearchTextController;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellEditor;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.value.ValueModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class SearchContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    public class SearchContextSensitiveEditor extends ContextSensitiveCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 3497763084474509632L;

        private SearchPanel searchPanel;

        private ValueModel valueModel;
        private SearchTextController sc;

        @Override
        public Object getCellEditorValue() {
            return valueModel.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row,
                int column) {

            if (table instanceof ITable) {
                column = ((ITable<?>) table).getActualColumn(column);
                row = ((ITable<?>) table).getActualRowsAt(row, column)[0];
            }
            searchPanel = new SearchPanel() {
                private static final long serialVersionUID = 6501527219696311043L;

                @Override
                protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
                    SearchTextField editorComp = getTextFields().entrySet().iterator().next().getValue();
                    editorComp.setTableBindingMode(true);
                    editorComp.getTextField().requestFocusInWindow();

                    InputMap map = editorComp.getTextField().getInputMap(condition);
                    ActionMap am = editorComp.getTextField().getActionMap();
                    if (map != null && am != null && isEnabled()) {
                        Object binding = map.get(ks);
                        Action action = (binding == null) ? null : am.get(binding);
                        if (action != null) {
                            return SwingUtilities.notifyAction(action, ks, e, editorComp.getTextField(),
                                    e.getModifiers());
                        }
                    }
                    return false;
                }

                @Override
                public void validateAndtransferFocus(Object objectSelected) {
                    // non trasferisco il focus alla fine dell'edit;
                    table.requestFocusInWindow();
                }
            };
            searchPanel.setRequestFocusEnabled(true);
            Map<String, String> parameters = new HashMap<String, String>();

            SearchContext context = (SearchContext) getEditorContext();

            DefaultBeanTableModel<?> tableModel = (DefaultBeanTableModel<?>) TableModelWrapperUtils
                    .getActualTableModel(table.getModel());
            DefaultFormModel formModel = context.createFormModel(tableModel.getElementAt(row));

            String formPropertyPath = context.getBasePropertyPath();
            if (formPropertyPath == null) {
                formPropertyPath = tableModel.getColumnPropertyNames()[column];
            }

            valueModel = formModel.getValueModel(formPropertyPath);

            HashMap<String, Object> bindingContext = context.getBindingContext();
            searchPanel.configure(formPropertyPath, bindingContext, formModel, context.getPropertyFilter());

            sc = new SearchTextController(searchPanel, parameters);
            searchPanel.setBorder(null);
            String[] renderProperties = (String[]) bindingContext.get(SearchTextBinder.RENDERER_KEY);
            valueModel.addValueChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    // se arriva null (ad es.se premo F8) non devo interrompere l'inserimento
                    if (evt.getNewValue() != null) {
                        stopCellEditing();
                    }
                }
            });

            searchPanel.getTextFields().get(renderProperties[0]).getTextField().requestFocusInWindow();
            return searchPanel;
        }

        @Override
        public void setClickCountToStart(int paramInt) {
            super.setClickCountToStart(1);
        }

        @Override
        public void setConverterContext(ConverterContext paramConverterContext) {
            super.setConverterContext(paramConverterContext);
        }

        @Override
        public boolean stopCellEditing() {
            if (searchPanel.getPopUp().isPopupVisible()) {
                sc.selectTableObject();
            }
            return super.stopCellEditing();
        }

    }

    public static final EditorContext CONTEXT = new SearchContext();

    @Override
    public CellEditor create() {
        return new SearchContextSensitiveEditor();
    }

}
