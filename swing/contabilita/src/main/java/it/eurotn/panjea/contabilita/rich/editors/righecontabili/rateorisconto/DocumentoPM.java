package it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

public class DocumentoPM {
	private Documento documento;

	/**
	 * Costruttore.
	 */
	public DocumentoPM() {
	}

	/**
	 *
	 * @param documento
	 *            documento wrappato
	 */
	public DocumentoPM(final Documento documento) {
		super();
		this.documento = documento;
	}

	/**
	 * @return Returns the documento.
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @param documento
	 *            The documento to set.
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

}