package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;

/**
 * @author Leonardo
 *
 */
public class PrevEditorCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "prevEditorCommand";

    /**
     * Costruttore.
     * 
     */
    public PrevEditorCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                PanjeaLifecycleApplicationEvent.PREV_EDITOR, PanjeaLifecycleApplicationEvent.PREV_EDITOR);
        Application.instance().getApplicationContext().publishEvent(event);
    }
}
