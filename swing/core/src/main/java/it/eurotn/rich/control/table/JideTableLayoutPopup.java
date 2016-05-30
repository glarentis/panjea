package it.eurotn.rich.control.table;

import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.JTableHeader;

import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.TableHeaderPopupMenuCustomizer;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;

public class JideTableLayoutPopup implements TableHeaderPopupMenuCustomizer {

    @Override
    public void customizePopupMenu(final JTableHeader jtableheader, JPopupMenu jpopupmenu, int i) {
        /**
         * Menù per abilitare/rimuovere l'opzione di visualizzare solamente i subtotali
         */
        JMenuItem subTotalOnlyItem = new JMenuItem(new Action() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean showSummary = ((AggregateTable) jtableheader.getTable()).getAggregateTableModel()
                        .isShowSummaryOnly();
                ((AggregateTable) jtableheader.getTable()).getAggregateTableModel().setShowSummaryOnly(!showSummary);
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

        subTotalOnlyItem.setText("Visualizza solamente i totali");

        int columnIndexConverted = ((ITable<?>) jtableheader.getTable()).getActualColumn(i);

        DefaultBeanTableModel<?> tm = (DefaultBeanTableModel<?>) TableModelWrapperUtils
                .getActualTableModel(jtableheader.getTable().getModel(), DefaultBeanTableModel.class);

        if (columnIndexConverted <= tm.getColumnCount() && columnIndexConverted >= 0) {
            // IN CASO CONTRARIO ERRORE NEL CALCOLARE L'INDICE DELLE COLONNE
            Class<?> columnClass = tm.getColumnClass(columnIndexConverted);
            ObjectConverter converter = ObjectConverterManager.getConverter(columnClass);
            if (converter instanceof PanjeaCompositeConverter) {
                SwitchConverterMenuItem menuItem = new SwitchConverterMenuItem(columnClass);
                menuItem.setText("Scambia codice-descrizione");
                jpopupmenu.add(menuItem);
            }
        }
        jpopupmenu.add(subTotalOnlyItem);
    }
}
