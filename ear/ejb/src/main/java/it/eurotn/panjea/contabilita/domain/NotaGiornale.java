/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Contiene la nota di invalidamento del giornale.
 * 
 * @author fattazzo
 * @version 1.0, 05/ott/07
 */
@Entity
@Audited
@Table(name = "cont_libri_giornale_note")
@NamedQueries({ @NamedQuery(name = "NotaGiornale.cancellaNoteByGiornale", query = "delete from NotaGiornale g where g.giornale.id = :paramIdGiornale") })
public class NotaGiornale extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = -3033003699496247762L;

	@ManyToOne
	private Giornale giornale;

	private ENoteGiornale tipoNotaGiornale;

	private String note;

	private String noteInterne;

	/**
	 * @return giornale
	 */
	public Giornale getGiornale() {
		return giornale;
	}

	/**
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return noteInterne
	 */
	public String getNoteInterne() {
		return noteInterne;
	}

	/**
	 * @return tipoNotaGiornale
	 */
	public ENoteGiornale getTipoNotaGiornale() {
		return tipoNotaGiornale;
	}

	/**
	 * @param giornale
	 *            the giornale to set
	 */
	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param noteInterne
	 *            the noteInterne to set
	 */
	public void setNoteInterne(String noteInterne) {
		this.noteInterne = noteInterne;
	}

	/**
	 * @param tipoNotaGiornale
	 *            the tipoNotaGiornale to set
	 */
	public void setTipoNotaGiornale(ENoteGiornale tipoNotaGiornale) {
		this.tipoNotaGiornale = tipoNotaGiornale;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("NoteGiornale[");
		buffer.append(" note = ").append(note);
		buffer.append(" noteGiornale = ").append(tipoNotaGiornale);
		buffer.append("]");
		return buffer.toString();
	}

}
