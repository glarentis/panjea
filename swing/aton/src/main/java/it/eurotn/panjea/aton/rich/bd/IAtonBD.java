package it.eurotn.panjea.aton.rich.bd;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

public interface IAtonBD {

	/**
	 * Esegue l'esportazione di Articoli, Attributi articoli, Clienti, Codici iva, Condiz, Rate, Tabella e Utenti.
	 */
	@AsyncMethodInvocation
	void esporta();

	/**
	 * Esegue l'esportazione di Articoli.
	 */
	@AsyncMethodInvocation
	void esportaArticoli();

	/**
	 * Esegue l'esportazione di Attributi articoli.
	 */
	@AsyncMethodInvocation
	void esportaAttributiArticoli();

	/**
	 * Esegue l'esportazione di Clienti.
	 */
	@AsyncMethodInvocation
	void esportaClienti();

	/**
	 * Esegue l'esportazione di Codici iva.
	 */
	@AsyncMethodInvocation
	void esportaCodiciIva();

	/**
	 * Esporta condizioni commerciali.
	 */
	@AsyncMethodInvocation
	void esportaCondiz();

	/**
	 * Esegue l'esportazione di Giacenze.
	 */
	@AsyncMethodInvocation
	void esportaGiacenze();

	/**
	 * Esegue l'esportazione di Rate.
	 */
	@AsyncMethodInvocation
	void esportaRate();

	/**
	 * Esegue l'esportazione di Tabella.
	 */
	@AsyncMethodInvocation
	void esportaTabelle();

	/**
	 * Esegue l'esportazione delle unità di misura.
	 */
	@AsyncMethodInvocation
	void esportaUm();

	/**
	 * Esegue l'esportazione di Utenti.
	 */
	@AsyncMethodInvocation
	void esportaUtenti();

	/**
	 * Importa gli ordini di atom utilizzando l'entity {@link OrdineImportato}. La cartella di importazione viene
	 * prelevata dalle {@link Preference}
	 * 
	 */
	@AsyncMethodInvocation
	void importa();

	/**
	 * @return il numero di file da importare
	 */
	@AsyncMethodInvocation
	int[] verificaOrdiniDaImportare();

}
