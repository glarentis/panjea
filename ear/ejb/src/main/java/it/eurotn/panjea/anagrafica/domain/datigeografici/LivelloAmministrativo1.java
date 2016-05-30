package it.eurotn.panjea.anagrafica.domain.datigeografici;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * Livello amministrativo 1, per l'italia equivale alla regione.
 * 
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "geog_livello_1")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiGeografici")
public class LivelloAmministrativo1 extends SuddivisioneAmministrativa {

	private static final long serialVersionUID = 3670792920779788816L;

	@Column(name = "codice_istat", length = 3)
	private java.lang.String codiceIstat;

	/**
	 * @return the codiceIstat
	 */
	public java.lang.String getCodiceIstat() {
		return codiceIstat;
	}

	@Override
	public NumeroLivelloAmministrativo getNumeroLivelloAmministrativo() {
		return NumeroLivelloAmministrativo.LVL1;
	}

	@Override
	public SuddivisioneAmministrativa getSuddivisioneAmministrativaPrecedente() {
		return null;
	}

	/**
	 * @param codiceIstat
	 *            the codiceIstat to set
	 */
	public void setCodiceIstat(java.lang.String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

}
