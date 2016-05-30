package it.eurotn.rich.command;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.Assert;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;

/**
 * @author fattazzo
 *
 */
public class OpenSearchCommand extends ApplicationWindowAwareCommand implements InitializingBean {

    private String idCommand;

    private String openString;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(idCommand, "L'id del command è nullo.");
        this.setId(idCommand);
    }

    @Override
    protected void doExecuteCommand() {
        if (openString != null) {
            ApplicationPage applicationPage = getApplicationWindow().getPage();
            ((PanjeaDockingApplicationPage) applicationPage).openResultView(openString);
        }
    }

    /**
     * @param idCommand
     *            the idCommand to set
     */
    public void setIdCommand(String idCommand) {
        this.idCommand = idCommand;
    }

    /**
     * @param openString
     *            the openString to set
     */
    public void setOpenString(String openString) {
        this.openString = openString;
    }

}
