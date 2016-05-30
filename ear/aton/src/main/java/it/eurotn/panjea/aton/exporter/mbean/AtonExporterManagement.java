package it.eurotn.panjea.aton.exporter.mbean;

import org.jboss.annotation.ejb.Management;

/**
 * MBean per il plugin di Gulliver.<br/>
 * Attiva il timer per leggere i file di importazione.
 *
 * @author giangi
 * @version 1.0, 11/ott/2011
 *
 */
@Management
public interface AtonExporterManagement {

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

	int getOra1();

	int getOra2();

	/**
	 * Setta l'intervallo di pooling.
	 *
	 * @param intervallo
	 *            tempo di poolign in millisecondi
	 */
	void setIntervallo(long intervallo);

	void setOra1(int ora);

	void setOra2(int ora);

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