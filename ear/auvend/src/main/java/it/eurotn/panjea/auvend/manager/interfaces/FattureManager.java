package it.eurotn.panjea.auvend.manager.interfaces;

import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface FattureManager {

	/**
	 * Chiude le fatture del deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data finale
	 * @return lista di id delle aree create
	 * 
	 */
	List<Integer> chiudiFatture(String deposito, Date dataFine);

	/**
	 * Importa le fatture in base al deposito e data specificati.
	 * 
	 * @param deposito
	 *            deposito di riferimento
	 * @param dataFine
	 *            data di riferimento
	 * @return <code>true</code> se l'importazione è andata a buon fine
	 * @throws SottoContiContabiliAssentiException
	 *             sollevata in mancanza dei sotto conti contabili
	 */
	boolean importaFatture(String deposito, Date dataFine) throws SottoContiContabiliAssentiException;

	/**
	 * Verifica le fatture.
	 * 
	 * @param depositi
	 *            depositi da analizzare
	 * @param dataFine
	 *            data finale
	 * @return mappa con i risultati. Nella chiave ho il codice del deposito
	 */
	Map<String, StatisticaImportazione> verificaFatture(List<String> depositi, Date dataFine);
}
