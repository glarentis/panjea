/**
 *
 */
package it.eurotn.panjea.aton.importer;

import org.jboss.annotation.ejb.Management;

/**
 * @author leonardo
 */
@Management
public interface AtonManagement {
	/**
	 * Lifecycle.
	 * 
	 * @throws Exception
	 *             eccezione generica
	 */
	void create() throws Exception;

	/**
	 * Lifecycle.
	 * 
	 * @throws Exception
	 */
	void destroy();

	/**
	 * 
	 * @return intervallo di pooling
	 */
	long getIntervallo();

	/**
	 * Setta l'intervallo di pooling.
	 * 
	 * @param intervallo
	 *            tempo di poolign in millisecondi
	 */
	void setIntervallo(long intervallo);

	/**
	 * Lifecycle.
	 * 
	 * @throws Exception
	 *             eccezione generica
	 */
	void start() throws Exception;

	/**
	 * Lifecycle.
	 * 
	 */
	void stop();
}
