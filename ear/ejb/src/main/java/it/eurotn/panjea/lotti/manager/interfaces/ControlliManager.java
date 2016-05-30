package it.eurotn.panjea.lotti.manager.interfaces;

import it.eurotn.panjea.lotti.util.GiacenzaLotto;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface ControlliManager {

	/**
	 * Carica la lista di tutti i lotti aperti in scadenza.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return lista di lotti caricati
	 */
	List<StatisticaLotto> caricaLottiInScadenza(ParametriRicercaScadenzaLotti parametri);

	/**
	 * Verifica la giacenza dei lotti restituendo tutti quelli in cui la rimanenza è diversa dalla giacenza
	 * dell'articolo in magazzino.
	 * 
	 * @return giacenze non valide trovate
	 */
	Set<GiacenzaLotto> verificaGiacenzeLotti();
}
