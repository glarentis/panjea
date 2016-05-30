package it.eurotn.panjea.rich.editors.query.table;

import com.jidesoft.grid.TreeTableModel;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.rich.editors.query.ProprietaEntityRow;
import it.eurotn.querybuilder.domain.filter.OperatoreQuery;

public class QueryTreeTableModel extends TreeTableModel<ProprietaEntityRow> {

    private static final long serialVersionUID = -4506616162866916115L;

    @Override
    public Class<?> getCellClassAt(int row, int col) {
        if (row == -1) {
            return super.getCellClassAt(row, col);
        }

        ProprietaEntityRow proprieta = getRowAt(row);
        switch (col) {
        case 1:
            return Boolean.class;
        case 2:
            return OperatoreQuery.class;
        case 3:
            return EntityBase.class.isAssignableFrom(proprieta.getProprieta().getType())
                    ? proprieta.getProprieta().getType() : String.class;
        default:
            return super.getCellClassAt(row, col);
        }
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "Proprietà";
        case 1:
            return "Visualizza";
        case 2:
            return "Operatore";
        case 3:
            return "Filtro";
        default:
            return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        switch (col) {
        case 1:
            return true;
        case 2:
        case 3:
            ProprietaEntityRow proprieta = getRowAt(row);
            return !proprieta.getProprieta().isQuerable();
        default:
            return false;
        }
    }

}
