/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface ListinoStatisticheManager {

	/**
	 * Carica il confronto in base ai parametri specificati.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return confronto
	 */
	ConfrontoListinoDTO caricaConfrontoListino(ParametriRicercaConfrontoListino parametri);

}
