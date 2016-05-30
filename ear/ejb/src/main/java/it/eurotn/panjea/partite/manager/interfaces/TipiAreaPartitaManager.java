package it.eurotn.panjea.partite.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;

import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Manager per la definizione di {@link TipoAreaPartita}.
 * 
 * @author adriano
 * @version 1.0, 08/lug/08
 * 
 */
@Local
public interface TipiAreaPartitaManager {

	/**
	 * Esegue la cancellazione di {@link TipoAreaPartita}.
	 * 
	 * @param tipoAreaPartita
	 *            tipo area partita da cancellare
	 */
	void cancellaTipoAreaPartita(TipoAreaPartita tipoAreaPartita);

	/**
	 * Cancella il {@link TipoDocumentoBasePartite} senza controlli.
	 * 
	 * @param tipoDocumentoBase
	 *            tipo documento base da cancellare
	 */
	void cancellaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase);

	/**
	 * Carica tutti i tipi area partita che generano pagamenti di tipologia {@link TipoPartita}
	 * 
	 * @param tipoPartita
	 *            tipoPartita che generano i pagamenti
	 * @return tipi area partita che generano pagamenti di tipologia {@link TipoPartita}
	 */
	List<TipoAreaPartita> caricaTipiAreaPartitaGenerazioneRate(TipoPartita tipoPartita);

	/**
	 * esegue il caricamento di {@link TipoAreaPartita} filtrate per l'azienda corrente.
	 * 
	 * @param loadTipiDocumentoDisabilitati
	 *            carica anche i tipi area disabilitati se <code>true</code>
	 * @return List di {@link TipoAreaPartita} caricate
	 */
	List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(boolean loadTipiDocumentoDisabilitati);

	/**
	 * Restituisce la lista dei tipiAreaPartita adatti alla generazione dei pagamenti. Filtra i tipi area partite
	 * secondo i seguenti casi:<br>
	 * {@link TipoOperazione}=GENERA --> sempre esclusi<br>
	 * {@link TipoOperazione} =CHIUSURA --> sempre inclusi<br>
	 * {@link TipoOperazione}=GESTIONE_DISTINTA --> inclusi se {@link TipoPartita}=PASSIVA tutti, se {@link TipoPartita}
	 * =ATTIVA carico il {@link TipoDocumentoBase}=DISTINTA_DI_PAGAMENTO<br>
	 * 
	 * @param valueSearch
	 *            valore da filtrare
	 * @param fieldSearch
	 *            campo da filtrare
	 * 
	 * @param tipoPartita
	 *            {@link TipoPartita} ATTIVA o PASSIVA
	 * @param escludiTipiAreaPartiteDistinta
	 *            se <code>true</code> esclude i tipi area partite definiti nei tipi documenti base di tipo:
	 *            DISTINTA,ACCREDITO,INSOLUTO,ANTICIPO e ACCONTO
	 * @param loadTipiDocumentoDisabilitati
	 *            carica anche i tipi documento disabilitati se <code>true</code>
	 * @return la lista di {@link TipoAreaPartita} filtrate
	 */
	List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(String fieldSearch, String valueSearch,
			TipoPartita tipoPartita, boolean loadTipiDocumentoDisabilitati, boolean escludiTipiAreaPartiteDistinta);

	/**
	 * Carica tutti i {@link TipoDocumentoBasePartite} per azienda.
	 * 
	 * @return list di {@link TipoDocumentoBasePartite} caricati
	 */
	List<TipoDocumentoBasePartite> caricaTipiDocumentoBase();

	/**
	 * esegue il caricamento di {@link TipoAreaPartita} identitficato da tipoAreaContabile.id e lo restituisce.
	 * 
	 * @param tipoAreaPartita
	 *            tipo area partita
	 * @return {@link TipoAreaPartita} caricato
	 */
	TipoAreaPartita caricaTipoAreaPartita(TipoAreaPartita tipoAreaPartita);

	/**
	 * carica il tipo area partita collegato a tipoDocumento.
	 * 
	 * @param tipoDocumento
	 *            tipo documento
	 * @return {@link TipoAreaPartita} collegato a tipoDocumento
	 */
	TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento);

	/**
	 * Carica il {@link TipoDocumentoBasePartite} con il tipo operazione specificato da
	 * {@link TipoDocumentoBasePartite.TipoOperazione}.
	 * 
	 * @param tipoOperazione
	 *            tipo operazione
	 * @return {@link TipoDocumentoBasePartite} caricato
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 */
	TipoDocumentoBasePartite caricaTipoDocumentoBase(TipoDocumentoBasePartite.TipoOperazione tipoOperazione)
			throws TipoDocumentoBaseException;

	/**
	 * esegue il salvataggio di {@link TipoAreaPartita} e lo restituisce.
	 * 
	 * @param tipoAreaPartita
	 *            tipo area da salvare
	 * @return {@link TipoAreaPartita} reso persistente
	 * @throws ModificaTipoAreaConDocumentoException .
	 */
	TipoAreaPartita salvaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) throws ModificaTipoAreaConDocumentoException;

	/**
	 * Esegue il salvataggio di {@link TipoDocumentoBasePartite} e lo restituisce salvato.
	 * 
	 * @param tipoDocumentoBase
	 *            tipo documento base da salvare
	 * @return {@link TipoDocumentoBasePartite} salvato
	 */
	TipoDocumentoBasePartite salvaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase);

}
