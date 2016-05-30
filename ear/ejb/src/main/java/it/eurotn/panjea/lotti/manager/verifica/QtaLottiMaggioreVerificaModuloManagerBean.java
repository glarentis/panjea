package it.eurotn.panjea.lotti.manager.verifica;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.VerificaModuloManager;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;

public class QtaLottiMaggioreVerificaModuloManagerBean implements VerificaModuloManager {

	@Override
	public void verifica(RigaMagazzino rigaMagazzino, DepositoLite deposito) throws RigheLottiNonValideException,
			RimanenzaLottiNonValidaException {
		// TODO Auto-generated method stub

	}

}
