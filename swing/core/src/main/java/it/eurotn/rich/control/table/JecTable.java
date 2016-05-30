package it.eurotn.rich.control.table;

import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;

/**
 * Tabella ereditata da <code>AggregateTable</code><br/>
 * con delle funzionalità installate di default.<br/>
 * <b>Menù</b>:Installato un menù di default.
 * 
 * @author giangi
 * 
 */
public class JecTable extends AggregateTable {
    private static final long serialVersionUID = 1L;

    /**
     * Costruttore di default.
     */
    public JecTable() {
        TableSearchable tableSearchable = SearchableUtils.installSearchable(this);
        tableSearchable.setMainIndex(-1); // search for all columns
        this.setRowResizable(true);
        this.setRowAutoResizes(true);
    }

}
