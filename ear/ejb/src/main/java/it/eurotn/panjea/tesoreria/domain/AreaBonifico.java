/**
 *
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.audit.envers.AuditableProperties;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;

/**
 * Classe responsabile degli effetti presentati alla Banca.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
@Entity
@Audited
@DiscriminatorValue("AB")
@NamedQueries({
		@NamedQuery(name = "AreaBonifico.carica", query = "select distinct ab from AreaBonifico ab inner join fetch ab.areaPagamentiCollegata apc inner join fetch apc.pagamenti pag inner join fetch pag.rata where ab.id = :paramIdAreaBonifico "),
		@NamedQuery(name = "AreaBonifico.caricaByAreaCollegata", query = "select distinct ab from AreaBonifico ab where ab.areaPagamentiCollegata.id = :paramIdAreaPagamentiCollegata ") })
@AuditableProperties(properties = { "documento" })
public class AreaBonifico extends AreaPagamenti {

	private static final long serialVersionUID = -7987946099135075942L;

	@OneToOne
	private AreaPagamenti areaPagamentiCollegata;

	/**
	 * @return areaPagamentiCollegata
	 */
	public AreaPagamenti getAreaPagamentiCollegata() {
		return areaPagamentiCollegata;
	}

	/**
	 * @return the pagamenti dell'area pagamenti collegata
	 */
	@Override
	public Set<Pagamento> getPagamenti() {
		return areaPagamentiCollegata.getPagamenti();
	}

	/**
	 * @param areaPagamentiCollegata
	 *            the areaPagamentiCollegata to set
	 */
	public void setAreaPagamentiCollegata(AreaPagamenti areaPagamentiCollegata) {
		this.areaPagamentiCollegata = areaPagamentiCollegata;
	}

}
