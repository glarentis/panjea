package it.eurotn.rich.editors;

import it.eurotn.rich.command.JideToggleCommand;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;

public class AutoResizesRowCommand extends JideToggleCommand {

    public static final String COMMAND_ID = "autoResizesRowCommand";

    private final JideTable table;

    /**
     * Costruttore.
     * 
     * @param table
     *            tabella di riferimento
     */
    public AutoResizesRowCommand(final JideTable table) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.table = table;
    }

    @Override
    protected void onDeselection() {
        super.onDeselection();
        table.setRowAutoResizes(false);
        table.setRowHeight(19);
    }

    @Override
    protected void onSelection() {
        super.onSelection();
        table.setRowAutoResizes(true);
    }
}