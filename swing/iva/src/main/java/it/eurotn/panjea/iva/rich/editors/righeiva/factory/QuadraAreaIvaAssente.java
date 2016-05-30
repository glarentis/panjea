/**
 * 
 */
package it.eurotn.panjea.iva.rich.editors.righeiva.factory;

import it.eurotn.panjea.iva.domain.AreaIva;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Quadratore per l'area iva di un documento disattivata.
 * 
 * @author Leonardo
 */
public class QuadraAreaIvaAssente implements StrategiaQuadraAreaIva {

	private static Logger logger = Logger.getLogger(QuadraAreaIvaAssente.class);

	@Override
	public BigDecimal getImportoSquadrato(AreaIva areaIva) {
		return BigDecimal.ZERO;
	}

	@Override
	public boolean isQuadrata(AreaIva areaIva) {
		logger.debug("--> Enter isQuadrata");
		logger.debug("--> Exit isQuadrata " + true);
		return true;
	}

}
