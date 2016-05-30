package it.eurotn.panjea.exporter.typehandlers;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class IntegerTrimTypeHander implements TypeHandler {

	@Override
	public String format(Object obj) {
		return null;
	}

	@Override
	public Class<?> getType() {
		return Integer.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {

		Integer returnValue = null;

		if (s != null && !s.trim().isEmpty()) {

			s = s.trim();

			try {
				returnValue = Integer.parseInt(s);
			} catch (Exception e) {
				returnValue = null;
			}
		}
		return returnValue;
	}

}
