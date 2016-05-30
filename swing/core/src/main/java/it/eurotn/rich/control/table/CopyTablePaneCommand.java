package it.eurotn.rich.control.table;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import javax.swing.TransferHandler;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

import com.jidesoft.grid.TableScrollPane;

public class CopyTablePaneCommand extends ActionCommand {

    public static final String COMMAND_ID = "copyTablePaneCommand";

    private TableScrollPane tableScrollPane;

    /**
     * Comando che copia il contenuto della tabella negli appunti.
     * 
     * @param tableScrollPane
     *            tableScrollPane con il contenuto
     */
    public CopyTablePaneCommand(final TableScrollPane tableScrollPane) {
        super(COMMAND_ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
        this.tableScrollPane = tableScrollPane;
    }

    @Override
    protected void doExecuteCommand() {
        TransferHandler th = tableScrollPane.getTransferHandler();
        if (th != null) {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            th.exportToClipboard(tableScrollPane, cb, TransferHandler.COPY);
        }
    }

}
