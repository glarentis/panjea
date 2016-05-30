package it.eurotn.panjea.preventivi.domain.documento;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoNote;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "prev_area_preventivo_note")
@Audited
public class AreaPreventivoNote extends EntityBase implements IAreaDocumentoNote {
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
	@Override
	public String getNotePiede() {
		return notePiede;
	}

	/**
	 * @return the noteTestata
	 * @uml.property name="noteTestata"
	 */
	@Override
	public String getNoteTestata() {
		return noteTestata;
	}

	/**
	 * 
	 * @return ture if empty
	 */
	@Override
	public boolean isEmpty() {
		return noteTestata.isEmpty() && notePiede.isEmpty();
	}

	/**
	 * @param notePiede
	 *            the notePiede to set
	 * @uml.property name="notePiede"
	 */
	@Override
	public void setNotePiede(String notePiede) {
		this.notePiede = notePiede;
	}

	/**
	 * @param noteTestata
	 *            the noteTestata to set
	 * @uml.property name="noteTestata"
	 */
	@Override
	public void setNoteTestata(String noteTestata) {
		this.noteTestata = noteTestata;
	}
}
