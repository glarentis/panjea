package it.eurotn.panjea.anagrafica.domain.datigeografici;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "geog_livello_4")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiGeografici")
public class LivelloAmministrativo4 extends SuddivisioneAmministrativa {

	private static final long serialVersionUID = -2560768409048172781L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello3_id")
	private LivelloAmministrativo3 suddivisioneAmministrativaPrecedente;

	@Override
	public NumeroLivelloAmministrativo getNumeroLivelloAmministrativo() {
		return NumeroLivelloAmministrativo.LVL4;
	}

	@Override
	public SuddivisioneAmministrativa getSuddivisioneAmministrativaPrecedente() {
		return suddivisioneAmministrativaPrecedente;
	}

	/**
	 * @param livelloAmministrativo3
	 *            the suddivisioneAmministrativaPrecedente to set
	 */
	public void setSuddivisioneAmministrativaPrecedente(LivelloAmministrativo3 livelloAmministrativo3) {
		this.suddivisioneAmministrativaPrecedente = livelloAmministrativo3;
	}

}
