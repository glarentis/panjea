package it.eurotn.rich.control.table.renderer;

import javax.swing.SwingConstants;

/**
 * Cell renderer per le classi numeriche che allinea a destra il valore visualizzato.
 * 
 * @author fattazzo
 * 
 */
public class NumericContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = 5050438161698279574L;

    /**
     * Costruttore.
     */
    public NumericContextSensitiveCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.RIGHT);
    }
}
