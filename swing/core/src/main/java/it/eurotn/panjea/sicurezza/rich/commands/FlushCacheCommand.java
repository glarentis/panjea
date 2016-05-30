/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.commands;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.rich.dialog.MessageAlert;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;

/**
 * @author Leonardo
 * 
 */
public class FlushCacheCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(NewRuoloCommand.class);

    public static final String ID = "flushCacheCommand";

    private ISicurezzaBD sicurezzaBD;

    /**
     * Costruttore.
     */
    public FlushCacheCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("---> Enter doExecuteCommand FlushCacheCommand");
        sicurezzaBD.flushCache();
        // messaggio di notifica dell' avvenuto aggiornamento dei permessi utente
        MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                .getService(MessageSource.class);

        String message = messageSource.getMessage("flushCommand.dialog.message", null, Locale.getDefault());
        Message msg = new DefaultMessage(message);
        MessageAlert messageAlert = new MessageAlert(msg);
        messageAlert.showAlert();
    }

    /**
     * @return Returns the sicurezzaBD.
     */
    public ISicurezzaBD getSicurezzaBD() {
        return sicurezzaBD;
    }

    /**
     * @param sicurezzaBD
     *            The sicurezzaBD to set.
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }

}
