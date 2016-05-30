/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 * 
 */
@Entity
@Audited
@Table(name = "cont_causali_ritenute_acconto")
public class CausaleRitenutaAcconto extends EntityBase {

	private static final long serialVersionUID = 1227410296424983617L;

	private String codice;

	@Column(length = 255)
	private String descrizione;

	private Double percentualeAliquota;

	private Double percentualeImponibile;

	private String tributo;

	private String sezione;

	{
		percentualeAliquota = 0.0;
		percentualeImponibile = 0.0;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the percentualeAliquota
	 */
	public Double getPercentualeAliquota() {
		percentualeAliquota = PanjeaEJBUtil.roundPercentuale(percentualeAliquota);
		return percentualeAliquota;
	}

	/**
	 * @return the percentualeImponibile
	 */
	public Double getPercentualeImponibile() {
		percentualeImponibile = PanjeaEJBUtil.roundPercentuale(percentualeImponibile);
		return percentualeImponibile;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @return the tributo
	 */
	public String getTributo() {
		return tributo;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param percentualeAliquota
	 *            the percentualeAliquota to set
	 */
	public void setPercentualeAliquota(Double percentualeAliquota) {
		this.percentualeAliquota = percentualeAliquota;
	}

	/**
	 * @param percentualeImponibile
	 *            the percentualeImponibile to set
	 */
	public void setPercentualeImponibile(Double percentualeImponibile) {
		this.percentualeImponibile = percentualeImponibile;
	}

	/**
	 * @param sezione
	 *            the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	/**
	 * @param tributo
	 *            the tributo to set
	 */
	public void setTributo(String tributo) {
		this.tributo = tributo;
	}
}
