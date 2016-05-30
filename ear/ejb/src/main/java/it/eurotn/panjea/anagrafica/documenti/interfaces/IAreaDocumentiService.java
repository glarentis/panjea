/**
 * 
 */
package it.eurotn.panjea.anagrafica.documenti.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

/**
 * Interfaccia da far implementare ai Service che devono verificare l'esistenza di una area per un documento.
 * 
 * 
 * @author adriano
 * @version 1.0, 17/mag/07
 * 
 */
public interface IAreaDocumentiService {

	/**
	 * metodo che verifica l'esistenza di un'area per {@link Documento}..
	 * 
	 * @param idDocumento
	 *            id del documento
	 * @return true se l'area è presente per il documento
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * 
	 */
	boolean isAreaPresente(Integer idDocumento) throws AnagraficaServiceException;

	/**
	 * metodo che verifica l'esistenza di un tipo area per {@link TipoDocumento}.
	 * 
	 * @param idTipoDocumento
	 *            id tipo documento
	 * @return true se il tipoArea è presente per il tipoDocumento
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 */
	boolean isTipoAreaPresente(Integer idTipoDocumento) throws AnagraficaServiceException;

}
