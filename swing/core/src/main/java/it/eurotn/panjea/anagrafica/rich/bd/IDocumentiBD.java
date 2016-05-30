package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

public interface IDocumentiBD {

	/**
	 * Esegue la cancellazione di {@link Documento}.
	 * 
	 * @param documento
	 *            documento da cancellare
	 */
	void cancellaDocumento(Documento documento);

	/**
	 * Esegue la cancellazione di {@link TipoDocumento}.
	 * 
	 * @param tipoDocumento
	 *            tipo documento da cancellare
	 */
	void cancellaTipoDocumento(TipoDocumento tipoDocumento);

	/**
	 * Carica tutte le aree del documento.
	 * 
	 * @param idDocumento
	 *            id del documento
	 * @return aree del documento caricate
	 */
	@AsyncMethodInvocation
	List<Object> caricaAreeDocumento(Integer idDocumento);

	/**
	 * Carica {@link Documento} identificato da idDocumento.
	 * 
	 * @param idDocumento
	 *            id del documento da caricare
	 * @return documento caricato
	 */
	Documento caricaDocumento(int idDocumento);

	/**
	 * Carica {@link Documento} identificato da idDocumento.
	 * 
	 * @param idDocumento
	 *            id del documento da caricare
	 * @param initDocumentiCollegati
	 *            true se si vuole initizializzare oi documenti collegati.
	 * @return documento caricato
	 */
	Documento caricaDocumento(int idDocumento, boolean initDocumentiCollegati);

	/**
	 * Carica la lista di {@link TipoDocumento}.
	 * 
	 * @param valueSearch
	 *            valore da filtrare
	 * @param fieldSearch
	 *            campo sul quale filtrare (codice o descrizione)
	 * 
	 * @param caricaNonAbilitati
	 *            se <code>true</code> carica anche i tipi documento non abilitati
	 * @return lista tipi documento non abilitati
	 */
	List<TipoDocumento> caricaTipiDocumento(String fieldSearch, String valueSearch, boolean caricaNonAbilitati);

	/**
	 * Carica tutti i tipi documento che possono essere associati alle note anagrafiche nelle note automatiche.
	 * 
	 * @return tipi documento caricati
	 */
	List<TipoDocumento> caricaTipiDocumentoPerNoteAutomatiche();

	/**
	 * carica {@link TipoDocumento} identificato da idTipoDocumento.
	 * 
	 * @param idTipoDocumento
	 *            id tipoDocumento da caricare
	 * @return TipoDocumento caricato
	 */
	TipoDocumento caricaTipoDocumento(int idTipoDocumento);

	/**
	 * Copia il tipo documento passato come parametro e i tipi area collegati.
	 * 
	 * @param codiceNuovoTipoDocumento
	 *            codice del nuovo tipo documento
	 * @param descrizioneNuovoTipoDocumento
	 *            descrizione del nuovo tipo documento
	 * @param tipoDocumento
	 *            tipo documento da copiare
	 * @return {@link TipoDocumento} copiato
	 */
	TipoDocumento copiaTipoDocumento(String codiceNuovoTipoDocumento, String descrizioneNuovoTipoDocumento,
			TipoDocumento tipoDocumento);

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
	@AsyncMethodInvocation
	DocumentoNode createNode(DocumentoGraph documentoGraph, boolean loadAllNodes);

	/**
	 * Ricerca i documenti per i parametri ricerca documento specificati.<br>
	 * 
	 * @param parametriRicercaDocumento
	 *            parametriRicercaDocumento
	 * @return List<Documento>
	 */
	@AsyncMethodInvocation
	List<Documento> ricercaDocumenti(ParametriRicercaDocumento parametriRicercaDocumento);

	/**
	 * Esegue il salvataggio di {@link Documento}.
	 * 
	 * @param documento
	 *            documento da salvare
	 * @return documento salvato
	 */
	Documento salvaDocumento(Documento documento);

	/**
	 * Esegue il salvataggio di {@link TipoDocumento}.
	 * 
	 * @param tipoDocumento
	 *            tipoDocumento da salvare
	 * @return tipoDocumento salvato
	 */
	TipoDocumento salvaTipoDocumento(TipoDocumento tipoDocumento);
}
