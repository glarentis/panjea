package it.eurotn.rich.command;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Command Fake utilizzato per richiedere all'interno della Toolbar l'inserimento di un glue
 *
 * @author adriano
 * @version 1.0, 03/ott/07
 *
 */
public class GlueActionCommand extends AbstractCommand {

    private static GlueActionCommand glueActionCommand;

    /**
     * @return glue command
     */
    public static GlueActionCommand getInstance() {
        if (glueActionCommand == null) {
            glueActionCommand = new GlueActionCommand();
        }
        return glueActionCommand;

    }

    /**
     * @see org.springframework.richclient.command.ActionCommandExecutor#execute()
     */
    @Override
    public void execute() {
        // nothing to do

    }

}
