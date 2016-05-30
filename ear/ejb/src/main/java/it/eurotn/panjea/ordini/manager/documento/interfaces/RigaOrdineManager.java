package it.eurotn.panjea.ordini.manager.documento.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

@Local
public interface RigaOrdineManager {

	/**
	 * Aggiorna la data di consegna in accordo con la data di consegna dell'area ordine.<br>
	 * Vengono modificate le righe con data di consegna uguale alla data di modifica o tutte le righe se la data di
	 * riferimento non è impostata.
	 *
	 * @param areaOrdine
	 *            area di riferimento
	 * @param dataRiferimento
	 *            se impostata la utilizzo come riferimento per filtrare le righe da modificare
	 */
	void aggiornaDataConsegna(AreaOrdine areaOrdine, Date dataRiferimento);

	/**
	 * Aggiunge o rimuove, in base all'azione, ad ogni riga dell'area uno sconto come primo sconto, shiftando quelli già
	 * presenti.
	 *
	 * @param areaOrdine
	 *            area di riferimento
	 * @param importoSconto
	 *            importo dello sconto commerciale da aggiungere. Se lo sconto è <code>null</code> o 0 verrà tolto dalle
	 *            righe se presente
	 */
	void aggiornaScontoCommerciale(AreaOrdine areaOrdine, BigDecimal importoSconto);

	/**
	 * Associa una configurazioneDistinta ad una riga ordine. Se la riga aveva una configurazione personalizzata questa
	 * verrà cancellata.
	 *
	 * @param rigaArticolo
	 *            riga
	 * @param configurazioneDistintaDaAssociare
	 *            conf
	 * @return RigaArticolo
	 */
	RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
			ConfigurazioneDistinta configurazioneDistintaDaAssociare);

	/**
	 *
	 * @param righeOrdineDaCambiare
	 *            righe da collegare all'ultima testata inserita
	 */
	void collegaTestata(Set<Integer> righeOrdineDaCambiare);

	/**
	 * Crea una riga articolo.
	 *
	 * @param parametriCreazioneRigaArticolo
	 *            di creazione
	 * @return nuova rigaArticolo con i parametri settatti.
	 */
	RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

	/**
	 * Crea una riga note automatica per l'area ordine indicata.
	 *
	 * @param areaOrdine
	 *            area ordine di riferimento
	 * @param note
	 *            note
	 * @return <code>true</code> se la riga viene generata e salvata correttamente
	 */
	boolean creaRigaNoteAutomatica(AreaOrdine areaOrdine, String note);

	/**
	 * Divide una riga in più righe con qta e data modificata
	 *
	 * @param rigaOriginale
	 *            id Riga da dividere
	 * @param righeDivise
	 *            righe con qta e dataConsegna da creare
	 */
	void dividiRiga(Integer rigaOriginale, List<RigaArticolo> righeDivise);

	/**
	 * Forza tutte le righe ordine.
	 *
	 * @param righe
	 *            righe da forzare
	 */
	void forzaRigheOrdine(List<Integer> righe);

	/**
	 *
	 * @return dao generico per la gestione delle righe ordine.
	 */
	RigaOrdineDAO getDao();

	/**
	 *
	 * @param parametriCreazioneRigaArticolo
	 *            parametriCreazioneRigaArticolo parametri per determinare il dao che gestisce la riga
	 * @return dao specifico per la gestione della rigaOrdine(che può essere un componente,distinta,padre,normale)
	 */
	RigaOrdineDAO getDao(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

	/**
	 *
	 * @param rigaOrdine
	 *            riga ordine da gestire con il dao richiesto
	 * @return dao specifico per la gestione della rigaOridne(che può essere un componente,distinta,padre,normale)
	 */
	RigaOrdineDAO getDao(RigaOrdine rigaOrdine);

	/**
	 *
	 * Crea e aslva le righe di magazzino per il raggruppamento voluto.
	 *
	 * @param idAreaOrdine
	 *            area magazzino alla quale associare le nuove righe
	 * @param provenienzaPrezzo
	 *            provenienza del prezzo. Da listino (comprende anche i contratti) o da costoUltimo.
	 * @param idRaggruppamentoArticoli
	 *            id del raggruppamento da inserire
	 * @param data
	 *            data del documento
	 * @param idSedeEntita
	 *            sede magazzino documento
	 * @param idListinoAlternativo
	 *            listino alternativo documento
	 * @param idListino
	 *            listino documento
	 * @param importo
	 *            importo con parametri di default per la valuta settati
	 * @param codiceIvaAlternativo
	 *            coedice iva da usare sulla riga articolo, se null viene usato il codice iva dell'articolo
	 * @param idTipoMezzo
	 *            = id del tipo mezzo
	 * @param idZonaGeografica
	 *            id della zona geografica
	 * @param noteSuDestinazione
	 *            imposta se stampare le note riga sul documento di destinazione
	 * @param codiceValuta
	 *            codice della valuta di riferimento
	 * @param codiceLingua
	 *            codice della lingua di riferimento
	 * @param dataConsegna
	 *            data di consegna
	 * @param idAgente
	 *            id agente
	 * @param tipologiaCodiceIvaAlternativo
	 *            tipologia codice iva sots.
	 * @param percentualeScontoCommerciale
	 *            percentuale sconto commerciale
	 * @throws RimanenzaLottiNonValidaException
	 *             rilanciata se il lotto non ha più quantità disponibili
	 */
	void inserisciRaggruppamentoArticoli(Integer idAreaOrdine, ProvenienzaPrezzo provenienzaPrezzo,
			Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
			Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
			Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
			Date dataConsegna, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
			BigDecimal percentualeScontoCommerciale) throws RimanenzaLottiNonValidaException;

	/**
	 * Ricalcola i prezzi e sconti della riga.
	 *
	 * @param rigaArticolo
	 *            riga articolo
	 * @param codicePagamento
	 *            codice di pagamento
	 * @return riga articolo ricalcolata
	 */
	RigaOrdine ricalcolaPrezziRigaArticolo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento);
}
