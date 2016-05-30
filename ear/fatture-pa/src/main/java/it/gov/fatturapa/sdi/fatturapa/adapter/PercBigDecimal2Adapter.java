package it.gov.fatturapa.sdi.fatturapa.adapter;

public class PercBigDecimal2Adapter extends BigDecimalAdapter {

	/**
	 * Costruttore.
	 */
	public PercBigDecimal2Adapter() {
		super("##0.00");
	}

	/**
	 * Costruttore.
	 *
	 * @param pattern
	 *            pattern
	 */
	public PercBigDecimal2Adapter(final String pattern) {
		super(pattern);
	}

}
