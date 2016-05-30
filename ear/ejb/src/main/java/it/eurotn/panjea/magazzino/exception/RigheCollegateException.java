package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione rilanciata quando vengono trovate delle righe collegate ad una riga magazzino.<br>
 * Ad es: Nel caso di riga testata dall'ordine e relative righe collegate; <br>
 * se voglio cancellare la riga testata e ho delle righe articolo collegate rilancio questa eccezione;<br>
 * devo cancellare prima le righe articolo.
 */
public class RigheCollegateException extends RuntimeException implements PanjeaPropertyException {

    private static final long serialVersionUID = -5168642485814394873L;
    private int numeroRigheCollegate = 0;

    /**
     * Costruttore.
     */
    public RigheCollegateException() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param message
     *            message
     */
    public RigheCollegateException(final String message) {
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
    public RigheCollegateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            cause
     */
    public RigheCollegateException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return the numeroRigheCollegate
     */
    public int getNumeroRigheCollegate() {
        return numeroRigheCollegate;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { String.valueOf(numeroRigheCollegate) };
    }

    /**
     * @param numeroRigheCollegate
     *            the numeroRigheCollegate to set
     */
    public void setNumeroRigheCollegate(int numeroRigheCollegate) {
        this.numeroRigheCollegate = numeroRigheCollegate;
    }

}
