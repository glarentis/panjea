package it.eurotn.panjea.anagrafica.documenti.service.exception;

import java.util.ArrayList;
import java.util.List;

public class DocumentiDuplicatiException extends Exception {

	private static final long serialVersionUID = 382663627047566846L;

	private final List<DocumentoDuplicateException> areeContabiliDuplicateExceptions;

	/**
	 * Costruttore.
	 */
	public DocumentiDuplicatiException() {
		super();
		this.areeContabiliDuplicateExceptions = new ArrayList<DocumentoDuplicateException>();
	}

	/**
	 * Aggiunge tutte le eccezioni contenute in una {@link DocumentiDuplicatiException}.
	 * 
	 * @param documentiDuplicatiException
	 *            exception da aggiungere
	 */
	public void add(DocumentiDuplicatiException documentiDuplicatiException) {
		this.areeContabiliDuplicateExceptions.addAll(documentiDuplicatiException.getDocumentiDuplicatiException());
	}

	/**
	 * Aggiunge una {@link DocumentoDuplicateException} alla lista.
	 * 
	 * @param areaContabileDuplicateException
	 *            exception da aggiungere
	 */
	public void add(DocumentoDuplicateException areaContabileDuplicateException) {
		this.areeContabiliDuplicateExceptions.add(areaContabileDuplicateException);
	}

	/**
	 * @return the areeContabiliDuplicateException
	 */
	public List<DocumentoDuplicateException> getDocumentiDuplicatiException() {
		return areeContabiliDuplicateExceptions;
	}
}
