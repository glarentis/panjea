package it.eurotn.panjea.rich.factory.table;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.springframework.richclient.factory.TableFactory;

import com.jidesoft.grid.JideTable;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;

public class JideTableFactory implements TableFactory {

    /**
     * Configura la tabella per le funzionalità comuni (ricerca,righe alternate etc...).
     * 
     * @param table
     *            tabella da configurare
     */
    private void configureTable(JTable table) {
        TableSearchable tableSearchable = SearchableUtils.installSearchable(table);
        tableSearchable.setMainIndex(-1); // search for all columns
    }

    @Override
    public JTable createTable() {
        JTable table = new JideTable();
        configureTable(table);
        return table;
    }

    @Override
    public JTable createTable(TableModel model) {
        JTable table = new JideTable(model);
        configureTable(table);
        return table;
    }

}
