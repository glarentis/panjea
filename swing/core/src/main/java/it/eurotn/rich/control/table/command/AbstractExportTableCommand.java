package it.eurotn.rich.control.table.command;

import javax.swing.JTable;

import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.editor.export.AbstractExportCommand;

public abstract class AbstractExportTableCommand extends AbstractExportCommand {

    private JTable table;

    private ITableHeader tableHeader;

    /**
     * Costruttore.
     *
     * @param commandId
     *            id del comando
     * @param fileExtension
     *            estensione del file
     * @param table
     *            table da esportare
     */
    public AbstractExportTableCommand(final String commandId, final String fileExtension, final JTable table) {
        super(commandId, fileExtension, null);
        this.table = table;
        this.fileExtension = fileExtension;
    }

    @Override
    protected void doExecuteCommand() {
        PanjeaSwingUtil.lockScreen("Esportazione tabella in corso...");

        try {
            super.doExecuteCommand();
        } finally {
            PanjeaSwingUtil.unlockScreen();
        }
    }

    /**
     * @return Returns the table.
     */
    public JTable getTable() {
        return table;
    }

    /**
     * @return Returns the tableHeader.
     */
    public ITableHeader getTableHeader() {
        return tableHeader;
    }

    /**
     * @param tableHeader
     *            The tableHeader to set.
     */
    public void setTableHeader(ITableHeader tableHeader) {
        this.tableHeader = tableHeader;
    }
}
