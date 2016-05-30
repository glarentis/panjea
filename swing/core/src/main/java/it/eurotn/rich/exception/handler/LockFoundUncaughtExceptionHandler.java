package it.eurotn.rich.exception.handler;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import it.eurotn.locking.exception.LockFoundException;

/**
 * Classe per gestire l'eccezione <code>LockFoundException</code>.
 *
 * @author Aracno
 * @version 1.0, 05/giu/06
 *
 */
public class LockFoundUncaughtExceptionHandler extends JecMessageDialogExceptionHandler {

    private static final String EXPLANATION_KEY = "it.eurotn.locking.exception.LockFoundException.description";

    @Override
    public Object createExceptionContent(Throwable throwable) {
        LockFoundException exception = (LockFoundException) throwable;
        Format format = new SimpleDateFormat("hh:mm:ss");
        String ora = format.format(exception.getLock().getTimeStart());
        format = new SimpleDateFormat("hh/MM/yyyy");
        String data = format.format(exception.getLock().getTimeStart());
        Object[] parameters = new Object[] { exception.getLock().getUserName(), ora, data };
        return messageSourceAccessor.getMessage(EXPLANATION_KEY, parameters, Locale.getDefault());
    }
}
