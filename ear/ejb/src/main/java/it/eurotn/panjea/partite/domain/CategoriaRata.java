package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Indica una categori di rata (F24-1001, Affitti, Assicurazioni ...).
 * 
 * @author vittorio
 */
@Entity
@Audited
@Table(name = "part_categorie_rate")
public class CategoriaRata extends EntityBase {

	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoCategoria {
		/**
		 * @uml.property name="gENERICO"
		 * @uml.associationEnd
		 */
		GENERICO, /**
		 * @uml.property name="f24"
		 * @uml.associationEnd
		 */
		F24
	}

	private static final long serialVersionUID = 5339364874821354613L;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	/**
	 * @uml.property name="descrizione"
	 */
	private String descrizione;

	/**
	 * @uml.property name="tipoCategoria"
	 * @uml.associationEnd
	 */
	@Enumerated
	private TipoCategoria tipoCategoria;

	/**
	 * @return the codiceAzienda
	 * @uml.property name="codiceAzienda"
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the descrizione
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the tipoCategoria
	 * @uml.property name="tipoCategoria"
	 */
	public TipoCategoria getTipoCategoria() {
		return tipoCategoria;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param tipoCategoria
	 *            the tipoCategoria to set
	 * @uml.property name="tipoCategoria"
	 */
	public void setTipoCategoria(TipoCategoria tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CategoriaRata[");
		buffer.append("descrizione = ").append(descrizione);
		buffer.append("tipoCategoria = ").append(tipoCategoria);
		buffer.append("]");
		return buffer.toString();
	}

}
