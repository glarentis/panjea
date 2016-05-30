package it.eurotn.panjea.aton.exporter.service.interfaces;

import javax.ejb.Remote;

@Remote
public interface AtonExporterService {

	/**
	 * Esegue l'esportazione di Articoli, Attributi articoli, Clienti, Codici iva, Condiz, Rate, Tabella e Utenti.
	 */
	void esporta();

	/**
	 * Esegue l'esportazione di Articoli.
	 */
	void esportaArticoli();

	/**
	 * Esegue l'esportazione di Attributi articoli.
	 */
	void esportaAttributiArticoli();

	/**
	 * Esegue l'esportazione di Clienti.
	 */
	void esportaClienti();

	/**
	 * Esegue l'esportazione di Codici iva.
	 */
	void esportaCodiciIva();

	/**
	 * Esegue l'esportazione di Condiz.
	 */
	void esportaCondiz();

	/**
	 * Esegue l'esportazione di Giacenze.
	 */
	void esportaGiacenze();

	/**
	 * Esegue l'esportazione di Rate.
	 */
	void esportaRate();

	/**
	 * Esegue l'esportazione di Tabella.
	 */
	void esportaTabelle();

	/**
	 * Esegue l'esportazione di unità di misura.
	 */
	void esportaUm();

	/**
	 * Esegue l'esportazione di Utenti.
	 */
	void esportaUtenti();

}
