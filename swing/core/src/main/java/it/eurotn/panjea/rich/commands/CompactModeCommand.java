package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.JideApplicationWindow;

/**
 * Command per visualizzare la home page dell'applicazione.
 *
 * @author Leonardo
 */
public class CompactModeCommand extends ApplicationWindowAwareCommand {

    private static final String ID = "goHomeCommand";

    /**
     * Costruttore.
     * 
     */
    public CompactModeCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        ((JideApplicationWindow) getApplicationWindow()).setCompactMode(true);
    }
}
