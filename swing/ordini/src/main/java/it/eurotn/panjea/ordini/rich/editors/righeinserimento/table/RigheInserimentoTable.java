package it.eurotn.panjea.ordini.rich.editors.righeinserimento.table;

import java.util.Arrays;
import java.util.List;

import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;

import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;
import it.eurotn.rich.control.table.JideTableWidget;

public class RigheInserimentoTable extends JideTableWidget<RigaOrdineInserimento> {

    /**
     * Fatto ad hoc per Cosaro che non vuole vedere quantità o altri attributi se non Colli, Confezioni e Pezzi.
     */
    private static final String[] VISIBLECOLUMNS = new String[] { "Articolo", "Colli", "Confezioni", "Pezzi" };

    /**
     * Costruttore.
     *
     * @param righeOrdineInserimento
     *            righe ordine inserimento
     */
    public RigheInserimentoTable(final RigheOrdineInserimento righeOrdineInserimento) {
        super("righeInserimentoTable", new RigheInserimentoTableModel(righeOrdineInserimento));

        ((SortableTable) getTable()).sortColumn(0);
        getTable().getTableHeader().setReorderingAllowed(false);
        getOverlayTable().setShowOptionPanel(false);
        ((AggregateTable) getTable()).getAggregateTableModel().setAggregatedColumns(new int[] {});
        ((AggregateTable) getTable()).getAggregateTableModel().aggregate();
        FilterableTableModel tm = (FilterableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getModel(), FilterableTableModel.class);
        tm.setFiltersApplied(false);
        tm.refresh();

        /*
         * Commanto i renderer che visualizzano qta e valore attributi provenienti dalle ricerce perchè Cosaro non le
         * vuole.
         *
         * getTable().getColumnModel().getColumn(1).setCellRenderer(new QtaInserimentoCellRenderer());
         *
         * for (int i = 2; i < getTable().getColumnModel().getColumnCount(); i++) {
         * getTable().getColumnModel().getColumn(i).setCellRenderer(new AttributoInserimentoCellRenderer()); }
         */

        List<String> visibleColsList = Arrays.asList(VISIBLECOLUMNS);
        for (int i = getTable().getColumnCount() - 1; i >= 0; i--) {
            if (!visibleColsList.contains(getTable().getColumnName(i))) {
                TableColumnChooser.hideColumn(getTable(), i);
            }
        }
    }

}
