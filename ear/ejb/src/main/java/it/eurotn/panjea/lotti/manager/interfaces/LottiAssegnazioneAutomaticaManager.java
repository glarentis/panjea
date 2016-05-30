package it.eurotn.panjea.lotti.manager.interfaces;

import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;

import javax.ejb.Local;

@Local
public interface LottiAssegnazioneAutomaticaManager {

	/**
	 * Assegna automaticamente tutti i lotti disponibili alla riga articolo e la salva. Se la quantità dei lotti non
	 * risulta uguale a quella della riga verrà rilanciata una {@link RimanenzaLottiNonValidaException} che conterrà
	 * tutte le righe lotto generate.
	 * 
	 * @param rigaArticolo
	 *            riga di riferimento
	 * @return riga salvata
	 * @throws RimanenzaLottiNonValidaException
	 *             rilanciata nel caso in cui non ci siano
	 * @throws QtaLottiMaggioreException
	 *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga articolo
	 */
	RigaMagazzino assegnaLotti(RigaArticolo rigaArticolo) throws RimanenzaLottiNonValidaException,
			QtaLottiMaggioreException;
}
