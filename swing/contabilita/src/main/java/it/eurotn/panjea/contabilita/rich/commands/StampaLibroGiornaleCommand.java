package it.eurotn.panjea.contabilita.rich.commands;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class StampaLibroGiornaleCommand extends ApplicationWindowAwareCommand {

    private static final Logger LOGGER = Logger.getLogger(StampaLibroGiornaleCommand.class);

    public static final String COMMAND_ID = "stampaLibroGiornaleCommand";

    @Override
    protected void doExecuteCommand() {
        LOGGER.debug("--> Enter doExecuteCommand");
        LifecycleApplicationEvent event = new OpenEditorEvent("libriGiornale");
        Application.instance().getApplicationContext().publishEvent(event);

        LOGGER.debug("--> Exit doExecuteCommand");
    }

}
