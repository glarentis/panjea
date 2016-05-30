package it.eurotn.panjea.magazzino.util.rigamagazzino.builder;

import java.util.ArrayList;
import java.util.Collection;

import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

public class RigheMagazzinoDTOResult extends ArrayList<RigaMagazzinoDTO> {

    private static final long serialVersionUID = 6456852498765451217L;

    private Integer numeroDecimaliQta;
    private Integer numeroDecimaliPrezzo;

    /**
     * Costruttore.
     */
    public RigheMagazzinoDTOResult() {
        super();
        numeroDecimaliQta = 0;
        numeroDecimaliPrezzo = 0;
    }

    /**
     * Costruttore.
     * 
     * @param righeDTO
     *            collection
     */
    public RigheMagazzinoDTOResult(final Collection<? extends RigaMagazzinoDTO> righeDTO) {
        super(righeDTO);
        numeroDecimaliQta = 0;
        numeroDecimaliPrezzo = 0;
    }

    /**
     * Costruttore.
     * 
     * @param initialCapacity
     *            initial capacity
     */
    public RigheMagazzinoDTOResult(final int initialCapacity) {
        super(initialCapacity);
        numeroDecimaliQta = 0;
        numeroDecimaliPrezzo = 0;
    }

    /**
     * @return Returns the numeroDecimaliPrezzo.
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return Returns the numeroDecimaliQta.
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            The numeroDecimaliPrezzo to set.
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliQta
     *            The numeroDecimaliQta to set.
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }
}
