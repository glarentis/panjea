/**
 *
 */
package it.eurotn.panjea.preventivi.service.exception;

import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;

/**
 * @author fattazzo
 *
 */
public class ClientePotenzialePresenteException extends Exception {

	private static final long serialVersionUID = 2228854429950345182L;

	private ClientePotenzialeLite clientePotenzialeLite;

	/**
	 * Costruttore.
	 *
	 * @param clientePotenzialeLite
	 *            cliente potenziale
	 */
	public ClientePotenzialePresenteException(final ClientePotenzialeLite clientePotenzialeLite) {
		super();
		this.clientePotenzialeLite = clientePotenzialeLite;
	}

	/**
	 * @return the clientePotenzialeLite
	 */
	public ClientePotenzialeLite getClientePotenzialeLite() {
		return clientePotenzialeLite;
	}

}
