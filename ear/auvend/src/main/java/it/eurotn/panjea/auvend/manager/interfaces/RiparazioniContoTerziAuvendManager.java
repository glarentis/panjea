package it.eurotn.panjea.auvend.manager.interfaces;

import it.eurotn.panjea.auvend.domain.Cliente;
import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;

import java.util.Date;
import java.util.List;

public interface RiparazioniContoTerziAuvendManager {
	/**
	 * Importa i movimenti generici.
	 * 
	 * @param dataInizio
	 *            data iniziale di importazione
	 * 
	 * @param dataFine
	 *            data di finale di importazione
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
	boolean importaMovimenti(Date dataInizio, Date dataFine);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return lista con gli articoli mancanti.
	 */
	List<Articolo> getArticoliMancanti(Date dataInizio, Date dataFine);
	
	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return lista clienti da verificare.
	 */
	List<Cliente> getClientiMancanti(Date dataInizio, Date dataFine);
	
	
	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param dataInizio
	 *            data iniziale
	 * @param dataFine
	 *            data finale
	 * @return lista clienti da verificare.
	 */
	StatisticaImportazione verifica(Date dataInizio, Date dataFine);
	
}
