package it.eurotn.panjea.vending.rest.manager.palmari.exception;

/**
 *
 * Rilanciata se un operatore non ha un tipo documento per generare una fattura da palmare
 *
 * @author giangi
 * @version 1.0, 19 feb 2016
 *
 */
public class OperatoreSenzaTipoDocumentoFattura extends Exception {

    private static final long serialVersionUID = 6866438001421255829L;
    /**
     * Comment for <code>serialVersionUID</code>
     */
    protected String installazione;
    protected String matricola;
    protected int progressivo;

    protected String denominazione;
    private String operatore;

    /**
     * Costruttore
     *
     * @param installazione
     *            installazione
     * @param matricola
     *            matricola
     * @param progressivo
     *            progressivo
     * @param denominazione
     *            denominazione
     * @param operatore
     *            operatore
     */
    public OperatoreSenzaTipoDocumentoFattura(final String installazione, final String matricola, final int progressivo,
            final String denominazione, final String operatore) {
        this.installazione = installazione;
        this.matricola = matricola;
        this.progressivo = progressivo;
        this.denominazione = denominazione;
        this.operatore = operatore;
    }

    @Override
    public String getMessage() {
        //@formatter:off
            return "Fatt. con operatore senza tipoDocumento per fatturazione: \n" +
                          "                     Progessivo" + progressivo +
                          ", Cliente:" + denominazione +
                          ", Installazione:" + installazione +
                          ", Matricola:" + matricola +
                          ", Operatore:" + operatore;
            //@formatter:on
    }

}
