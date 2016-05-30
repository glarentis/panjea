package it.eurotn.panjea.documenti.graph.builder.interfaces;

import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;

import javax.ejb.Local;

@Local
public interface NodeBuilder {

	/**
	 * Crea il nodo con tutte le associazioni legate al documento specificato.
	 * 
	 * @param documentoGraph
	 *            documento di riferimento
	 * @param loadAllNodes
	 *            se <code>true</code> carica tutti i collegamenti fra i documenti altrimenti vengono caricati solo i
	 *            collegamenti dei documenti in riferimento al documento passato come parametro.
	 * @return nodo creato
	 */
	DocumentoNode createNode(DocumentoGraph documentoGraph, boolean loadAllNodes);

}
