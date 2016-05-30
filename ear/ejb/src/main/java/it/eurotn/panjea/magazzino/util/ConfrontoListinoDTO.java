package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fattazzo
 *
 */
public class ConfrontoListinoDTO implements Serializable {

    private static final long serialVersionUID = -5554728282913204467L;

    private List<RigaConfrontoListinoDTO> righeConfronto;

    {
        righeConfronto = new ArrayList<RigaConfrontoListinoDTO>();
    }

    /**
     * Costruttore.
     */
    public ConfrontoListinoDTO() {
        super();
    }

    /**
     * @return the righeConfronto
     */
    public List<RigaConfrontoListinoDTO> getRigheConfronto() {
        return righeConfronto;
    }

    /**
     * @param righeConfronto
     *            the righeConfronto to set
     */
    public void setRigheConfronto(List<RigaConfrontoListinoDTO> righeConfronto) {
        this.righeConfronto = righeConfronto;
    }
}
