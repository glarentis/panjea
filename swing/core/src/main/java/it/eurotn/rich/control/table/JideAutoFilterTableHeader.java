package it.eurotn.rich.control.table;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.jidesoft.pivot.AggregateTableHeader;

/**
 * Classe personalizzata per il tableHeader del filtro.
 * 
 * @author giangi
 * 
 */
public class JideAutoFilterTableHeader extends AggregateTableHeader {

    private static final long serialVersionUID = 5121358262111423841L;

    /**
     * Costruttore di default.
     * 
     * @param table
     *            tabella dove verrà installato il tableheader
     */
    public JideAutoFilterTableHeader(final JTable table) {
        super(table);
        setShowFilterIcon(false);
        setAutoFilterEnabled(true);
        setLabelFont(new JLabel().getFont().deriveFont(Font.BOLD));
        setUseNativeHeaderRenderer(true);
        setGroupHeaderEnabled(false);
        setAllowMultipleValues(true);
        setFont(getFont().deriveFont(Font.BOLD));
    }
}
