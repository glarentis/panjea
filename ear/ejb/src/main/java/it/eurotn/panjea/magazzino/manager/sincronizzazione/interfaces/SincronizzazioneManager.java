package it.eurotn.panjea.magazzino.manager.sincronizzazione.interfaces;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SincronizzazioneManager {

	/**
	 * Sincronizza tutti i movimenti fatturati nei {@link DatiGenerazione} di riferimento.
	 * 
	 * @param datiGenerazione
	 *            dati di fatturazione
	 * @return La string di risposta della sincronizzazione
	 */
	String sincronizzaDDT(List<DatiGenerazione> datiGenerazione);

}
