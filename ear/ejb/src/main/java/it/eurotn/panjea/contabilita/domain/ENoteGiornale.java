/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

/**
 * Rappresenta i tipi di note di invalidamento di un libro giornale.
 * 
 * @author fattazzo
 * @version 1.0, 05/ott/07
 */
public enum ENoteGiornale {
	/**
	 * @uml.property name="gIORNALE_PRECEDENTE_INVALIDATO"
	 * @uml.associationEnd
	 */
	GIORNALE_PRECEDENTE_INVALIDATO,
	/**
	 * @uml.property name="iNSERITO_MOVIMENTO_NEL_GIORNALE"
	 * @uml.associationEnd
	 */
	INSERITO_MOVIMENTO_NEL_GIORNALE,
	/**
	 * @uml.property name="iNSERITO_MOVIMENTO_DOPO_GIORNALE"
	 * @uml.associationEnd
	 */
	INSERITO_MOVIMENTO_DOPO_GIORNALE,
	/**
	 * @uml.property name="cAMBIO_STATO_DOCUMENTO"
	 * @uml.associationEnd
	 */
	CAMBIO_STATO_DOCUMENTO,
	/**
	 * @uml.property name="iNSERITO_NUOVO_MOVIMENTO"
	 * @uml.associationEnd
	 */
	INSERITO_NUOVO_MOVIMENTO
}
