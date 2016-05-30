package it.eurotn.rich.command;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public abstract class AbstractSearchCommand extends ActionCommand {

    protected static final String COMMAND_ID = "searchCommand";

    /**
     * Costruttore.
     */
    public AbstractSearchCommand() {
        super(COMMAND_ID);
        this.setSecurityControllerId(getPrefixName() + COMMAND_ID);
        RcpSupport.configure(this);
    }

    /**
     * Restituisce una stringa da utilizzare come prefisso per il SecurityControllerId e per il setName del button.
     * 
     * @return prefisso
     */
    protected abstract String getPrefixName();

    @Override
    protected void onButtonAttached(AbstractButton button) {
        super.onButtonAttached(button);
        button.setName(getPrefixName() + "SearchCommand");
    }

}
