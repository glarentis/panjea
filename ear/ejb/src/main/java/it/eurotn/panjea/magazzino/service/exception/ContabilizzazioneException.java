package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiDuplicatiException;
import it.eurotn.panjea.contabilita.service.exception.AreeContabiliDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ContabilizzazioneException extends Exception {

	private static final long serialVersionUID = -7591775245074410266L;

	/**
	 * @uml.property name="sottoContiContabiliAssentiException"
	 * @uml.associationEnd
	 */
	private SottoContiContabiliAssentiException sottoContiContabiliAssentiException;

	/**
	 * @uml.property name="contiEntitaAssentiException"
	 * @uml.associationEnd
	 */
	private ContiEntitaAssentiException contiEntitaAssentiException;

	/**
	 * @uml.property name="areeContabiliDuplicateException"
	 * @uml.associationEnd
	 */
	private AreeContabiliDuplicateException areeContabiliDuplicateException;

	private DocumentiDuplicatiException documentiDuplicatiException;

	/**
	 * Costruttore.
	 */
	public ContabilizzazioneException() {
		super();
	}

	/**
	 * Aggiunge il contenuto di una {@link ContabilizzazioneException}.
	 * 
	 * @param contabilizzazioneException
	 *            exception
	 */
	public void add(ContabilizzazioneException contabilizzazioneException) {

		if (contabilizzazioneException.getContiEntitaAssentiException() != null) {
			if (this.contiEntitaAssentiException == null) {
				this.contiEntitaAssentiException = contabilizzazioneException.getContiEntitaAssentiException();
			} else {
				this.contiEntitaAssentiException.add(contabilizzazioneException.getContiEntitaAssentiException());
			}
		}

		if (contabilizzazioneException.getSottoContiContabiliAssentiException() != null) {
			if (this.sottoContiContabiliAssentiException == null) {
				this.sottoContiContabiliAssentiException = contabilizzazioneException
						.getSottoContiContabiliAssentiException();
			} else {
				this.sottoContiContabiliAssentiException.add(contabilizzazioneException
						.getSottoContiContabiliAssentiException());
			}
		}

		if (contabilizzazioneException.getDocumentiDuplicatiException() != null) {
			if (this.documentiDuplicatiException == null) {
				this.documentiDuplicatiException = contabilizzazioneException.getDocumentiDuplicatiException();
			} else {
				this.documentiDuplicatiException.add(contabilizzazioneException.getDocumentiDuplicatiException());
			}
		}

		if (contabilizzazioneException.getAreeContabiliDuplicateException() != null) {
			if (this.areeContabiliDuplicateException == null) {
				this.areeContabiliDuplicateException = contabilizzazioneException.getAreeContabiliDuplicateException();
			} else {
				this.areeContabiliDuplicateException.add(contabilizzazioneException
						.getAreeContabiliDuplicateException());
			}
		}
	}

	/**
	 * @return the areeContabiliDuplicateException
	 * @uml.property name="areeContabiliDuplicateException"
	 */
	public AreeContabiliDuplicateException getAreeContabiliDuplicateException() {
		return areeContabiliDuplicateException;
	}

	/**
	 * @return the contiEntitaAssentiException
	 * @uml.property name="contiEntitaAssentiException"
	 */
	public ContiEntitaAssentiException getContiEntitaAssentiException() {
		return contiEntitaAssentiException;
	}

	/**
	 * @return the documentiDuplicatiException
	 */
	public DocumentiDuplicatiException getDocumentiDuplicatiException() {
		return documentiDuplicatiException;
	}

	/**
	 * @return the sottoContiContabiliAssentiException
	 * @uml.property name="sottoContiContabiliAssentiException"
	 */
	public SottoContiContabiliAssentiException getSottoContiContabiliAssentiException() {
		return sottoContiContabiliAssentiException;
	}

	/**
	 * @return <code>true</code> se non contiene nessuna eccezione, <code>false</code> altrimenti.
	 */
	public boolean isEmpty() {
		boolean empty = (this.getContiEntitaAssentiException() == null
				&& this.getSottoContiContabiliAssentiException() == null && this.getAreeContabiliDuplicateException() == null);
		return empty;
	}

	/**
	 * @param areeContabiliDuplicateException
	 *            the areeContabiliDuplicateException to set
	 * @uml.property name="areeContabiliDuplicateException"
	 */
	public void setAreeContabiliDuplicateException(AreeContabiliDuplicateException areeContabiliDuplicateException) {
		this.areeContabiliDuplicateException = areeContabiliDuplicateException;
	}

	/**
	 * @param contiEntitaAssentiException
	 *            the contiEntitaAssentiException to set
	 * @uml.property name="contiEntitaAssentiException"
	 */
	public void setContiEntitaAssentiException(ContiEntitaAssentiException contiEntitaAssentiException) {
		this.contiEntitaAssentiException = contiEntitaAssentiException;
	}

	/**
	 * @param documentiDuplicatiException
	 *            the documentiDuplicatiException to set
	 */
	public void setDocumentiDuplicatiException(DocumentiDuplicatiException documentiDuplicatiException) {
		this.documentiDuplicatiException = documentiDuplicatiException;
	}

	/**
	 * @param sottoContiContabiliAssentiException
	 *            the sottoContiContabiliAssentiException to set
	 * @uml.property name="sottoContiContabiliAssentiException"
	 */
	public void setSottoContiContabiliAssentiException(
			SottoContiContabiliAssentiException sottoContiContabiliAssentiException) {
		this.sottoContiContabiliAssentiException = sottoContiContabiliAssentiException;
	}
}
