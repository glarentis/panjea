package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class UpdateCommand extends ApplicationWindowAwareCommand {

    @Override
    protected void doExecuteCommand() {
        LifecycleApplicationEvent event = new OpenEditorEvent("updatePanjea");
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
