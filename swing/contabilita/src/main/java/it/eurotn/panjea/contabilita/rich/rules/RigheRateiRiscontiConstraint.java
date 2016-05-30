package it.eurotn.panjea.contabilita.rich.rules;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.rules.closure.BinaryConstraint;

public class RigheRateiRiscontiConstraint implements BinaryConstraint {

	@Override
	public boolean test(Object obj) {
		return true;
	}

	@Override
	public boolean test(Object obj, Object obj1) {
		BigDecimal importo = (BigDecimal) obj;
		@SuppressWarnings("unchecked")
		Collection<RigaRateoRisconto> righeaRateoRisconto = (Collection<RigaRateoRisconto>) obj1;
		BigDecimal totaleRateiRisconti = BigDecimal.ZERO;
		for (RigaRateoRisconto rigaRateoRisconto : righeaRateoRisconto) {
			if (rigaRateoRisconto.getImporto() != null) {
				totaleRateiRisconti = totaleRateiRisconti.add(rigaRateoRisconto.getImporto());
			}
		}
		if (importo == null) {
			return false;
		}
		return totaleRateiRisconti.compareTo(importo) == 0;
	}

}