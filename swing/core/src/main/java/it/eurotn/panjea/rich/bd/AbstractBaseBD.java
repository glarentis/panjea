package it.eurotn.panjea.rich.bd;

import java.util.Locale;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.statusbar.StatusBar;
import org.springframework.richclient.application.statusbar.support.StatusBarProgressMonitor;

/**
 * Classe di base per tutti i BusinessDelegate.
 *
 * @author giangi
 * @version 1.0, 15/nov/06
 *
 */
public abstract class AbstractBaseBD {

    private static final Logger LOGGER = Logger.getLogger(AbstractBaseBD.class);

    /**
     * Costruttore.
     */
    public AbstractBaseBD() {
        LOGGER.debug("--> ## COSTRUISCO BD " + getClass().getName());
    }

    /**
     * Aggiorna la status bar rimuovendo il messaggio idMessage.
     *
     * @param idMessage
     *            Id del messaggio da nscondere nella status Bar
     */
    protected void end(final String idMessage) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> End della chiamata a " + idMessage);
        }
        if (!SwingUtilities.isEventDispatchThread()) {
            // Se non sono nel thread AWT lo sposto e lo richiamo
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    endAwt();
                }
            });
        } else {
            endAwt();
        }
    }

    /**
     * Aggiorna la status bar rimuovendo il messaggio idMessage.
     */
    private void endAwt() {
        if (!Application.isLoaded()) {
            return;
        }
        if (Application.instance().getActiveWindow() != null) {
            StatusBar statusBar = Application.instance().getActiveWindow().getStatusBar();
            statusBar.getProgressMonitor().done();

            // ripristina il normale stato dell'applicazione
            BusyIndicator.clearAt();
        }
    }

    /**
     * Aggiorna la statusBar.
     *
     * @param idMessage
     *            Id del messaggio da visualizzare nella status Bar
     */
    protected void start(final String idMessage) {
        if (!SwingUtilities.isEventDispatchThread()) {
            // Se non sono nel thread AWT lo sposoo e lo richiamo
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    startAwt(idMessage);
                }
            });
        } else {
            startAwt(idMessage);
        }
    }

    /**
     * Aggiorna la status bar.<b>NB:</b>Il metodo deve essere chiamato nel thread AWT
     *
     * @param idMessage
     *            Id del messaggio da visualizzare nella status Bar
     */
    private void startAwt(String idMessage) {
        if (!Application.isLoaded()) {
            return;
        }

        if (Application.instance().getActiveWindow() != null) {
            BusyIndicator.showAt();

            StatusBar statusBar = Application.instance().getActiveWindow().getStatusBar();
            MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                    .getService(MessageSource.class);
            String message = "";
            try {
                message = messageSource.getMessage(idMessage, null, Locale.getDefault());
            } catch (NoSuchMessageException e) {
                LOGGER.warn("--> Non trovo il messaggio per la status bar " + idMessage, e);
            }
            statusBar.getProgressMonitor().taskStarted(message, StatusBarProgressMonitor.UNKNOWN);
        }
    }
}
