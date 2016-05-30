package it.eurotn.panjea.lotti.manager.verifica;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.VerificaModuloManager;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.QuantitaRigheLottoVerificaModuloManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.QuantitaRigheLottoVerificaModuloManagerBean")
public class QuantitaRigheLottoVerificaModuloManagerBean implements VerificaModuloManager {

	@Override
	public void verifica(RigaMagazzino rigaMagazzino, DepositoLite deposito) throws RigheLottiNonValideException {

		if (rigaMagazzino != null && (rigaMagazzino instanceof RigaArticolo)
				&& !((RigaArticolo) rigaMagazzino).isLottiValid()) {
			throw new RigheLottiNonValideException((RigaArticolo) rigaMagazzino);
		}
	}
}
