/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface AreaAccreditoAssegnoContabilitaManager {

	/**
	 * Cancella l'area Contabile dell'areaAccreditoAssegno se esiste.
	 * 
	 * @param areaAccreditoAssegno
	 *            l'area accredito assegno di cui cancellare l'area contabile
	 */
	void cancellaAreaContabileAccreditoAssegno(AreaAccreditoAssegno areaAccreditoAssegno);

	/**
	 * Crea l'area contabile dell'accreditoAssegno.
	 * 
	 * @param areaAccreditoAssegno
	 *            l'area accredito assegno di cui generare l'area contabile
	 */
	void creaAreaContabileAccreditoAssegno(AreaAccreditoAssegno areaAccreditoAssegno);
}
