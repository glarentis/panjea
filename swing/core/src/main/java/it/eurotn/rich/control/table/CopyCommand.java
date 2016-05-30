/**
 * 
 */
package it.eurotn.rich.control.table;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Copia tutte le righe selezionate della tabella
 * 
 * @author fattazzo
 * @version 1.0, 02/lug/07
 * 
 */
public class CopyCommand extends ActionCommand {

    public static final String COMMAND_ID = "copyCommand";

    private JTable table;

    public CopyCommand(JTable table) {
        super(COMMAND_ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
        this.table = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
     */
    @Override
    protected void doExecuteCommand() {
        TransferHandler th = table.getTransferHandler();
        if (th != null) {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            th.exportToClipboard(table, cb, TransferHandler.COPY);
        }
    }

}
