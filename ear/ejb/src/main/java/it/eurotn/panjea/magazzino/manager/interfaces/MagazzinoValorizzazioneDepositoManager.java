package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;

import java.util.List;

public interface MagazzinoValorizzazioneDepositoManager {

	/**
	 * Carica la valorizzazione del deposito.
	 *
	 * @param parametriRicercaValorizzazione parametri
	 * @param idDeposito id del deposito
	 * @return valorizzazione
	 */
	List<ValorizzazioneArticolo> caricaValorizzazione(ParametriRicercaValorizzazione parametriRicercaValorizzazione,
			int idDeposito);

	/**
	 * Sql per il caricamento della valorizzazione.
	 *
	 * @param parametriRicercaValorizzazione parametri
	 * @param idDeposito id del deposito
	 * @return SQL
	 */
	String getSqlString(ParametriRicercaValorizzazione parametriRicercaValorizzazione, int idDeposito);
}
