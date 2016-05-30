/**
 *
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.audit.envers.AuditableProperties;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * Classe area anticipo fatture.
 */
@Entity
@Audited
@DiscriminatorValue("AF")
@NamedQueries({ @NamedQuery(name = "AreaAnticipoFatture.carica", query = "select distinct af from AreaAnticipoFatture af inner join fetch af.pagamenti pag inner join fetch pag.rata where af.id = :paramIdAreaAnticipoFatture ") })
@AuditableProperties(properties = { "documento" })
public class AreaAnticipoFatture extends AreaPagamenti {

	private static final long serialVersionUID = -7987946099135075942L;

	@Temporal(TemporalType.DATE)
	private Date dataScadenzaAnticipoFatture;

	/**
	 * @return the dataScadenzaAnticipoFatture
	 */
	public Date getDataScadenzaAnticipoFatture() {
		return dataScadenzaAnticipoFatture;
	}

	/**
	 * @param dataScadenzaAnticipoFatture
	 *            the dataScadenzaAnticipoFatture to set
	 */
	public void setDataScadenzaAnticipoFatture(Date dataScadenzaAnticipoFatture) {
		this.dataScadenzaAnticipoFatture = dataScadenzaAnticipoFatture;
	}

}
