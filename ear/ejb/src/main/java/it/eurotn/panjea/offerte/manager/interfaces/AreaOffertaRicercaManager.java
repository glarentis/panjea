/**
 * 
 */
package it.eurotn.panjea.offerte.manager.interfaces;

import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.util.ParametriRicercaOfferte;

import java.util.List;

import javax.ejb.Local;

/**
 * @author Leonardo
 * 
 */
@Local
public interface AreaOffertaRicercaManager {

	/**
	 * Ricerca tutte le aree offerte che corrispondono ai parametri di ricerca.
	 * 
	 * @param parametriRicercaOfferte
	 *            parametri di ricerca
	 * @return aree trovate
	 */
	List<RigaOfferta> ricercaOfferte(ParametriRicercaOfferte parametriRicercaOfferte);

}
