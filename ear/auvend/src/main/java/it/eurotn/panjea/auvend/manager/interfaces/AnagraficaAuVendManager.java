package it.eurotn.panjea.auvend.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * 
 * Manager per la gestione delle classi di anagrafica( {@link LetturaFlussoAuVend}, {@link TipoDocumentoBaseAuVend} ).
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
@Local
public interface AnagraficaAuVendManager {

	/**
	 * elimina il CodiceIvaAuVend identificato da id.
	 * 
	 * @param id
	 *            id del codice iva
	 */
	void cancellaCodiceIvaAuVend(Integer id);

	/**
	 * cancella {@link LetturaFlussoAuVend}.
	 * 
	 * @param letturaFlussoAuVend
	 *            lettura da cancellare
	 */
	void cancellaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend);

	/**
	 * cancella {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @param tipoDocumentoBaseAuVend
	 *            tipo documento base da cancellare
	 */
	void cancellaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend);

	/**
	 * carica e restituisce la {@link Collection} di depositi che rappresentano i caricatori di AuVend.
	 * 
	 * @return lista di depositi
	 * @throws AuVendException
	 *             eccezione generica
	 */
	List<Deposito> caricaCaricatori() throws AuVendException;

	/**
	 * 
	 * @return lista delle causali di auvend non associate ad un tipod documento base.
	 */
	List<String> caricaCausaliNonAssociateAuvend();

	/**
	 * carica {@link CodiceIvaAuVend} identificato da id.
	 * 
	 * @param id
	 *            id codice iva auvend
	 * @return codiceIvaAuVend identificato da id
	 * @throws AuVendException
	 *             eccezione generica
	 */
	CodiceIvaAuVend caricaCodiceIvaAuVend(Integer id) throws AuVendException;

	/**
	 * carica la lista di CodiceIvaAuVend per l'azienda corrente.
	 * 
	 * @return lista di codici iva
	 * @throws AuVendException
	 *             eccezione generica
	 */
	List<CodiceIvaAuVend> caricaCodiciIvaAuVend() throws AuVendException;

	/**
	 * carica {@link LetturaFlussoAuVend}.
	 * 
	 * @param letturaFlussoAuVend
	 *            {@link LetturaFlussoAuVend} da caricare
	 * @return {@link LetturaFlussoAuVend} caricato
	 * @throws AuVendException
	 *             eccezione generica
	 */
	LetturaFlussoAuVend caricaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) throws AuVendException;

	/**
	 * Carica una lettura flusso in base al codice del deposito.
	 * 
	 * @param codiceDeposito
	 *            codice deposito
	 * @return {@link LetturaFlussoAuVend} caricata
	 * @throws AuVendException
	 *             eccezione generica
	 */
	LetturaFlussoAuVend caricaLetturaFlussoAuVend(String codiceDeposito) throws AuVendException;

	/**
	 * 
	 * Carica la lettura flusso per la fatturazione dei rifornimenti
	 * 
	 * @return la lettura flusso per la fatturazione dei rifornimenti
	 * @throws AuVendException
	 *             eccezione generica
	 * 
	 */
	LetturaFlussoAuVend caricaLetturaFlussoFattuazioneRifornimenti() throws AuVendException;

	/**
	 * 
	 * Carica la lettura flusso per i movimenti generici.
	 * 
	 * @return la lettura flusso per i movimenti generici
	 * @throws AuVendException
	 *             eccezione generica
	 */
	LetturaFlussoAuVend caricaLetturaFlussoMovimenti() throws AuVendException;

	/**
	 * 
	 * Carica la lettura flusso per le riparazioni conto terzi
	 * 
	 * @return la lettura flusso per le riparazioni conto terzi
	 * @throws AuVendException
	 *             eccezione generica
	 */
	LetturaFlussoAuVend caricaLetturaFlussoRiparazioneContoTerzi() throws AuVendException;

	
	/**
	 * carica la {@link List} di {@link LetturaFlussoAuVend}.
	 * 
	 * @return lista di {@link LetturaFlussoAuVend}
	 * @throws AuVendException
	 *             eccezione generica
	 */
	List<LetturaFlussoAuVend> caricaLettureFlussoAuVend() throws AuVendException;

	/**
	 * carica la {@link List} di {@link LetturaFlussoAuVend} solo per i carichi.
	 * 
	 * @return lista di {@link LetturaFlussoAuVend}
	 * @throws AuVendException
	 *             eccezione generica
	 */
	Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoCarichi() throws AuVendException;

	/**
	 * 
	 * @return lista di {@link LetturaFlussoAuvend}
	 * @throws AuVendException
	 *             eccezione generica
	 */
	LetturaFlussoAuVend caricaLettureFlussoFatturazioneRifornimenti() throws AuVendException;

	/**
	 * carica la {@link List} di {@link LetturaFlussoAuVend} solo per le fatture.
	 * 
	 * @return lista di {@link LetturaFlussoAuVend}
	 * @throws AuVendException
	 *             eccezione generica
	 */
	Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoFatture() throws AuVendException;

	/**
	 * carica {@link List} di {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @return lista di {@link TipoDocumentoBaseAuVend}
	 * @throws AuVendException
	 *             eccezione generica
	 */
	List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVend() throws AuVendException;

	/**
	 * carica {@link TipoDocumentoBaseAuVend} per {@link TipoOperazione}.
	 * 
	 * @param tipoOperazione
	 *            tipo operazione
	 * @return lista di {@link TipoDocumentoBaseAuVend}
	 * @throws AuVendException
	 *             eccezione generica
	 */
	List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione)
			throws AuVendException;

	/**
	 * carica {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @param tipoDocumentoBaseAuVend
	 *            tipo documento da caricare
	 * @return tipo documento caricato
	 * @throws AuVendException
	 *             eccezione generica
	 */
	TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend)
			throws AuVendException;

	/**
	 * carica {@link TipoDocumentoBaseAuVend} per {@link TipoOperazione}.
	 * 
	 * @param tipoOperazione
	 *            tipo operazione
	 * @return tipo documento caricato
	 */
	TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione);

	/**
	 * salvataggio di {@link CodiceIvaAuVend}.
	 * 
	 * @param codiceIvaAuVend
	 *            codice iva da salvare
	 * @return codiceIvaAuVend codice iva reso persistente
	 */
	CodiceIvaAuVend salvaCodiceIvaAuVend(CodiceIvaAuVend codiceIvaAuVend);

	/**
	 * salva {@link LetturaFlussoAuVend}.
	 * 
	 * @param letturaFlussoAuVend
	 *            lettura da salvare
	 * @return lettura salvata
	 */
	LetturaFlussoAuVend salvaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend);

	/**
	 * salva {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @param tipoDocumentoBaseAuVend
	 *            tipo documento da salvare
	 * @return tipo documento salvato
	 */
	TipoDocumentoBaseAuVend salvaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend);

}
