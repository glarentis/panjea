/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.panjea.sicurezza.domain.Ruolo;

import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * @author Leonardo
 * 
 */
public class SearchRuoliCommand extends ApplicationWindowAwareCommand {

    private static final String ID = "searchRuoliCommand";

    /**
     * Costruttore.
     */
    public SearchRuoliCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("---> Enter doExecuteCommand SearchRuoliCommand");
        ApplicationPage applicationPage = getApplicationWindow().getPage();
        ((PanjeaDockingApplicationPage) applicationPage).openResultView(Ruolo.class.getName());
    }

}
