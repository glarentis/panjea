package it.eurotn.panjea.anagrafica.service.exception;

public class TipoDocumentoBaseException extends Exception {

	private static final long serialVersionUID = 3376891463227432087L;

	private String[] tipiMancanti;

	/**
	 * Costruttore.
	 * 
	 * @param tipiMancanti
	 *            tipi operazioni mancanti
	 */
	public TipoDocumentoBaseException(final String[] tipiMancanti) {
		super();
		this.tipiMancanti = tipiMancanti;
	}

	/**
	 * @return the tipiMancanti
	 */
	public String[] getTipiMancanti() {
		return tipiMancanti;
	}
}
