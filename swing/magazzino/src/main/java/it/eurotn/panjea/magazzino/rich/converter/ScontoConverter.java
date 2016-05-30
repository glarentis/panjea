package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.rich.converter.PanjeaConverter;

import java.math.BigDecimal;
import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class ScontoConverter extends PanjeaConverter implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		Sconto s1 = (Sconto) o1;
		Sconto s2 = (Sconto) o2;
		if (s1.getSconto1().compareTo(s2.getSconto1()) != 0) {
			return s1.getSconto1().compareTo(s2.getSconto1());
		}
		if (s1.getSconto2().compareTo(s2.getSconto2()) != 0) {
			return s1.getSconto2().compareTo(s2.getSconto2());
		}
		if (s1.getSconto3().compareTo(s2.getSconto3()) != 0) {
			return s1.getSconto3().compareTo(s2.getSconto3());
		}
		return s1.getSconto4().compareTo(s2.getSconto4());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Sconto.class;
	}

	@Override
	public boolean supportFromString(String arg0, ConverterContext arg1) {
		return false;
	}

	@Override
	public boolean supportToString(Object arg0, ConverterContext arg1) {
		return true;
	}

	@Override
	public String toString(Object value, ConverterContext arg1) {
		StringBuilder result = new StringBuilder("");

		if (value != null && value instanceof Sconto && ((Sconto) value).getSconto1() != null) {
			Sconto sconto = (Sconto) value;

			if (BigDecimal.ZERO.compareTo(sconto.getSconto1()) != 0) {
				result.append(sconto.getSconto1());
			}
			if (sconto.getSconto2() != null) {
				if (BigDecimal.ZERO.compareTo(sconto.getSconto2()) != 0) {
					result.append(" ");
					result.append(sconto.getSconto2());
				}
				if (sconto.getSconto3() != null) {
					if (BigDecimal.ZERO.compareTo(sconto.getSconto3()) != 0) {
						result.append(" ");
						result.append(sconto.getSconto3());
					}
					if (sconto.getSconto4() != null) {
						if (BigDecimal.ZERO.compareTo(sconto.getSconto4()) != 0) {
							result.append(" ");
							result.append(sconto.getSconto4());
						}
					}
				}
			}
		}
		return result.toString();
	}
}
