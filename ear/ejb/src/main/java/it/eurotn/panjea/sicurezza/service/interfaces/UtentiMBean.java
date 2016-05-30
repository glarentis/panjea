package it.eurotn.panjea.sicurezza.service.interfaces;

import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;
import it.eurotn.panjea.sicurezza.license.exception.MassimoNumerUtentiCollegati;

import java.util.List;

import org.jboss.annotation.ejb.Management;

@Management
public interface UtentiMBean {
	/**
	 * Aggiunge un utente agli utenti collegati
	 *
	 * @param utente
	 *            utente da aggiungere
	 * @throws MassimoNumerUtentiCollegati
	 *             rilanciata se supero il numero massimo di utenti collegati
	 */
	void aggiungiUtente(UtenteCollegato utente) throws MassimoNumerUtentiCollegati;

	/**
	 * Aggiorna lo stato del client
	 *
	 * @param utente
	 *            utente
	 * @throws MassimoNumerUtentiCollegati
	 *             rilanciata se supero il numero massimo di utenti collegati
	 */
	void cheakAlive(UtenteCollegato utente) throws MassimoNumerUtentiCollegati;

	/**
	 * Lifecycle.
	 *
	 * @throws Exception
	 *             eccezione generica
	 */
	void create() throws Exception;

	/**
	 * Lifecycle.
	 *
	 * @throws Exception
	 */
	void destroy();

	/**
	 *
	 * @return lista degli utentei collegati con i campi separati da #
	 */
	List<UtenteCollegato> getUtentiCollegati();

	/**
	 * rimuove un utente
	 *
	 * @param utente
	 *            utente da rimuovere
	 */
	void rimuoviUtente(UtenteCollegato utente);

	/**
	 * Lifecycle.
	 *
	 * @throws Exception
	 *             eccezione generica
	 */
	void start() throws Exception;

	/**
	 * Lifecycle.
	 *
	 */
	void stop();
}
