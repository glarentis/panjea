package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;

import it.eurotn.panjea.magazzino.domain.Listino;

/**
 * @author fattazzo
 *
 */
public class TipoConfronto implements Serializable {

    public enum EConfronto {
        LISTINO, ULTIMO_COSTO_AZIENDALE
    }

    private static final long serialVersionUID = -7809807632554817695L;

    private EConfronto confronto;

    private Listino listino;

    /**
     * @return the confronto
     */
    public EConfronto getConfronto() {
        return confronto;
    }

    /**
     * @return the listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @param confronto
     *            the confronto to set
     */
    public void setConfronto(EConfronto confronto) {
        this.confronto = confronto;
    }

    /**
     * @param listino
     *            the listino to set
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

}
