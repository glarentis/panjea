/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "anag_banche")
public class Banca extends EntityBase {

	private static final long serialVersionUID = -8913067251186683282L;

	public static final String REF = "Banca";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_CIN = "cin";

	/**
	 * @uml.property name="codice"
	 */
	@Column(length = 5)
	@Index(name = "codice")
	private String codice;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 200)
	@Index(name = "descrizione")
	private String descrizione;

	/**
	 * @uml.property name="cin"
	 */
	@Column(length = 1, nullable = false)
	private String cin;

	/**
	 * @return Returns the cin.
	 * @uml.property name="cin"
	 */
	public String getCin() {
		return cin;
	}

	/**
	 * @return Returns the codice.
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the descrizione
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param cin
	 *            The cin to set.
	 * @uml.property name="cin"
	 */
	public void setCin(String cin) {
		this.cin = cin;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Banca[");
		buffer.append(super.toString());
		buffer.append(" cin = ").append(cin);
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}
}
