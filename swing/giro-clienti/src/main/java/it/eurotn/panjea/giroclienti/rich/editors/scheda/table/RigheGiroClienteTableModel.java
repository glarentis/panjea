package it.eurotn.panjea.giroclienti.rich.editors.scheda.table;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.rich.converter.DateConverter;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RigheGiroClienteTableModel extends DefaultBeanTableModel<RigaGiroCliente> {

    private static final long serialVersionUID = 4592093488503325057L;

    /**
     * Costruttore.
     */
    public RigheGiroClienteTableModel() {
        super("righeGiroClienteTableModel", new String[] { "ora", "sedeEntita", "contatti", "areaOrdine" },
                RigaGiroCliente.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int col) {

        if (col == 0) {
            return DateConverter.ORA_CONTEXT;
        }

        return super.getConverterContextAt(row, col);
    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {

        if (col == 3) {
            return AreaOrdineGiroClienteCellEditorRenderer.CONTEXT;
        }

        return super.getEditorContextAt(row, col);
    }

}
