package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

public class ApriStatisticaArticoloRicercaCommand extends ApriStatisticaArticoloCommand {

	/**
	 * Costruttore.
	 */
	public ApriStatisticaArticoloRicercaCommand() {
		super("apriStatisticaArticoloCommand");
	}

	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { ArticoloLite.class, Articolo.class, String.class };
	}
}
