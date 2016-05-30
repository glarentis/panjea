package it.eurotn.panjea.documenti.graph.builder.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface AreaDocumentoBuilder {

	/**
	 * Crea il nodo dell'area in bae a quello del documento.
	 *
	 * @param node
	 *            nodo di riferimento del documento
	 * @return nodo creato, <code>null</code> se non esiste l'area
	 */
	AreaDocumentoNode createNode(DocumentoNode node);

	/**
	 * Restituisce la lista dei documenti che vengono puntati dal documento del nodo specificato. Nella mappa restituita
	 * ogni documento è legato alla relazione con il documento di origine ( fatturazione, evasione, rate ecc... ).
	 *
	 * @param documentoNode
	 *            nodo di riferimento
	 * @return documenti collegati
	 */
	Map<Documento, String> getNextDocumentiCollegati(DocumentoNode documentoNode);

	/**
	 * Restituisce la lista dei documenti che puntano al documento del nodo specificato. Nella mappa restituita ogni
	 * documento è legato alla relazione con il documento di origine ( fatturazione, evasione, rate ecc... ).
	 *
	 * @param documentoNode
	 *            nodo di riferimento
	 * @return documenti collegati
	 */
	Map<Documento, String> getPreviousDocumentiCollegati(DocumentoNode documentoNode);
}
