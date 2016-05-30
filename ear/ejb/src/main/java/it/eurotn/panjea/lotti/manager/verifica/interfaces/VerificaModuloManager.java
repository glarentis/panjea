package it.eurotn.panjea.lotti.manager.verifica.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;

import javax.ejb.Local;

@Local
public interface VerificaModuloManager {

	/**
	 * Esegue tutte le verifiche relative ai lotti per la riga magazzino di riferimento.
	 * 
	 * @param rigaMagazzino
	 *            riga magazzino
	 * @param deposito
	 *            deposito di riferimento
	 * @throws RigheLottiNonValideException
	 *             rilanciata se le righe lotti non sono valide per la riga articolo
	 * @throws RimanenzaLottiNonValidaException
	 *             rilanciata se le quantità assegnate alle righe lotto eccedono dalle rimanenze
	 */
	void verifica(RigaMagazzino rigaMagazzino, DepositoLite deposito) throws RigheLottiNonValideException,
			RimanenzaLottiNonValidaException;
}
