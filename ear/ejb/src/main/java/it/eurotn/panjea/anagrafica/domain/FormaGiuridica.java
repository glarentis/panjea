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
 * Classe di domain FormaGiuridica.
 * 
 * @author adriano
 * @version 1.0, 13/dic/07
 */
@Entity
@Audited
@Table(name = "anag_forme_giuridiche")
public class FormaGiuridica extends EntityBase {

	private static final long serialVersionUID = 1L;

	public static final String REF = "FormaGiuridica";

	public static final String PROP_DESCRIZIONE = "descrizione";

	public static final String PROP_SIGLA = "sigla";

	public static final String PROP_ID = "id";

	/**
	 * @uml.property name="sigla"
	 */
	@Column(length = 14)
	private String sigla;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 50)
	private String descrizione;

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the sigla.
	 * @uml.property name="sigla"
	 */
	public String getSigla() {
		return sigla;
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
	 * @param sigla
	 *            The sigla to set.
	 * @uml.property name="sigla"
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("FormaGiuridica[");
		buffer.append(super.toString());
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" sigla = ").append(sigla);
		buffer.append("]");
		return buffer.toString();
	}

}
