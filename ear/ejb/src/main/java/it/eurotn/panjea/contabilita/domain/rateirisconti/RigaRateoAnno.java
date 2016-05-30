package it.eurotn.panjea.contabilita.domain.rateirisconti;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("RAT")
public class RigaRateoAnno extends RigaRiscontoAnno {

	private static final long serialVersionUID = -2593851306269968352L;

	@Override
	public String getDescrizione() {
		return "RATEO";
	}

}
