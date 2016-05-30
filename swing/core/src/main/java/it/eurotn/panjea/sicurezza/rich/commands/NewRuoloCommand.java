/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author Leonardo
 */
public class NewRuoloCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(NewRuoloCommand.class);

    public static final String ID = "newRuoloCommand";

    /**
     * Costruttore.
     */
    public NewRuoloCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("---> Enter doExecuteCommand NewRuoloCommand");
        Ruolo ruolo = new Ruolo();
        ruolo.setCodiceAzienda(PanjeaSwingUtil.getUtenteCorrente().getCodiceAzienda());
        LifecycleApplicationEvent event = new OpenEditorEvent(ruolo);
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
