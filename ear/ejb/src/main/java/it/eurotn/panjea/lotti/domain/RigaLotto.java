package it.eurotn.panjea.lotti.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "maga_righe_lotti")
public class RigaLotto extends EntityBase {

	private static final long serialVersionUID = 3355325909887900759L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private RigaArticolo rigaArticolo;

	@ManyToOne
	private Lotto lotto;

	private Double quantita;

	/**
	 * Costruttore.
	 * 
	 */
	public RigaLotto() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RigaLotto other = (RigaLotto) obj;
		if (lotto == null) {
			if (other.lotto != null) {
				return false;
			}
		} else if (!lotto.equals(other.lotto)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the lotto
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @return the quantita
	 */
	public Double getQuantita() {
		return quantita == null ? 0.0 : quantita;
	}

	/**
	 * @return the rigaArticolo
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lotto == null) ? 0 : lotto.hashCode());
		return result;
	}

	/**
	 * @param lotto
	 *            the lotto to set
	 */
	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param rigaArticolo
	 *            the rigaArticolo to set
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
	}

}
