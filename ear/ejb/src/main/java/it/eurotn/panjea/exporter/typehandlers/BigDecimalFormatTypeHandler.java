package it.eurotn.panjea.exporter.typehandlers;

import java.math.BigDecimal;

public class BigDecimalFormatTypeHandler extends NumberFormatTypeHandler {

	@Override
	protected Number createNumber(BigDecimal bigdecimal) {
		return bigdecimal;
	}

	@Override
	protected Number createNumber(String s) {
		return new BigDecimal(s);
	}

	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

}
