/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 * 
 */
@Entity
@Audited
@Table(name = "cont_contributi_previdenziali")
@DiscriminatorValue("P")
public abstract class ContributoPrevidenziale extends EntityBase {

	private static final long serialVersionUID = -833353478848492240L;

	private String codice;

	private Double percContributiva;

	private Double percCaricoLavoratore;

	{
		percContributiva = 0.0;
		percCaricoLavoratore = 0.0;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the percCaricoLavoratore
	 */
	public Double getPercCaricoAzienda() {
		Double percLav = getPercCaricoLavoratore() == null ? 0.0 : getPercCaricoLavoratore();
		return PanjeaEJBUtil.roundPercentuale(100 - percLav);
	}

	/**
	 * @return the percCaricoLavoratore
	 */
	public Double getPercCaricoLavoratore() {
		return PanjeaEJBUtil.roundPercentuale(percCaricoLavoratore);
	}

	/**
	 * @return the percContributiva
	 */
	public Double getPercContributiva() {
		return PanjeaEJBUtil.roundPercentuale(percContributiva);
	}

	/**
	 * Tipo conto base associato al contributo previdenziale.
	 * 
	 * @return tipo conto base
	 */
	public abstract ETipoContoBase getTipoContoBase();

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param percCaricoLavoratore
	 *            the percCaricoLavoratore to set
	 */
	public void setPercCaricoLavoratore(Double percCaricoLavoratore) {
		this.percCaricoLavoratore = percCaricoLavoratore;
	}

	/**
	 * @param percContributiva
	 *            the percContributiva to set
	 */
	public void setPercContributiva(Double percContributiva) {
		this.percContributiva = percContributiva;
	}

}
