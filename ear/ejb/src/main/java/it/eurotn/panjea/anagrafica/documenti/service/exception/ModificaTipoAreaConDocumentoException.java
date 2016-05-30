/**
 * 
 */
package it.eurotn.panjea.anagrafica.documenti.service.exception;

/**
 * Eccezione sollevata quando la modifica di un tipo area risulta impossibile a causa del fatto che ci sono documenti
 * collegati di cui verrebbe alterata la struttura.
 * 
 * @author Leonardo
 * 
 */
public class ModificaTipoAreaConDocumentoException extends Exception {

	private static final long serialVersionUID = -6221514575535368208L;

	/**
	 * 
	 */
	public ModificaTipoAreaConDocumentoException() {
		super();
	}

	/**
	 * @param message
	 */
	public ModificaTipoAreaConDocumentoException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ModificaTipoAreaConDocumentoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ModificaTipoAreaConDocumentoException(Throwable cause) {
		super(cause);
	}

}
