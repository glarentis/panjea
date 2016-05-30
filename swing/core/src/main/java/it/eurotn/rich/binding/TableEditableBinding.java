package it.eurotn.rich.binding;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.SortableTable;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.editor.SearchContext;

public class TableEditableBinding<T> extends CustomBinding {

    private class NewSearchAction extends AbstractAction {

        private static final long serialVersionUID = -6415705716217138083L;

        @SuppressWarnings("unchecked")
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != tableWidget.getTable()) {
                return;
            }
            int column = tableWidget.getTable().getSelectedColumn();
            if (column >= 0) {
                column = ((ITable<T>) tableWidget.getTable()).getActualColumn(column);
                EditorContext context = ((ContextSensitiveTableModel) tableWidget.getTable().getModel())
                        .getEditorContextAt(0, column);
                if (context instanceof SearchContext) {
                    tableWidget.getTable().editCellAt(tableWidget.getTable().getSelectedRow(),
                            tableWidget.getTable().getSelectedColumn());
                    if (tableWidget.getTable().getEditorComponent() != null) {
                        KeyEvent keyEvent = new KeyEvent(tableWidget.getTable().getEditorComponent(),
                                KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_F3,
                                KeyStroke.getKeyStroke("F3").getKeyChar());
                        tableWidget.getTable().getEditorComponent().dispatchEvent(keyEvent);
                    }
                }
            }
        }
    }

    private class TableModelChangedListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            try {
                Collection<T> data;
                if (requiredSourceClass.equals(List.class)) {
                    data = new ArrayList<T>();
                } else {
                    if (comparator != null) {
                        data = new TreeSet<T>(comparator);

                    } else {
                        data = new HashSet<T>();
                    }
                }
                data.addAll(tableModel.getObjects());

                controlValueChanged(data);
            } catch (Exception e1) {
                logger.error("-->errore nell'istanziare un ogg. di classe " + getPropertyType().getName(), e1);
                e1.printStackTrace();
            }
        }
    }

    private DefaultBeanTableModel<T> tableModel;
    private JideTableWidget<T> tableWidget;
    private TableModelChangedListener changeListener;
    private Comparator<T> comparator;
    private Class<?> requiredSourceClass;

    {
        changeListener = new TableModelChangedListener();
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            .
     * @param formPropertyPath
     *            .
     * @param requiredSourceClass
     *            .
     * @param tableModel
     *            .
     */
    public TableEditableBinding(final FormModel formModel, final String formPropertyPath,
            final Class<?> requiredSourceClass, final DefaultBeanTableModel<T> tableModel) {
        this(formModel, formPropertyPath, requiredSourceClass, tableModel, null, null);
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            .
     * @param formPropertyPath
     *            .
     * @param requiredSourceClass
     *            .
     * @param tableModel
     *            .
     * @param comparator
     *            .
     *
     */
    public TableEditableBinding(final FormModel formModel, final String formPropertyPath,
            final Class<?> requiredSourceClass, final DefaultBeanTableModel<T> tableModel,
            final Comparator<T> comparator) {
        this(formModel, formPropertyPath, requiredSourceClass, tableModel, comparator, null);
        this.tableModel = tableModel;
        this.comparator = comparator;
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            .
     * @param formPropertyPath
     *            .
     * @param requiredSourceClass
     *            .
     * @param tableModel
     *            .
     * @param comparator
     *            .
     * @param tableWidget
     *            tableWidet
     *
     */
    public TableEditableBinding(final FormModel formModel, final String formPropertyPath,
            final Class<?> requiredSourceClass, final DefaultBeanTableModel<T> tableModel,
            final Comparator<T> comparator, final JideTableWidget<T> tableWidget) {
        super(formModel, formPropertyPath, requiredSourceClass);
        this.requiredSourceClass = requiredSourceClass;
        this.tableModel = tableModel;
        this.comparator = comparator;
        this.tableWidget = tableWidget;
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            .
     * @param formPropertyPath
     *            .
     * @param requiredSourceClass
     *            .
     * @param tableModel
     *            . .
     * @param tableWidget
     *            tableWidet
     *
     */
    public TableEditableBinding(final FormModel formModel, final String formPropertyPath,
            final Class<?> requiredSourceClass, final DefaultBeanTableModel<T> tableModel,
            final JideTableWidget<T> tableWidget) {
        this(formModel, formPropertyPath, requiredSourceClass, tableModel, null, tableWidget);
    }

    @Override
    protected JComponent doBindControl() {
        if (tableWidget == null) {
            tableWidget = new JideTableWidget<T>(tableModel.getModelId(), tableModel);
        }
        tableWidget.getTable().getTableHeader().setReorderingAllowed(false);
        ((SortableTable) tableWidget.getTable()).setSortable(comparator == null);

        tableWidget.getTable().getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0),
                "newSearch");
        tableWidget.getTable().getActionMap().put("newSearch", new NewSearchAction());

        tableWidget.getTable().getModel().addTableModelListener(changeListener);
        valueModelChanged(getValue());
        return tableWidget.getComponent();
    }

    @Override
    protected void enabledChanged() {
        // nei nostri form per abilitare/disabilitare i componenti gestiamo lo stato readOnly,
        // mentre lo stato enabled del form è sempre true;
        tableWidget.setEditable(isEnabled());
    }

    /**
     * @return Returns the tableWidget.
     */
    public JideTableWidget<T> getTableWidget() {
        return tableWidget;
    }

    @Override
    protected void readOnlyChanged() {
        tableWidget.setEditable(!isReadOnly());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void valueModelChanged(Object paramObject) {
        Object listaClonata = PanjeaSwingUtil.cloneObject(paramObject);
        Collection<T> righeClonate = (Collection<T>) listaClonata;
        tableWidget.getTable().getModel().removeTableModelListener(changeListener);
        try {
            tableWidget.setRows(righeClonate);
        } catch (Exception ex) {
            Collection<T> data;
            if (requiredSourceClass.equals(List.class)) {
                data = new ArrayList<T>();
            } else {
                if (comparator != null) {
                    data = new TreeSet<T>(comparator);

                } else {
                    data = new HashSet<T>();
                }
            }
            tableModel.setRows(data);
        } finally {
            tableWidget.getTable().getModel().addTableModelListener(changeListener);
        }
    }
}
