package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.util.StatisticaArticolo;

public interface StatisticaArticoloManager {

	/**
	 * Carica la statistica dell'articolo.
	 *
	 * @param idArticolo
	 *            id articolo
	 * @param anno
	 *            anno
	 * @param idDeposito
	 *            id deposito
	 * @return statistica
	 */
	StatisticaArticolo caricaStatisticaArticolo(Integer idArticolo, Integer anno, Integer idDeposito);
}
