/**
 * 
 */
package it.eurotn.panjea.anagrafica.documenti.service.exception;

/**
 * Exception generata dal metodo salvaCodiceIva nel caso in cui viene riscontrata una referenza circolare tra codici iva
 * 
 * @author Leonardo
 */
public class CodiceIvaRicorsivoException extends Exception {

	private static final long serialVersionUID = -2400853803073346968L;

	public CodiceIvaRicorsivoException() {
		super("--> Errore, trovata una referenza circolare tra codici iva");
	}

	public CodiceIvaRicorsivoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodiceIvaRicorsivoException(String message) {
		super(message);
	}

	public CodiceIvaRicorsivoException(Throwable cause) {
		super(cause);
	}

}
