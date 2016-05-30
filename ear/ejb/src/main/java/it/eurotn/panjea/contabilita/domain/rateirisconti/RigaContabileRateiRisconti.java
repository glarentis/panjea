package it.eurotn.panjea.contabilita.domain.rateirisconti;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.RigaContabile;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("R")
public class RigaContabileRateiRisconti extends RigaContabile {
	private static final long serialVersionUID = 2696051325018570286L;

	@Transient
	private List<Documento> documentiRiscontiCollegati;

	/**
	 * @return Returns the documentiRiscontiCollegati.
	 */
	public List<Documento> getDocumentiRiscontiCollegati() {
		return documentiRiscontiCollegati;
	}

	/**
	 * @param documentiRiscontiCollegati
	 *            The documentiRiscontiCollegati to set.
	 */
	public void setDocumentiRiscontiCollegati(List<Documento> documentiRiscontiCollegati) {
		this.documentiRiscontiCollegati = documentiRiscontiCollegati;
	}

}
