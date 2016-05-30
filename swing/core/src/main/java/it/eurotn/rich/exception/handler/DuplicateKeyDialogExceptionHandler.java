package it.eurotn.rich.exception.handler;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.dao.exception.DuplicateKeyObjectException;

/**
 * @author Leonardo
 *
 */
public class DuplicateKeyDialogExceptionHandler extends JecMessageDialogExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(DuplicateKeyDialogExceptionHandler.class);

    @Override
    public Object createExceptionContent(Throwable throwable) {
        DuplicateKeyObjectException exception = (DuplicateKeyObjectException) throwable;
        StringBuilder proprietaI18N = new StringBuilder();

        for (int i = 0; i < exception.getPropertiesCostraintNames().length; i++) {
            String message = RcpSupport.getMessage(exception.getPropertiesCostraintNames()[i]);
            if (message.isEmpty()) {
                message = exception.getPropertiesCostraintNames()[i];
                LOGGER.error(
                        "-->errore: la proprietà contenuta in un indice univoco non è internazionalizzata. Proprietà: "
                                + message);
            }
            proprietaI18N.append(" -" + message + "-");

        }
        return RcpSupport.getMessage("", "it.eurotn.dao.exception.DuplicateKeyObjectException", "description",
                new Object[] { proprietaI18N.toString() });
    }
}
