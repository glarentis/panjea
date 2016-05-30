package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione.EModalitaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface MagazzinoValorizzazioneManager {

	/**
	 * Carica la valorizzazione (per report).
	 *
	 * @param parametri
	 *            paramentri.
	 * @return valorizzazione caricata
	 */
	List<ValorizzazioneArticolo> caricaValorizzazione(Map<Object, Object> parametri);

	/**
	 * Carica la valorizzazione secondo i parametri specificati.
	 *
	 * @param parametriRicercaValorizzazione
	 *            parametri di ricerca
	 * @return valorizzazione caricata
	 */
	List<ValorizzazioneArticolo> caricaValorizzazione(ParametriRicercaValorizzazione parametriRicercaValorizzazione);

	/**
	 * Restituisce il manager corretto da utilizzare per il caricamento della valroizzazione.
	 *
	 * @param modalitaValorizzazione modalita di valorizzazione
	 * @return manager
	 */
	MagazzinoValorizzazioneDepositoManager getValorizzazioneDepositoManager(EModalitaValorizzazione modalitaValorizzazione);
}
