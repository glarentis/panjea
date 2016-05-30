/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Classe embedded di DatiProtocollo.
 * 
 * @author adriano
 * @version 1.0, 14/dic/07
 */
@Embeddable
public class DatiProtocollo implements Serializable {

	private static final long serialVersionUID = 6290338564336886476L;

	/**
	 * @uml.property name="codice"
	 */
	@Column(name = "datiProtocollo_codice")
	private Integer codice;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(name = "datiProtocollo_descrizione")
	private String descrizione;

	/**
	 * @uml.property name="incremento"
	 */
	@Column(name = "datiProtocollo_incremento")
	private Integer incremento;

	/**
	 * @return Returns the codice.
	 * @uml.property name="codice"
	 */
	public Integer getCodice() {
		return codice;
	}

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the incremento.
	 * @uml.property name="incremento"
	 */
	public Integer getIncremento() {
		return incremento;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 * @uml.property name="codice"
	 */
	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param incremento
	 *            The incremento to set.
	 * @uml.property name="incremento"
	 */
	public void setIncremento(Integer incremento) {
		this.incremento = incremento;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DatiProtocollo[");
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" incremento = ").append(incremento);
		buffer.append("]");
		return buffer.toString();
	}

}
