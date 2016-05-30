/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

/**
 * Rappresenta la tipologia del conto che non deve essere confusa con il tipo del conto.
 * 
 * @author fattazzo
 * @version 1.0, 29/ago/07
 */
public enum ETipologiaConto {
	/**
	 * @uml.property name="cONTO"
	 * @uml.associationEnd
	 */
	CONTO,
	/**
	 * @uml.property name="eNTITA"
	 * @uml.associationEnd
	 */
	ENTITA,
	/**
	 * @uml.property name="cONTO_BASE"
	 * @uml.associationEnd
	 */
	CONTO_BASE,
	/**
	 * @uml.property name="cONTRO_PARTITA"
	 * @uml.associationEnd
	 */
	CONTRO_PARTITA
}
