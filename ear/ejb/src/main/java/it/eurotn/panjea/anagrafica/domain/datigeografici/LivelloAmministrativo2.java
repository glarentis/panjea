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
 * Livello amministrativo 2, per l'italia equivale alla provincia.
 * 
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "geog_livello_2")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiGeografici")
public class LivelloAmministrativo2 extends SuddivisioneAmministrativa {

	private static final long serialVersionUID = -5044740221360392319L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "livello1_id")
	private LivelloAmministrativo1 suddivisioneAmministrativaPrecedente;

	@Column(length = 3)
	private String codiceIstat;

	@Column(length = 4)
	private String sigla;

	/**
	 * @return the codiceIstat
	 */
	public String getCodiceIstat() {
		return codiceIstat;
	}

	@Override
	public NumeroLivelloAmministrativo getNumeroLivelloAmministrativo() {
		return NumeroLivelloAmministrativo.LVL2;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	@Override
	public SuddivisioneAmministrativa getSuddivisioneAmministrativaPrecedente() {
		return suddivisioneAmministrativaPrecedente;
	}

	/**
	 * @param codiceIstat
	 *            the codiceIstat to set
	 */
	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	/**
	 * @param sigla
	 *            the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @param livelloAmministrativo1
	 *            the suddivisioneAmministrativaPrecedente to set
	 */
	public void setSuddivisioneAmministrativaPrecedente(LivelloAmministrativo1 livelloAmministrativo1) {
		this.suddivisioneAmministrativaPrecedente = livelloAmministrativo1;
	}

}
