package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_righe_struttura_partita")
public class RigaStrutturaPartite extends EntityBase {

	private static final long serialVersionUID = 3093660901510921638L;

	@Column
	private Integer numeroRata;

	@Column(name = "primaPercentuale", precision = 19, scale = 4)
	private BigDecimal primaPercentuale;

	@Column(name = "secondaPercentuale", precision = 19, scale = 4)
	private BigDecimal secondaPercentuale;

	@Column
	private Integer intervallo;

	@ManyToOne(optional = false)
	private StrutturaPartita strutturaPartita;

	/**
	 * @return the intervallo
	 */
	public Integer getIntervallo() {
		return intervallo;
	}

	/**
	 * @return the numeroRata
	 */
	public Integer getNumeroRata() {
		return numeroRata;
	}

	/**
	 * @return the primaPercentuale
	 */
	public BigDecimal getPrimaPercentuale() {
		return primaPercentuale;
	}

	/**
	 * @return the secondaPercentuale
	 */
	public BigDecimal getSecondaPercentuale() {
		return secondaPercentuale;
	}

	/**
	 * @return the strutturaPartita
	 */
	public StrutturaPartita getStrutturaPartita() {
		return strutturaPartita;
	}

	/**
	 * @param intervallo
	 *            the intervallo to set
	 */
	public void setIntervallo(Integer intervallo) {
		this.intervallo = intervallo;
	}

	/**
	 * @param numeroRata
	 *            the numeroRata to set
	 */
	public void setNumeroRata(Integer numeroRata) {
		this.numeroRata = numeroRata;
	}

	/**
	 * @param primaPercentuale
	 *            the primaPercentuale to set
	 */
	public void setPrimaPercentuale(BigDecimal primaPercentuale) {
		this.primaPercentuale = primaPercentuale;
	}

	/**
	 * @param secondaPercentuale
	 *            the secondaPercentuale to set
	 */
	public void setSecondaPercentuale(BigDecimal secondaPercentuale) {
		this.secondaPercentuale = secondaPercentuale;
	}

	/**
	 * @param strutturaPartita
	 *            the strutturaPartita to set
	 */
	public void setStrutturaPartita(StrutturaPartita strutturaPartita) {
		this.strutturaPartita = strutturaPartita;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("RigaStrutturaPartite[");
		buffer.append("intervallo = ").append(intervallo);
		buffer.append(" numeroRata = ").append(numeroRata);
		buffer.append(" primaPercentuale = ").append(primaPercentuale);
		buffer.append(" secondaPercentuale = ").append(secondaPercentuale);
		buffer.append("]");
		return buffer.toString();
	}

}
