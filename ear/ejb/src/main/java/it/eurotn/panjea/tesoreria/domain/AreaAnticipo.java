/**
 *
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.audit.envers.AuditableProperties;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

/**
 * Responsabile delle Righe Anticipo. Il tot doc e' == alla sommatoria delle RigaAnticipo
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
@Entity
@Audited
@DiscriminatorValue("AN")
@NamedQueries({
		@NamedQuery(name = "AreaAnticipo.carica", query = "select distinct aa from AreaAnticipo aa join fetch aa.anticipi where aa.id = :paramIdAreaAnticipo"),
		@NamedQuery(name = "AreaAnticipo.caricaByAreaDistintaBancaria", query = "select distinct aa from AreaAnticipo aa join fetch aa.anticipi ant where ant.areaEffetti.id = :paramIdAreaDistinta") })
@AuditableProperties(properties = { "documento" })
public class AreaAnticipo extends AreaTesoreria {

	private static final long serialVersionUID = -3048020552954306700L;

	@OneToMany(mappedBy = "areaAnticipo", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private List<RigaAnticipo> anticipi;

	/**
	 * @return the anticipi
	 */
	public List<RigaAnticipo> getAnticipi() {
		return anticipi;
	}

	/**
	 * @param anticipi
	 *            the anticipi to set
	 */
	public void setAnticipi(List<RigaAnticipo> anticipi) {
		this.anticipi = anticipi;
	}

}
