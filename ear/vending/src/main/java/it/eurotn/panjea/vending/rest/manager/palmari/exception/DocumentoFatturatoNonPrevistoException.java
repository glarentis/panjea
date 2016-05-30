package it.eurotn.panjea.vending.rest.manager.palmari.exception;

public class DocumentoFatturatoNonPrevistoException extends Exception {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    protected static final long serialVersionUID = 1L;
    protected String installazione;
    protected String matricola;
    protected int progressivo;
    protected String denominazione;

    /**
     *
     * @param installazione
     *            installazione
     * @param matricola
     *            matricola
     * @param progressivo
     *            progressivo
     * @param denominazione
     *            denominazione
     */
    public DocumentoFatturatoNonPrevistoException(final String installazione, final String matricola,
            final int progressivo, final String denominazione) {
        this.installazione = installazione;
        this.matricola = matricola;
        this.progressivo = progressivo;
        this.denominazione = denominazione;
    }

    @Override
    public String getMessage() {
        //@formatter:off
        return "Fatt. non prevista: \n" +
                      "                     Progessivo" + progressivo +
                      ", Cliente:" + denominazione +
                      ", Installazione:" +
                      installazione + ", Matricola:" + matricola;
        //@formatter:on
    }

}
