package it.eurotn.panjea.anagrafica.documenti.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;

import java.util.List;

import javax.ejb.Local;

@Local
public interface DocumentiManager {

	/**
	 * Esegue la cancellazione di {@link Documento}.
	 * 
	 * @param documento
	 *            documento da cancellare
	 */
	void cancellaDocumento(Documento documento);

	/**
	 * Carica tutte le aree del documento.
	 * 
	 * @param idDocumento
	 *            id del documento
	 * @return aree del documento caricate
	 */
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
	 * Carica il numero di aree collegate al documento scelto.
	 * 
	 * @param documento
	 *            il documento di cui trovare il numero di aree
	 * @return il numero di aree di un documento
	 */
	int caricaNumeroAreeCollegate(Documento documento);

	/**
	 * esegue la verifica dell'univocità di {@link Documento} secondo la corrispondenza degli attributi: CodiceAzienda <br>
	 * DataDocumento Numero (attributo codice)/Prefisso TipoDocumento Entita/RapportoBancarioAzienda.
	 * 
	 * @param documento
	 *            documento da verificare
	 * @return true se il documento è univoco, false altrimenti.
	 */
	boolean isDocumentoUnivoco(Documento documento);

	/**
	 * Ricerca i documenti per i parametri ricerca documento specificati.<br>
	 * 
	 * @param parametriRicercaDocumento
	 *            parametriRicercaDocumento
	 * @return List<Documento>
	 */
	List<Documento> ricercaDocumenti(ParametriRicercaDocumento parametriRicercaDocumento);

	/**
	 * Esegue il salvataggio di {@link Documento}.
	 * 
	 * @param documento
	 *            documento da salvare
	 * @return documento salvato
	 * @throws DocumentoDuplicateException
	 *             rilanciata quando esiste un documento con la stessa chiave di dominio
	 */
	Documento salvaDocumento(Documento documento) throws DocumentoDuplicateException;

	/**
	 * Salva il documento senza eseguire nessun controllo di univocità o altro.
	 * 
	 * @param documento
	 *            il documento da salvare
	 * @return Documento
	 */
	Documento salvaDocumentoNoCheck(Documento documento);

}
