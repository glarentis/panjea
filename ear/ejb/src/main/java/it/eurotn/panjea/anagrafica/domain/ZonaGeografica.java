package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * Zona geografica.
 * 
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "anag_zone_geografiche", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
public class ZonaGeografica extends EntityBase {

	private static final long serialVersionUID = -2252617214862300949L;

	/**
	 * @uml.property name="codice"
	 */
	@Column(length = 10, nullable = false)
	private String codice;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 30, nullable = false)
	private String descrizione;

	/**
	 * Costruttore di default.
	 */
	public ZonaGeografica() {
		super();
	}

	/**
	 * @return the codice
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

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
	 * @param codice
	 *            the codice to set
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		this.codice = codice;
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

}
