package it.eurotn.panjea.anagrafica.documenti.service.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DocumentiService {

	/**
	 * Esegue la cancellazione di {@link Documento}.
	 * 
	 * @param documento
	 *            documento da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaDocumento(Documento documento) throws AnagraficaServiceException;

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
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Documento caricaDocumento(int idDocumento) throws AnagraficaServiceException;

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
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Documento salvaDocumento(Documento documento) throws AnagraficaServiceException, DocumentoDuplicateException;

	/**
	 * Esegue il salvataggio di {@link TipoDocumento}.
	 * 
	 * @param tipoDocumento
	 *            tipoDocumento da salvare
	 * @return tipoDocumento salvato
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * @throws ModificaTipoAreaConDocumentoException
	 *             rilanciata quando si cerca di modificare certe proprietà di un tipoDocumento che ha dei documenti
	 *             collegati.
	 */
	TipoDocumento salvaTipoDocumento(TipoDocumento tipoDocumento) throws AnagraficaServiceException,
			ModificaTipoAreaConDocumentoException;
}
