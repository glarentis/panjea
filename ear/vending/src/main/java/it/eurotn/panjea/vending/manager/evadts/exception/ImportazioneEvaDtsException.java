package it.eurotn.panjea.vending.manager.evadts.exception;

public class ImportazioneEvaDtsException extends Exception {

    private static final long serialVersionUID = -2808486126120154735L;

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     */
    public ImportazioneEvaDtsException(final String message) {
        super(message);
    }

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public ImportazioneEvaDtsException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
