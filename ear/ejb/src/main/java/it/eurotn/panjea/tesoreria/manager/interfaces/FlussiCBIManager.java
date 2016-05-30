package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.service.exception.CodiceSIAAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.TipoDocumentoChiusuraAssenteException;

import javax.ejb.Local;

@Local
public interface FlussiCBIManager {

	/**
	 * Genera il flusso testo da spedire alla banca in funzione del {@link TipoPagamento} del documento.
	 * 
	 * @param areaChiusure
	 *            area da esportare
	 * @return path del file creato sul server per poterlo recuperare in un secondo momento
	 * @throws RapportoBancarioPerFlussoAssenteException
	 *             lanciata quando ci sono dei rapporti bancari entità assenti sulla rata
	 * @throws CodiceSIAAssenteException
	 *             sollevata se il codice SIA è assente
	 * @throws TipoDocumentoChiusuraAssenteException
	 *             sollevata se il tipo documento di chiusura non è assicurato
	 */
	String generaFlusso(AreaChiusure areaChiusure) throws RapportoBancarioPerFlussoAssenteException,
			CodiceSIAAssenteException, TipoDocumentoChiusuraAssenteException;
}
