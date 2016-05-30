/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.panjea.sicurezza.domain.Utente;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * @author Leonardo
 * 
 */
public class SearchUtentiCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(SearchRuoliCommand.class);

    private static final String ID = "searchUtentiCommand";

    /**
     * Costruttore.
     */
    public SearchUtentiCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("---> Enter doExecuteCommand SearchUtentiCommand");
        ApplicationPage applicationPage = getApplicationWindow().getPage();
        ((PanjeaDockingApplicationPage) applicationPage).openResultView(Utente.class.getName());
    }

}
