package it.eurotn.panjea.sicurezza.service.interfaces;

import it.eurotn.panjea.sicurezza.license.exception.MassimoNumerUtentiCollegati;

import javax.ejb.Remote;

@Remote
public interface ConnessioneUtenteService {

	/**
	 *
	 * @param macAddress
	 *            mac address scheda di rete
	 * @throws MassimoNumerUtentiCollegati
	 *             rilanciato se ho superato il numero massimo di utenti collegati
	 */
	void aggiungiUtente(String macAddress) throws MassimoNumerUtentiCollegati;

	/**
	 * Indica al server che sono un client attivo. Se non sono stato aggiunto come utente (non sono riuscito ad
	 * aggiornare lo stato prima del mio timeout) provo a riaggiungermi
	 *
	 * @param macAddress
	 *            mac address scheda di rete
	 * @throws MassimoNumerUtentiCollegati
	 *             rilanciato se ho superato il numero massimo di utenti collegati
	 */
	void checkAlive(String macAddress) throws MassimoNumerUtentiCollegati;

	/**
	 * Rimuove il client fra i client attivi
	 *
	 * @param macAddress
	 *            mac address scheda di rete
	 *
	 */
	void rimuoviUtente(String macAddress);
}
