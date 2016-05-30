package it.eurotn.panjea.rich.commands;

import org.springframework.core.io.Resource;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import it.eurotn.panjea.rich.dialogs.ChangeLogDialog;

/**
 * @author Leonardo
 *
 */
public class ChangeLogCommand extends ApplicationWindowAwareCommand {

    private ChangeLogDialog changeLogDialog = new ChangeLogDialog();

    @Override
    protected void doExecuteCommand() {
        changeLogDialog.showDialog();
    }

    /**
     *
     * @param changeLogTextPath
     *            set changelogTextPath
     */
    public void setChangeLogResourcePath(Resource changeLogTextPath) {
        changeLogDialog.setChangeLogTextPath(changeLogTextPath);
    }

}
