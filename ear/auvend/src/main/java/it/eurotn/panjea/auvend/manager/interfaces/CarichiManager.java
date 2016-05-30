package it.eurotn.panjea.auvend.manager.interfaces;

import it.eurotn.panjea.auvend.domain.StatisticaImportazione;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface CarichiManager {

	/**
	 * Importa i movimenti di carico in base al deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data di riferimento
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 */
	boolean importaCarichiERifornimenti(Date dataInizio, Date dataFine);

	/**
	 * Verifica i movimenti di carico.
	 * 
	 * @param depositi
	 *            depositi da analizzare
	 * @param dataFine
	 *            data finale
	 * @return mappa con i risultati. Nella chiave ho il codice del deposito
	 */
	StatisticaImportazione verificaCarichi(Date dataInizio, Date dataFine);
}
