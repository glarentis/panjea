package it.eurotn.panjea.exception;

public class GenericException extends RuntimeException {

    private static final long serialVersionUID = 5581625596880549639L;

    /**
     *
     * @param e
     *            ex
     */
    public GenericException(Exception e) {
        super(e);
    }

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio dell'eccezione
     */
    public GenericException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            messaggio
     * @param cause
     *            ecc.
     */
    public GenericException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
