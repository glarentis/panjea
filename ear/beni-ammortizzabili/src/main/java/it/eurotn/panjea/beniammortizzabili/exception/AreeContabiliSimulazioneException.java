/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.exception;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fattazzo
 *
 */
public class AreeContabiliSimulazioneException extends Exception {

	private static final long serialVersionUID = -86700056390932588L;

	private List<AreaContabile> areeContabiili;

	/**
	 * Costruttore.
	 *
	 * @param areeContabili
	 *            aree contabili
	 */
	public AreeContabiliSimulazioneException(final List<AreaContabile> areeContabili) {
		super();
		this.areeContabiili = areeContabili;
	}

	/**
	 * Restituisce una mappa che contiene come chiave lo stato dell'area e come valore il numero delle aree in quello
	 * stato.
	 *
	 * @return mappa
	 */
	public Map<StatoAreaContabile, Integer> getNumeroAreePerStato() {

		Map<StatoAreaContabile, Integer> mapStati = new HashMap<StatoAreaContabile, Integer>();

		for (AreaContabile areaContabile : areeContabiili) {

			Integer numeroAC = mapStati.get(areaContabile.getStatoAreaContabile());
			if (numeroAC == null) {
				numeroAC = new Integer(0);
			}
			numeroAC++;
			mapStati.put(areaContabile.getStatoAreaContabile(), numeroAC);
		}

		return mapStati;
	}

}
