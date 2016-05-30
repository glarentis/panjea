package it.eurotn.panjea.corrispettivi.domain;

import java.io.Serializable;
import java.util.List;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

/**
 * @author fattazzo
 */
public class GiornoCorrispettivo implements Serializable {

    private static final long serialVersionUID = -8567084348610733807L;

    private boolean feriale;

    private int numero;

    private List<Documento> documenti;

    private Corrispettivo corrispettivo;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GiornoCorrispettivo other = (GiornoCorrispettivo) obj;
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }

    /**
     * @return corrispettivo
     */
    public Corrispettivo getCorrispettivo() {
        return corrispettivo;
    }

    /**
     * @return the documenti
     */
    public List<Documento> getDocumenti() {
        return documenti;
    }

    /**
     * @return numero
     */
    public int getNumero() {
        return numero;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.numero;
        return hash;
    }

    /**
     * @return feriale
     */
    public boolean isFeriale() {
        return feriale;
    }

    /**
     * @param corrispettivo
     *            the corrispettivo to set
     */
    public void setCorrispettivo(Corrispettivo corrispettivo) {
        this.corrispettivo = corrispettivo;
    }

    /**
     * @param documenti
     *            the documenti to set
     */
    public void setDocumenti(List<Documento> documenti) {
        this.documenti = documenti;
    }

    /**
     * @param feriale
     *            the feriale to set
     */
    public void setFeriale(boolean feriale) {
        this.feriale = feriale;
    }

    /**
     * @param numero
     *            the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }
}
