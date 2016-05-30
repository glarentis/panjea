package it.eurotn.panjea.fatturepa.mbean.scheduler.interfaces;

import org.jboss.annotation.ejb.Management;

@Management
public interface FatturaPAScheduler {

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