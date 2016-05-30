package it.eurotn.rich.control.table;

import org.springframework.richclient.command.ActionCommand;

public class NavigateFirstCommand extends ActionCommand {

    public static final String PARAM_TABLE_WIDGET = "paramTableWidget";

    public NavigateFirstCommand(String commandId) {
        super(commandId);
    }

    @Override
    protected void doExecuteCommand() {

        JideTableWidget<?> tableWidget = (JideTableWidget<?>) getParameter(PARAM_TABLE_WIDGET, null);

        if (tableWidget != null) {
            tableWidget.getTable().getSelectionModel().setSelectionInterval(0, 0);
            tableWidget.scrollToSelectedRow();
        }
    }
}