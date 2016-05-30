package it.eurotn.panjea.auvend.manager.interfaces;

import it.eurotn.panjea.auvend.domain.StatisticaImportazione;

import java.util.Date;
import java.util.Map;

public interface MovimentiAuvendManager {
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
	 * @return mappa con i risultati. Nella chiave ho il codice del deposito (null in questo caso)
	 */
	Map<String, StatisticaImportazione> verifica(Date dataInizio, Date dataFine);
}
