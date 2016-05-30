package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import java.util.List;

public class TipiDocumentiFatturazionePAAssentiException extends RuntimeException {

	private static final long serialVersionUID = -9158513340585734632L;

	private List<TipoDocumento> tipiDoc;

	/**
	 * Costruttore.
	 *
	 * @param tipiDoc
	 *            tipi documenti senza tipo documento fatturazione PA impostato
	 */
	public TipiDocumentiFatturazionePAAssentiException(final List<TipoDocumento> tipiDoc) {
		super();
		this.tipiDoc = tipiDoc;
	}

	/**
	 * @return the tipiDoc
	 */
	public List<TipoDocumento> getTipiDoc() {
		return tipiDoc;
	}
}
