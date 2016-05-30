package it.eurotn.panjea.documenti.graph.builder.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;

import java.util.List;

import javax.ejb.Local;

@Local
public interface DocumentoBuilder {

	/**
	 * Crea il nodo dall'id documento.
	 *
	 * @param documento
	 *            documento di riferimento
	 * @return nodo creato
	 */
	DocumentoNode createNode(Documento documento);

	/**
	 * Restituisce la lista di tutti i documento collegati.
	 *
	 * @param idDocumento
	 *            id documento di riferimento
	 * @return documenti collegati
	 */
	List<Integer> getLinkDocumentiDestinazione(Integer idDocumento);

	/**
	 * Restituisce la lista di tutti i documento collegati.
	 *
	 * @param idDocumento
	 *            id documento di riferimento
	 * @return documenti collegati
	 */
	List<Integer> getLinkDocumentiOrigine(Integer idDocumento);
}
