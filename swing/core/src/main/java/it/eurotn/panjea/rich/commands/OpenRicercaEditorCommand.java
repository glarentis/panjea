package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.JideApplicationWindow;

/**
 * @author Leonardo
 *
 */
public class OpenRicercaEditorCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "openRicercaEditorCommand";

    /**
     * Costruttore.
     */
    public OpenRicercaEditorCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        ((JideApplicationWindow) Application.instance().getActiveWindow()).activateOpenEditorSearch();
    }
}
