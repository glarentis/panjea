package it.eurotn.panjea.auvend.manager.interfaces;

import it.eurotn.panjea.auvend.domain.StatisticaImportazione;

import java.util.Date;

public interface FatturazioneRifornimentiAuvendManager {
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
	 * @return mappa con i risultati.
	 */
	StatisticaImportazione verifica(Date dataInizio, Date dataFine);
}
