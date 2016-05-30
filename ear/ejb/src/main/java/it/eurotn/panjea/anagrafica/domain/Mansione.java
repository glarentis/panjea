/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Classe di dominio Mansione.
 * 
 * @author adriano
 * @version 1.0, 14/dic/07
 */
@Entity
@Audited
@Table(name = "anag_mansioni")
public class Mansione extends EntityBase {

	private static final long serialVersionUID = -992132518222584006L;

	public static final String REF = "Mansione";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 60, nullable = false)
	private String descrizione;

	/**
	 * Costruttore.
	 */
	public Mansione() {

	}

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Mansione[");
		buffer.append(super.toString());
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
