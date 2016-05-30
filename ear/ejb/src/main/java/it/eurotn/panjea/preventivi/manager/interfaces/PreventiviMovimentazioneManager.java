package it.eurotn.panjea.preventivi.manager.interfaces;

import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface PreventiviMovimentazioneManager {

	/**
	 * Carica la movimentazione dei preventivi con la possibilità di paginazione.
	 * 
	 * @param parametriRicercaMovimentazione
	 *            parametri di ricerca
	 * @param page
	 *            pagina caricata
	 * @param sizeOfPage
	 *            dimensione della pagina
	 * @return righe di movimentazione caricate
	 */
	List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage);
}
