/**
 * 
 */
package it.eurotn.rich.control.table;

import org.jdesktop.swingx.JXTable;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Seleziona tutte le righe di una tabella
 * 
 * @author fattazzo
 * @version 1.0, 02/lug/07
 * 
 */
public class SelectAllCommand extends ActionCommand {

    public static final String COMMAND_ID = "selectAllCommand";

    public JXTable table;

    public SelectAllCommand(JXTable table) {
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
        table.selectAll();
    }

}
