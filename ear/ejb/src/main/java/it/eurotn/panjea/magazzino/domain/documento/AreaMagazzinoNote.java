package it.eurotn.panjea.magazzino.domain.documento;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AreaMagazzinoNote implements Serializable {

    private static final long serialVersionUID = 1L;

    {
        noteTestata = "";
    }

    @Column(length = 300)
    private String noteTestata;

    /**
     * @return noteTestata
     */
    public String getNoteTestata() {
        return noteTestata;
    }

    /**
     * @return <code>true</code> se non ci sono note di testata
     */
    public boolean isEmpty() {
        return noteTestata == null ? true : noteTestata.isEmpty();
    }

    /**
     * @param noteTestata
     *            the noteTestata to set
     */
    public void setNoteTestata(String noteTestata) {
        this.noteTestata = noteTestata;
    }

}
