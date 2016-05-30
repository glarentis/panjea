package it.eurotn.panjea.anagrafica.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;

/**
 * @author Leonardo
 *
 */
public class TipiDocumentoBaseExceptionHandler extends MessagesDialogExceptionHandler {

    private int evaluatedChainedIndex = 0;

    @Override
    public Object createExceptionContent(Throwable throwable) {
        List<String> messageDescriptionKeyList = new ArrayList<String>();
        Throwable evaluatedThrowable = determineEvaluatedThrowable(throwable);
        Class<?> clazz = evaluatedThrowable.getClass();
        while (clazz != Object.class) {
            messageDescriptionKeyList.add(clazz.getName() + ".description");
            clazz = clazz.getSuperclass();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(RcpSupport.getMessage(evaluatedThrowable.getClass().getName() + ".description"));
        sb.append("</br><ul type='disc'>");
        for (String string : ((TipoDocumentoBaseException) evaluatedThrowable).getTipiMancanti()) {
            sb.append("<li>");
            sb.append(string);
            sb.append("</li>");
        }
        sb.append("</ul></html>");

        return new JLabel(sb.toString());
    }

    /**
     * 
     * @param throwable
     * @return
     */
    private Throwable determineEvaluatedThrowable(Throwable throwable) {
        Throwable evaluatedThrowable = throwable;
        for (int i = 0; i < evaluatedChainedIndex; i++) {
            Throwable cause = evaluatedThrowable.getCause();
            if (cause == null || cause == evaluatedThrowable) {
                break;
            } else {
                evaluatedThrowable = cause;
            }
        }
        return evaluatedThrowable;
    }

    /**
     * @param evaluatedChainedIndex
     *            The evaluatedChainedIndex to set.
     */
    public void setEvaluatedChainedIndex(int evaluatedChainedIndex) {
        this.evaluatedChainedIndex = evaluatedChainedIndex;
    }
}
