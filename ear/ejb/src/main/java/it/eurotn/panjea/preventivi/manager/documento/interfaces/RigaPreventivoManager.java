package it.eurotn.panjea.preventivi.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface RigaPreventivoManager {

	/**
	 * Aggiunge o rimuove, in base all'azione, ad ogni riga dell'area uno sconto come primo sconto, shiftando quelli già
	 * presenti.
	 * 
	 * @param areaPreventivo
	 *            area di riferimento
	 * @param importoSconto
	 *            importo dello sconto commerciale da aggiungere. Se lo sconto è <code>null</code> o 0 verrà tolto dalle
	 *            righe se presente
	 */
	void aggiornaScontoCommerciale(AreaPreventivo areaPreventivo, BigDecimal importoSconto);

	/**
	 * 
	 * @param righePreventivoDaCambiare
	 *            righe da collegare all'ultima testata inserita
	 */
	void collegaTestata(Set<Integer> righePreventivoDaCambiare);

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
	 * @param areaPreventivo
	 *            area ordine di riferimento
	 * @param note
	 *            note
	 * @return <code>true</code> se la riga viene generata e salvata correttamente
	 */
	boolean creaRigaNoteAutomatica(AreaPreventivo areaPreventivo, String note);

	/**
	 * 
	 * @return dao generico per la gestione delle righe ordine.
	 */
	RigaPreventivoDAO getDao();

	/**
	 * 
	 * @param parametriCreazioneRigaArticolo
	 *            parametriCreazioneRigaArticolo parametri per determinare il dao che gestisce la riga
	 * @return dao specifico per la gestione della rigaPreventivo(che può essere un componente,distinta,padre,normale)
	 */
	RigaPreventivoDAO getDao(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

	/**
	 * 
	 * @param rigaPreventivo
	 *            riga ordine da gestire con il dao richiesto
	 * @return dao specifico per la gestione della rigaOridne(che può essere un componente,distinta,padre,normale)
	 */
	RigaPreventivoDAO getDao(RigaPreventivo rigaPreventivo);

	/**
	 * 
	 * Crea e aslva le righe di magazzino per il raggruppamento voluto.
	 * 
	 * @param idAreaPreventivo
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
	 * @param idAgente
	 *            id agente
	 * @param tipologiaCodiceIvaAlternativo
	 *            tipologia codice iva sots.
	 * @param percentualeScontoCommerciale
	 *            percentuale sconto commerciale
	 */
	void inserisciRaggruppamentoArticoli(Integer idAreaPreventivo, ProvenienzaPrezzo provenienzaPrezzo,
			Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
			Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
			Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
			Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
			BigDecimal percentualeScontoCommerciale);

	/**
	 * Ricalcola i prezzi e sconti della riga.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 * @param codicePagamento
	 *            codice di pagamento
	 * @return riga articolo ricalcolata
	 */
	RigaPreventivo ricalcolaPrezziRigaArticolo(RigaArticolo rigaArticolo, CodicePagamento codicePagamento);
}
