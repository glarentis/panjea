/**
 * 
 */
package it.eurotn.panjea.conai.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_conai_esenzioni", uniqueConstraints = @UniqueConstraint(columnNames = { "conaiArticolo_id",
		"entita_id" }))
public class ConaiEsenzione extends EntityBase {

	private static final long serialVersionUID = -4558466747406634118L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private ConaiArticolo conaiArticolo;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private EntitaLite entita;

	@Column(precision = 5, scale = 2)
	private BigDecimal percentualeEsenzione;

	/**
	 * Costruttore.
	 */
	public ConaiEsenzione() {
		super();
	}

	/**
	 * @return the conaiArticolo
	 */
	public ConaiArticolo getConaiArticolo() {
		return conaiArticolo;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the percentualeEsenzione
	 */
	public BigDecimal getPercentualeEsenzione() {
		return percentualeEsenzione;
	}

	/**
	 * @param conaiArticolo
	 *            the conaiArticolo to set
	 */
	public void setConaiArticolo(ConaiArticolo conaiArticolo) {
		this.conaiArticolo = conaiArticolo;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param percentualeEsenzione
	 *            the percentualeEsenzione to set
	 */
	public void setPercentualeEsenzione(BigDecimal percentualeEsenzione) {
		this.percentualeEsenzione = percentualeEsenzione;
	}

}
