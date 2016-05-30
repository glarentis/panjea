/**
 *
 */
package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.panjea.anagrafica.util.StatisticaEntitaDTO;

import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface StatisticheAnagraficaManager {

	/**
	 * Carica le statistiche generali delle entità.
	 *
	 * @return statistiche
	 */
	List<StatisticaEntitaDTO> caricaStatisticheEntita();

}
