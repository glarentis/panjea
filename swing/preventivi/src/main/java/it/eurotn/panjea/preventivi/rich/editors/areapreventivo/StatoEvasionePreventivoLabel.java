package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;

public class StatoEvasionePreventivoLabel extends AbstractLabelDocumento<AreaPreventivo> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 *
	 */
	public StatoEvasionePreventivoLabel() {
		super("true", "processato");
	}

	@Override
	protected boolean isDaVisualizzare(AreaPreventivo areaDocumento) {
		return areaDocumento != null && areaDocumento.isProcessato();
	}

}
