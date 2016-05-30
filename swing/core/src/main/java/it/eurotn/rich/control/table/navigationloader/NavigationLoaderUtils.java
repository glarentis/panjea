/**
 * 
 */
package it.eurotn.rich.control.table.navigationloader;

import javax.swing.JTable;

/**
 * @author fattazzo
 * 
 */
public final class NavigationLoaderUtils {

    /**
     * Installa i listener necessari per la gestione dei navigation loaders sulla tabella.
     * 
     * @param table
     *            tabella
     */
    public static void install(JTable table) {

        NavigationLoaderTableListener listener = new NavigationLoaderTableListener(table);

        table.addMouseMotionListener(listener);
        table.addMouseListener(listener);

        table.putClientProperty("navigationLoadersListener", listener);
    }

    /**
     * Rimuove gli aventuali listener installati per la gestione dei navigation loaders.
     * 
     * @param table
     *            tablella
     */
    public static void uninstall(JTable table) {

        NavigationLoaderTableListener listener = (NavigationLoaderTableListener) table
                .getClientProperty("navigationLoadersListener");
        if (listener != null) {
            table.removeMouseMotionListener(listener);
            table.removeMouseListener(listener);
            table.putClientProperty("navigationLoadersListener", null);
        }
    }

    /**
     * Costruttore.
     */
    private NavigationLoaderUtils() {
        super();
    }
}
