package it.eurotn.panjea.mail.exception;

public class EmailException extends Exception {

    private static final long serialVersionUID = 8833504932041613219L;

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

}
