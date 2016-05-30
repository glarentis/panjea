/**
 *
 */
package it.eurotn.panjea.mrp.manager.interfaces;

import java.util.Date;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface MrpCalcoloManager {

	/**
	 * Il bucket di 60 giorni extra è per gli ordini in ritardo.
	 */
	int BUCKET_ZERO = 365;

	/**
	 * Calcola MRP.
	 *
	 * @param numBucket
	 *            lunghezza dell'orizzonte temporale
	 * @param startDate
	 *            data di inizio
	 * @param idAreaOrdine
	 *            limita il calcolo dell'mrp ad un particolare ordine
	 */
	void calcola(int numBucket, Date startDate, Integer idAreaOrdine);

}
