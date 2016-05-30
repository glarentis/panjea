package it.eurotn.rich.exception.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import it.eurotn.panjea.exception.PanjeaPropertyException;

public class JecMessageDialogExceptionHandler extends MessagesDialogExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(JecMessageDialogExceptionHandler.class);

    @Override
    public Object createExceptionContent(Throwable throwable) {
        if (throwable instanceof PanjeaPropertyException) {
            String[] messagesKeys = getMessagesKeys(throwable);
            String[] parameters = ((PanjeaPropertyException) throwable).getPropertyVaules();
            if (parameters == null || parameters.length == 0) {
                parameters = new String[] { formatMessage(throwable.getMessage()) };
            }

            return messageSourceAccessor
                    .getMessage(new DefaultMessageSourceResolvable(messagesKeys, parameters, messagesKeys[0]));
        } else {
            return super.createExceptionContent(throwable);
        }
    }

    private String[] getMessagesKeys(Throwable throwable) {
        List<String> messageDescriptionKeyList = new ArrayList<String>();
        Class<?> clazz = throwable.getClass();
        while (clazz != Object.class) {
            messageDescriptionKeyList.add(clazz.getName() + ".description");
            clazz = clazz.getSuperclass();
        }
        return messageDescriptionKeyList.toArray(new String[messageDescriptionKeyList.size()]);
    }

    @Override
    public void notifyUserAboutException(final Thread thread, final Throwable throwable) {
        LOGGER.error("--> errore ", throwable);
        throwable.printStackTrace();
        if (SwingUtilities.isEventDispatchThread()) {
            // errore da mail.
            try {
                super.notifyUserAboutException(thread, throwable);
            } catch (Exception ex) {
                LOGGER.error(
                        "-->errore nel visualizzare la finestra di dialogo per un errore (nb:...di un altro errore -o)",
                        ex);
            }
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        JecMessageDialogExceptionHandler.this.notifyUserAboutException(thread, throwable);
                        LOGGER.error("-->errore ", throwable);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
