package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * 
 * Manager per la gestione dei {@link TipoAreaContabile}
 * 
 * @author adriano
 * @version 1.0, 31/ago/07
 * 
 */
@Local
public interface TipiAreaContabileManager {

	/**
	 * Cancella un <code>CodiceIvaPrevalente</code>
	 * 
	 * @param codiceIvaPrevalente
	 */
	void cancellaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente);

	/**
	 * metodo che cancella {@link TipoAreaContabile}
	 * 
	 * @param tipoAreaContabile
	 */
	void cancellaTipoAreaContabile(TipoAreaContabile tipoAreaContabile);

	/**
	 * cancella {@link TipoDocumentoBase}
	 * 
	 * @param tipoDocumentoBase
	 */
	void cancellaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase);

	/**
	 * Carica un <code>CodiceIvaPrevalente</code>. Se l'entità è nulla viene caricato il codice iva predefinito per il
	 * tipo area contabile altrimenti viene caricato quello specifico per l'entità. Se non esiste viene retituito un
	 * valore a <code>null</code>.
	 * 
	 * @param tipoAreaContabile
	 * @param entita
	 * @return
	 */
	CodiceIvaPrevalente caricaCodiceIvaPrevalente(TipoAreaContabile tipoAreaContabile, EntitaLite entita);

	/**
	 * Metodo che restituisce i {@link TipoAreaContabile} per l'azienda loggata.
	 * 
	 * @param valueSearch
	 *            valore da filtrare
	 * @param fieldSearch
	 *            campo da filtrare
	 * @return tipiareacontabile filtrati.
	 * 
	 * @throws ContabilitaException
	 */
	List<TipoAreaContabile> caricaTipiAreaContabile(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentiDisabilitati) throws ContabilitaException;

	/**
	 * Metodo che restituisce l'elenco di {@link TipoDocumento} istanziati da {@link TipoAreaContabile}
	 * 
	 * @return
	 * @throws ContabilitaException
	 */
	List<TipoDocumento> caricaTipiDocumentiContabili() throws ContabilitaException;

	/**
	 * Carica la lista di {@link TipoDocumentoBase} per azienda. <b>NB:</b>La query è in cache quindi il metodo può
	 * essere utilizzato più volte senza problemi di prestazioni
	 * 
	 * @return
	 * @throws ContabilitaException
	 */
	List<TipoDocumentoBase> caricaTipiDocumentoBase();

	/**
	 * Carica la lista di {@link TipoDocumentoBase} per azienda e restituisce una {@link Map} con key
	 * {@link TipoOperazioneTipoDocumento} e value {@link TipoAreaContabile} associato.
	 * 
	 * @return
	 * @throws ContabilitaException
	 */
	TipiDocumentoBase caricaTipiOperazione() throws ContabilitaException;

	/**
	 * metodo che restituisce {@link TipoAreaContabile}
	 * 
	 * @param id
	 * @return
	 * @throws ContabilitaException
	 */
	TipoAreaContabile caricaTipoAreaContabile(Integer id) throws ContabilitaException;

	/**
	 * Metodo che restituisce il {@link TipoAreaContabile} per l'id di {@link TipoDocumento} passato per parametro
	 * 
	 * @param tipoDocumento
	 * @return
	 * @throws ContabilitaException
	 */
	TipoAreaContabile caricaTipoAreaContabilePerTipoDocumento(Integer idTipoDocumento) throws ContabilitaException;

	/**
	 * carica {@link TipoAreaContabile} corrispondente a {@link TipoOperazione} richiesto, se non esiste viene
	 * rilanciata l'eccezione {@link TipiDocumentoBaseException}
	 * 
	 * @return
	 * @throws ContabilitaException
	 * @throws TipoDocumentoBaseException
	 */
	TipoAreaContabile caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento tipoOperazione)
			throws TipoDocumentoBaseException, ContabilitaException;

	/**
	 * carica {@link TipoDocumentoBase}
	 * 
	 * @param idTipoDocumentoBase
	 * @return
	 * @throws ContabilitaException
	 */
	TipoDocumentoBase caricaTipoDocumentoBase(Integer idTipoDocumentoBase) throws ContabilitaException;

	/**
	 * Salva un <code>CodiceIvaPrevalente</code>
	 * 
	 * @param codiceIvaPrevalente
	 * @return
	 */
	CodiceIvaPrevalente salvaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente);

	/**
	 * metodo che esegue il salvataggio di {@link TipoAreaContabile}
	 * 
	 * @param tipoAreaContabile
	 * @return
	 * @throws ModificaTipoAreaConDocumentoException
	 */
	TipoAreaContabile salvaTipoAreaContabile(TipoAreaContabile tipoAreaContabile)
			throws ModificaTipoAreaConDocumentoException;

	/**
	 * salva {@link TipoDocumentoBase}
	 * 
	 * @param tipoDocumentoBase
	 * @return
	 */
	TipoDocumentoBase salvaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase);
}
