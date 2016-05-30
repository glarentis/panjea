package it.eurotn.panjea.exporter.typehandlers;

import java.math.BigDecimal;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class BigDecimalNDecimalTypeHandlers implements TypeHandler {

	private int numDecimali = 0;

	@Override
	public String format(Object arg0) {
		BigDecimal number = (BigDecimal) arg0;

		String returnString = "0";

		if (number != null) {
			double ordineDiGrandezza = Math.pow(10.0, numDecimali);
			Integer intNumber = number.multiply(BigDecimal.valueOf(ordineDiGrandezza)).intValue();
			returnString = new String(intNumber.toString());
		}

		return returnString;
	}

	/**
	 * @return the numDecimali
	 */
	public int getNumDecimali() {
		return numDecimali;
	}

	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {

		BigDecimal returnValue = BigDecimal.ZERO;

		if (s != null && !s.trim().isEmpty()) {

			s = s.trim();

			try {
				double ordineDiGrandezza = Math.pow(10.0, numDecimali);
				returnValue = new BigDecimal(s).divide(BigDecimal.valueOf(ordineDiGrandezza));
			} catch (Exception e) {
				returnValue = BigDecimal.ZERO;
			}
		}
		return returnValue;
	}

	/**
	 * @param numDecimali
	 *            the numDecimali to set
	 */
	public void setNumDecimali(int numDecimali) {
		this.numDecimali = numDecimali;
	}

}
