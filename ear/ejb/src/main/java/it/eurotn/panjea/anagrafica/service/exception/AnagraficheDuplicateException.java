package it.eurotn.panjea.anagrafica.service.exception;

import java.util.ArrayList;
import java.util.List;

public class AnagraficheDuplicateException extends Exception {

	private static final long serialVersionUID = 382663627047566846L;

	private final List<AnagraficheDuplicateException> anagraficheDuplicateExceptions;

	/**
	 * Costruttore.
	 */
	public AnagraficheDuplicateException() {
		super();
		this.anagraficheDuplicateExceptions = new ArrayList<AnagraficheDuplicateException>();
	}

	/**
	 * Aggiunge tutte le eccezioni contenute in una {@link AnagraficheDuplicateException}.
	 * 
	 * @param anagraficheDuplicate
	 *            exception da aggiungere
	 */
	public void add(AnagraficheDuplicateException anagraficheDuplicate) {
		this.anagraficheDuplicateExceptions.addAll(anagraficheDuplicate.getAnagraficheDuplicateException());
	}

	/**
	 * @return the areeContabiliDuplicateException
	 */
	public List<AnagraficheDuplicateException> getAnagraficheDuplicateException() {
		return anagraficheDuplicateExceptions;
	}
}
