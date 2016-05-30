package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.dialog.MessageAlert;

/**
 * @author Leonardo
 *
 */
public class ClearEditorCacheCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "clearEditorCacheCommand";

    /**
     * Costruttore.
     */
    public ClearEditorCacheCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                PanjeaLifecycleApplicationEvent.CLEAR_CACHE, PanjeaLifecycleApplicationEvent.CLEAR_CACHE);
        Application.instance().getApplicationContext().publishEvent(event);

        Message msg = new DefaultMessage("Cache editors svuotata!");
        MessageAlert messageAlert = new MessageAlert(msg);
        messageAlert.showAlert();
    }
}
