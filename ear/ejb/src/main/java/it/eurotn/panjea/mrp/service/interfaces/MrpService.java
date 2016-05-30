package it.eurotn.panjea.mrp.service.interfaces;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;

import java.net.ConnectException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

@Remote
public interface MrpService {
	/**
	 * Calcola le giacenze per ogni articolo e deposito.
	 *
	 * @param idDepositoLite
	 *            deposito
	 * @param data
	 *            ata per il calcolo
	 * @return giacenze
	 */
	List<Giacenza> calcolaGiacenze(int idDepositoLite, Date data);

	/**
	 * Calcola mrp.<br/>
	 * Cancella un eventuale mrp già presente e pubblica sulla coda panjeamdb l'inizio del calcolo.
	 *
	 * @param numBucket
	 *            numero di bucket da calcolare
	 * @param startDate
	 *            data iniziale per il calcolo
	 * @param idAreaOrdine
	 *            limita il calcolo dell'mrp ad un particolare ordine
	 * @throws ConnectException
	 *             ConnectException
	 */
	void calcolaMrp(int numBucket, Date startDate, Integer idAreaOrdine) throws ConnectException;

	/**
	 *
	 * @param selectedObjects
	 *            righe da cancellare
	 */
	void cancellaRigheRisultati(List<RisultatoMrpFlat> selectedObjects);

	/**
	 *
	 * @return articolo con i dati di anagrafica ed eventualmente i dati del foritore abituale
	 */
	List<ArticoloAnagrafica> caricaArticoloAnagrafica();

	/**
	 *
	 *
	 * @return adjacent list delle disinte base.
	 */
	List<Object[]> caricaBomBase();

	/**
	 * @param configurazioniUtilizzate
	 *            configurazioni utilizzate nelle righe ordini che verranno considerate.
	 * @return adjacent list delle disinte base.
	 */
	List<Object[]> caricaBomConfigurazioni(Set<Integer> configurazioniUtilizzate);

	/**
	 *
	 * @param configurazioniUtilizzate
	 *            configurazioni utilizzate
	 * @return componenti bom e bom configurazioni
	 */
	List<Object[]> caricaBoms(Set<Integer> configurazioniUtilizzate);

	/**
	 *
	 * @param parametriMrpRisultato
	 *            parametri
	 * @return risultato del calcolo mrp
	 */
	List<RisultatoMrpFlat> caricaRisultatoMrp(ParametriMrpRisultato parametriMrpRisultato);

	/**
	 * Esplode tutte le distinte.
	 *
	 * @return mappa con chiave id articolo (distinta) e valore la struttura della bom
	 */
	Map<ArticoloConfigurazioneKey, Bom> esplodiBoms();

	/**
	 * genera gli ordini in base alla tabella del risultato mrp.
	 *
	 * @param idRisultatiMrp
	 *            array contenente gli id dei risultati di cui generare gli ordini
	 * @return timeStampCreazione
	 */
	Long generaOrdini(Integer[] idRisultatiMrp);

	/**
	 * @return id dei depositi abilitati per il calcolo MRP.
	 */
	List<Integer> getIdDepositi();

	/**
	 * /**
	 *
	 * @param righeMrpFlat
	 *            rihge da salvare
	 * @return righe salvate
	 */
	List<RisultatoMrpFlat> salvaRigheRisultato(List<RisultatoMrpFlat> righeMrpFlat);

	/**
	 * @param risultati
	 *            risultati del calcolo mrp da salvare
	 */
	void salvaRisultatoMRP(List<RisultatoMRPArticoloBucket> risultati);

	/**
	 * Svuota i risultati mrp.
	 */
	void svuotaRigheRisultati();
}
