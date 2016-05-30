package it.gov.fatturapa.sdi.fatturapa.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class BigDecimalAdapter extends XmlAdapter<BigDecimal, BigDecimal> {

	private DecimalFormat myFormatter;

	/**
	 *
	 * Costruttore.
	 *
	 * @param pattern
	 *            pattern
	 */
	public BigDecimalAdapter(final String pattern) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		myFormatter = new DecimalFormat(pattern, symbols);
	}

	@Override
	public BigDecimal marshal(BigDecimal value) throws Exception {
		if (value != null) {
			String formettedValue = myFormatter.format(value.doubleValue());
			return new BigDecimal(formettedValue);
		} else {
			return null;
		}
	}

	@Override
	public BigDecimal unmarshal(BigDecimal arg0) throws Exception {
		return arg0;
	}

}
