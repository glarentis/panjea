package it.eurotn.panjea.tesoreria.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

public class ContoCassaAssenteException extends RuntimeException {

	private static final long serialVersionUID = 8721187957867460804L;

	private TipoDocumento tipoDocumento;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public ContoCassaAssenteException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param tipoDocumento
	 *            il tipoDocumento dove è definito il conto
	 */
	public ContoCassaAssenteException(final TipoDocumento tipoDocumento) {
		super();
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
