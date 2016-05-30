package it.eurotn.panjea.magazzino.rulesvalidation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;

public class RigaMagazzinoProvvisoriaRulesValidation extends AbstractRigaArticoloRulesValidation {

	private static final long serialVersionUID = -800619218696307218L;

	/**
	 * Costruttore.
	 */
	public RigaMagazzinoProvvisoriaRulesValidation() {
		super("Area magazzino in stato provvisorio");
	}

	@Override
	public boolean valida(RigaArticolo rigaArticolo) {
		return rigaArticolo.getAreaMagazzino().getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO;
	}

	@Override
	public boolean valida(RigaArticoloLite rigaArticoloLite) {
		return rigaArticoloLite.getAreaMagazzino().getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO;
	}

}
