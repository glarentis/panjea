package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.eurotn.entity.EntityBase;

/**
 * @author leonardo
 */
@Entity
@Table(name = "maga_scaglioni_listini_note")
public class ScaglioneListinoNota extends EntityBase {

    private static final long serialVersionUID = 6138748001656255833L;

    @Column(length = 4000, name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ScaglioneListino scaglioneListino;

    /**
     * Costruttore.
     */
    public ScaglioneListinoNota() {
        super();
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the scaglioneListino
     */
    public ScaglioneListino getScaglioneListino() {
        return scaglioneListino;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param scaglioneListino
     *            the scaglioneListino to set
     */
    public void setScaglioneListino(ScaglioneListino scaglioneListino) {
        this.scaglioneListino = scaglioneListino;
    }

}
