package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "anag_categoria_entita")
public class CategoriaEntita extends EntityBase {

	private static final long serialVersionUID = -2309559078598487945L;
	/**
	 * @uml.property name="sezione"
	 */
	@Column(name = "sezione", length = 30)
	private String sezione;
	/**
	 * @uml.property name="descrizione"
	 */
	@Column(name = "descrizione", length = 60)
	private String descrizione;

	/**
	 * Costruttore.
	 */
	public CategoriaEntita() {
		super();
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param sezione
	 *            the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Entita[");
		buffer.append(super.toString());
		buffer.append("sezione = ").append(sezione);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}
}
