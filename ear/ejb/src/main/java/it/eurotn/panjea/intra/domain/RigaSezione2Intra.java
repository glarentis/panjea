package it.eurotn.panjea.intra.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("2")
public class RigaSezione2Intra extends RigaSezioneIntra {
	private static final long serialVersionUID = -7582072716826315745L;
	private Integer mese;
	private Integer trimestre;
	private Integer anno;

	@ManyToOne
	private Nomenclatura nomenclatura;

	@Column(precision = 19, scale = 0)
	private BigDecimal valoreStatisticoEuro;

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return l'anno per esportazione per la presentazione intra, solo gli ultimi due caratteri di anno
	 */
	public String getAnnoPerEsportazione() {
		String annoPerExp = new String("");
		if (anno != null && anno > 1900) {
			annoPerExp = anno.toString();
			annoPerExp = annoPerExp.substring(2);
		}
		return annoPerExp;
	}

	/***
	 * 
	 * @return abs dell'importo.
	 */
	public BigDecimal getImportoAbs() {
		return getImporto().abs();
	}

	/**
	 * 
	 * @return valore assoluto dell'importo in valuta.
	 */
	public BigDecimal getImportoInValutaAbs() {
		return getImportoInValuta().abs();
	}

	/**
	 * 
	 * @return valore ass.
	 */
	public BigDecimal getImportoInValutaExportAbs() {
		return getImportoInValutaExport().abs();
	}

	/**
	 * @return Returns the mese.
	 */
	public Integer getMese() {
		return mese;
	}

	/**
	 * @return Returns the nomenclatura.
	 */
	public Nomenclatura getNomenclatura() {
		return nomenclatura;
	}

	/**
	 * @return Returns the segno.
	 */
	public String getSegno() {
		if (getImporto() == null) {
			return "+";
		}
		return getImporto().compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
	}

	/**
	 * @return Returns the trimestre.
	 */
	public Integer getTrimestre() {
		return trimestre;
	}

	/**
	 * @return Returns the valoreStatisticoEuro.
	 */
	public BigDecimal getValoreStatisticoEuro() {
		return valoreStatisticoEuro;
	}

	@Override
	public void negateImporti() {
		super.negateImporti();
		valoreStatisticoEuro = valoreStatisticoEuro.negate();
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param mese
	 *            The mese to set.
	 */
	public void setMese(Integer mese) {
		this.mese = mese;
	}

	/**
	 * @param nomenclatura
	 *            The nomenclatura to set.
	 */
	public void setNomenclatura(Nomenclatura nomenclatura) {
		this.nomenclatura = nomenclatura;
		getCrc().update(nomenclatura);
	}

	/**
	 * @param trimestre
	 *            The trimestre to set.
	 */
	public void setTrimestre(Integer trimestre) {
		this.trimestre = trimestre;
	}

	/**
	 * @param valoreStatisticoEuro
	 *            The valoreStatisticoEuro to set.
	 */
	public void setValoreStatisticoEuro(BigDecimal valoreStatisticoEuro) {
		this.valoreStatisticoEuro = valoreStatisticoEuro;
	}

}
