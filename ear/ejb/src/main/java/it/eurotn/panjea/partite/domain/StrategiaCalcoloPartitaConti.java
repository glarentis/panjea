package it.eurotn.panjea.partite.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@DiscriminatorValue("C")
public class StrategiaCalcoloPartitaConti extends StrategiaCalcoloPartita {
	private static final long serialVersionUID = -6491313722271217517L;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<ContoPartita> contiPartita;

	/**
	 * Costruttore.
	 */
	public StrategiaCalcoloPartitaConti() {
		contiPartita = new ArrayList<ContoPartita>();
	}

	@Override
	public String getCodiceStrategia() {
		return StrategiaCalcoloPartita.CONTI;
	}

	/**
	 * @return the contiPartita
	 */
	public List<ContoPartita> getContiPartita() {
		return contiPartita;
	}

	/**
	 * @param contiPartita
	 *            the contiPartita to set
	 */
	public void setContiPartita(List<ContoPartita> contiPartita) {
		this.contiPartita = contiPartita;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("StrategiaCalcoloPartitaConti[");
		buffer.append(super.toString());
		buffer.append("]");
		return buffer.toString();
	}

}
