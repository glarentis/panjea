/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;

/**
 * Eccezione rilanciata se viene riscontrato un movimento di chiusura già esistente.
 * 
 * @author adriano
 * @version 1.0, 05/set/07
 */
public class ChiusuraEsistenteException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property name="tipoConto"
	 * @uml.associationEnd
	 */
	private TipoConto tipoConto;

	/**
	 * @param message
	 */
	public ChiusuraEsistenteException(TipoConto tipoConto) {
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

	/**
	 * @param tipoConto
	 * @uml.property name="tipoConto"
	 */
	public void setTipoConto(TipoConto tipoConto) {
		this.tipoConto = tipoConto;
	}

}
