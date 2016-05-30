package it.eurotn.panjea.exceptions;

/**
 * Viene utilizzata per inglobare un'eccezione applicativa e poterla gestire nel framework delle eccezioni di spring
 * rich client.
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class PanjeaRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2624187334171080646L;

    /**
     * 
     */
    public PanjeaRuntimeException() {
        super();
    }

    /**
     * @param message
     *            messaggio
     */
    public PanjeaRuntimeException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public PanjeaRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     *            causa
     */
    public PanjeaRuntimeException(final Throwable cause) {
        super(cause);
    }
}
