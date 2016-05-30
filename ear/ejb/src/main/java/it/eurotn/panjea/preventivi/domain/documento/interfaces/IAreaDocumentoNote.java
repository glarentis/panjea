package it.eurotn.panjea.preventivi.domain.documento.interfaces;

public interface IAreaDocumentoNote {

	/**
	 * 
	 * @return note piede
	 */
	String getNotePiede();

	/**
	 * 
	 * @return note testata
	 */
	String getNoteTestata();

	/**
	 * 
	 * @return true se vuoto
	 */
	boolean isEmpty();

	/**
	 * 
	 * @param notePiede
	 *            note piede
	 */
	void setNotePiede(String notePiede);

	/**
	 * 
	 * @param noteTestata
	 *            note testata
	 */
	void setNoteTestata(String noteTestata);
}
