package it.eurotn.panjea.contabilita.service.exception;

import java.util.ArrayList;
import java.util.List;

public class AreeContabiliDuplicateException extends Exception {

	private static final long serialVersionUID = 6131158490423785634L;

	private List<AreaContabileDuplicateException> areeContabiliDuplicateExceptions;

	/**
	 * Costruttore.
	 */
	public AreeContabiliDuplicateException() {
		super();
		this.areeContabiliDuplicateExceptions = new ArrayList<AreaContabileDuplicateException>();
	}

	/**
	 * Aggiunge una {@link AreaContabileDuplicateException} alla lista.
	 * 
	 * @param areaContabileDuplicateException
	 *            exception da aggiungere
	 */
	public void add(AreaContabileDuplicateException areaContabileDuplicateException) {
		this.areeContabiliDuplicateExceptions.add(areaContabileDuplicateException);
	}

	/**
	 * Aggiunge tutte le eccezioni contenute in una {@link AreeContabiliDuplicateException}.
	 * 
	 * @param areeContabiliDuplicateException
	 *            exception da aggiungere
	 */
	public void add(AreeContabiliDuplicateException areeContabiliDuplicateException) {
		this.areeContabiliDuplicateExceptions.addAll(areeContabiliDuplicateException
				.getAreeContabiliDuplicateException());
	}

	/**
	 * @return the areeContabiliDuplicateException
	 */
	public List<AreaContabileDuplicateException> getAreeContabiliDuplicateException() {
		return areeContabiliDuplicateExceptions;
	}
}
