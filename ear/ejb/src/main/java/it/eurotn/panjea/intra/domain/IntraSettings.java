/**
 *
 */
package it.eurotn.panjea.intra.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Intra settings, raccoglie le impostazioni di base per la configurazione della gestione intra.
 */
@Entity
@Audited
@Table(name = "intr_settings")
@NamedQueries({ @NamedQuery(name = "IntraSettings.caricaAll", query = "from IntraSettings intrasett where intrasett.codiceAzienda=:codiceAzienda") })
public class IntraSettings extends EntityBase {

	private static final long serialVersionUID = -6683460520156214991L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@Column(length = 10)
	private String codiceUA;

	@Column(length = 10)
	private String sezioneDoganale;

	@Column(length = 3)
	private String progressivoSede;

	@Column(length = 12)
	private String codiceFiscaleSpedizionere;

	@Enumerated
	private TipoPeriodo tipoPeriodo;

	private Integer percValoreStatistico;

	{
		tipoPeriodo = TipoPeriodo.M;
		percValoreStatistico = 0;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the codiceFiscaleSpedizionere.
	 */
	public String getCodiceFiscaleSpedizionere() {
		return codiceFiscaleSpedizionere;
	}

	/**
	 * @return Returns the codiceUA.
	 */
	public String getCodiceUA() {
		return codiceUA;
	}

	/**
	 * @return the percValoreStatistico
	 */
	public Integer getPercValoreStatistico() {
		return percValoreStatistico;
	}

	/**
	 * @return Returns the progressivoSede.
	 */
	public String getProgressivoSede() {
		return progressivoSede;
	}

	/**
	 * @return Returns the sezioneDoganale.
	 */
	public String getSezioneDoganale() {
		return sezioneDoganale;
	}

	/**
	 * @return the tipoPeriodo
	 */
	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiceFiscaleSpedizionere
	 *            The codiceFiscaleSpedizionere to set.
	 */
	public void setCodiceFiscaleSpedizionere(String codiceFiscaleSpedizionere) {
		this.codiceFiscaleSpedizionere = codiceFiscaleSpedizionere;
	}

	/**
	 * @param codiceUA
	 *            The codiceUA to set.
	 */
	public void setCodiceUA(String codiceUA) {
		this.codiceUA = codiceUA;
	}

	/**
	 * @param percValoreStatistico
	 *            the percValoreStatistico to set
	 */
	public void setPercValoreStatistico(Integer percValoreStatistico) {
		this.percValoreStatistico = percValoreStatistico;
	}

	/**
	 * @param progressivoSede
	 *            The progressivoSede to set.
	 */
	public void setProgressivoSede(String progressivoSede) {
		this.progressivoSede = progressivoSede;
	}

	/**
	 * @param sezioneDoganale
	 *            The sezioneDoganale to set.
	 */
	public void setSezioneDoganale(String sezioneDoganale) {
		this.sezioneDoganale = sezioneDoganale;
	}

	/**
	 * @param tipoPeriodo
	 *            the tipoPeriodo to set
	 */
	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

}
