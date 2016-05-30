package it.eurotn.panjea.rich.commands;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.JideApplicationWindow;

import it.eurotn.rich.dialog.DockingLayoutManager;

/**
 * Simple command to reload the JIDE layout from a store. Simple delegates to the LayoutManager. Could be extended
 * (along with LayoutManager) to load named layouts (cf perspectives in Eclipse)
 *
 * @author Jonny Wray
 *
 */
public class RestoreDefaultLayoutCommand extends ApplicationWindowAwareCommand {
    private static final String ID = "restoreDefaultLayoutCommand";

    private Logger logger = Logger.getLogger(RestoreDefaultLayoutCommand.class);

    /**
     * Costruttore.
     * 
     */
    public RestoreDefaultLayoutCommand() {
        super(ID);
    }

    /**
     * Ripristina il layout della prospettiva corrente.
     */
    @Override
    protected void doExecuteCommand() {
        logger.debug("---> Enter doExecuteCommand");
        DockingLayoutManager.restoreCurrentLayout(((JideApplicationWindow) getApplicationWindow()).getDockingManager(),
                this.getId());
    }

}
