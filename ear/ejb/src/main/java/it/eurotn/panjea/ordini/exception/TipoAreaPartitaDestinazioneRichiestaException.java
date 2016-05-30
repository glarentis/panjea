package it.eurotn.panjea.ordini.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

public class TipoAreaPartitaDestinazioneRichiestaException extends Exception {

	private static final long serialVersionUID = -2547085517793968244L;

	private TipoDocumento tipoDocumentoOrdineDaEvadere;

	private TipoDocumento tipoDocumentoDestinazione;

	/**
	 * Costruttore.
	 * 
	 * @param tipoDocumentoOrdineDaEvadere
	 *            tipo documento ordine da evadere
	 * @param tipoDocumentoDestinazione
	 *            tipo documento magazzino di destinazione
	 */
	public TipoAreaPartitaDestinazioneRichiestaException(final TipoDocumento tipoDocumentoOrdineDaEvadere,
			final TipoDocumento tipoDocumentoDestinazione) {
		super();
		this.tipoDocumentoOrdineDaEvadere = tipoDocumentoOrdineDaEvadere;
		this.tipoDocumentoDestinazione = tipoDocumentoDestinazione;
	}

	/**
	 * @return Returns the tipoDocumentoDestinazione.
	 */
	public TipoDocumento getTipoDocumentoDestinazione() {
		return tipoDocumentoDestinazione;
	}

	/**
	 * @return Returns the tipoDocumentoOrdineDaEvadere.
	 */
	public TipoDocumento getTipoDocumentoOrdineDaEvadere() {
		return tipoDocumentoOrdineDaEvadere;
	}

}
