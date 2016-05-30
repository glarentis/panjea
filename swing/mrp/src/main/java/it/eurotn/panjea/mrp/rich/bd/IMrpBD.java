package it.eurotn.panjea.mrp.rich.bd;

import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;

import java.util.Date;
import java.util.List;

public interface IMrpBD {

	String BEAN_ID = "mrpBD";

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
	 */
	void calcolaMrp(int numBucket, Date startDate, Integer idAreaOrdine);

	/**
	 *
	 * @param selectedObjects
	 *            righe da cancellare
	 */
	void cancellaRigheRisultati(List<RisultatoMrpFlat> selectedObjects);

	/**
	 * @param parametriMrpRisultato
	 *            parametri
	 * @return parametri per il calcolo
	 */
	List<RisultatoMrpFlat> caricaRisultatoMrp(ParametriMrpRisultato parametriMrpRisultato);

	/**
	 * genera gli ordini in base alla tabella del risultato mrp.
	 *
	 * @param idRisultatiMrp
	 *            array contenente gli id dei risultati di cui generare gli ordini
	 * @return timeStmapCreazione
	 */
	Long generaOrdini(Integer[] idRisultatiMrp);

	/**
	 *
	 * @param righeMrpFlat
	 *            rihge da salvare
	 * @return righe salvate
	 */
	List<RisultatoMrpFlat> salvaRigheRisultato(List<RisultatoMrpFlat> righeMrpFlat);

	/**
	 * Svuota i risultati mrp.
	 */
	void svuotaRigheRisultati();
}
