/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

/**
 * Rappresenta la tipologia del conto della contro partita.
 * 
 * @author fattazzo
 */
public enum ETipologiaContoControPartita {
	/**
	 * @uml.property name="sOTTOCONTO"
	 * @uml.associationEnd
	 */
	SOTTOCONTO,
	/**
	 * @uml.property name="cONTO"
	 * @uml.associationEnd
	 */
	CONTO,
	/**
	 * @uml.property name="cONTO_BASE"
	 * @uml.associationEnd
	 */
	CONTO_BASE
}
