/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "anag_unita_misura")
@NamedQueries({ @NamedQuery(name = "UnitaMisura.caricaByCodice", query = "from UnitaMisura u where u.codice = :paramCodice order by u.codice") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "unitaMisura")
public class UnitaMisura extends EntityBase {

	private static final long serialVersionUID = 7852677533139066688L;

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda = null;

	@Column(length = 15)
	private String codice;

	private String descrizione;

	/**
	 * Indica che lìunità di misura è utilizzata solamente nella gestione intra.
	 */
	private Boolean intra;

	/**
	 * @return codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return descrizione
	 */
	public String getDescrizione() {
		if (descrizione == null) {
			return "";
		}
		return descrizione;
	}

	@Override
	public Integer getId() {
		return super.getId();
	}

	/**
	 * @return Returns the intra.
	 */
	public Boolean getIntra() {
		if (intra == null) {
			return false;
		}
		return intra;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param intra
	 *            The intra to set.
	 */
	public void setIntra(Boolean intra) {
		this.intra = intra;
	}

}
