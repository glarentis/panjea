package it.eurotn.panjea.ordini.domain.documento;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 */
@Entity
@Table(name = "ordi_area_ordine_note")
@Audited
public class AreaOrdineNote extends EntityBase {

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="noteTestata"
	 */
	@Column(length = 300)
	private String noteTestata;

	/**
	 * @uml.property name="notePiede"
	 */
	@Column(length = 300)
	private String notePiede;

	{
		notePiede = "";
		noteTestata = "";
	}

	/**
	 * @return the notePiede
	 * @uml.property name="notePiede"
	 */
	public String getNotePiede() {
		return notePiede;
	}

	/**
	 * @return the noteTestata
	 * @uml.property name="noteTestata"
	 */
	public String getNoteTestata() {
		return noteTestata;
	}

	/**
	 * @return <code>true</code> se non esistono note per la testata e per il piede
	 */
	public boolean isEmpty() {
		return noteTestata.isEmpty() && notePiede.isEmpty();
	}

	/**
	 * @param notePiede
	 *            the notePiede to set
	 * @uml.property name="notePiede"
	 */
	public void setNotePiede(String notePiede) {
		this.notePiede = notePiede;
	}

	/**
	 * @param noteTestata
	 *            the noteTestata to set
	 * @uml.property name="noteTestata"
	 */
	public void setNoteTestata(String noteTestata) {
		this.noteTestata = noteTestata;
	}

}
