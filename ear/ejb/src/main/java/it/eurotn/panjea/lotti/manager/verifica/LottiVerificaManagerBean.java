package it.eurotn.panjea.lotti.manager.verifica;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.LottiVerificaManager;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.VerificaModuloManager;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.LottiVerificaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LottiVerificaManager")
public class LottiVerificaManagerBean implements LottiVerificaManager {

	@EJB(beanName = "QuantitaRigheLottoVerificaModuloManagerBean")
	private VerificaModuloManager quantitaRigheLottoVerificaModuloManagerBean;

	@EJB(beanName = "RimanenzaLottiModuloManagerBean")
	private VerificaModuloManager rimanenzaLottiModuloManagerBean;

	@Override
	public void verifica(List<String> codiciArticolo, DepositoLite deposito) throws RigheLottiNonValideException,
	RimanenzaLottiNonValidaException {

		quantitaRigheLottoVerificaModuloManagerBean.verifica(null, deposito);

		try {
			rimanenzaLottiModuloManagerBean.verifica(null, deposito);
		} catch (RimanenzaLottiNonValidaException e) {

			// Rilancio l'eccezione solo se ci solo lotti con rimanenza non valida che si riferiscono agli articoli
			// richiesti
			List<Lotto> lottiArticoli = new ArrayList<Lotto>();
			List<Lotto> lotti = e.getLotti();
			for (Lotto lotto : lotti) {
				if (codiciArticolo != null && codiciArticolo.contains(lotto.getArticolo().getCodice())) {
					lottiArticoli.add(lotto);
				}
			}

			if (!lottiArticoli.isEmpty()) {
				e.getLotti().retainAll(lottiArticoli);
				throw e;
			}
		}

	}

	@Override
	public void verifica(RigaMagazzino rigaMagazzino, DepositoLite deposito) throws RigheLottiNonValideException,
	RimanenzaLottiNonValidaException {

		quantitaRigheLottoVerificaModuloManagerBean.verifica(rigaMagazzino, deposito);

		try {
			rimanenzaLottiModuloManagerBean.verifica(rigaMagazzino, deposito);
		} catch (RimanenzaLottiNonValidaException e) {
			if (!(rigaMagazzino instanceof RigaArticolo)) {
				throw e;
			}
			// Rilancio l'eccezione solo se ci solo lotti con rimanenza non valida che si riferiscono all'articolo delle
			// riga. Tutti gli altri li tolgo.
			List<Lotto> lottiArticoloRiga = new ArrayList<Lotto>();
			List<Lotto> lotti = e.getLotti();
			for (Lotto lotto : lotti) {
				if (lotto.getArticolo().equals(((RigaArticolo) rigaMagazzino).getArticolo())) {
					lottiArticoloRiga.add(lotto);
				}
			}

			if (!lottiArticoloRiga.isEmpty()) {
				e.getLotti().retainAll(lottiArticoloRiga);
				throw e;
			}
		}

	}

}
