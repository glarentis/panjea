/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.exception;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;

import java.util.Map;

/**
 * @author fattazzo
 *
 */
public class SottocontiBeniNonValidiException extends Exception {

	private static final long serialVersionUID = -6967636904460025166L;

	private Map<EntityBase, SottocontiBeni> sottocontiBeniNonValidi;

	/**
	 * Costruttore.
	 *
	 * @param sottocontiBeniNonValidi
	 *            sottoconti non validi
	 */
	public SottocontiBeniNonValidiException(final Map<EntityBase, SottocontiBeni> sottocontiBeniNonValidi) {
		super();
		this.sottocontiBeniNonValidi = sottocontiBeniNonValidi;
	}

	/**
	 * @return the sottocontiBeniNonValidi
	 */
	public Map<EntityBase, SottocontiBeni> getSottocontiBeniNonValidi() {
		return sottocontiBeniNonValidi;
	}

}
