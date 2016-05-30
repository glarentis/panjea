package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 * Avverte del fatto che non e' stato impostato un conto per l'entita' selezionata puo' accadere nella generazione delle
 * righe contabili da struttura contabile o in altri casi in cui è richiesto il conto dell'entita'.
 *
 * @author Leonardo
 */
public class ContoEntitaAssenteException extends Exception {

    private static final long serialVersionUID = -2597921626624123087L;

    private EntitaLite entitaLite = null;

    /**
     * Costruttore.
     */
    public ContoEntitaAssenteException() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param entitaLite
     *            entitaLite
     */
    public ContoEntitaAssenteException(final EntitaLite entitaLite) {
        super();
        this.entitaLite = entitaLite;
    }

    /**
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public ContoEntitaAssenteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     *            cause
     */
    public ContoEntitaAssenteException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return the entita
     */
    public String getEntita() {
        String entity = null;
        if (entitaLite != null) {
            entity = entitaLite.getCodice() + " - " + entitaLite.getAnagrafica().getDenominazione();
        }
        return entity;
    }

    /**
     * @return the entitaLite
     */
    public EntitaLite getEntitaLite() {
        return entitaLite;
    }

    /**
     * @param entitaLite
     *            the entitaLite to set
     */
    public void setEntitaLite(EntitaLite entitaLite) {
        this.entitaLite = entitaLite;
    }

}
