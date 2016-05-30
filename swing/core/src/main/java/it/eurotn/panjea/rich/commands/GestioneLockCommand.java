package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class GestioneLockCommand extends ApplicationWindowAwareCommand {

    @Override
    protected void doExecuteCommand() {
        logger.debug("--> Apro l'editor per i lock");

        LifecycleApplicationEvent event = new OpenEditorEvent("gestioneLockEditor");
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
