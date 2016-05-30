package it.eurotn.panjea.anagrafica.documenti.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Embeddable
public class DatiValidazione implements Serializable {

	private static final long serialVersionUID = 6271676239710539230L;

	/**
	 * @uml.property name="valid"
	 */
	@Column(nullable = true)
	private boolean valid;

	/**
	 * @uml.property name="validData"
	 */
	@Column()
	@Temporal(TemporalType.DATE)
	private Date validData;

	/**
	 * @uml.property name="validUtente"
	 */
	@Column(length = 50)
	private String validUtente;

	/**
	 * Costruttore di default.
	 */
	public DatiValidazione() {
		super();
		this.initialize();
	}

	/**
	 * @return the validData
	 * @uml.property name="validData"
	 */
	public Date getValidData() {
		return validData;
	}

	/**
	 * @return the validUtente
	 * @uml.property name="validUtente"
	 */
	public String getValidUtente() {
		return validUtente;
	}

	/**
	 * Metodo di inizializzazione.
	 */
	private void initialize() {
		this.validData = null;
		this.valid = false;
		this.validUtente = "";

	}

	/**
	 * Invalida tutti i campi richiamando i valori di default.
	 */
	public void invalida() {
		this.initialize();
	}

	/**
	 * @return the valid
	 * @uml.property name="valid"
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Serve a validare i tre campi :<br>
	 * validData = data corrente<br>
	 * valid = true<br>
	 * validUtente = userName da parametro.
	 * 
	 * @param userName
	 *            lo userName corrente
	 */
	public void valida(String userName) {
		this.validData = new Date();
		this.valid = true;
		this.validUtente = userName;
	}
}
