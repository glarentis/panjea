package it.eurotn.panjea.magazzino.exception;

/**
 * @author leonardo
 */
public class DataWarehouseException extends Exception {

    private static final long serialVersionUID = 3218626290852551310L;

    /**
     * Costruttore.
     */
    public DataWarehouseException() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param message
     *            message
     */
    public DataWarehouseException(final String message) {
        super(message);
    }

    /**
     * Costruttore.
     *
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public DataWarehouseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            cause
     */
    public DataWarehouseException(final Throwable cause) {
        super(cause);
    }

}
