package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

public class DistintaComponenteRepeat extends RuntimeException {
	private static final long serialVersionUID = -4783914047788618136L;

	/**
	 *
	 * @param articoloLite
	 *            articolo ripetuto
	 */
	public DistintaComponenteRepeat(final ArticoloLite articoloLite) {
		super(articoloLite.getCodice() + " - " + articoloLite.getDescrizione());
	}

}
