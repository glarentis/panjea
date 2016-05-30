package it.eurotn.rich.exception.handler;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import com.jidesoft.grid.EditingNotStoppedException;

public class TableValidationExceptionHandler extends MessagesDialogExceptionHandler {

    private int evaluatedChainedIndex = 0;

    @Override
    public Object createExceptionContent(Throwable throwable) {
        Throwable evaluatedThrowable = determineEvaluatedThrowable(throwable);

        if (evaluatedThrowable instanceof EditingNotStoppedException) {
            EditingNotStoppedException editingNotStoppedException = (EditingNotStoppedException) evaluatedThrowable;
            return editingNotStoppedException.getValidationResult().getMessage();
        }
        return null;
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