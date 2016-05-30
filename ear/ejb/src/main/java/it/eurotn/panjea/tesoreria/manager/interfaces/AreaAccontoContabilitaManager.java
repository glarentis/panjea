package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaAcconto;

public interface AreaAccontoContabilitaManager {

	/**
	 * Cancella l'area contabile legata all'acconto.
	 * 
	 * @param areaAcconto
	 *            arae acconto per la quale cancellare l'area contabile
	 */
	void cancellaAreaContabileAcconto(AreaAcconto areaAcconto);

	/**
	 * Crea un'area contabile per l'acconto.
	 * 
	 * @param areaAcconto
	 *            areaAcconto per la quale creare l'area contabile
	 */
	void creaAreaContabileAcconto(AreaAcconto areaAcconto);

	/**
	 * Crea una area contabile per l'acconto iva.
	 * 
	 * @param areaAcconto
	 *            areaAcconto per la quale creare l'area contabile
	 */
	void creaAreaContabileAccontoIva(AreaAcconto areaAcconto);

}