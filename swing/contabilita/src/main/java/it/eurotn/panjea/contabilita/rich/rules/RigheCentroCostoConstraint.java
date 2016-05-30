package it.eurotn.panjea.contabilita.rich.rules;

import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.rules.closure.BinaryConstraint;

public class RigheCentroCostoConstraint implements BinaryConstraint {

	@Override
	public boolean test(Object obj) {
		return true;
	}

	@Override
	public boolean test(Object obj, Object obj1) {
		BigDecimal importo = (BigDecimal) obj;
		@SuppressWarnings("unchecked")
		Set<RigaCentroCosto> righeCentroCosto = (Set<RigaCentroCosto>) obj1;
		BigDecimal totaleCentri = BigDecimal.ZERO;
		for (RigaCentroCosto rigaCentroCosto : righeCentroCosto) {
			totaleCentri = totaleCentri.add(rigaCentroCosto.getImporto());
		}
		return totaleCentri.compareTo(importo) == 0;
	}

}