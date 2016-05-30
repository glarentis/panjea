/**
 * 
 */
package it.eurotn.panjea.offerte.manager.interfaces;

import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;

import javax.ejb.Local;

/**
 * @author Leonardo
 * 
 */
@Local
public interface AreaOffertaCancellaManager {

	/**
	 * Cancella l'area offerta e del documento legato.
	 * 
	 * @param areaOfferta
	 *            area da cancellare
	 */
	void cancellaAreaOfferta(AreaOfferta areaOfferta);

	/**
	 * Cancella solo l'area offerta senza cancellare il documento legato.
	 * 
	 * @param areaOfferta
	 *            area da cancellare
	 */
	void cancellaAreaOffertaNoCheck(AreaOfferta areaOfferta);

	/**
	 * Cancella la riga offerta.
	 * 
	 * @param rigaOfferta
	 *            riga da cancellare
	 */
	void cancellaRigaOfferta(RigaOfferta rigaOfferta);

	/**
	 * Cancella tutte le righe offerta dell'area.
	 * 
	 * @param idAreaOfferta
	 *            area di riferimento
	 */
	void cancellaRigheOfferta(Integer idAreaOfferta);

}
