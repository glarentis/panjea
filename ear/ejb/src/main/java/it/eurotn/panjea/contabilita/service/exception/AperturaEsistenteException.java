/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;

/**
 * Eccezione rilanciata in caso di movimento contabile apertura già esistente.
 * 
 * @author adriano
 * @version 1.0, 05/set/07
 */
public class AperturaEsistenteException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4308403375663832808L;
	/**
	 * @uml.property name="tipoConto"
	 * @uml.associationEnd
	 */
	private TipoConto tipoConto;

	public AperturaEsistenteException(TipoConto tipoConto) {
		super();
		this.tipoConto = tipoConto;
	}

	/**
	 * @return
	 * @uml.property name="tipoConto"
	 */
	public TipoConto getTipoConto() {
		return tipoConto;
	}

}
