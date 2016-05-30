package it.eurotn.panjea.anagrafica.domain.datigeografici;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "geog_localita")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "localita")
public class Localita extends IdentificativoLocalita {

	private static final long serialVersionUID = -7634830684825156774L;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "geog_localita_cap", joinColumns = { @JoinColumn(name = "localita_id") }, inverseJoinColumns = { @JoinColumn(name = "cap_id") })
	private Set<Cap> cap;

	/**
	 * Costruttore.
	 */
	public Localita() {
		super();
		initialize();
	}

	/**
	 * @return the cap
	 */
	public Set<Cap> getCap() {
		return cap;
	}

	/**
	 * 
	 * @return dati geografici legati alla località
	 */
	public DatiGeografici getDatiGeografici() {
		DatiGeografici datiGeografici = new DatiGeografici();
		datiGeografici.setNazione(getNazione());
		datiGeografici.setLivelloAmministrativo1(getLivelloAmministrativo1());
		datiGeografici.setLivelloAmministrativo2(getLivelloAmministrativo2());
		datiGeografici.setLivelloAmministrativo3(getLivelloAmministrativo3());
		datiGeografici.setLivelloAmministrativo4(getLivelloAmministrativo4());
		return datiGeografici;
	}

	/**
	 * Init della collection di cap.
	 */
	private void initialize() {
		cap = new HashSet<Cap>();
	}

	/**
	 * @param cap
	 *            the cap to set
	 */
	public void setCap(Set<Cap> cap) {
		this.cap = cap;
	}

}
