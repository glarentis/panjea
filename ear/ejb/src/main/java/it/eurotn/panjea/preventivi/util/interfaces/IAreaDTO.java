package it.eurotn.panjea.preventivi.util.interfaces;

import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.rate.domain.AreaRate;

public interface IAreaDTO<E extends IAreaDocumentoTestata> {

	/**
	 * 
	 * @return areaDocumento
	 */
	E getAreaDocumento();

	/**
	 * 
	 * @return areaRate
	 */
	AreaRate getAreaRate();

	/**
	 * 
	 * @return true se contien area rate
	 */
	boolean isAreaRateEnabled();

	/**
	 * 
	 * @param areaDocumento
	 *            areaDocumento
	 */
	void setAreaDocumento(E areaDocumento);
}
