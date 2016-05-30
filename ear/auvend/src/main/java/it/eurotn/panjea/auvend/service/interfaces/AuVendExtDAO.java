package it.eurotn.panjea.auvend.service.interfaces;

import it.eurotn.panjea.auvend.exception.ConnessioneAuVendException;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;

import javax.ejb.Local;

/**
 * 
 * manager incaricato di connettersi con il database di AuVend e recuperare i documenti che dovranno essere caricati in
 * Panjea.<br>
 * 
 * 
 * @author adriano
 * @version 1.0, 20/gen/2009
 * 
 */
@Local
public interface AuVendExtDAO extends Serializable {

	/**
	 * Richiama una stored procedure.
	 * 
	 * @param procedure
	 *            stored da richiamare
	 * @param prametri
	 *            parametri della stored
	 */
	void callProcedure(String procedure, List<Object> prametri);

	/**
	 * Esegue una query.
	 * 
	 * @param procedure
	 *            query da eseguire
	 * @param parametri
	 *            parametri della query
	 * @return risutalti della query
	 */
	ResultSet callQuery(String procedure, List<Object> parametri);

	/**
	 * Cleanup delle risorse.
	 */
	void cleanUp();

	/**
	 * Chisura della connessione attiva.
	 */
	void closeConnection();

	boolean execute(String sql);

	/**
	 * Esegue una query.
	 * 
	 * @param sql
	 *            sql per la query
	 * @return risultati della query
	 */
	ResultSet executeQuery(String sql);

	/**
	 * Apre la connessione.
	 * 
	 * @throws ConnessioneAuVendException
	 *             eccezione se la connessione genera errore all'apertura
	 */
	void initConnection() throws ConnessioneAuVendException;
}
