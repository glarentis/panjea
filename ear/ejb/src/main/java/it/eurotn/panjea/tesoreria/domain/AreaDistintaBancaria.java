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
@DiscriminatorValue("DB")
@NamedQueries({
		@NamedQuery(name = "AreaDistintaBancaria.carica", query = "select distinct ad from AreaDistintaBancaria ad inner join fetch ad.areaEffettiCollegata ae inner join fetch ae.effetti eff inner join fetch eff.pagamenti pag inner join fetch pag.rata  where ad.id = :paramIdAreaEffetti "),
		@NamedQuery(name = "AreaDistintaBancaria.caricaByAreaCollegata", query = "select distinct ad from AreaDistintaBancaria ad where ad.areaEffettiCollegata.id = :paramIdAreaEffettiCollegata ") })
@AuditableProperties(properties = { "documento" })
public class AreaDistintaBancaria extends AreaEffetti {

	private static final long serialVersionUID = -7987946099135075942L;

	/**
	 * @uml.property name="areaEffettiCollegata"
	 * @uml.associationEnd
	 */
	@OneToOne
	private AreaEffetti areaEffettiCollegata;

	/**
	 * @return the areaEffettiCollegata
	 * @uml.property name="areaEffettiCollegata"
	 */
	public AreaEffetti getAreaEffettiCollegata() {
		return areaEffettiCollegata;
	}

	/**
	 * @return the effettiPresentati
	 */
	@Override
	public Set<Effetto> getEffetti() {
		return areaEffettiCollegata.getEffetti();
	}

	/**
	 * @param areaEffettiCollegata
	 *            the areaEffettiCollegata to set
	 * @uml.property name="areaEffettiCollegata"
	 */
	public void setAreaEffettiCollegata(AreaEffetti areaEffettiCollegata) {
		this.areaEffettiCollegata = areaEffettiCollegata;
	}

}
