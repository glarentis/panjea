/**
 *
 */
package it.eurotn.panjea.ordini.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

/**
 * @author leonardo
 */
public class TipoAreaOrdineAssenteException extends RuntimeException {

	private static final long serialVersionUID = 168323109658175317L;

	private TipoDocumento tipoDocumento;

	/**
	 * Costruttore.
	 */
	public TipoAreaOrdineAssenteException() {
		super();
	}

	/**
	 * @return Returns the tipoDocumento.
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
