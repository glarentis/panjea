package it.eurotn.panjea.auvend.manager.interfaces;

import java.sql.ResultSet;

import javax.ejb.Local;

@Local
public interface IAuVendDAO {

	/**
	 * Chiude la connessione.
	 */
	void cleanUp();

	/**
	 * Esegue la query specificata.
	 * 
	 * @param sql
	 *            query da eseguire
	 * @return resultset generato
	 */
	ResultSet executeQuery(String sql);

	/**
	 * Crea la connessione dal datasource di auvend.
	 */
	void initialize();
}
