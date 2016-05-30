/**
 * 
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.audit.envers.AuditableProperties;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

/**
 * Classe responsabile degli effetti accreditati.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Entity
@Audited
@DiscriminatorValue("AC")
@NamedQueries({ @NamedQuery(name = "AreaAccredito.carica", query = "select distinct aa from AreaAccredito aa join fetch aa.effettiAccreditati effs join fetch effs.pagamenti pags join fetch pags.rata where aa.id = :paramIdAreaAccredito") })
@AuditableProperties(properties = { "documento" })
public class AreaAccredito extends AreaEffetti {

	private static final long serialVersionUID = 8795604121239018344L;

	@OneToMany(mappedBy = "areaAccredito", fetch = FetchType.LAZY)
	private Set<Effetto> effettiAccreditati;

	/**
	 * @return the effettiAccreditati
	 */
	@Override
	public Set<Effetto> getEffetti() {
		return effettiAccreditati;
	}

	/**
	 * @param effetti
	 *            the effettiInsoluti to set
	 */
	public void setEffetti(Set<Effetto> effetti) {
		this.effettiAccreditati = effetti;
	}

}
