package it.eurotn.panjea.auvend.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.auvend.service.AuVendServiceBean;
import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * 
 * Service per il plugin di AuVend.
 * 
 * @see AuVendServiceBean
 * 
 * @author adriano
 * @version 1.0, 05/gen/2009
 * 
 */
@Remote
public interface AuVendService {

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

	LetturaFlussoAuVend caricaLetturaFlussoFatturazioneRifornimenti() throws AuVendException;

	/**
	 * 
	 * Carica la lettura flusso per i movimenti generici.
	 * 
	 * @return la lettura flusso per i movimenti generici
	 * @throws AuVendException
	 *             eccezione generica
	 */
	LetturaFlussoAuVend caricaLetturaFlussoMovimenti() throws AuVendException;

	LetturaFlussoAuVend caricaLetturaFlussoRiparazioniContoTerzi() throws AuVendException;

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
	 * Chiude le fatture del deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data finale
	 * @return lista di id delle aree create
	 * 
	 */
	List<Integer> chiudiFatture(String deposito, Date dataFine);

	boolean importa(TipoDocumentoBaseAuVend.TipoOperazione tipoOperazione, Date dataInizio, Date dataFine);

	/**
	 * Importa i movimenti di carico in base al deposito e data specificati.
	 * 
	 * @param dataInizio
	 *            data inizio
	 * @param dataFine
	 *            data fine
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
	boolean importaCarichiERifornimenti(Date dataInizio, Date dataFine);

	/**
	 * 
	 * @param dataInizio
	 * @param dataFine
	 * @return
	 * @throws AuVendException
	 */
	boolean importaFatturazioneRifornimenti(Date dataInizio, Date dataFine) throws AuVendException;

	/**
	 * Importa le fatture in base al deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data di riferimento
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 * @throws SottoContiContabiliAssentiException
	 *             sollevata in mancanza dei sotto conti contabili
	 */
	boolean importaFatture(String deposito, Date dataFine) throws SottoContiContabiliAssentiException;

	/**
	 * Importa i movimenti generici.
	 * 
	 * @param dataInizio
	 *            data iniziale di importazione
	 * @param dataFine
	 *            data di finale di importazione
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
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
	LetturaFlussoAuVend salvaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend);

	/**
	 * salva {@link TipoDocumentoBaseAuVend}.
	 * 
	 * @param tipoDocumentoBaseAuVend
	 *            tipo documento da salvare
	 * @return tipo documento salvato
	 */
	TipoDocumentoBaseAuVend salvaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend);

	StatisticaImportazione verifica(TipoDocumentoBaseAuVend.TipoOperazione tipoOperazione, Date dataInizio,
			Date dataFine);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data inizio
	 * @param dataFine
	 *            data finale
	 * @return Statistica con gli articoli mancanti in PanJea.
	 */
	public StatisticaImportazione verificaCarichi(Date dataInizio, Date dataFine);

	/**
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return i risultati della verifica
	 */
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
	StatisticaImportazione verificaRiparazioniContoTerzi(Date dataInizio, Date dataFine);
}
