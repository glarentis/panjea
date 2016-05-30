package it.eurotn.panjea.contabilita.domain.rateirisconti;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "cont_righe_rateo_risconto_anno")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING, length = 3)
@DiscriminatorValue("RIS")
public class RigaRiscontoAnno extends EntityBase {

	private static final long serialVersionUID = -8878311578403617719L;
	private int anno;
	private BigDecimal importo;
	private int giorni;
	private BigDecimal importoSuccessivo;
	@ManyToOne
	@JoinColumn(name = "rigaContabile_id")
	private RigaContabileRateiRisconti rigaContabile;

	private int giorniSuccessivo;

	@ManyToOne
	private RigaRateoRisconto rigaRateoRisconto;

	/**
	 * @return Returns the anno.
	 */
	public int getAnno() {
		return anno;
	}

	/**
	 *
	 * @return descrizione del costo
	 */
	public String getDescrizione() {
		return "RISCONTO";
	}

	/**
	 * @return Returns the giorni.
	 */
	public int getGiorni() {
		return giorni;
	}

	/**
	 * @return Returns the giorniSuccessivo.
	 */
	public int getGiorniSuccessivo() {
		return giorniSuccessivo;
	}

	/**
	 * @return Returns the importo.
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @return Returns the importoSuccessivo.
	 */
	public BigDecimal getImportoSuccessivo() {
		if (importoSuccessivo == null) {
			return BigDecimal.ZERO;
		}
		return importoSuccessivo;
	}

	/**
	 * @return Returns the rigaContabile.
	 */
	public RigaContabileRateiRisconti getRigaContabile() {
		return rigaContabile;
	}

	/**
	 * @return Returns the rigaRateoRisconto.
	 */
	public RigaRateoRisconto getRigaRateoRisconto() {
		return rigaRateoRisconto;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(int anno) {
		this.anno = anno;
	}

	/**
	 * @param giorni
	 *            The giorni to set.
	 */
	public void setGiorni(int giorni) {
		this.giorni = giorni;
	}

	/**
	 * @param giorniSuccessivo
	 *            The giorniSuccessivo to set.
	 */
	public void setGiorniSuccessivo(int giorniSuccessivo) {
		this.giorniSuccessivo = giorniSuccessivo;
	}

	/**
	 * @param importo
	 *            The importo to set.
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @param importoSuccessivo
	 *            The importoSuccessivo to set.
	 */
	public void setImportoSuccessivo(BigDecimal importoSuccessivo) {
		this.importoSuccessivo = importoSuccessivo;
	}

	/**
	 * @param rigaContabile
	 *            The rigaContabile to set.
	 */
	public void setRigaContabile(RigaContabileRateiRisconti rigaContabile) {
		this.rigaContabile = rigaContabile;
	}

	/**
	 * @param rigaRateoRisconto
	 *            The rigaRateoRisconto to set.
	 */
	public void setRigaRateoRisconto(RigaRateoRisconto rigaRateoRisconto) {
		this.rigaRateoRisconto = rigaRateoRisconto;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RigaRateoRiscontoCalcolato [anno=" + anno + ", importo=" + importo + ", giorni=" + giorni
				+ ", importoSuccessivo=" + importoSuccessivo + ", giorniSuccessivo=" + giorniSuccessivo + "]";
	}

}
