package it.eurotn.panjea.intra.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 *
 */
@Entity
@Audited
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_NOMENCLATURA", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("S")
@Table(name = "intr_nomenclatura")
@NamedQueries({ @NamedQuery(name = "Servizio.caricaNomenclaturaByCodice", query = "select n from Nomenclatura n where n.codice=:codice") })
public class Servizio extends EntityBase {

	private static final long serialVersionUID = -8598036127961206660L;

	@Column(length = 10)
	private String codice;

	@Column(length = 1000)
	private String descrizione;

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		return "Servizio [codice=" + codice + ", descrizione=" + descrizione + "]";
	}

}
