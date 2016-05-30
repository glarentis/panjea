package it.eurotn.rich.exception.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

public class IllegalArgumentAssenteExceptionHandler extends MessagesDialogExceptionHandler {

    /**
     * La classe PivotField di jide non si basa sui jide-properties per rilanciare gli errori ma vengono create delle
     * exception con testo predefinito. Se trovo che nell'eccezione c'è una di queste stringhe la rilancio informando
     * l'utente sulla colonna che solleva l'eccezione.
     */
    public static final String PIVOT_FILED_MATCHER = "is not allowed as data field|is not allowed as a filter field|is not allowed as a row field|is not allowed as a column field";

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

        Pattern pattern = Pattern.compile(PIVOT_FILED_MATCHER);
        Matcher matcher = pattern.matcher(evaluatedThrowable.getMessage());
        if (matcher.find()) {
            Pattern patternColonna = Pattern.compile("\".*\"");
            Matcher matcherColonna = patternColonna.matcher(evaluatedThrowable.getMessage());
            matcherColonna.find();
            return new JLabel("Impossibile eseguire lo spostamento della colonna " + matcherColonna.group(0));
        } else {
            throwable.printStackTrace();
            return new JLabel("Errore durante l'esecuzione dell'operazione");
        }
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
