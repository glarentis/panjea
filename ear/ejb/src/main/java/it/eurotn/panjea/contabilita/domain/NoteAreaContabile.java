/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import java.io.Serializable;

/**
 * @author fattazzo
 *
 */
public class NoteAreaContabile implements Serializable {

	private static final long serialVersionUID = 8498767819596678093L;

	private String noteSede = null;

	private String noteEntita = null;

	/**
	 * @return the noteEntita
	 */
	public String getNoteEntita() {
		return noteEntita;
	}

	/**
	 * @return the noteSede
	 */
	public String getNoteSede() {
		return noteSede;
	}

	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		boolean emptyNoteSede = false;
		boolean emptyNoteEntita = false;
		emptyNoteSede = (noteSede == null || noteSede.isEmpty());
		emptyNoteEntita = (noteEntita == null || noteEntita.isEmpty());
		return (emptyNoteEntita && emptyNoteSede);
	}

	/**
	 * @param noteEntita
	 *            the noteEntita to set
	 */
	public void setNoteEntita(String noteEntita) {
		this.noteEntita = noteEntita;
	}

	/**
	 * @param noteSede
	 *            the noteSede to set
	 */
	public void setNoteSede(String noteSede) {
		this.noteSede = noteSede;
	}

}
