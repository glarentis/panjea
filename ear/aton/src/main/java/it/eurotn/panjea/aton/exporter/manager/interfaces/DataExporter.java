package it.eurotn.panjea.aton.exporter.manager.interfaces;

import it.eurotn.panjea.exporter.exception.FileCreationException;

import javax.ejb.Local;

/**
 * 
 * Metodi per l'esportazione dei file per la procedura AtonOnSale.
 * 
 * @author giangi
 * @version 1.0, 22/nov/2010
 * 
 */
@Local
public interface DataExporter {
	/**
	 * Esporta i file per la procedure AtonOnSale.<br/>
	 * La cartella dove esportarli la si trova nei settings globali del server.
	 * 
	 * @throws FileCreationException
	 *             rilanciata se manca la chiave di configurazione o il file di template.
	 */
	void esporta() throws FileCreationException;
}
