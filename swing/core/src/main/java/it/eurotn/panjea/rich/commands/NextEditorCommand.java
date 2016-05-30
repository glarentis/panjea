package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;

/**
 * @author Leonardo
 *
 */
public class NextEditorCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "nextEditorCommand";

    /**
     * Costruttore.
     */
    public NextEditorCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                PanjeaLifecycleApplicationEvent.NEXT_EDITOR, PanjeaLifecycleApplicationEvent.NEXT_EDITOR);
        Application.instance().getApplicationContext().publishEvent(event);

    }
}
