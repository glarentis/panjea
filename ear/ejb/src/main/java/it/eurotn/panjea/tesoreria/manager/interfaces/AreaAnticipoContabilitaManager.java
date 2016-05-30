package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;

import javax.ejb.Local;

@Local
public interface AreaAnticipoContabilitaManager {

	/**
	 * Esegue la creazione dei documenti contabili dell'area anticipo.
	 * 
	 * @param areaAnticipo area anticipo
	 */
	void creaAreaContabileAnticipo(AreaAnticipo areaAnticipo);
}
