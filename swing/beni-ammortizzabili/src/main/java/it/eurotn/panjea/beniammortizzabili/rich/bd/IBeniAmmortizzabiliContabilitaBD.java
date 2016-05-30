/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.bd;

import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;

/**
 * @author fattazzo
 *
 */
public interface IBeniAmmortizzabiliContabilitaBD {

	/**
	 * Conferma tutte le aree contabili che fanno riferimento alla simulazione.
	 *
	 * @param idSimulazione
	 *            id della simulazione
	 */
	void confermaAreeContaibliSimulazione(Integer idSimulazione);

	/**
	 * Crea le aree contabili della simulazione.
	 *
	 * @param idSimulazione
	 *            id della simulazione
	 * @throws SottocontiBeniNonValidiException
	 *             sollevata se ci sono sottoconti non validi su specie, sottospecie o beni
	 */
	void creaAreeContabili(Integer idSimulazione) throws SottocontiBeniNonValidiException;
}
