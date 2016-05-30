/**
 * 
 */
package it.eurotn.panjea.ordini.exception;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

import java.util.Set;

/**
 * @author fattazzo
 * 
 */
public class CodicePagamentoAssenteException extends Exception {

	private static final long serialVersionUID = -7391156020035893243L;

	private Set<AreaOrdine> areeOrdine;

	/**
	 * Costruttore.
	 * 
	 * @param areeOrdine
	 *            aree ordini
	 */
	public CodicePagamentoAssenteException(final Set<AreaOrdine> areeOrdine) {
		super();
		this.areeOrdine = areeOrdine;
	}

	/**
	 * @return the areeOrdine
	 */
	public Set<AreaOrdine> getAreeOrdine() {
		return areeOrdine;
	}

	/**
	 * @return <code>true</code> se non ci sono aree ordine
	 */
	public boolean isEmpty() {
		return this.areeOrdine.isEmpty();
	}

}
