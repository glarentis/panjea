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
 * Domain class per TipologiaSede.
 * 
 * @author adriano
 * @version 1.0, 15/dic/07
 */
@Entity
@Audited
@Table(name = "anag_tipologie_sedi")
public class TipologiaSede extends EntityBase {

	private static final long serialVersionUID = 1775984020775591041L;

	public static final String REF = "TipologiaSede";
	public static final String PROP_ID = "id";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_DESCRIZIONE = "descrizione";

	/**
	 * @uml.property name="codice"
	 */
	@Column(nullable = false)
	private Integer codice;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 40, nullable = false)
	private String descrizione;

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

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TipologiaSede[");
		buffer.append(super.toString());
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
