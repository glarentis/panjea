package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.AreaContabile;

import java.util.List;

public class ChiusureRiscontiAnniSuccessiviPresentiException extends RuntimeException {

	private static final long serialVersionUID = 3658917743944465990L;

	private int annoRiferimento;

	private List<AreaContabile> movimentiPresenti;

	/**
	 * Costruttore.
	 *
	 * @param annoRiferimento
	 *            anno di riferimento
	 * @param movimentiPresenti
	 *            movimenti presenti negli anni successivi
	 */
	public ChiusureRiscontiAnniSuccessiviPresentiException(final int annoRiferimento,
			final List<AreaContabile> movimentiPresenti) {
		super();
		this.annoRiferimento = annoRiferimento;
		this.movimentiPresenti = movimentiPresenti;
	}

	/**
	 * @return the annoRiferimento
	 */
	public int getAnnoRiferimento() {
		return annoRiferimento;
	}

	/**
	 * @return the movimentiPresenti
	 */
	public List<AreaContabile> getMovimentiPresenti() {
		return movimentiPresenti;
	}
}
