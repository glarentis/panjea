package it.eurotn.panjea.rateirisconti.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ChiusureRiscontiAnniSuccessiviPresentiException;

public class ChiusureRiscontiAnniSuccessiviPresentiExceptionHandler extends MessagesDialogExceptionHandler {

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
        sb.append(
                "Non è possibile eseguire l'operazione poichè esitono documenti di chiusura dei risconti successivi all'anno <b>");
        sb.append(((ChiusureRiscontiAnniSuccessiviPresentiException) evaluatedThrowable).getAnnoRiferimento());
        sb.append("</b></br><ul type='disc'>");
        List<AreaContabile> areePresenti = ((ChiusureRiscontiAnniSuccessiviPresentiException) evaluatedThrowable)
                .getMovimentiPresenti();

        for (AreaContabile areaContabile : areePresenti) {
            sb.append("<li>");
            sb.append(ObjectConverterManager.toString(areaContabile));
            sb.append("</li>");
        }
        sb.append("</ul><br>");
        sb.append("Cancellare i documenti prima per poter eseguire la chiusura dei risconti.");
        sb.append("</html>");

        return new JLabel(sb.toString());
    }

    /**
     *
     * @param throwable
     *            throwable
     * @return Throwable
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
