package it.eurotn.panjea.lotti.manager;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.LottiManagerBean.RimanenzaLotto;
import it.eurotn.panjea.lotti.manager.interfaces.LottiAssegnazioneAutomaticaManager;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.LottiAssegnazioneAutomaticaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LottiAssegnazioneAutomaticaManager")
public class LottiAssegnazioneAutomaticaManagerBean implements LottiAssegnazioneAutomaticaManager {

	private static Logger logger = Logger.getLogger(LottiAssegnazioneAutomaticaManagerBean.class);

	@EJB
	private LottiManager lottiManager;

	@EJB
	@IgnoreDependency
	private RigaMagazzinoManager rigaMagazzinoManager;

	@Override
	public RigaMagazzino assegnaLotti(RigaArticolo rigaArticolo) throws RimanenzaLottiNonValidaException,
	QtaLottiMaggioreException {
		logger.debug("--> Enter assegnaLotti");
		List<Lotto> lottiAperti = new ArrayList<Lotto>();
		if (rigaArticolo instanceof RigaArticoloComponente) {
			lottiAperti = lottiManager.caricaLotti(rigaArticolo.getArticolo(), rigaArticolo.getAreaMagazzino()
					.getDepositoDestinazione(),RimanenzaLotto.POSITIVA,null,null,null);
		} else {
			lottiAperti = lottiManager.caricaLotti(rigaArticolo.getArticolo(), rigaArticolo.getAreaMagazzino()
					.getDepositoOrigine(),RimanenzaLotto.POSITIVA,null,null,null);
		}

		Collections.sort(lottiAperti, new Comparator<Lotto>() {

			@Override
			public int compare(Lotto o1, Lotto o2) {

				// il lotto con priorità più alta viene usato per primo
				if (o1.getPriorita().compareTo(o2.getPriorita()) != 0) {
					return o1.getPriorita().compareTo(o2.getPriorita()) * -1;
				} else {
					return o1.getDataScadenza().compareTo(o2.getDataScadenza());
				}
			}
		});

		Double qtaRiga = (rigaArticolo.getQta() == null) ? 0.0 : rigaArticolo.getQta();
		Double qtaLotti = 0.0;

		Map<Lotto, Double> mapLottiRiga = new HashMap<Lotto, Double>();
		for (RigaLotto rigaLotto : rigaArticolo.getRigheLotto()) {
			mapLottiRiga.put(rigaLotto.getLotto(), rigaLotto.getQuantita());
			if (rigaLotto.getQuantita() != null) {
				qtaLotti = PanjeaEJBUtil.roundToDecimals(qtaLotti + rigaLotto.getQuantita(),
						rigaArticolo.getNumeroDecimaliQta());
			}
		}
		qtaLotti = PanjeaEJBUtil.roundToDecimals(qtaLotti, rigaArticolo.getArticolo().getNumeroDecimaliQta());

		if (qtaRiga.doubleValue() < qtaLotti.doubleValue()) {
			throw new QtaLottiMaggioreException(qtaLotti, rigaArticolo);
		}

		for (Lotto lotto : lottiAperti) {
			if (qtaLotti.doubleValue() == qtaRiga.doubleValue()) {
				break;
			}

			Double qtaDaAggiungere = 0.0;
			// se il lotto non era presente nelle righe lotto lo aggiungo
			if (!mapLottiRiga.containsKey(lotto)) {
				qtaDaAggiungere = ((qtaRiga - qtaLotti) >= lotto.getRimanenza() ? lotto.getRimanenza()
						: (qtaRiga - qtaLotti));
				mapLottiRiga.put(lotto,
						PanjeaEJBUtil.roundToDecimals(qtaDaAggiungere, rigaArticolo.getNumeroDecimaliQta()));
			} else {
				// se il lotto era già stato inserito nella riga devo verificare se posso aggiungere altra quantità
				Double qtaRigaDisponibile = qtaRiga - qtaLotti;
				Double qtaLottoEsistente = mapLottiRiga.get(lotto);

				qtaDaAggiungere = (qtaRigaDisponibile >= lotto.getRimanenza()) ? lotto.getRimanenza()
						: qtaRigaDisponibile;
				mapLottiRiga.put(lotto, qtaDaAggiungere + qtaLottoEsistente);
			}
			qtaLotti = qtaLotti + BigDecimal.valueOf(qtaDaAggiungere).doubleValue();
		}

		Set<RigaLotto> righeLotti = new HashSet<RigaLotto>();
		for (Entry<Lotto, Double> entry : mapLottiRiga.entrySet()) {
			RigaLotto rigaLotto = new RigaLotto();
			rigaLotto.setLotto(entry.getKey());
			rigaLotto.setQuantita(entry.getValue());
			rigaLotto.setRigaArticolo(rigaArticolo);

			righeLotti.add(rigaLotto);
		}

		qtaLotti = PanjeaEJBUtil.roundToDecimals(qtaLotti, rigaArticolo.getNumeroDecimaliQta());
		if (qtaRiga.doubleValue() == qtaLotti.doubleValue()) {
			rigaArticolo.getRigheLotto().clear();
			rigaArticolo.getRigheLotto().addAll(righeLotti);
			rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(rigaArticolo).salvaRigaMagazzinoNoCheck(
					rigaArticolo);
		} else {
			if (rigaArticolo.getArticolo().isLottoFacoltativo()) {
				return rigaArticolo;
			}
			throw new RimanenzaLottiNonValidaException(rigaArticolo, righeLotti);
		}

		logger.debug("--> Exit assegnaLotti");
		return rigaArticolo;
	}
}
