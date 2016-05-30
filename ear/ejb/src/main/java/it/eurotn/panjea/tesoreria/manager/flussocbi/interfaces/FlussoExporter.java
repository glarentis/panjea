package it.eurotn.panjea.tesoreria.manager.flussocbi.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;

import javax.ejb.Local;

@Local
public interface FlussoExporter {

	/**
	 * Crea un file in locale e ritorna il path del file creato.
	 * 
	 * @param areaChiusure
	 *            area da esportare
	 * @return path del file creato sul server per poterlo recuperare in un secondo momento
	 * @throws RapportoBancarioPerFlussoAssenteException
	 *             lanciata quando ci sono dei rapporti bancari entità assenti sulla rata
	 */
	String esportaFlusso(AreaChiusure areaChiusure) throws RapportoBancarioPerFlussoAssenteException;

}
