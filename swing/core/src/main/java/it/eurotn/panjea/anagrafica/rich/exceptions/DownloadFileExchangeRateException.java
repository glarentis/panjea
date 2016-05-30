/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.exceptions;

/**
 * Eccezione sollevata se ci sono problemi durante il download del file dei tassi di cambio.
 * 
 * @author fattazzo
 * 
 */
public class DownloadFileExchangeRateException extends RuntimeException {

	private static final long serialVersionUID = 1188972052411692071L;

	/**
	 * Costruttore.
	 * 
	 * @param arg0
	 *            messaggio
	 */
	public DownloadFileExchangeRateException(final String arg0) {
		super(arg0);
	}
}
