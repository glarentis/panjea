package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.editors.webbrowser.WikiUrl;

/**
 * @author fattazzo
 *
 */
public class PanjeaWikiCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "panjeaWikiCommand";

    /**
     * Costruttore.
     */
    public PanjeaWikiCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        getApplicationWindow().getPage().openEditor(new WikiUrl());
    }

}
