package it.eurotn.panjea.fatturepa.manager.exception;

import org.apache.commons.lang3.time.DateFormatUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

public class XMLCreationException extends Exception {

    private static final long serialVersionUID = 4597048722304273730L;

    private final Documento documento;

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param documento
     *            documento di riferimento
     */
    public XMLCreationException(final String message, final Documento documento) {
        super(message);
        this.documento = documento;
    }

    /**
     * @return the documento
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * @return messaggio formattato
     */
    public String getFormattedMessage() {

        StringBuilder message = new StringBuilder();
        message.append("<html>Documento di riferimento: <b>");
        if (getDocumento().getTipoDocumento() != null && getDocumento().getTipoDocumento().getCodice() != null
                && !getDocumento().getTipoDocumento().getCodice().isEmpty()) {
            message.append(getDocumento().getTipoDocumento().getCodice());
            message.append(" n° " + getDocumento().getCodice().getCodice());
            message.append(" del " + DateFormatUtils.format(getDocumento().getDataDocumento(), "dd/MM/yyyy"));
        }
        message.append("</b><br><br>Risultati della generazione del file XML:<br><ul><li>");
        message.append(getMessage());
        message.append("</li></ul></html>");

        return message.toString();
    }

}
