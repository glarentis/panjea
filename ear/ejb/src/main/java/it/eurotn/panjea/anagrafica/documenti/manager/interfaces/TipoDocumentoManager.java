package it.eurotn.panjea.anagrafica.documenti.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

import java.util.List;

import javax.ejb.Local;

@Local
public interface TipoDocumentoManager {

	/**
	 * Esegue la cancellazione di {@link TipoDocumento}.
	 * 
	 * @param tipoDocumento
	 *            tipo documento da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaTipoDocumento(TipoDocumento tipoDocumento) throws AnagraficaServiceException;

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
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<TipoDocumento> caricaTipiDocumento(String fieldSearch, String valueSearch, boolean caricaNonAbilitati)
			throws AnagraficaServiceException;

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
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	TipoDocumento caricaTipoDocumento(int idTipoDocumento) throws AnagraficaServiceException;

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
	 * Verifica se c'e' almeno un documento con associato il tipoDocumento scelto.
	 * 
	 * @param tipoDocumento
	 *            il tipo documento di cui cercare i documenti associati
	 * @return true se ci sono documenti del tipo documento scelto, false altrimenti
	 */
	boolean hasDocuments(TipoDocumento tipoDocumento);

	/**
	 * Esegue il salvataggio di {@link TipoDocumento}.
	 * 
	 * @param tipoDocumento
	 *            tipoDocumento da salvare
	 * @return tipoDocumento salvato
	 * @throws ModificaTipoAreaConDocumentoException
	 *             rilanciata quando si cerca di modificare certe proprietà di un tipoDocumento che ha dei documenti
	 *             collegati.
	 */
	TipoDocumento salvaTipoDocumento(TipoDocumento tipoDocumento) throws ModificaTipoAreaConDocumentoException;
}
