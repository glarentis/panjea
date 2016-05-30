package it.eurotn.panjea.intra.manager.typehandler;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class BigDecimalTypeHandler implements TypeHandler {

	private static final String[] CARATTERI_NEGATIVI = new String[] { "p", "q", "r", "s", "t", "u", "v", "w", "x", "y" };
	private static final NumberFormat FORMAT = new DecimalFormat("0");

	/**
	 * test.
	 * 
	 * @param args
	 *            .
	 */
	public static void main(String[] args) {
		BigDecimalTypeHandler test = new BigDecimalTypeHandler();
		System.out.println(test.format(new BigDecimal("-18521")));
		System.out.println(test.format(new BigDecimal("-18528")));
		System.out.println(test.format(new BigDecimal("99")));
		System.out.println(test.format(new BigDecimal("0")));
	}

	@Override
	public String format(Object arg0) {
		BigDecimal importo = (BigDecimal) arg0;
		if (importo == null) {
			return "0";
		}

		String result = FORMAT.format(importo);
		if (importo.compareTo(BigDecimal.ZERO) < 0) {
			int numero = Integer.valueOf(StringUtils.right(result, 1));
			result = StringUtils.left(result, result.length() - 1);
			result += CARATTERI_NEGATIVI[numero];
			// rimuovo il -
			result = result.replace("-", "");
		}
		return result;
	}

	@Override
	public Class<?> getType() {
		return BigDecimal.class;
	}

	@Override
	public Object parse(String arg0) throws TypeConversionException {
		return BigDecimal.ZERO;
	}

}
