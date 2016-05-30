package it.eurotn.rich.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author fattazzo
 *
 */
public abstract class TableDataSwingWorker<T> extends SwingWorker<Collection<T>, Void> {

    private static final Logger LOGGER = Logger.getLogger(TableDataSwingWorker.class);

    private AbstractTablePageEditor<T> tablePage;

    /**
     * Costruttore.
     *
     * @param tablePage
     *            table page
     */
    public TableDataSwingWorker(final AbstractTablePageEditor<T> tablePage) {
        super();
        this.tablePage = tablePage;
    }

    @Override
    protected final Collection<T> doInBackground() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                tablePage.showSearcInProgressMessage();
            }
        });
        return getData();
    }

    @Override
    protected final void done() {
        super.done();
        if (!isCancelled()) {
            try {
                Collection<T> result = get();
                if (result != null) {
                    tablePage.processTableData(get());
                }
            } catch (InterruptedException e) {
                LOGGER.warn("load table data interrotto");
            } catch (ExecutionException e) {
                Collection<T> clearList = new ArrayList<T>();
                tablePage.processTableData(clearList);
                PanjeaSwingUtil.checkAndThrowException(e.getCause());
            } finally {
                if (tablePage.isControlCreated()) {
                    tablePage.hideSearcInProgressMessage();
                }
            }
        }
    }

    /**
     * Dati da caricare nella pagina.
     *
     * @return dati
     */
    public abstract Collection<T> getData();
}
