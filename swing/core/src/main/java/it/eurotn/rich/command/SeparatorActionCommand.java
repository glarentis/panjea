package it.eurotn.rich.command;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Command Fake utilizzato per richiedere all'interno della Toolbar l'inserimento di un separator.
 *
 * @author adriano
 * @version 1.0, 03/ott/07
 *
 */
public class SeparatorActionCommand extends AbstractCommand {

    private static SeparatorActionCommand separatorActionCommand;

    /**
     *
     * @return separator action command
     */
    public static SeparatorActionCommand getInstance() {
        if (separatorActionCommand == null) {
            separatorActionCommand = new SeparatorActionCommand();
        }
        return separatorActionCommand;

    }

    /**
     * @see org.springframework.richclient.command.ActionCommandExecutor#execute()
     */
    @Override
    public void execute() {
        // nothing to do

    }

}
