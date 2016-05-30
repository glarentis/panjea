package it.eurotn.panjea.partite.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@DiscriminatorValue("F")
public class StrategiaCalcoloPartitaFormula extends StrategiaCalcoloPartita {
	private static final long serialVersionUID = -7557978824068746833L;

	@Column
	private String formula;

	@Override
	public String getCodiceStrategia() {
		return StrategiaCalcoloPartita.FORMULA;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("StragegiaCalcoloPartitaFormula[");
		buffer.append(super.toString());
		buffer.append("formula = ").append(formula);
		buffer.append("]");
		return buffer.toString();
	}

}
