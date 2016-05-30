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
 * Classe di dominio Carica.
 * 
 * @author adriano
 * @version 1.0, 14/dic/07
 */
@Entity
@Audited
@Table(name = "anag_cariche")
public class Carica extends EntityBase {

	private static final long serialVersionUID = -1705843960936932594L;

	public static final String REF = "Carica";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 40, nullable = false)
	private String descrizione;

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
		buffer.append("Carica[");
		buffer.append(super.toString());
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
