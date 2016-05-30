package it.eurotn.panjea.documenti.graph.node.loader.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.documenti.graph.node.AreaDocumentoNode;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface AreaDocumentoNodeLoader {

	String LOADER_JNDI_NAME = "Panjea.AreaDocumentoNodeLoader";

	/**
	 * Carica il nodo dell'area in base al documento.
	 * 
	 * @param idDocumento
	 *            id del documento
	 * @return nodo creato
	 */
	AreaDocumentoNode caricaAreaDocumentoNode(Integer idDocumento);

	/**
	 * Restituisce la lista dei documenti che vengono puntati dall'area documento specificata. Nella mappa restituita
	 * ogni documento è legato alla relazione con il documento di origine ( fatturazione, evasione, rate ecc... ).
	 * 
	 * @param idAreaDocumento
	 *            id area di riferimento
	 * @return documenti collegati
	 */
	Map<Documento, String> getNextDocumentiCollegati(Integer idAreaDocumento);

	/**
	 * Restituisce la lista dei documenti che puntano all'area specificata. Nella mappa restituita ogni documento è
	 * legato alla relazione con il documento di origine ( fatturazione, evasione, rate ecc... ).
	 * 
	 * @param idAreaDocumento
	 *            id area di riferimento
	 * @return documenti collegati
	 */
	Map<Documento, String> getPreviousDocumentiCollegati(Integer idAreaDocumento);
}
