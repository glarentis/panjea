package it.eurotn.panjea.giroclienti.rich.editors.scheda.table;

import javax.swing.CellEditor;

import com.jidesoft.grid.CellEditorFactory;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.pivot.AggregateTable;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.ContattiGiroCellRenderer;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.rich.control.table.JideTableWidget;

public class RigheSchedaGiroTable extends JideTableWidget<RigaGiroCliente> {

    /**
     * Costruttore.
     */
    public RigheSchedaGiroTable() {
        super("righeGiroTable", new RigheGiroClienteTableModel());

        ((SortableTable) getTable()).setSortable(false);
        getTable().getTableHeader().setReorderingAllowed(false);
        getOverlayTable().setShowOptionPanel(false);
        getTable().setRowHeight(40);
        ((AggregateTable) getTable()).getAggregateTableModel().setAggregatedColumns(new int[] { 0 });
        ((AggregateTable) getTable()).getAggregateTableModel().aggregate();

        getTable().getColumnModel().getColumn(1).setCellRenderer(new EntitaGiroCellRenderer());
        getTable().getColumnModel().getColumn(2).setCellRenderer(new ContattiGiroCellRenderer());

        // registro qui il renderer senza appesantire l'xml
        CellRendererManager.registerRenderer(AreaOrdine.class, new AreaOrdineGiroClienteCellEditorRenderer(this),
                AreaOrdineGiroClienteCellEditorRenderer.CONTEXT);
        CellEditorManager.registerEditor(AreaOrdine.class, new CellEditorFactory() {

            @Override
            public CellEditor create() {
                return new AreaOrdineGiroClienteCellEditorRenderer(RigheSchedaGiroTable.this);
            }
        }, AreaOrdineGiroClienteCellEditorRenderer.CONTEXT);
    }

}
