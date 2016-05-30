/**
 * 
 */
package it.eurotn.panjea.tesoreria.solleciti.editors;

import java.io.Serializable;

/**
 * Parametri ricerca solleciti.
 * 
 * @author angelo
 */
public class ParametriStampaSollecito implements Serializable {

	private static final long serialVersionUID = 6485882565233416093L;
	// id
	private int codice;
	// decrizione del cliente
	private String cliente;
	// definice se espedirlo per mail
	private Boolean inEmail;

	// se ha o meno la mail settata
	private Boolean hasEmail;

	// definice se realizare la stampa
	private Boolean inStampa;

	/**
	 * Costruttore di default.
	 */
	public ParametriStampaSollecito() {
		initailize();
	}

	/**
	 * @return the cliente
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * @return the cod_cliente
	 */
	public int getCodice() {
		return codice;
	}

	/**
	 * @return the hasEmail
	 */
	public Boolean getHasEmail() {
		return hasEmail;
	}

	/**
	 * @return the inEmail
	 */
	public Boolean getInEmail() {
		return inEmail;
	}

	/**
	 * @return the inStampa
	 */
	public Boolean getInStampa() {
		return inStampa;
	}

	/**
	 * Init delle propriet� che non devono essere null.
	 */
	private void initailize() {

		this.inEmail = false;
		this.hasEmail = false;
		this.inStampa = true;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	/**
	 * @param codice
	 *            the cod_cliente to set
	 */
	public void setCodice(int codice) {
		this.codice = codice;
	}

	/**
	 * @param hasEmail
	 *            the hasEmail to set
	 */
	public void setHasEmail(Boolean hasEmail) {
		this.hasEmail = hasEmail;
	}

	/**
	 * @param inEmail
	 *            the inEmail to set
	 */
	public void setInEmail(Boolean inEmail) {
		this.inEmail = inEmail;
	}

	/**
	 * @param inStampa
	 *            the inStampa to set
	 */
	public void setInStampa(Boolean inStampa) {
		this.inStampa = inStampa;
	}

}
