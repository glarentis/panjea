/**
 *
 */
package it.eurotn.panjea.bi.manager.interfaces;

import it.eurotn.panjea.bi.exception.MappaNonPresenteException;
import it.eurotn.panjea.bi.util.Mappa;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

/**
 * @author fattazzo
 *
 */
@Local
public interface MappeBiManager {

	/**
	 * Restituisce tutti i file che fanno parte della mappa richiesta.
	 *
	 * @param nomeFileMappa
	 *            nome del file della mappa
	 * @return mappa contenente come chiave il nome del file e come valore il suo contenuto come array di byte
	 * @throws MappaNonPresenteException
	 *             sollevata se non esiste una mappa con il nome file richiesto
	 */
	Map<String, byte[]> caricaFilesMappa(String nomeFileMappa) throws MappaNonPresenteException;

	/**
	 * Carica tutta la lista delle mappe presenti.
	 *
	 * @return mappe caricate
	 */
	List<Mappa> caricaMappe();
}
