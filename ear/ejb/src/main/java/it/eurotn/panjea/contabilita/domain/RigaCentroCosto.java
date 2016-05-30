package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.centricosto.domain.CentroCosto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Riga del centro di costo collegata ad una riga contabile.
 * 
 * @author giangi
 * @version 1.0, 22/dic/2010
 */
@Entity
@Audited
@Table(name = "cont_righe_centrocosto")
public class RigaCentroCosto extends EntityBase {
	private static final long serialVersionUID = 2103628054760698624L;

	private BigDecimal importo;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private RigaContabile rigaContabile;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private CentroCosto centroCosto;

	@Column(length = 30)
	private String nota;

	/**
	 * Costruttore.
	 */
	public RigaCentroCosto() {
		importo = BigDecimal.ZERO;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RigaCentroCosto other = (RigaCentroCosto) obj;
		if (centroCosto == null) {
			if (other.centroCosto != null) {
				return false;
			}
		} else if (!centroCosto.equals(other.centroCosto)) {
			return false;
		}
		if (rigaContabile == null) {
			return other.rigaContabile == null;
		} else if (!rigaContabile.equals(other.rigaContabile)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the centroCosto.
	 */
	public CentroCosto getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @return Returns the importo.
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @return Returns the nota.
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @return Returns the rigaContabile.
	 */
	public RigaContabile getRigaContabile() {
		return rigaContabile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((centroCosto == null) ? 0 : centroCosto.hashCode());
		// result = prime * result + ((rigaContabile == null) ? 0 : rigaContabile.hashCode());
		return result;
	}

	/**
	 * @param centroCosto
	 *            The centroCosto to set.
	 */
	public void setCentroCosto(CentroCosto centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @param importo
	 *            The importo to set.
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @param nota
	 *            The nota to set.
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @param rigaContabile
	 *            The rigaContabile to set.
	 */
	public void setRigaContabile(RigaContabile rigaContabile) {
		this.rigaContabile = rigaContabile;
	}

}
