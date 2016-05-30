package it.eurotn.panjea.lotti.manager.verifica;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.LottiManagerBean.RimanenzaLotto;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.VerificaModuloManager;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.RimanenzaLottiModuloManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RimanenzaLottiModuloManagerBean")
public class RimanenzaLottiModuloManagerBean implements VerificaModuloManager {

	@EJB
	private LottiManager lottiManager;

	@Override
	public void verifica(RigaMagazzino rigaMagazzino, DepositoLite deposito) throws RimanenzaLottiNonValidaException {

		if (rigaMagazzino != null && !(rigaMagazzino instanceof RigaArticolo)) {
			return;
		}

		ArticoloLite articolo = null;
		if (rigaMagazzino != null) {
			articolo = ((RigaArticolo) rigaMagazzino).getArticolo();
		}

		if (articolo != null && articolo.getTipoLotto() == TipoLotto.NESSUNO) {
			return;
		}

		// carico i lotti per l'articolo con le rimanenze negative
		List<Lotto> lotti = lottiManager.caricaLotti(articolo, deposito, RimanenzaLotto.NEGATIVA,null,null,null);

		RimanenzaLottiNonValidaException rimanenzaException = new RimanenzaLottiNonValidaException(
				(RigaArticolo) rigaMagazzino);
		rimanenzaException.getLotti().addAll(lotti);

		if (!rimanenzaException.getLotti().isEmpty()) {
			throw rimanenzaException;
		}
	}
}
