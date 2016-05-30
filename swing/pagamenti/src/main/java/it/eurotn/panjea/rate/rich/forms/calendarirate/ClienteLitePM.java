package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;

public class ClienteLitePM extends Object {

	private ClienteLite clienti;

	private boolean senzaCalendarioRate;

	/**
	 * Costruttore.
	 * 
	 */
	public ClienteLitePM() {
		super();
		this.clienti = new ClienteLite();
		this.senzaCalendarioRate = Boolean.TRUE;
	}

	/**
	 * @return the clienti
	 */
	public ClienteLite getClienti() {
		return clienti;
	}

	/**
	 * @return the senzaCalendarioRate
	 */
	public boolean isSenzaCalendarioRate() {
		return senzaCalendarioRate;
	}

	/**
	 * @param clienti
	 *            the clienti to set
	 */
	public void setClienti(ClienteLite clienti) {
		this.clienti = clienti;
	}
}