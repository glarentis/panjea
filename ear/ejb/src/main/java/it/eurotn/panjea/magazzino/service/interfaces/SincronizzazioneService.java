package it.eurotn.panjea.magazzino.service.interfaces;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SincronizzazioneService {

	/**
	 * Sincronizza tutti i movimenti fatturati nei {@link DatiGenerazione} di riferimento.
	 * 
	 * @param datiGenerazione
	 *            dati di fatturazione
	 * @return la risposta della sincronizzazione
	 */
	String sincronizzaDDT(List<DatiGenerazione> datiGenerazione);
}
