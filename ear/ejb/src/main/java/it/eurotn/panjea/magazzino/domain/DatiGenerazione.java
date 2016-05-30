package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Embeddable
public class DatiGenerazione implements Serializable {

    public enum TipoGenerazione {
        FATTURAZIONE, EVASIONE, ESTERNO, TUTTI, ATON
    }

    private static final long serialVersionUID = -8903007663544295340L;

    private String utente;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataGenerazione;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCreazione;

    @Column(length = 60)
    private String note;

    private TipoGenerazione tipoGenerazione;

    private boolean esportato;

    /**
     * Costruttore.
     */
    public DatiGenerazione() {
        super();
        init();
    }

    /**
     * Costruttore.
     * 
     * @param utente
     *            utente che effettua la generazione
     * @param dataGenerazione
     *            data di generazione
     * @param dataCreazione
     *            data di creazione
     * @param note
     *            note
     * @param esportato
     *            esportato
     * @param tipoGenerazione
     *            tipo di generazione
     */
    public DatiGenerazione(final String utente, final Date dataGenerazione, final Date dataCreazione, final String note,
            final boolean esportato, final TipoGenerazione tipoGenerazione) {
        super();
        this.utente = utente;
        this.dataGenerazione = dataGenerazione;
        this.dataCreazione = dataCreazione;
        this.note = note;
        this.esportato = esportato;
        this.tipoGenerazione = tipoGenerazione;
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
        DatiGenerazione other = (DatiGenerazione) obj;
        if (dataCreazione == null) {
            if (other.dataCreazione != null) {
                return false;
            }
        } else if (!dataCreazione.equals(other.dataCreazione)) {
            return false;
        }
        if (dataGenerazione == null) {
            if (other.dataGenerazione != null) {
                return false;
            }
        } else if (!dataGenerazione.equals(other.dataGenerazione)) {
            return false;
        }
        if (tipoGenerazione != other.tipoGenerazione) {
            return false;
        }
        if (utente == null) {
            if (other.utente != null) {
                return false;
            }
        } else if (!utente.equals(other.utente)) {
            return false;
        }
        return true;
    }

    /**
     * @return dataCreazione
     */
    public Date getDataCreazione() {
        return dataCreazione;
    }

    /**
     * @return dataGenerazione
     */
    public Date getDataGenerazione() {
        return dataGenerazione;
    }

    /**
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the tipoGenerazione
     */
    public TipoGenerazione getTipoGenerazione() {
        return tipoGenerazione;
    }

    /**
     * @return utente
     */
    public String getUtente() {
        return utente;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataCreazione == null) ? 0 : dataCreazione.hashCode());
        result = prime * result + ((dataGenerazione == null) ? 0 : dataGenerazione.hashCode());
        result = prime * result + ((tipoGenerazione == null) ? 0 : tipoGenerazione.hashCode());
        result = prime * result + ((utente == null) ? 0 : utente.hashCode());
        return result;
    }

    /**
     * Inizializza i valori di default.
     */
    private void init() {
        esportato = false;
    }

    /**
     * @return esportato
     */
    public boolean isEsportato() {
        return esportato;
    }

    /**
     * @param dataCreazione
     *            the dataCreazione to set
     */
    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    /**
     * @param dataGenerazione
     *            the dataGenerazione to set
     */
    public void setDataGenerazione(Date dataGenerazione) {
        this.dataGenerazione = dataGenerazione;
    }

    /**
     * @param esportato
     *            the esportato to set
     */
    public void setEsportato(boolean esportato) {
        this.esportato = esportato;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param tipoGenerazione
     *            the tipoGenerazione to set
     */
    public void setTipoGenerazione(TipoGenerazione tipoGenerazione) {
        this.tipoGenerazione = tipoGenerazione;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(String utente) {
        this.utente = utente;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("DatiFatturazione[");
        buffer.append("dataCreazione = ").append(dataCreazione);
        buffer.append(" dataGenerazione = ").append(dataGenerazione);
        buffer.append(" note = ").append(note);
        buffer.append(" utente = ").append(utente);
        buffer.append("]");
        return buffer.toString();
    }
}
