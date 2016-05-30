/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.List;

/**
 * Exception rilanciata se gli attributi chiave di dominio di {@link Documento} di {@link AreaMagazzino} identificano
 * altri documenti.
 * 
 * @author adriano
 * @version 1.0, 02/set/2008
 */
public class DocumentiEsistentiPerAreaMagazzinoException extends Exception {

	private static final long serialVersionUID = 3416484972684725188L;

	/**
	 * @uml.property name="documenti"
	 */
	private List<Documento> documenti;

	/**
	 * Costruttore.
	 * 
	 * @param documenti
	 *            documenti esistenti
	 */
	public DocumentiEsistentiPerAreaMagazzinoException(final List<Documento> documenti) {
		super();
		this.documenti = documenti;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param documenti
	 *            documenti esistenti
	 */
	public DocumentiEsistentiPerAreaMagazzinoException(final String message, final List<Documento> documenti) {
		super(message);
		this.documenti = documenti;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param throwable
	 *            eccezione
	 * @param documenti
	 *            documenti esistenti
	 */
	public DocumentiEsistentiPerAreaMagazzinoException(final String message, final Throwable throwable,
			final List<Documento> documenti) {
		super(message, throwable);
		this.documenti = documenti;
	}

	/**
	 * Costruttore.
	 * 
	 * @param throwable
	 *            eccezione
	 * @param documenti
	 *            documenti esistenti
	 */
	public DocumentiEsistentiPerAreaMagazzinoException(final Throwable throwable, final List<Documento> documenti) {
		super(throwable);
		this.documenti = documenti;
	}

	/**
	 * @return Returns the documenti.
	 * @uml.property name="documenti"
	 */
	public List<Documento> getDocumenti() {
		return documenti;
	}

}
