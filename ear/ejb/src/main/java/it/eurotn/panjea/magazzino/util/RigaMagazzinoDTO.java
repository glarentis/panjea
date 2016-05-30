package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public abstract class RigaMagazzinoDTO implements Serializable {

    private static final long serialVersionUID = 9000099624360534875L;

    private Integer id;
    private String descrizione;
    private boolean rigaCollegata;
    private Integer livello;
    private Integer numeroDecimaliPrezzo;
    private Integer numeroDecimaliQta;

    /**
     * Default constructor.
     */
    public RigaMagazzinoDTO() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RigaMagazzinoDTO other = (RigaMagazzinoDTO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the livello
     */
    public Integer getLivello() {
        return livello;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliQta
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     *
     * @return istanza di riga magazzino
     */
    public abstract RigaMagazzino getRigaMagazzino();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @return the rigaCollegata
     */
    public boolean isRigaCollegata() {
        return rigaCollegata;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param livello
     *            the livello to set
     */
    public void setLivello(Integer livello) {
        this.livello = livello;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param rigaCollegata
     *            the rigaCollegata to set
     */
    public void setRigaCollegata(boolean rigaCollegata) {
        this.rigaCollegata = rigaCollegata;
    }

}
