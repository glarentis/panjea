package it.eurotn.panjea.service;

import java.io.Serializable;

/**
 * 
 * Utilizzata per spedire al client un messaggio con delle informazioni aggiuntive<br/>
 * timeout importa il timeout della finestra dell'alert. timeout=0 visualizza una finestra con un pulsante chiusi.<br/>
 * roles abilita solamente determinati ruoli a ricevere il messaggio.
 * 
 * @author giangi
 * @version 1.0, 20/mar/2013
 * 
 */
public class TextMessageExtend implements Serializable {
	private static final long serialVersionUID = -4602364183417453211L;

	private int timeout;
	private String[] roles;
	private String testo;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param testo
	 *            testo del messaggio.
	 * @param timeout
	 *            timeout del alert del messaggio. timeout=0 la finestra non viene chiusa ma viene inserito il pulsante
	 *            delete.
	 * @param roles
	 *            ruoli per i quali visualizzare il messaggio.
	 */
	public TextMessageExtend(final String testo, final String[] roles, final int timeout) {
		super();
		this.timeout = timeout;
		this.roles = roles;
		this.testo = testo;
	}

	/**
	 * @return Returns the roles.
	 */
	public String[] getRoles() {
		return roles;
	}

	/**
	 * @return Returns the testo.
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * @return Returns the timeout.
	 */
	public int getTimeout() {
		return timeout;
	}

}
