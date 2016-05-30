package it.eurotn.rich.control.table.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;

/**
 * Copia tutte le righe selezionate della tabella.
 *
 * @author fattazzo
 * @version 1.0, 02/lug/07
 *
 */
public class CopyCommand extends ActionCommand {

    public static final String COMMAND_ID = "copyCommand";

    private JTable table;

    /**
     * Costruttore.
     *
     * @param table
     *            tabella dove copiare le righe
     */
    public CopyCommand(final JTable table) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.table = table;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
     */
    @Override
    protected void doExecuteCommand() {
        AsyncWorker.post(new AsyncTask() {

            @Override
            public void failure(Throwable arg0) {

            }

            @Override
            public Object run() throws Exception {
                TransferHandler th = table.getTransferHandler();
                if (th != null) {
                    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                    th.exportToClipboard(table, cb, TransferHandler.COPY);
                }
                return null;
            }

            @Override
            public void success(Object arg0) {

            }
        });
    }
}
