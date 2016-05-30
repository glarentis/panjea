/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.sicurezza.domain.Utente;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author Leonardo
 */
public class NewUtenteCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(NewUtenteCommand.class);

    public static final String ID = "newUtenteCommand";

    /**
     * Costruttore.
     */
    public NewUtenteCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("---> Enter doExecuteCommand NewUtenteCommand");
        LifecycleApplicationEvent event = new OpenEditorEvent(new Utente());
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
