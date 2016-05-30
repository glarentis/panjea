/**
 *
 */
package it.eurotn.panjea.intra.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@DiscriminatorValue("B")
public class RigaBeneIntra extends RigaIntra {

	private static final long serialVersionUID = 7812701378346415262L;

	@ManyToOne
	private Nomenclatura nomenclatura;

	/**
	 * Si riferisce alla nazione di origine dell'articolo nel caso di bene.<br>
	 */
	protected String paeseOrigineArticolo;

	/**
	 * Copiato da articolo.
	 */
	@Column(precision = 12, scale = 3)
	private BigDecimal massa;

	/**
	 * Copiato da nomenclatura.
	 */
	private String um;

	{
		massa = BigDecimal.ZERO;
		um = "";
	}

	/**
	 * Costruttore.
	 */
	public RigaBeneIntra() {
		super();
		massa = BigDecimal.ZERO;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RigaBeneIntra other = (RigaBeneIntra) obj;
		if (areaIntra == null) {
			if (other.areaIntra != null) {
				return false;
			}
		} else if (!areaIntra.equals(other.areaIntra)) {
			return false;
		}
		if (nomenclatura == null) {
			if (other.nomenclatura != null) {
				return false;
			}
		} else if (!nomenclatura.equals(other.nomenclatura)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the massa.
	 */
	public BigDecimal getMassa() {
		return massa;
	}

	/**
	 * @return Returns the nomenclatura.
	 */
	public Nomenclatura getNomenclatura() {
		return nomenclatura;
	}

	/**
	 * @return the paeseOrigineArticolo
	 */
	public String getPaeseOrigineArticolo() {
		return paeseOrigineArticolo;
	}

	/**
	 * @return Returns the um.
	 */
	public String getUm() {
		return um;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((areaIntra == null) ? 0 : areaIntra.hashCode());
		result = prime * result + ((nomenclatura == null) ? 0 : nomenclatura.hashCode());
		return result;
	}

	/**
	 * @param massa
	 *            The massa to set.
	 */
	public void setMassa(BigDecimal massa) {
		this.massa = massa;
	}

	/**
	 * @param nomenclatura
	 *            The nomenclatura to set.
	 */
	public void setNomenclatura(Nomenclatura nomenclatura) {
		this.nomenclatura = nomenclatura;
	}

	/**
	 * @param paeseOrigineArticolo
	 *            the paeseOrigineArticolo to set
	 */
	public void setPaeseOrigineArticolo(String paeseOrigineArticolo) {
		this.paeseOrigineArticolo = paeseOrigineArticolo;
	}

	/**
	 * @param um
	 *            The um to set.
	 */
	public void setUm(String um) {
		this.um = um;
	}

	@Override
	public String toString() {
		return "RigaBeneIntra [nomenclatura=" + nomenclatura + ", massa=" + massa + ", um=" + um + "]";
	}
}
