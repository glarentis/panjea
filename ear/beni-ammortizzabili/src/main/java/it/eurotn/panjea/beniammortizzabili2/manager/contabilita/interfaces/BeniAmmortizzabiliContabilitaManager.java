/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces;

import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliDaSimulazionePresentiException;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;

import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface BeniAmmortizzabiliContabilitaManager {

	/**
	 * Cancella le aree contabili a cui si riferisce la simulazione.
	 *
	 * @param idSimulazione
	 *            id simulazione
	 */
	void cancellaAreeContabiliSimulazione(Integer idSimulazione);

	/**
	 * Carica le aree contabili legate alla simulaizone.
	 *
	 * @param idSimulazione
	 *            id delal simulazione
	 * @return aree caricate
	 */
	List<AreaContabile> caricaAreeContabiliSimulazione(Integer idSimulazione);

	/**
	 * Conferma tutte le aree contabili che fanno riferimento alla simulazione.
	 *
	 * @param idSimulazione
	 *            id della simulazione
	 * @throws RigheContabiliNonValidiException .
	 * @throws ContabilitaException .
	 */
	void confermaAreeContaibliSimulazione(Integer idSimulazione) throws ContabilitaException,
			RigheContabiliNonValidiException;

	/**
	 * Crea le aree contabili della simulazione.
	 *
	 * @param idSimulazione
	 *            id della simulazione
	 * @throws AreeContabiliDaSimulazionePresentiException
	 *             sollevata se esistono già aree contabili generate delle simulazioni nell'anno
	 * @throws SottocontiBeniNonValidiException
	 *             sollevata se ci sono sottoconti non validi su specie, sottospecie o beni
	 */
	void creaAreeContabili(Integer idSimulazione) throws AreeContabiliDaSimulazionePresentiException,
	SottocontiBeniNonValidiException;

}
