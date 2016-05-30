package it.eurotn.panjea.spedizioni.rich.rules;

import it.eurotn.panjea.rich.rules.ConfrontoPropertiesConstraint;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioniDocumento.ModalitaIncasso;

import java.math.BigDecimal;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.GreaterThan;

public class ModalitaIncassoPropertyConstraint extends ConfrontoPropertiesConstraint {

	public ModalitaIncassoPropertyConstraint(String importoContrassegno, String consegna) {
		super(importoContrassegno, new GreaterThan(), consegna);
	}

	@Override
	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		BigDecimal importo = (BigDecimal) domainObjectAccessStrategy.getPropertyValue(getOtherPropertyName());
		ModalitaIncasso modalitaIncasso = (ModalitaIncasso) domainObjectAccessStrategy
				.getPropertyValue(getPropertyName());

		boolean testResult = true;

		if (importo != null && importo.compareTo(BigDecimal.ZERO) != 0) {

			testResult = modalitaIncasso.ordinal() > 0;
		}

		return testResult;
	}

	@Override
	public String toString() {
		return RcpSupport.getMessage("mopdalitaIncasso") + "richiesta con "
				+ RcpSupport.getMessage("importoContrassegno");
	}

}
