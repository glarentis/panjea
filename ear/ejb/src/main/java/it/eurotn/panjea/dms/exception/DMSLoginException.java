package it.eurotn.panjea.dms.exception;

public class DMSLoginException extends Exception {

    private static final long serialVersionUID = 2860973838025384671L;

    /**
     * @param message
     *            message
     */
    public DMSLoginException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            massage
     * @param cause
     *            cause
     */
    public DMSLoginException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     *            cause
     */
    public DMSLoginException(final Throwable cause) {
        super(cause);
    }
}
