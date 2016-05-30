/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces;

import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface GeneratoreAreaContabileManager {

	/**
	 * Crea le aree contabili della simulazione.
	 *
	 * @param idSimulazione
	 *            id della simulazione
	 * @param tipoAreaContabile
	 *            tipo area contabile da utilizzare per la creazione delle aree
	 * @throws SottocontiBeniNonValidiException
	 *             rilanciata se non tutti i sottoconti sono configurati per specie, sottospecie o bene
	 */
	void creaAreeContabili(Integer idSimulazione, TipoAreaContabile tipoAreaContabile)
			throws SottocontiBeniNonValidiException;

}
