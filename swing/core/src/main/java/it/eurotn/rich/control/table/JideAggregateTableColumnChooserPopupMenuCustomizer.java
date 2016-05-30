package it.eurotn.rich.control.table;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import com.jidesoft.grid.TableColumnChooserDialog;
import com.jidesoft.pivot.AggregateTableColumnChooserPopupMenuCustomizer;

public class JideAggregateTableColumnChooserPopupMenuCustomizer extends AggregateTableColumnChooserPopupMenuCustomizer {

    public JideAggregateTableColumnChooserPopupMenuCustomizer() {
    }

    private JMenuItem createMenuItem(final ITable table, final Action paramBaseAction) {
        final Action baseAction = paramBaseAction;
        JMenuItem menuItem = new JMenuItem(new Action() {

            @Override
            public void actionPerformed(ActionEvent e) {

                baseAction.actionPerformed(e);

                String[] defaultAggregateColumns = (String[]) ((JComponent) table)
                        .getClientProperty(JideTableWidget.DEFAULT_AGGREGATE_COLUMNS_PROPERTY);

                if (defaultAggregateColumns != null) {
                    table.setAggregatedColumns(defaultAggregateColumns);
                }
            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {
            }

            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public void putValue(String key, Object value) {
            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {
            }

            @Override
            public void setEnabled(boolean b) {
            }
        });
        return menuItem;
    }

    @Override
    public TableColumnChooserDialog createTableColumnChooserDialog(Window window, String s, JTable jtable) {
        return super.createTableColumnChooserDialog(window, s, jtable);
    }

    @Override
    public void customizePopupMenu(JTableHeader jtableheader, JPopupMenu jpopupmenu, int i) {
        super.customizePopupMenu(jtableheader, jpopupmenu, i);
        final JTableHeader header = jtableheader;
        ITable<?> table = (ITable<?>) header.getTable();

        Action action = null;
        List<Integer> idxRemove = new ArrayList<Integer>();

        for (int j = 0; j < jpopupmenu.getComponents().length; j++) {
            Component component = jpopupmenu.getComponents()[j];

            if (CONTEXT_MENU_RESET_COLUMNS.equals(component.getName())) {
                action = ((JMenuItem) component).getAction();
                idxRemove.add(j);
            } else if (CONTEXT_MENU_MORE.equals(component.getName())) {
                idxRemove.add(j);
            }
        }

        for (int j = idxRemove.size() - 1; j >= 0; j--) {
            jpopupmenu.remove(idxRemove.get(j));
        }

        JMenuItem menuItem = createMenuItem(table, action);
        menuItem.setText("Ripristina colonne iniziali");
        jpopupmenu.add(menuItem);

    }
}
