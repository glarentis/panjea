package it.eurotn.panjea.magazzino.util.ricercaarticoli;

import java.io.Serializable;

import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;

public abstract class ParametroRicercaAttributo implements Serializable {

    private static final long serialVersionUID = 7199503421467713830L;

    protected String nome;
    protected String operatore;
    protected Object valore;

    /**
     * Costruttore.
     */
    public ParametroRicercaAttributo() {
        super();
    }

    /**
     * Restituisce una nuova istanza di this.
     * 
     * @return ParametroRicercaAttributo
     */
    public abstract ParametroRicercaAttributo getNewInstance();

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the operatore
     */
    public String getOperatore() {
        return operatore;
    }

    /**
     * @return the tipoDato
     */
    public abstract ETipoDatoTipoAttributo getTipoDato();

    /**
     * La string per identificare il tipo di dato.
     * 
     * @return string, boolean, integer,double
     */
    public abstract String getTipoDatoString();

    /**
     * @return the valore
     */
    public Object getValore() {
        return valore;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @param operatore
     *            the operatore to set
     */
    public void setOperatore(String operatore) {
        this.operatore = operatore;
    }

    /**
     * @param valore
     *            the valore to set
     */
    public void setValore(Object valore) {
        this.valore = valore;
    }

}
