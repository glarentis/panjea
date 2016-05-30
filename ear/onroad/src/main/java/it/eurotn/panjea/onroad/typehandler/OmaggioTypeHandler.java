package it.eurotn.panjea.onroad.typehandler;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class OmaggioTypeHandler implements TypeHandler {

	@Override
	public String format(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType() {
		return Boolean.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {

		return s != null && "OMA".equals(s.trim());
	}

}
