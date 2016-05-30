/**
 * 
 */
package it.eurotn.panjea.iva.rich.editors.righeiva.factory;

import it.eurotn.panjea.iva.domain.AreaIva;

import java.math.BigDecimal;

/**
 * Interfaccia che identifica un quadratore dell'area iva, le classi che lo implementano definiscono per quali regole
 * viene quadrata l'area iva ricevuta da testare.
 * 
 * @author Leonardo
 */
public interface StrategiaQuadraAreaIva {

	/**
	 * Restituisce l'importo squadrato dell'area iva.
	 * 
	 * @param areaIva
	 *            area iva di riferimento
	 * @return importo squadrato
	 */
	BigDecimal getImportoSquadrato(AreaIva areaIva);

	/**
	 * Verifica se l'area iva risulta essere quadrata.
	 * 
	 * @param areaIva
	 *            l'area iva da verificare
	 * @return true se quadrata, false altrimenti
	 */
	boolean isQuadrata(AreaIva areaIva);

}
