package it.eurotn.panjea.auvend.exception;

import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione da sollevare per gestire l'assenza di un articolo di AuVend.
 *
 * @author adriano
 * @version 1.0, 23/dic/2008
 *
 */
public class ArticoloAuVendNotFoundException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = 1L;

    private String codiceArticolo;

    /**
     * Costruttore.
     *
     * @param codiceArticolo
     *            codice articolo
     */
    public ArticoloAuVendNotFoundException(final String codiceArticolo) {
        super();
        this.codiceArticolo = codiceArticolo;

    }

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param codiceArticolo
     *            codice articolo
     */
    public ArticoloAuVendNotFoundException(final String message, final String codiceArticolo) {
        super(message);
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param cause
     *            causa
     * @param codiceArticolo
     *            codice articolo
     */
    public ArticoloAuVendNotFoundException(final String message, final String codiceArticolo, final Throwable cause) {
        super(message, cause);
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            causa
     * @param codiceArticolo
     *            codice articolo
     */
    public ArticoloAuVendNotFoundException(final String codiceArticolo, final Throwable cause) {
        super(cause);
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @return Returns the codiceArticolo.
     */
    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { codiceArticolo };
    }

}
