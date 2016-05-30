package it.eurotn.panjea.anagrafica.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

/**
 * 
 * SessionBean Manager per la gestione delle banche.
 * 
 * @author adriano
 * @version 1.0, 15/dic/07
 * 
 */
@Local
public interface BancheManager extends Serializable {

	/**
	 * esegue la cancellazione di {@link Banca}.
	 * 
	 * @param banca
	 *            banca da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaBanca(Banca banca) throws AnagraficaServiceException;

	/**
	 * esegue la cancellazione di {@link Filiale}.
	 * 
	 * @param filiale
	 *            filiale da cancellare
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	void cancellaFiliale(Filiale filiale) throws AnagraficaServiceException;

	/**
	 * carica {@link Banca} identificata da idBanca.
	 * 
	 * @param idBanca
	 *            id della banca
	 * @return banca caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Banca caricaBanca(Integer idBanca) throws AnagraficaServiceException;

	/**
	 * Carica la {@link List} di {@link Banca}.
	 * 
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return banche caricate
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<Banca> caricaBanche(String fieldSearch, String valueSearch) throws AnagraficaServiceException;

	/**
	 * carica {@link Filiale} identificato da idFiliale.
	 * 
	 * @param idFiliale
	 *            id della filiale
	 * @return filiale caricata
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	Filiale caricaFiliale(Integer idFiliale) throws AnagraficaServiceException;

	/**
	 * Carica la {@link List} di {@link Filiale}.
	 * 
	 * @return filiali
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<Filiale> caricaFiliali() throws AnagraficaServiceException;

	/**
	 * Carica {@link List} di {@link Filiale} associate all'argomento. {@link Banca}
	 * 
	 * @param banca
	 *            banca di riferimento
	 * @param fieldSearch
	 *            .
	 * @param valueSearch
	 *            .
	 * @return filiali caricate
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	List<Filiale> caricaFiliali(Banca banca, String fieldSearch, String valueSearch) throws AnagraficaServiceException;

	/**
	 * salva {@link Banca}.
	 * 
	 * @param banca
	 *            banca da salvare
	 * @return banca salvata
	 */
	Banca salvaBanca(Banca banca);

	/**
	 * esegue il salvataggio di {@link Filiale}.
	 * 
	 * @param filiale
	 *            filiale da salvare
	 * @return filiale salvata
	 */
	Filiale salvaFiliale(Filiale filiale);

}
