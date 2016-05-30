package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;

import javax.ejb.Local;

/**
 *
 * Calcola le statistiche per un articolo.
 *
 * @author giangi
 * @version 1.0, 14/set/2010
 *
 */
@Local
public interface StatisticheArticoloManager {

	/**
	 * Carica le statistiche per un articolo.
	 *
	 * @param articolo
	 *            articolo per il quale caricare le statistiche. Può avere avvalorato solamente l'id.
	 * @param anno
	 *            anno per il quale calcolare le statistiche
	 * @return {@link StatisticheArticolo}
	 */
	StatisticheArticolo caricaStatisticheArticolo(Articolo articolo, Integer anno);

}
