/**
 *
 */
package it.eurotn.rich.control.table.navigationloader;

import com.jidesoft.grid.ContextSensitiveCellRenderer;

/**
 * @author fattazzo
 * 
 */
public class NavigatioLoaderContextSensitiveCellRenderer extends ContextSensitiveCellRenderer {

    private static final long serialVersionUID = 961647638606886717L;

    /**
     * @return il numero di icone presenti oltre all'icona di classe
     */
    public int getNumberOfExtraIcons() {
        return 1;
    }

}
