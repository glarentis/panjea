package it.eurotn.rich.control.table.command;

import javax.swing.JTable;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;

public class OpenReportCommand extends ActionCommand {

    public static final String COMMAND_ID = "openReportCommand";

    private final JTable table;
    private ITableHeader tableHeader;

    /**
     * Costruttore.
     * 
     * @param table
     *            tabella di cui creare il report
     */
    public OpenReportCommand(final JTable table) {
        super(COMMAND_ID);
        RcpSupport.configure(this);

        this.table = table;
    }

    @Override
    protected void doExecuteCommand() {
        new OpenTablePreviewTask(table, tableHeader).execute();
    }

    /**
     * @param tableHeader
     *            The tableHeader to set.
     */
    public void setTableHeader(ITableHeader tableHeader) {
        this.tableHeader = tableHeader;
    }
}
