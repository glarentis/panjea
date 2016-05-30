package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;

import java.util.List;

public interface IndiciRotazioneGiacenzaManager {
	/**
	 *
	 * @param parametri
	 *            parametri per il calcolo dell'indice di rotazione
	 * @return indici di rotazione per gli articoli/depositi passati nei parametri
	 */
	List<IndiceGiacenzaArticolo> calcolaIndiciRotazione(ParametriCalcoloIndiciRotazioneGiacenza parametri);
}
