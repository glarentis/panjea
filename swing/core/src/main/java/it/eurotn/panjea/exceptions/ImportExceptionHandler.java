package it.eurotn.panjea.exceptions;

import it.eurotn.panjea.exporter.exception.ImportException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

public class ImportExceptionHandler extends MessagesDialogExceptionHandler {

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
        if (evaluatedThrowable instanceof ImportException) {
            ImportException importException = (ImportException) evaluatedThrowable;

            sb.append("Errore durante l'importazione del file ");
            sb.append(importException.getNomeFile());
            sb.append(". \n Errore riscontrato: \n");
            sb.append(importException.getMessage());
        } else {
            sb.append(evaluatedThrowable.getMessage());
        }
        return sb.toString();
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
