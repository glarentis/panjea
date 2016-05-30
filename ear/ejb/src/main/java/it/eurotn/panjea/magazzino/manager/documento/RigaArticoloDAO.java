package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.manager.interfaces.AgentiSettingsManager;
import it.eurotn.panjea.agenti.manager.provvigionestrategy.CalcoloPrezzoProvvigioneStrategy;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.interfaces.LottiAssegnazioneAutomaticaManager;
import it.eurotn.panjea.lotti.manager.verifica.interfaces.LottiVerificaManager;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.RigaArticoloDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaArticoloDAO")
public class RigaArticoloDAO extends RigaMagazzinoAbstractDAOBean {

	@Resource
	private SessionContext sessionContext;

	@EJB
	private LottiAssegnazioneAutomaticaManager lottiAssegnazioneAutomaticaManager;

	@EJB
	private MagazzinoSettingsManager magazzinoSettingsManager;

	@EJB
	private LottiVerificaManager lottiVerificaManager;

	@EJB
	private AgentiSettingsManager agentiSettingsManager;

	@Override
	protected RigaMagazzino saveRigaMagazzino(RigaMagazzino rigaMagazzino) throws RimanenzaLottiNonValidaException,
	RigheLottiNonValideException, QtaLottiMaggioreException {

		RigaMagazzino rigaMagazzinoSalvata = super.saveRigaMagazzino(rigaMagazzino);

		// controllo, se sono gestiti, la correttezza dei lotti inseriti
		boolean lottiAutomatici = magazzinoSettingsManager.caricaMagazzinoSettings().isGestioneLottiAutomatici();
		if (((RigaArticolo) rigaMagazzinoSalvata).isAssegnazioneLottiAutomaticaAbilitata(lottiAutomatici)) {
			rigaMagazzinoSalvata = lottiAssegnazioneAutomaticaManager.assegnaLotti((RigaArticolo) rigaMagazzinoSalvata);
		}

		// eseguo sempre la verifica perchè se avessi inserito a mano le righe
		// lotto devo verificare che siano corrette
		if (((RigaArticolo) rigaMagazzinoSalvata).getArticolo().getTipoLotto() != TipoLotto.NESSUNO) {
			lottiVerificaManager.verifica(rigaMagazzinoSalvata, rigaMagazzinoSalvata.getAreaMagazzino()
					.getDepositoOrigine());
		}

		return rigaMagazzinoSalvata;
	}

	@Override
	protected RigaMagazzino saveRigaMagazzinoNoCheck(RigaMagazzino rigaMagazzino) {
		RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;
		if (rigaArticolo.isNew()) {
			rigaArticolo.aggiornaCollections();
		}
		if (!rigaArticolo.isQtaValid()) {
			throw new IllegalArgumentException("Qta non può essere 0");
		}

		// se la riga articolo ha un agente vado a calcolarne il prezzo base
		// provvigionale
		BigDecimal prezzoUnitarioBaseProvvigionale = BigDecimal.ZERO;
		BigDecimal prezzoNettoBaseProvvigionale = BigDecimal.ZERO;
		if (rigaArticolo.getAgente() != null && rigaArticolo.getAgente().getId() != null) {
			AgentiSettings agentiSettings = agentiSettingsManager.caricaAgentiSettings();

			try {
				CalcoloPrezzoProvvigioneStrategy strategy = (CalcoloPrezzoProvvigioneStrategy) sessionContext
						.lookup(agentiSettings.getBaseProvvigionaleSettings().getBaseProvvigionaleStrategy()
								.getJndiCalculatorStrategy());
				prezzoUnitarioBaseProvvigionale = strategy.calcolaPrezzoUnitario(rigaArticolo);
				prezzoNettoBaseProvvigionale = strategy.calcolaPrezzoNetto(rigaArticolo);
			} catch (Exception e) {
				logger.error("--> errore durante il calcolo della base provvigionale.", e);
				throw new RuntimeException("errore durante il calcolo della base provvigionale.", e);
			}
		}
		rigaArticolo.setPrezzoUnitarioBaseProvvigionale(prezzoUnitarioBaseProvvigionale);
		rigaArticolo.setPrezzoNettoBaseProvvigionale(prezzoNettoBaseProvvigionale);

		if (rigaArticolo.getNotePerStampa(null) == null) {
			rigaArticolo.setNoteRiga(null);
		}

		RigaMagazzino rigaMagazzinoSalvata = super.saveRigaMagazzinoNoCheck(rigaArticolo);

		// Init delle righe lotti se previste dall'articolo
		if (rigaMagazzinoSalvata instanceof RigaArticolo
				&& ((RigaArticolo) rigaMagazzinoSalvata).getArticolo().getTipoLotto() != TipoLotto.NESSUNO) {
			((RigaArticolo) rigaMagazzinoSalvata).getRigheLotto().size();
		}

		return rigaMagazzinoSalvata;
	}
}
