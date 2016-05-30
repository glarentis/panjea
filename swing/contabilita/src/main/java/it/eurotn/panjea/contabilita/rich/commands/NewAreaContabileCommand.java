package it.eurotn.panjea.contabilita.rich.commands;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;

/**
 * Command per l'apertura dell'editor documento in area contabile,l'inizalizzazione delle proprieta' dell'area vengono
 * eseguite nella createNewObject del form.
 *
 * @author adriano
 * @version 1.0, 30/mag/07
 */
public class NewAreaContabileCommand extends ApplicationWindowAwareCommand {

    private static final Logger LOGGER = Logger.getLogger(NewAreaContabileCommand.class);

    public static final String COMMAND_ID = "newAreaContabileCommand";

    @Override
    protected void doExecuteCommand() {
        LOGGER.debug("--> Enter doExecuteCommand");
        AreaContabileFullDTO areaContabileFullDTO = new AreaContabileFullDTO();
        LifecycleApplicationEvent event = new OpenEditorEvent(areaContabileFullDTO);
        Application.instance().getApplicationContext().publishEvent(event);
        LOGGER.debug("--> Exit doExecuteCommand");
    }

}
