package it.eurotn.rich.binding.searchtext;

import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.search.AbstractSearchObject;

import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import com.jidesoft.grid.SortableTable;

/**
 * tabella da associare alle searchtext.
 * 
 * @author giangi
 */
public class TableSearchText extends JideTableWidget<Object> {

    /**
     * Costruttore di default.
     * 
     * @param searchObject
     *            searchObject per le ricerche
     * @param objectClass
     *            classe dell'oggetto da gestire
     */
    public TableSearchText(final AbstractSearchObject searchObject, final Class<Object> objectClass) {
        super(searchObject.getId(), searchObject.getColumns().toArray(new String[0]), objectClass);
    }

    /**
     * @param l
     *            mouse listener da aggiungere alla tabella
     */
    public void addMouseListener(MouseListener l) {
        tableControl.addMouseListener(l);
    }

    /**
     * Rimuove tutte le righe presenti.
     */
    public void clearData() {
        setRows(new ArrayList<Object>());
    }

    /**
     * @return riga selezionata
     */
    public int getSelectedRow() {
        return tableControl.getSelectedRow();
    }

    /**
     * 
     * @return modello filterable della tabella
     */
    public TableModel getTableModel() {
        return tableControl.getModel();
    }

    @Override
    protected void init() {
        super.init();
        getTable().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ((SortableTable) getTable()).setSortable(false);
    }

    /**
     * @param tableModel
     *            table model da assegnare alla tabella
     */
    public void setTableModel(TableModel tableModel) {
        tableControl.setModel(tableModel);
    }
}
