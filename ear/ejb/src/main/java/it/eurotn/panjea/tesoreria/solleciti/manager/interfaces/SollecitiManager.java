package it.eurotn.panjea.tesoreria.solleciti.manager.interfaces;

import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface SollecitiManager {

	/**
	 * cancella i solleciti cui rate sono state cancellate.
	 * 
	 * @throws PagamentiException
	 *             eccezione generica
	 */
	void cancellaSollecitiOrphan() throws PagamentiException;

	/**
	 * cancella un solecito.
	 * 
	 * @param sollecito
	 *            sollecito da cancellare
	 */
	void cancellaSollecito(Sollecito sollecito);

	/**
	 * Carica le righe sollecito in base ai parametri.
	 * 
	 * @param parametri
	 *            parametri
	 * @return righe sollecito caricate
	 */
	List<RigaSollecito> caricaRigheSollecito(Map<Object, Object> parametri);

	/**
	 * Carica i solleciti.
	 * 
	 * @return solleciti caricati
	 * @throws PagamentiException
	 *             eccezione generica
	 */
	List<Sollecito> caricaSolleciti() throws PagamentiException;

	/**
	 * Carica i solleciti per un cliente.
	 * 
	 * @param codice
	 *            codice
	 * @return solleciti caricati
	 * @throws PagamentiException
	 *             eccezione generica
	 */
	List<Sollecito> caricaSollecitiByCliente(Integer codice) throws PagamentiException;

	/**
	 * Carica tutti i solleciti della rata specificata.
	 * 
	 * @param idRata
	 *            id rata
	 * @return solleciti caricati
	 * @throws PagamentiException
	 *             eccezione generica
	 */
	List<Sollecito> caricaSollecitiByRata(Integer idRata) throws PagamentiException;

	/**
	 * Carica il sollecito specificato.
	 * 
	 * @param idSollecito
	 *            id del sollecito
	 * @return sollecito caricato
	 * @throws PagamentiException
	 *             eccezione generica
	 */

	Sollecito caricaSollecito(int idSollecito) throws PagamentiException;

	/**
	 * salva solecito.
	 * 
	 * @param sollecito
	 *            da salvare
	 * @return sollecito salvato.
	 */
	Sollecito salvaSollecito(Sollecito sollecito);

}
