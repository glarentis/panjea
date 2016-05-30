package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione rilanciata in caso di AreaContabile duplicata
 *
 * @author adriano
 * @version 1.0, 07/nov/07
 */
public class AreaContabileDuplicateException extends Exception implements PanjeaPropertyException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * @uml.property name="areaContabileEsistente"
     * @uml.associationEnd
     */
    private AreaContabile areaContabileEsistente;

    /**
     *
     */
    public AreaContabileDuplicateException() {
        super();
    }

    /**
     * @param message
     *            messaggio
     */
    public AreaContabileDuplicateException(final String message) {
        super(message);
    }

    /**
     *
     * @param message
     *            messaggio
     * @param areaContabileEsistente
     *            area contabile
     */
    public AreaContabileDuplicateException(final String message, final AreaContabile areaContabileEsistente) {
        super(message);
        this.areaContabileEsistente = areaContabileEsistente;
    }

    /**
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public AreaContabileDuplicateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     *            causa
     */
    public AreaContabileDuplicateException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return Returns the areaContabileEsistente.
     * @uml.property name="areaContabileEsistente"
     */
    public AreaContabile getAreaContabileEsistente() {
        return areaContabileEsistente;
    }

    @Override
    public String[] getPropertyVaules() {
        String[] values = new String[] { "-", "" };
        if (areaContabileEsistente != null) {
            values = new String[] { areaContabileEsistente.getCodice().getCodice() != null
                    ? areaContabileEsistente.getCodice().toString() : "-" };
        }
        return values;
    }

}
