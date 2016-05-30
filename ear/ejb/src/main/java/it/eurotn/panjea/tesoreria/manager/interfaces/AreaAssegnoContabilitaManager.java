/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface AreaAssegnoContabilitaManager {

	/**
	 * Cancella l'areaContabile dell'areaAssegno passata.
	 * 
	 * @param areaAssegno
	 *            l'area assegno di cui cancellare l'areaContabile
	 */
	void cancellaAreaContabileAssegno(AreaAssegno areaAssegno);

	/**
	 * Carica la lista di AreaAssegno associate ad AreaAccreditoAssegno e per ognuna di esse, ne cancella l'area
	 * contabile.
	 * 
	 * @param areaAccreditoAssegno
	 *            l'areaAccreditoAssegno per trovare la lista di AreaAssegno collegate di cui cancellarne
	 *            l'areaContabile.
	 */
	void cancellaAreeContabiliAssegni(AreaAccreditoAssegno areaAccreditoAssegno);

	/**
	 * Crea l'areaContabile per l'AreaAssegno passata.
	 * 
	 * @param areaAccreditoAssegno
	 *            l'area accredito per recuperare le informazioni utili quali la data documento, il tipo Area Contabile
	 *            e il rapporto bancario
	 * @param areaAssegno
	 *            l'assegno di cui generare l'area contabile
	 */
	void creaAreaContabileAssegno(AreaAccreditoAssegno areaAccreditoAssegno, AreaAssegno areaAssegno);

	/**
	 * Crea l'areaContabile per ogni AreaAssegno legato all'areaAccreditoAssegno.
	 * 
	 * @param areaAccreditoAssegno
	 *            l'area accredito assegno per trovare le aree assegno di cui creare l'area contabile
	 */
	void creaAreeContabiliAssegni(AreaAccreditoAssegno areaAccreditoAssegno);

}
