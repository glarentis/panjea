package it.eurotn.panjea.intra.domain;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

@DiscriminatorValue(value = "N")
@Entity
@Audited
public class Nomenclatura extends Servizio {

	private static final long serialVersionUID = 8103339320271164060L;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	private UnitaMisura umsupplementare;

	/**
	 * @return Returns the umsupplementare.
	 */
	public UnitaMisura getUmsupplementare() {
		return umsupplementare;
	}

	/**
	 * @param umsupplementare
	 *            The umsupplementare to set.
	 */
	public void setUmsupplementare(UnitaMisura umsupplementare) {
		this.umsupplementare = umsupplementare;
	}

	@Override
	public String toString() {
		return "Nomenclatura [getCodice()=" + getCodice() + ", getDescrizione()=" + getDescrizione() + "]";
	}

}
