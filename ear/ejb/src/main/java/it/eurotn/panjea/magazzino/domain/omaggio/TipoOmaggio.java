package it.eurotn.panjea.magazzino.domain.omaggio;

/**
 * Tipo omaggio per la diversa gestione.<br>
 * <ul>
 * <li>OMAGGIO: omaggio in cui viene ceduto un prodotto di produzione propria
 * <li>PROMOZIONE: promozioni per campagne di vendita in cui viene ceduto al cliente un articolo che non fa parte dei
 * propri prodotti
 * <li>CESSIONE: cessione di articoli di produzione propria ad associazioni e simili
 * <li>ALTRO_OMAGGIO: omaggio di articoli propri che non rientra nelle categorie precedenti
 * </ul>
 *
 * @author leonardo
 */
public enum TipoOmaggio {

    NESSUNO(false, false), OMAGGIO(true, true), PROMOZIONE(true, true), CESSIONE(true, true), ALTRO_OMAGGIO(true,
            false);

    private boolean omaggio;
    private boolean sostituzioneIva;

    /**
     * Costruttore.
     * 
     */
    private TipoOmaggio() {
        this.omaggio = false;
    }

    /**
     * Costruttore.
     * 
     * @param omaggio
     *            indica se è una gestione omaggio
     * @param sostituzioneIva
     *            indica se devo sostituire l'iva alla riga articolo.
     */
    private TipoOmaggio(final boolean omaggio, final boolean sostituzioneIva) {
        this.omaggio = omaggio;
        this.sostituzioneIva = sostituzioneIva;
    }

    /**
     * @return Returns the omaggio.
     */
    public boolean isOmaggio() {
        return omaggio;
    }

    /**
     * @return Returns the sostituzioneIva.
     */
    public boolean isSostituzioneIva() {
        return sostituzioneIva;
    }

}
