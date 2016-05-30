package it.eurotn.panjea.anagrafica.exceptionhandler;

import javax.swing.JLabel;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import it.eurotn.panjea.anagrafica.documenti.exception.DataProtocolloNonValidaException;

public class DataProtocolloNonValidaExceptionHandler extends MessagesDialogExceptionHandler {

    private int evaluatedChainedIndex = 0;

    @Override
    public Object createExceptionContent(Throwable throwable) {
        DataProtocolloNonValidaException exc = (DataProtocolloNonValidaException) determineEvaluatedThrowable(
                throwable);

        StringBuilder sb = new StringBuilder();
        sb.append("<html>Data non congrua.");
        sb.append("<br>");
        sb.append("<b>Protocollo: </b>" + exc.getProtocolloDocumento().getCodice() + " - "
                + exc.getProtocolloDocumento().getDescrizione());
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<u>Documento attuale:</u>");
        sb.append("<br>");
        sb.append("<b>Data: </b>" + DateFormatUtils.format(exc.getData(), "dd/MM/yyyy"));
        sb.append("<br>");
        sb.append("<b>Valore protocollo: </b>" + exc.getNumeroProtocollo());
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<u>Documento precedente:</u>");
        sb.append("<br>");
        if (exc.getDataRegistrazioneArea() != null) {
            sb.append("<b>Data registrazione: </b>"
                    + DateFormatUtils.format(exc.getDataRegistrazioneArea(), "dd/MM/yyyy"));
            sb.append("<br>");
        }
        sb.append("<b>Data: </b>" + DateFormatUtils.format(exc.getDataDocumento(), "dd/MM/yyyy"));
        sb.append("<br>");
        sb.append("<b>Numero: </b>" + exc.getNumeroDocumento());
        sb.append("<br>");
        sb.append("<b>Valore protocollo: </b>" + exc.getValoreProtocolloDocumento());
        sb.append("</html>");

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
