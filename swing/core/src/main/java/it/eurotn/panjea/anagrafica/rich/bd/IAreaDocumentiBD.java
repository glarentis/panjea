/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

/**
 * Interfaccia da far implementare ai business delegate che devono verificare l'esistenza di una area per un documento.
 * 
 * @author adriano
 * @version 1.0, 17/mag/07
 * 
 */
public interface IAreaDocumentiBD {

	/**
	 * metodo che verifica l'esistenza di un'area.
	 * 
	 * @param idDocumento
	 * @return true se l'area esiste
	 */
	public boolean isAreaPresente(Integer idDocumento);

	/**
	 * metodo che verifica l'esistenza di un tipo area per {@link TipoDocumento}.
	 * 
	 * @param idTipoDocumento
	 * @return true se tipo area esiste
	 */
	public boolean isTipoAreaPresente(Integer idTipoDocumento);

}
