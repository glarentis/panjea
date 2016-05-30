package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class PreferencesCommand extends ApplicationWindowAwareCommand {

    @Override
    protected void doExecuteCommand() {
        logger.debug("--> Apro l'editor per le preferences");

        LifecycleApplicationEvent event = new OpenEditorEvent("preferencesEditor");
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
