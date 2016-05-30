package it.eurotn.panjea.sicurezza.license.exception;

public class MassimoNumerUtentiCollegati extends Exception {

	private static final long serialVersionUID = -5401410697115410751L;

	/**
	 * Costruttore.
	 */
	public MassimoNumerUtentiCollegati() {
		super("Raggiunto il numero massimo di utenti collegati.");

	}
}
