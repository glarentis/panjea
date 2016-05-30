/**
 *
 */
package it.eurotn.panjea.intra.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@DiscriminatorValue("S")
public class RigaServizioIntra extends RigaIntra {

	private static final long serialVersionUID = 4125335507211044290L;

	@ManyToOne
	private Servizio servizio;

	@Enumerated
	private ModalitaErogazione modalitaErogazione;

	/**
	 * Costruttore.
	 */
	public RigaServizioIntra() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		RigaServizioIntra other = (RigaServizioIntra) obj;
		if (areaIntra == null) {
			if (other.areaIntra != null) {
				return false;
			}
		} else if (!areaIntra.equals(other.areaIntra)) {
			return false;
		}
		if (servizio == null) {
			if (other.servizio != null) {
				return false;
			}
		} else if (!servizio.equals(other.servizio)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the modalitaErogazione
	 */
	public ModalitaErogazione getModalitaErogazione() {
		return modalitaErogazione;
	}

	/**
	 * @return the servizio
	 */
	public Servizio getServizio() {
		return servizio;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((areaIntra == null) ? 0 : areaIntra.hashCode());
		result = prime * result + ((servizio == null) ? 0 : servizio.hashCode());
		return result;
	}

	/**
	 * @param modalitaErogazione
	 *            the modalitaErogazione to set
	 */
	public void setModalitaErogazione(ModalitaErogazione modalitaErogazione) {
		this.modalitaErogazione = modalitaErogazione;
	}

	/**
	 * @param servizio
	 *            the servizio to set
	 */
	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}

}
