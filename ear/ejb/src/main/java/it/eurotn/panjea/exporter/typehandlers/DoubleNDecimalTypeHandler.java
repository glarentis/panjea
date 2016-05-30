package it.eurotn.panjea.exporter.typehandlers;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

/**
 * Moltiplica (o divide) il valore per 10^N.
 * 
 * @author giangi
 * @version 1.0, 12/ott/2011
 * 
 */
public class DoubleNDecimalTypeHandler implements TypeHandler {

	private int numDecimali;

	@Override
	public String format(Object obj) {
		Double valore = (Double) obj;

		String returnString = "0";

		if (valore != null) {
			valore = valore * Math.pow(10.0, numDecimali);
			Integer integerValue = valore.intValue();
			returnString = integerValue.toString();
		}
		return returnString;
	}

	/**
	 * @return Returns the numDecimali.
	 */
	public int getNumDecimali() {
		return numDecimali;
	}

	@Override
	public Class<?> getType() {
		return Double.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {

		Double returnValue = null;

		if (s != null && !s.trim().isEmpty()) {
			s = s.trim();

			try {
				returnValue = Double.parseDouble(s) / Math.pow(10.0, numDecimali);
			} catch (Exception e) {
				returnValue = 0.0;
			}
		}

		return returnValue;
	}

	/**
	 * @param numDecimali
	 *            The numDecimali to set.
	 */
	public void setNumDecimali(int numDecimali) {
		this.numDecimali = numDecimali;
	}

}
