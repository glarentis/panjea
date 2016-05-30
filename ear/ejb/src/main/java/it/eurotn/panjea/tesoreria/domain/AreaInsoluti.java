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
 * Classe responsabile degli effetti insoluti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Entity
@Audited
@DiscriminatorValue("IN")
@NamedQueries({ @NamedQuery(name = "AreaInsoluti.carica", query = "select distinct ai from AreaInsoluti ai join fetch ai.effettiInsoluti effs join fetch effs.pagamenti pags join fetch pags.rata where ai.id = :paramIdAreaInsoluti") })
@AuditableProperties(properties = { "documento" })
public class AreaInsoluti extends AreaEffetti {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3395643363711061679L;

	@OneToMany(mappedBy = "areaInsoluti", fetch = FetchType.LAZY)
	private Set<Effetto> effettiInsoluti;

	/**
	 * @return the effettiInsoluti
	 */
	@Override
	public Set<Effetto> getEffetti() {
		return effettiInsoluti;
	}

	/**
	 * @param effetti
	 *            the effettiInsoluti to set
	 */
	public void setEffetti(Set<Effetto> effetti) {
		this.effettiInsoluti = effetti;
	}

}
