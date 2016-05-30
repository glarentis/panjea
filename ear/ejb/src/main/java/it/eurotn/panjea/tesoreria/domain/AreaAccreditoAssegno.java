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
 * @author leonardo
 */
@Entity
@Audited
@DiscriminatorValue("CS")
@NamedQueries({ @NamedQuery(name = "AreaAccreditoAssegno.carica", query = "select distinct aa from AreaAccreditoAssegno aa left join fetch aa.pagamentiAccreditati pags left join fetch pags.rata where aa.id = :paramIdAreaAccreditoAssegno") })
@AuditableProperties(properties = { "documento" })
public class AreaAccreditoAssegno extends AreaPagamenti {

	private static final long serialVersionUID = 3738113891347252922L;

	@OneToMany(mappedBy = "areaAccreditoAssegno", fetch = FetchType.LAZY)
	private Set<Pagamento> pagamentiAccreditati;

	/**
	 * @return the pagamentiAccreditati
	 */
	@Override
	public Set<Pagamento> getPagamenti() {
		return pagamentiAccreditati;
	}

	/**
	 * @param pagamenti
	 *            the pagamenti to set
	 */
	@Override
	public void setPagamenti(Set<Pagamento> pagamenti) {
		this.pagamentiAccreditati = pagamenti;
	}

}
