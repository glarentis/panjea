package it.eurotn.panjea.auvend.rich.bd;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Business Delegate per AuVend.
 * 
 * @see AuVendBD
 * 
 * @author adriano
 * @version 1.0, 30/dic/2008
 * 
 */
public interface IAuVendBD {

	String BEAN_ID = "auVendBD";

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
	@AsyncMethodInvocation
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
	 */
	@AsyncMethodInvocation
	List<Deposito> caricaCaricatori();

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
	 */
	CodiceIvaAuVend caricaCodiceIvaAuVend(Integer id);

	/**
	 * carica la lista di CodiceIvaAuVend per l'azienda corrente.
	 * 
	 * @return lista di codici iva
	 */
	@AsyncMethodInvocation
	List<CodiceIvaAuVend> caricaCodiciIvaAuVend();

	/**
	 * carica {@link LetturaFlussoAuVend}.
	 * 
	 * @param letturaFlussoAuVend
	 *            {@link LetturaFlussoAuVend} da caricare
	 * @return {@link LetturaFlussoAuVend} caricato
	 * @throws AuVendException
	 *             eccezione generica
	 */
	@AsyncMethodInvocation
	LetturaFlussoAuVend caricaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) throws AuVendException;

	@AsyncMethodInvocation
	LetturaFlussoAuVend caricaLetturaFlussoFatturazioneRifornimenti();

	/**
	 * 
	 * Carica la lettura flusso per i movimenti generici.
	 * 
	 * @return la lettura flusso per i movimenti generici
	 */
	@AsyncMethodInvocation
	LetturaFlussoAuVend caricaLetturaFlussoMovimenti();

	@AsyncMethodInvocation
	LetturaFlussoAuVend caricaLetturaFlussoRiparazioniContoTerzi();

	/**
	 * carica la {@link List} di {@link LetturaFlussoAuVend}.
	 * 
	 * @return lista di {@link LetturaFlussoAuVend}
	 */
	@AsyncMethodInvocation
	List<LetturaFlussoAuVend> caricaLettureFlussoAuVend();

	/**
	 * carica la {@link List} di {@link LetturaFlussoAuVend} solo per i carichi.
	 * 
	 * @return lista di {@link LetturaFlussoAuVend}
	 */
	@AsyncMethodInvocation
	Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoCarichi();

	/**
	 * carica la {@link List} di {@link LetturaFlussoAuVend} solo per le fatture.
	 * 
	 * @return lista di {@link LetturaFlussoAuVend}
	 */
	@AsyncMethodInvocation
	Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoFatture();

	/**
	 * carica {@link List} di {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @return lista di {@link TipoDocumentoBaseAuVend}
	 */
	List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVend();

	/**
	 * carica {@link TipoDocumentoBaseAuVend} per {@link TipoOperazione}.
	 * 
	 * @param tipoOperazione
	 *            tipo operazione
	 * @return lista di {@link TipoDocumentoBaseAuVend}
	 */
	List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione);

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
	 * Chiude le fatture del deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data finale
	 * @return lista di id delle aree create
	 * 
	 */
	@AsyncMethodInvocation
	List<Integer> chiudiFatture(String deposito, Date dataFine);

	/**
	 * Importa i movimenti di carico in base al deposito e data specificati.
	 * 
	 * @param dataInizio
	 *            data inizio
	 * @param dataFine
	 *            data fine
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
	@AsyncMethodInvocation
	boolean importaCarichiERifornimenti(Date dataInizio, Date dataFine);

	@AsyncMethodInvocation
	boolean importaFatturazioneRifornimenti(Date dataInizio, Date dataFine);

	/**
	 * Importa le fatture in base al deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data di riferimento
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
	@AsyncMethodInvocation
	boolean importaFatture(String deposito, Date dataFine);

	/**
	 * Importa i movimenti generici.
	 * 
	 * @param dataInizio
	 *            data iniziale di importazione
	 * 
	 * @param dataFine
	 *            data di finale di importazione
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
	@AsyncMethodInvocation
	boolean importaMovimenti(Date dataInizio, Date dataFine);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return vero se l'importazione è andata a buon fine.
	 */
	@AsyncMethodInvocation
	boolean importaRiparazioniContoTerzi(Date dataInizio, Date dataFine);

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
	@AsyncMethodInvocation
	LetturaFlussoAuVend salvaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend);

	/**
	 * salva {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @param tipoDocumentoBaseAuVend
	 *            tipo documento da salvare
	 * @return tipo documento salvato
	 */
	TipoDocumentoBaseAuVend salvaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return Statistica con l'eventuale elenco di articoli mancanti.
	 */
	@AsyncMethodInvocation
	StatisticaImportazione verificaCarichi(Date dataInizio, Date dataFine);

	@AsyncMethodInvocation
	StatisticaImportazione verificaFatturazioneRifornimenti(Date dataInizio, Date dataFine);

	/**
	 * Verifica le fatture.
	 * 
	 * @param depositi
	 *            depositi da analizzare
	 * @param dataFine
	 *            data finale
	 * @return mappa con i risultati. Nella chiave ho il codice del deposito
	 */
	@AsyncMethodInvocation
	Map<String, StatisticaImportazione> verificaFatture(List<String> depositi, Date dataFine);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return mappa con i risultati. Nella chiave ho il codice del deposito (null in questo caso)
	 */
	@AsyncMethodInvocation
	Map<String, StatisticaImportazione> verificaMovimenti(Date dataInizio, Date dataFine);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return statistica con articoli e clienti mancanti.
	 */
	@AsyncMethodInvocation
	StatisticaImportazione verificaRiparazioniContoTerzi(Date dataInizio, Date dataFine);
}
