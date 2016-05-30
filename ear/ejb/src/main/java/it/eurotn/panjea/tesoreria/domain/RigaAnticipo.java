package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * RigaAnticipo su pagamenti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
@Entity
@Audited
@Table(name = "part_anticipi")
@NamedQueries({ @NamedQuery(name = "RigaAnticipo.caricaImportoAnticipi", query = "select sum(ra.importoAnticipato) from RigaAnticipo ra join ra.areaAnticipo aa join aa.documento doc where ra.dataValuta = :paramDataValuta and doc.rapportoBancarioAzienda = :paramRapportoBancario and ra.areaEffetti = :paramAreaEffetti") })
public class RigaAnticipo extends EntityBase {

	private static final long serialVersionUID = -2714386992955759732L;

	@Temporal(TemporalType.DATE)
	private Date dataValuta;

	@Column
	private BigDecimal importoAnticipato;

	@ManyToOne
	private AreaEffetti areaEffetti;

	@ManyToOne
	private AreaAnticipo areaAnticipo;

	/**
	 * @return the areaAnticipo
	 */
	public AreaAnticipo getAreaAnticipo() {
		return areaAnticipo;
	}

	/**
	 * @return the areaEffetti
	 */
	public AreaEffetti getAreaEffetti() {
		return areaEffetti;
	}

	/**
	 * @return the dataValuta
	 */
	public Date getDataValuta() {
		return dataValuta;
	}

	/**
	 * @return the importoAnticipato
	 */
	public BigDecimal getImportoAnticipato() {
		return importoAnticipato;
	}

	/**
	 * @param areaAnticipo
	 *            the areaAnticipo to set
	 */
	public void setAreaAnticipo(AreaAnticipo areaAnticipo) {
		this.areaAnticipo = areaAnticipo;
	}

	/**
	 * @param areaEffetti
	 *            the areaEffetti to set
	 */
	public void setAreaEffetti(AreaEffetti areaEffetti) {
		this.areaEffetti = areaEffetti;
	}

	/**
	 * @param dataValuta
	 *            the dataValuta to set
	 */
	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}

	/**
	 * @param importoAnticipato
	 *            the importoAnticipato to set
	 */
	public void setImportoAnticipato(BigDecimal importoAnticipato) {
		this.importoAnticipato = importoAnticipato;
	}

}
