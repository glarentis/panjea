package it.eurotn.panjea.cosaro.sync.exporter.interfaces;

import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import javax.ejb.Local;

/**
 * 
 * Metodi per l'esportazione dei file per la procedura Cosaro - Gamma Meat.
 * 
 * @author giangi
 * @version 1.0, 22/nov/2010
 * 
 */
@Local
public interface DataDocumentoExporter {

	/**
	 * Esporta i file per COSARO - GAMMA MEAT.<br/>
	 * La cartella dove esportarli la si trova nei settings globali del server.
	 * 
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @throws FileCreationException
	 *             rilanciata se manca la chiave di configurazione o il file di template.
	 */
	void esporta(AreaMagazzino areaMagazzino) throws FileCreationException;
}
