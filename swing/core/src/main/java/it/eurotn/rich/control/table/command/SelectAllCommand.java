package it.eurotn.rich.control.table.command;

import javax.swing.JTable;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Seleziona tutte le righe di una tabella.
 *
 * @author fattazzo
 * @version 1.0, 02/lug/07
 *
 */
public class SelectAllCommand extends ActionCommand {

    public static final String COMMAND_ID = "selectAllCommand";
    private JTable table;

    /**
     * costruttore.
     * 
     * @param table
     *            tabella
     */
    public SelectAllCommand(final JTable table) {
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
        table.selectAll();
    }

}
