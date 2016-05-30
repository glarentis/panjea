package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DichiarazioneIntento implements Serializable {

    private static final long serialVersionUID = -7850763473190056341L;

    @Temporal(TemporalType.DATE)
    private Date dataScadenza;

    @Column(length = 100)
    private String testo;

    /**
     * Indica se addebitare l'articolo configurato creando una riga articolo.
     */
    private boolean addebito;

    /**
     * Cancella la data scadenza e il testo della dicharazione di intento.
     */
    public void clear() {
        this.dataScadenza = null;
        this.testo = null;
        this.addebito = true;
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
        DichiarazioneIntento other = (DichiarazioneIntento) obj;
        if (addebito != other.addebito) {
            return false;
        }
        if (dataScadenza == null) {
            if (other.dataScadenza != null) {
                return false;
            }
        } else if (!dataScadenza.equals(other.dataScadenza)) {
            return false;
        }
        if (testo == null) {
            if (other.testo != null) {
                return false;
            }
        } else if (!testo.equals(other.testo)) {
            return false;
        }
        return true;
    }

    /**
     * @return Returns the dataScadenza.
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * @return Returns the testo.
     */
    public String getTesto() {
        return testo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (addebito ? 1231 : 1237);
        result = prime * result + ((dataScadenza == null) ? 0 : dataScadenza.hashCode());
        result = prime * result + ((testo == null) ? 0 : testo.hashCode());
        return result;
    }

    /**
     * @return the addebito
     */
    public boolean isAddebito() {
        return addebito;
    }

    /**
     * @param addebito
     *            the addebito to set
     */
    public void setAddebito(boolean addebito) {
        this.addebito = addebito;
    }

    /**
     * @param dataScadenza
     *            The dataScadenza to set.
     */
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * @param testo
     *            The testo to set.
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    @Override
    public String toString() {
        return "dataScadenza=" + dataScadenza + ", testo=" + testo + ", addebito=" + (addebito ? "SI" : "NO");
    }

}
