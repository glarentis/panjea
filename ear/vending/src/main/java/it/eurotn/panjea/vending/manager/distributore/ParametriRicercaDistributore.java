package it.eurotn.panjea.vending.manager.distributore;

import java.io.Serializable;

import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;

public class ParametriRicercaDistributore extends ParametriRicercaArticoliMI implements Serializable {

    private static final long serialVersionUID = 3198222535668392319L;

    private Integer idModello;

    private Integer idTipoModello;

    private Integer idCliente;

    private Integer idSedeCliente;

    private String descrizioneModello;

    /**
     * @return the descrizioneModello
     */
    public final String getDescrizioneModello() {
        return descrizioneModello;
    }

    /**
     * @return Returns the idCliente.
     */
    public final Integer getIdCliente() {
        return idCliente;
    }

    /**
     * @return the idModello
     */
    public final Integer getIdModello() {
        return idModello;
    }

    /**
     * @return Returns the idSedeCliente.
     */
    public final Integer getIdSedeCliente() {
        return idSedeCliente;
    }

    /**
     * @return the idTipoModello
     */
    public final Integer getIdTipoModello() {
        return idTipoModello;
    }

    /**
     * @param descrizioneModello
     *            the descrizioneModello to set
     */
    public final void setDescrizioneModello(String descrizioneModello) {
        this.descrizioneModello = descrizioneModello;
    }

    /**
     * @param idCliente
     *            The idCliente to set.
     */
    public final void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @param idModello
     *            the idModello to set
     */
    public final void setIdModello(Integer idModello) {
        this.idModello = idModello;
    }

    /**
     * @param idSedeCliente
     *            The idSedeCliente to set.
     */
    public final void setIdSedeCliente(Integer idSedeCliente) {
        this.idSedeCliente = idSedeCliente;
    }

    /**
     * @param idTipoModello
     *            the idTipoModello to set
     */
    public final void setIdTipoModello(Integer idTipoModello) {
        this.idTipoModello = idTipoModello;
    }

}
