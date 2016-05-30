package it.eurotn.panjea.magazzino.rich.rules;

import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.reporting.TypeResolvableSupport;

public class EanConstraint extends TypeResolvableSupport implements Constraint {

	/**
	 * Costruttore.
	 */
	public EanConstraint() {
		setType("eanConstraint");
	}

	@Override
	public boolean test(Object argument) {
		String ean = (String) argument;
		boolean result = true;
		if (ean != null && !ean.isEmpty()) {
			if (ean.length() == 13) {
				char[] eanChar = ean.toCharArray();

				boolean even = false;
				int sum = 0;
				for (char c : eanChar) {
					if (Character.isDigit(c)) {
						Integer digit = Integer.valueOf(c - 48);
						if (even) {
							digit *= 3;
						}
						sum += digit;
						even = !even;
					}
				}
				result = sum % 10 == 0;
			} else {
				result = false;
			}
		}
		return result;
	}
}
