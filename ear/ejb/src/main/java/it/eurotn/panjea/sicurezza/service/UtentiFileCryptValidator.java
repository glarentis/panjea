package it.eurotn.panjea.sicurezza.service;

import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;

import java.util.List;

public class UtentiFileCryptValidator implements UtentiValidator {

	private int num;

	/**
	 * Costruttore
	 *
	 * @param num
	 *            numero utenti cont.
	 */
	public UtentiFileCryptValidator(final int num) {
		this.num = num;
	}

	@Override
	public boolean canAddUser(List<UtenteCollegato> utenti, UtenteCollegato utente) {
		if (utenti.contains(utente)) {
			return true;
		}
		return utenti.size() < num;
	}

}
