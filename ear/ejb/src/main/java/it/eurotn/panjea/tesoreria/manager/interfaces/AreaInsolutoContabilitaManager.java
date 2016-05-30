package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.util.Set;

import javax.ejb.Local;

@Local
public interface AreaInsolutoContabilitaManager {

	/**
	 * Esegue la creazione dei documenti contabili dell'area insoluti.
	 * 
	 * @param areaInsoluti
	 *            area insoluti
	 * @param effetti
	 *            effetti
	 */
	void creaAreaContabileInsoluto(AreaInsoluti areaInsoluti, Set<Effetto> effetti);
}
