package it.eurotn.panjea.lotti.manager.rimanenzefinali.interfaces;

import it.eurotn.panjea.lotti.manager.rimanenzefinali.RimanenzeFinaliDTO;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

@Local
public interface RimanenzeFinaliManager {

	/**
	 * Carica le rimanenze finali.
	 * 
	 * @param data
	 *            data di ricerca
	 * @param idFornitore
	 *            id fornitore, se <code>null</code> ricerca su tutti i fornitori
	 * @param idDeposito
	 *            id deposito di ricerca
	 * @param idCategoria
	 *            id categoria, se <code>null</code> ricerca su tutti le categorie
	 * @param caricaGiacenzeAZero
	 *            indica se escludere dal caricamento delle rimanenze gli articoli che non hanno giacenza
	 * @param ordinamento
	 *            ordinamento risultati: 'A' per articolo codice, 'U' per ubicazione
	 * @return rimanenze
	 */
	List<RimanenzeFinaliDTO> caricaRimanenzeFinali(Date data, Integer idFornitore, Integer idDeposito,
			Integer idCategoria, boolean caricaGiacenzeAZero, String ordinamento);
}
