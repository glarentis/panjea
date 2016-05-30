package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.StatiAreaMagazzinoManager;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.StatiAreaMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StatiAreaMagazzinoManager")
public class StatiAreaMagazzinoManagerBean implements StatiAreaMagazzinoManager {

	@EJB
	private RigaMagazzinoManager rigaMagazzinoManager;
	@EJB
	private AreaIntraManager areaIntraManager;

	private static Logger logger = Logger.getLogger(StatiAreaMagazzinoManagerBean.class);

	@Override
	public AreaMagazzino cambiaStatoDaConfermatoInProvvisorio(AreaMagazzino areaMagazzino) {
		logger.debug("--> Enter cambiaStatoDaConfermatoInProvvisorio");
		areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);

		rigaMagazzinoManager.getDao().cancellaRigheAutomatiche(areaMagazzino);
		areaIntraManager.cancellaAreaIntra(areaMagazzino.getDocumento());

		logger.debug("--> Exit cambiaStatoDaConfermatoInProvvisorio");
		return areaMagazzino;
	}

	@Override
	public AreaMagazzino cambiaStatoDaProvvisorioInConfermato(AreaMagazzino areaMagazzino) {
		logger.debug("--> Enter cambiaStatoDaProvvisorioInConfermato");
		areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.CONFERMATO);
		logger.debug("--> Exit cambiaStatoDaProvvisorioInConfermato");
		return areaMagazzino;
	}

	@Override
	public AreaMagazzino cambiaStatoDaProvvisorioInForzato(AreaMagazzino areaMagazzino) {
		logger.debug("--> Enter cambiaStatoDaProvvisorioInForzato");
		areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.FORZATO);
		logger.debug("--> Exit cambiaStatoDaProvvisorioInForzato");
		return areaMagazzino;
	}

}
