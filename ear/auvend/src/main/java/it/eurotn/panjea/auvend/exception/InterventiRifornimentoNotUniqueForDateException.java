/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.List;

/**
 * Eccezione che indica la presenza di più documenti di intervento rifornimento per la stessa data e per lo stesso
 * {@link Deposito} sorgente.
 * 
 * @author adriano
 * @version 1.0, 16/gen/2009
 * 
 */
public class InterventiRifornimentoNotUniqueForDateException extends Exception {

	private List<AreaMagazzino> areeMagazzinoEsistenti;

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public InterventiRifornimentoNotUniqueForDateException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param areeMagazzinoEsistenti
	 *            aree magazzino
	 */
	public InterventiRifornimentoNotUniqueForDateException(final String message,
			final List<AreaMagazzino> areeMagazzinoEsistenti) {
		this(message);
		this.areeMagazzinoEsistenti = areeMagazzinoEsistenti;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public InterventiRifornimentoNotUniqueForDateException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public InterventiRifornimentoNotUniqueForDateException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @return Returns the areeMagazzinoEsistenti.
	 */
	public List<AreaMagazzino> getAreeMagazzinoEsistenti() {
		return areeMagazzinoEsistenti;
	}

	/**
	 * @param areeMagazzinoEsistenti
	 *            The areeMagazzinoEsistenti to set.
	 */
	public void setAreeMagazzinoEsistenti(List<AreaMagazzino> areeMagazzinoEsistenti) {
		this.areeMagazzinoEsistenti = areeMagazzinoEsistenti;
	}

}
