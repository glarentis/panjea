package it.eurotn.panjea.agenti.domain;

import java.io.Serializable;

import javax.persistence.ManyToOne;

import it.eurotn.panjea.magazzino.domain.Listino;

/**
 * @author fattazzo
 *
 */
public class BaseProvvigionaleSettings implements Serializable {

    private static final long serialVersionUID = 4777953536653443851L;

    private BaseProvvigionaleStrategy baseProvvigionaleStrategy;

    @ManyToOne
    private Listino listino;

    /**
     * Costruttore.
     */
    public BaseProvvigionaleSettings() {
        super();
    }

    /**
     * @return the baseProvvigionaleStrategy
     */
    public BaseProvvigionaleStrategy getBaseProvvigionaleStrategy() {
        return baseProvvigionaleStrategy;
    }

    /**
     * @return the listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @param baseProvvigionaleStrategy
     *            the baseProvvigionaleStrategy to set
     */
    public void setBaseProvvigionaleStrategy(BaseProvvigionaleStrategy baseProvvigionaleStrategy) {
        this.baseProvvigionaleStrategy = baseProvvigionaleStrategy;
    }

    /**
     * @param listino
     *            the listino to set
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

}
