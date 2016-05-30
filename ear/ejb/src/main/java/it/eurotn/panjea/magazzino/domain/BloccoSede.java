package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BloccoSede implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Indica se la sede risulta essere bloccata (vedi UCs 1581,1582,1583).
     */
    private boolean blocco;

    @Column(length = 300)
    private String noteBlocco;

    /**
     * Costruttore.
     */
    public BloccoSede() {
        super();
        this.blocco = Boolean.FALSE;
    }

    /**
     * @return the noteBlocco
     */
    public String getNoteBlocco() {
        return noteBlocco;
    }

    /**
     * @return the blocco
     */
    public boolean isBlocco() {
        return blocco;
    }

    /**
     * @param blocco
     *            the blocco to set
     */
    public void setBlocco(boolean blocco) {
        this.blocco = blocco;
    }

    /**
     * @param noteBlocco
     *            the noteBlocco to set
     */
    public void setNoteBlocco(String noteBlocco) {
        this.noteBlocco = noteBlocco;
    }
}
