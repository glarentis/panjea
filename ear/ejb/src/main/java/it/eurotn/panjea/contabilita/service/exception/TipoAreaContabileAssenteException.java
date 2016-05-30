package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

public class TipoAreaContabileAssenteException extends RuntimeException {

	private static final long serialVersionUID = -3074606388070002655L;

	private TipoDocumento tipoDocumento;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param tipoDocumento
	 *            tipoDocumento del quale non ho il tipoAreaContabile
	 */
	public TipoAreaContabileAssenteException(final TipoDocumento tipoDocumento) {
		super();
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return Returns the tipoDocumento.
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

}
