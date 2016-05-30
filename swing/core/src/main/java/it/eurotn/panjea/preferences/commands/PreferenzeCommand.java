/**
 * 
 */
package it.eurotn.panjea.preferences.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class PreferenzeCommand extends ApplicationWindowAwareCommand {

    /*
     * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
     */
    @Override
    protected void doExecuteCommand() {
        LifecycleApplicationEvent event = new OpenEditorEvent("preferenzeEditor");
        Application.instance().getApplicationContext().publishEvent(event);
    }

    @Override
    public String getId() {
        return "preferenzeCommand";
    }

}
