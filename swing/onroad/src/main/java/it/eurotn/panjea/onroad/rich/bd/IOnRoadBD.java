package it.eurotn.panjea.onroad.rich.bd;

import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;

public interface IOnRoadBD {

	/**
	 * Esegue l'esportazione di Articoli, Attributi articoli, Clienti, Codici iva, Condiz, Rate, Tabella e Utenti.
	 */
	void esporta();

	/**
	 * Esegue l'esportazione di Articoli.
	 */
	void esportaArticoli();

	/**
	 * Esegue l'esportazione di assortimenti articolo.
	 */
	void esportaAssortimentoArticoli();

	/**
	 * Esegue l'esportazione di Attributi articoli.
	 */
	void esportaAttributiArticoli();

	/**
	 * Esegue l'esportazione di Clienti.
	 */
	void esportaClienti();

	/**
	 * Esegue l'esportazione dei legami cliente-cessionario.
	 */
	void esportaClientiCessionari();

	/**
	 * Esegue l'esportazione di Codici iva.
	 */
	void esportaCodiciIva();

	/**
	 * Esegue l'esportazione di Codici pagamento.
	 */
	void esportaCodiciPagamento();

	/**
	 * Esporta condizioni commerciali.
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
	 * Esegue l'esportazione delle unità di misura.
	 */
	void esportaUm();

	/**
	 * Esegue l'esportazione di Utenti.
	 */
	void esportaUtenti();

	/**
	 * @return ClientiOnRoad riepilogo importazione
	 */
	ClientiOnRoad importaClienti();

	/**
	 * @return DocumentiOnRoad riepilogo importazione
	 */
	DocumentiOnRoad importaDocumenti();
}
