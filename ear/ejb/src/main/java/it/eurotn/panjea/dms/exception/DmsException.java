package it.eurotn.panjea.dms.exception;

/**
 *
 * Eccezione generica per il dms
 *
 * @author giangi
 * @version 1.0, 31 lug 2015
 *
 */
public class DmsException extends RuntimeException {
    private static final long serialVersionUID = 4372460310008361782L;

    /**
     * @param message
     *            message
     */
    public DmsException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            massage
     * @param cause
     *            cause
     */
    public DmsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     *            cause
     */
    public DmsException(final Throwable cause) {
        super(cause);
    }

}
