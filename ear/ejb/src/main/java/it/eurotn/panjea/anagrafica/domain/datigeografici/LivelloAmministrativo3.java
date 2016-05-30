package it.eurotn.panjea.anagrafica.domain.datigeografici;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * Livello amministrativo 3, per l'italia equivale al comune.
 * 
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "geog_livello_3")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiGeografici")
public class LivelloAmministrativo3 extends SuddivisioneAmministrativa {

	private static final long serialVersionUID = -5553144700568539503L;

	@Column(length = 6)
	private String codiceIstat;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello2_id")
	private LivelloAmministrativo2 suddivisioneAmministrativaPrecedente;

	@Column(length = 4)
	private String codiceCatastale;

	/**
	 * @return the codiceCatastale
	 */
	public String getCodiceCatastale() {
		return codiceCatastale;
	}

	/**
	 * @return the codiceIstat
	 */
	public String getCodiceIstat() {
		return codiceIstat;
	}

	@Override
	public NumeroLivelloAmministrativo getNumeroLivelloAmministrativo() {
		return NumeroLivelloAmministrativo.LVL3;
	}

	@Override
	public SuddivisioneAmministrativa getSuddivisioneAmministrativaPrecedente() {
		return suddivisioneAmministrativaPrecedente;
	}

	/**
	 * @param codiceCatastale
	 *            the codiceCatastale to set
	 */
	public void setCodiceCatastale(String codiceCatastale) {
		this.codiceCatastale = codiceCatastale;
	}

	/**
	 * @param codiceIstat
	 *            the codiceIstat to set
	 */
	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	/**
	 * @param livelloAmministrativo2
	 *            the suddivisioneAmministrativaPrecedente to set
	 */
	public void setSuddivisioneAmministrativaPrecedente(LivelloAmministrativo2 livelloAmministrativo2) {
		this.suddivisioneAmministrativaPrecedente = livelloAmministrativo2;
	}

}
