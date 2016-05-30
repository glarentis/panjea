package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

public class LayoutStampaPredefinitoNonPressenteException extends Exception {

	private static final long serialVersionUID = -1040504279209161619L;

	private TipoDocumento tipoDocumento;

	/**
	 * Costruttore.
	 * 
	 * @param tipoDocumento
	 *            tipo documento senza il layout di stampa predefinito
	 * @param message
	 *            messaggio
	 */
	public LayoutStampaPredefinitoNonPressenteException(final TipoDocumento tipoDocumento, final String message) {
		super(message);
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return Returns the tipoDocumento.
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
}
