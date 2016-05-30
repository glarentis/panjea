/**
 * 
 */
package it.eurotn.panjea.preventivi.manager.evasione.interfaces;

import it.eurotn.panjea.preventivi.util.RigaEvasione;

import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface PreventiviEvasioneManager {

	/**
	 * Carica tutte le righe che possono essere ancora evase del preventivo di riferimento.
	 * 
	 * @param idAreaPreventivo
	 *            id preventivo da evadere
	 * @return righe evasione
	 */
	List<RigaEvasione> caricaRigheEvasione(Integer idAreaPreventivo);

}
