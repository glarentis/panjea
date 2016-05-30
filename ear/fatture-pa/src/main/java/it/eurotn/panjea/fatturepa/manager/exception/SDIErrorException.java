package it.eurotn.panjea.fatturepa.manager.exception;

public class SDIErrorException extends Exception {

    private static final long serialVersionUID = -6533179855052536544L;

    private final String codiceErrore;

    private final String descrizioneErrore;

    /**
     * Costruttore.
     *
     * @param codiceErrore
     *            codice errore SDI
     * @param descrizioneErrore
     *            descrizione errore SDI
     */
    public SDIErrorException(final String codiceErrore, final String descrizioneErrore) {
        super();
        this.codiceErrore = codiceErrore;
        this.descrizioneErrore = descrizioneErrore;
    }

    /**
     * @return the codiceErrore
     */
    public String getCodiceErrore() {
        return codiceErrore;
    }

    /**
     * @return the descrizioneErrore
     */
    public String getDescrizioneErrore() {
        return descrizioneErrore;
    }
}
