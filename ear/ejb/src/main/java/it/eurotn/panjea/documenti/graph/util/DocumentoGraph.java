package it.eurotn.panjea.documenti.graph.util;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

import java.io.Serializable;

public class DocumentoGraph implements Serializable {

	private static final long serialVersionUID = -1720262213087204181L;

	private Documento documento = new Documento();

	/**
	 * @return Returns the idDocumento.
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @param idDocumento
	 *            The idDocumento to set.
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.documento.setId(idDocumento);
	}
}
