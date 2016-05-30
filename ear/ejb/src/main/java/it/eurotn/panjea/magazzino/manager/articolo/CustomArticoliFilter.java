/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.articolo;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface CustomArticoliFilter {

	/**
	 * Filtra gli articoli in base ai parametri presenti.
	 * 
	 * @param articoli
	 *            articoli da filtrare
	 * @param params
	 *            parametri
	 * @return articoli filtrati
	 */
	List<ArticoloRicerca> filtra(List<ArticoloRicerca> articoli, Map<String, Object> params);
}
