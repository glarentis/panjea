package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "cont_settings_pro_rata")
public class ProRataSetting extends EntityBase {

	private static final long serialVersionUID = -7289988918170433515L;

	private Integer anno;
	private BigDecimal percentuale;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ContabilitaSettings contabilitaSettings;

	@ManyToOne
	private RegistroIva registroIva;

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return Returns the contabilitaSettings.
	 */
	public ContabilitaSettings getContabilitaSettings() {
		return contabilitaSettings;
	}

	/**
	 * @return Returns the percentuale.
	 */
	public BigDecimal getPercentuale() {
		return percentuale;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param contabilitaSettings
	 *            The contabilitaSettings to set.
	 */
	public void setContabilitaSettings(ContabilitaSettings contabilitaSettings) {
		this.contabilitaSettings = contabilitaSettings;
	}

	/**
	 * @param percentuale
	 *            The percentuale to set.
	 */
	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	/**
	 * @param registroIva
	 *            the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}
}
