package it.eurotn.panjea.ordini.manager.documento.evasione.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoLotto;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface DistintaCaricoManager {

	/**
	 *
	 * @param righeDistintaDaAggiornare
	 *            righe della distinta di carico da aggiornare
	 * @return righe della distinta con i dati di evasione instanziati
	 */
	List<RigaDistintaCarico> aggiornaRigheCaricoConDatiEvasione(List<RigaDistintaCarico> righeDistintaDaAggiornare);

	/**
	 * Associa ad ogni {@link EntitaLite} il {@link TipoAreaMagazzino} come tipo documento di evasione.
	 *
	 * @param map
	 *            mappa che contiene gli estremi {@link EntitaLite} - {@link TipoAreaMagazzino} per l'associazione
	 */
	void associaTipoAreaEvasione(Map<TipoAreaMagazzino, Set<EntitaLite>> map);

	/**
	 * Calcola la giacenza ad una determinata data per tutti gli articoli di un deposito.<br/>
	 * La giacenza ritornata comprende la giacenza di magazzino meno le righe che sono sulle distinte di carico.
	 *
	 * @param data
	 *            data per la giacenza
	 * @param depositoLite
	 *            deposito interessato alla giacenza
	 * @return mappa contenente l'articolo come chiave e la sua giacenza come valore
	 */
	Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data);

	/**
	 * Rimuove una lista di righedistinteCarico.
	 *
	 * @param righeDistintaCarico
	 *            righe da rimuovere
	 */
	void cancellaRigheDistintaCarico(Set<RigaDistintaCarico> righeDistintaCarico);

	/**
	 * Cancella tutte le righe distinte carico lotti legate alla rida distinta.
	 *
	 * @param rigaDistintaCarico
	 *            riga distinta carico
	 */
	void cancellaRigheDistintaCaricoLotti(RigaDistintaCarico rigaDistintaCarico);

	/**
	 * Carica tutte le {@link RigaDistintaCarico} delle distinte .
	 *
	 * @param distinte
	 *            distinte di riferimento
	 * @return righe caricate
	 */
	List<RigaDistintaCarico> caricaRigheDistintaCarico(List<DistintaCarico> distinte);

	/**
	 * Carica le {@link RigaDistintaCaricoLotto} della {@link RigaDistintaCarico}.
	 *
	 * @param rigaDistintaCarico
	 *            riga distinta
	 * @return righe distinta lotto
	 */
	List<RigaDistintaCaricoLotto> caricaRigheDistintaCaricoLotto(RigaDistintaCarico rigaDistintaCarico);

	/**
	 * Carica tutte le righe associate ad una distinta di carico.<br/>
	 * Essendo associate ad una distinta di carico queste sono "in magazzino" pronte per essere evase
	 *
	 * @return righe attaulamente in magazzino per essere evase
	 */
	List<RigaDistintaCarico> caricaRigheInMagazzino();

	/**
	 * Carica tutte le righe ordine che possono essere evase in base ai parametri di ricerca.
	 *
	 * @param parametriRicercaEvasione
	 *            parametri di ricerca
	 * @return {@link List} di {@link RigaDistintaCarico}
	 */
	List<RigaDistintaCarico> caricaRighePerEvasione(ParametriRicercaEvasione parametriRicercaEvasione);

	/**
	 * Crea le distinte di carico con le righe evasione passate come parametro.<br/>
	 * Viene creata una distinta di carico per ogni deposito.
	 *
	 * @param righeEvasione
	 *            righe evasione con le quali creare la distinta di carico
	 * @return distinte di carico create (una per deposito).
	 */
	List<DistintaCarico> creaDistintadiCarico(List<RigaDistintaCarico> righeEvasione);

	/**
	 * Evade una lista di righe.
	 *
	 * @param righeEvasione
	 *            righe da evadere
	 * @param documentoEvasione
	 *            documento di destinazione
	 * @throws EvasioneLottiException
	 *             rlanciata se ho lotti obbligatori e non li ho nelle righeEvasione.
	 */
	void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
			throws EvasioneLottiException;

	/**
	 * Effettua l'evasione della lista di righe passate come parametro. <br/>
	 * Crea i documenti di destinazione e li contabilizza.
	 *
	 * @param righeEvasione
	 *            righe da evadere
	 * @param dataEvasione
	 *            data di evasione
	 * @return i documenti evasi
	 * @throws EntitaSenzaTipoDocumentoEvasioneException
	 *             eccezione sollevata nel caso in cui esistano entità senza tipo documento di evasione
	 * @throws ContabilizzazioneException
	 *             sollevata in caso di errori durante la contabilizzazione
	 * @throws ContiBaseException
	 *             sollevata se manca il conto base SpesediIncasso
	 * @throws TipoAreaPartitaDestinazioneRichiestaException
	 *             sollevata se l'ordine che si sta evadendo non prevede un tipo area partita e il documento di
	 *             destinazione si
	 * @throws CodicePagamentoEvasioneAssenteException
	 *             sollevata se non è possibile recuperare il codice pagamento dalla sede
	 * @throws CodicePagamentoAssenteException
	 *             sollevata se l'ordine con tipo area partite non ha un codice di pagamento
	 * @throws LottiException
	 *             errore sui lotti
	 */
	List<AreaMagazzino> evadiOrdini(List<RigaDistintaCarico> righeEvasione, Date dataEvasione)
			throws EntitaSenzaTipoDocumentoEvasioneException, ContabilizzazioneException, ContiBaseException,
			TipoAreaPartitaDestinazioneRichiestaException, CodicePagamentoEvasioneAssenteException,
			CodicePagamentoAssenteException, LottiException;

	/**
	 *
	 * @return numero di ordini pronti per l'evasione (ordini presenti nell {@link DistintaCarico}
	 */
	int verificaNumeroOrdiniDaEvadere();
}