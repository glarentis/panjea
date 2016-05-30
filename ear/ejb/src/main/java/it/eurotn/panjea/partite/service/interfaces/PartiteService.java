package it.eurotn.panjea.partite.service.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaService;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 * Interfaccia remota del Service del modulo Pagamenti.
 * 
 * @author adriano
 * @version 1.0, 18/lug/08
 */
@Remote
public interface PartiteService {

	/**
	 * Serve per cancellare una categoria rata Test OK.
	 * 
	 * @param categoriaRata
	 *            la categoria da cancellare
	 */
	void cancellaCategoriaRata(CategoriaRata categoriaRata);

	/**
	 * Cancella la riga struttura.
	 * 
	 * @param rigaStrutturaPartite
	 *            la riga da cancellare
	 */
	void cancellaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite);

	/**
	 * Cancella TUTTA la struttura partita compreso righe e formule Test OK.
	 * 
	 * @param strutturaPartite
	 *            la struttura partita da cancellare
	 */
	void cancellaStrutturaPartita(StrutturaPartita strutturaPartite);

	/**
	 * Cancella il {@link TipoAreaPartita}.
	 * 
	 * @param tipoAreaPartita
	 *            il tipo area partita da cancellare
	 */
	void cancellaTipoAreaPartita(TipoAreaPartita tipoAreaPartita);

	/**
	 * Cancella il {@link TipoDocumentoBasePartite} senza controlli.
	 * 
	 * @param tipoDocumentoBase
	 *            il tipo documento base partita da cancellare
	 */
	void cancellaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase);

	/**
	 * Carica una categoria rata con ID Test OK.
	 * 
	 * @param idCategoriaRata
	 *            l'id della categoria da caricare
	 * @return CategoriaRata
	 */
	CategoriaRata caricaCategoriaRata(Integer idCategoriaRata);

	/**
	 * Carica tutte le categorie per elenchi Test OK.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return List<CategoriaRata>
	 */
	List<CategoriaRata> caricaCategorieRata(String fieldSearch, String valueSearch);

	/**
	 * Carica la struttura partita completa di righe e formule Test OK.
	 * 
	 * @param idStruttura
	 *            l'id della struttura da caricare
	 * @return StrutturaPartita
	 */
	StrutturaPartita caricaStrutturaPartita(Integer idStruttura);

	/**
	 * Carica tutte le strutture partite con unico discriminate il codice azienda Test OK.
	 * 
	 * @return List<StrutturaPartita>
	 */
	List<StrutturaPartitaLite> caricaStrutturePartita();

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
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 */
	List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(String fieldSearch, String valueSearch,
			TipoPartita tipoPartita, boolean loadTipiDocumentoDisabilitati, boolean escludiTipiAreaPartiteDistinta)
			throws TipoDocumentoBaseException;

	/**
	 * Carica tutti i {@link TipoDocumentoBasePartite} per azienda.
	 * 
	 * @return List<TipoDocumentoBasePartite>
	 */
	List<TipoDocumentoBasePartite> caricaTipiDocumentoBase();

	/**
	 * Restituisce il {@link TipoAreaPartita}.
	 * 
	 * @param tipoAreaPartita
	 *            il tipo area partita da caricare
	 * @return TipoAreaPartita
	 */
	TipoAreaPartita caricaTipoAreaPartita(TipoAreaPartita tipoAreaPartita);

	/**
	 * Carica il {@link TipoAreaPartita} associato al tipo documento <br>
	 * Metodo duplicato su {@link ContabilitaService}.
	 * 
	 * @param tipoDocumento
	 *            il tipo documento di cui caricare il tipo area partite
	 * @return il tipoAreaPartita collegato o una nuova istanza
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
	 * Restituisce le righe della <code>StrutturaPartita</code> che serviranno al <code>CodicePagamento</code> per
	 * calcolare le scadenze relative Test OK.
	 * 
	 * @param numeroRate
	 *            il numero di rate da creare
	 * @param intervallo
	 *            l'intervallo tra una rata e l'altra
	 * @param strutturaPartita
	 *            la struttura a cui associare le righe
	 * @return List<RigaStrutturaPartite>
	 */
	List<RigaStrutturaPartite> creaRigheStrutturaPartite(StrutturaPartita strutturaPartita, int numeroRate,
			int intervallo);

	/**
	 * Metodo che fornisce le variabili usabili dalle formule delle partite.
	 * 
	 * @return Map<String, BigDecimal>
	 */
	Map<String, BigDecimal> getVariabiliAreaPartita();

	/**
	 * Metodo per salvare una categoria rata generata dal codice di pagamento (Es. F24, ....) Test OK.
	 * 
	 * @param categoriaRata
	 *            la categoria rata da salvare
	 * @return CategoriaRata
	 */
	CategoriaRata salvaCategoriaRata(CategoriaRata categoriaRata);

	/**
	 * Salva la riga struttura.
	 * 
	 * @param rigaStrutturaPartite
	 *            la riga da salvare
	 * @return RigaStrutturaPartite
	 */
	RigaStrutturaPartite salvaRigaStrutturaPartite(RigaStrutturaPartite rigaStrutturaPartite);

	/**
	 * Salva tutta la StrutturaPartite, con le righe e formule Test OK.
	 * 
	 * @param strutturaPartite
	 *            la struttura partita da salvare
	 * @return StrutturaPartita salvata
	 */
	StrutturaPartita salvaStrutturaPartita(StrutturaPartita strutturaPartite);

	/**
	 * Salva il {@link TipoAreaPartita}.
	 * 
	 * @param tipoAreaPartita
	 *            il tipo area partita da salvare
	 * @return TipoAreaPartita
	 * @throws ModificaTipoAreaConDocumentoException
	 *             se vengono modificate delle proprietà del tipo documento e ci sono dei documenti di quel tipo viene
	 *             sollevata questa eccezione
	 */
	TipoAreaPartita salvaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) throws ModificaTipoAreaConDocumentoException;

	/**
	 * Esegue il salvataggi di {@link TipoDocumentoBasePartite} e lo restituisce salvato.
	 * 
	 * @param tipoDocumentoBase
	 *            il tipo documento base parite da salvare
	 * @return TipoDocumentoBasePartite
	 */
	TipoDocumentoBasePartite salvaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase);

}
