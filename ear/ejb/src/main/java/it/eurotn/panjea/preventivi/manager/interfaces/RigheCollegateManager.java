/**
 * 
 */
package it.eurotn.panjea.preventivi.manager.interfaces;

import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;

import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface RigheCollegateManager {

	/**
	 * Carica tutte le righe collegate alle riga preventivo passata come parametro.
	 * 
	 * @param rigaPreventivo
	 *            riga preventivo di riferimento
	 * @return righe collegate caricate
	 */
	List<RigaDestinazione> caricaRigheCollegate(RigaPreventivo rigaPreventivo);

}
