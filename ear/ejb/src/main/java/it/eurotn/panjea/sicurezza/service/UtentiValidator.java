package it.eurotn.panjea.sicurezza.service;

import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;

import java.util.List;

public interface UtentiValidator {
	/**
	 *
	 * @param utenti
	 *            utenti già presenti
	 * @param utente
	 *            utente da validare
	 * @return true se posso aggiungere l'utente.
	 */
	boolean canAddUser(List<UtenteCollegato> utenti, UtenteCollegato utente);
}
