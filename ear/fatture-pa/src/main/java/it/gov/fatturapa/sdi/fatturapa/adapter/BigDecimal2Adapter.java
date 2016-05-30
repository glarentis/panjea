package it.gov.fatturapa.sdi.fatturapa.adapter;

public class BigDecimal2Adapter extends BigDecimalAdapter {

	/**
	 * Costruttore.
	 */
	public BigDecimal2Adapter() {
		super("##########0.00");
	}

	/**
	 * Costruttore.
	 *
	 * @param pattern
	 *            pattern
	 */
	public BigDecimal2Adapter(final String pattern) {
		super(pattern);
	}

}
