package it.eurotn.panjea.giroclienti.exception;

public class GiroClientiException extends RuntimeException {

    private static final long serialVersionUID = -5354115109929086406L;

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public GiroClientiException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
