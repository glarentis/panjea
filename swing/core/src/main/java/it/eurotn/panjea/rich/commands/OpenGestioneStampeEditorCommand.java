package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class OpenGestioneStampeEditorCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "openGestioneStampeEditorCommand";

    /**
     * Costruttore.
     */
    public OpenGestioneStampeEditorCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        LifecycleApplicationEvent event = new OpenEditorEvent("gestioneStampeEditor");
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
