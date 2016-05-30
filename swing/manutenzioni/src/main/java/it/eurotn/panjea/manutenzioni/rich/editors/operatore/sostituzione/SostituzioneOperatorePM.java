package it.eurotn.panjea.manutenzioni.rich.editors.operatore.sostituzione;

import it.eurotn.panjea.manutenzioni.domain.Operatore;

public class SostituzioneOperatorePM {

    private Operatore operatore;

    private Operatore operatoreDaSostituire;

    private boolean tecnico = false;
    private boolean caricatore = false;

    /**
     * Costruttore.
     */
    public SostituzioneOperatorePM() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param operatore
     *            operatore
     */
    public SostituzioneOperatorePM(final Operatore operatore) {
        super();
        setOperatore(operatore);
    }

    /**
     * @return the operatore
     */
    public Operatore getOperatore() {
        return operatore;
    }

    /**
     * @return the operatoreDaSostituire
     */
    public Operatore getOperatoreDaSostituire() {
        return operatoreDaSostituire;
    }

    /**
     * @return the caricatore
     */
    public boolean isCaricatore() {
        return caricatore;
    }

    /**
     * @return the tecnico
     */
    public boolean isTecnico() {
        return tecnico;
    }

    /**
     * @param caricatore
     *            the caricatore to set
     */
    public void setCaricatore(boolean caricatore) {
        this.caricatore = caricatore;
    }

    /**
     * @param operatore
     *            the operatore to set
     */
    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
        this.tecnico = operatore.isTecnico();
        this.caricatore = operatore.isCaricatore();
    }

    /**
     * @param operatoreDaSostituire
     *            the operatoreDaSostituire to set
     */
    public void setOperatoreDaSostituire(Operatore operatoreDaSostituire) {
        this.operatoreDaSostituire = operatoreDaSostituire;
    }

    /**
     * @param tecnico
     *            the tecnico to set
     */
    public void setTecnico(boolean tecnico) {
        this.tecnico = tecnico;
    }

}
