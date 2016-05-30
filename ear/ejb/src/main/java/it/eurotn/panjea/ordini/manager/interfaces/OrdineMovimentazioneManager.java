package it.eurotn.panjea.ordini.manager.interfaces;

import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;

import java.util.List;

import javax.ejb.Local;

@Local
public interface OrdineMovimentazioneManager {

	/**
	 * Carica la movimentazione degli ordini con la possibilità di paginazione.
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
