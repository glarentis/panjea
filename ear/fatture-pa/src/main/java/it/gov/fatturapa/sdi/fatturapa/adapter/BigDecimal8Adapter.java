package it.gov.fatturapa.sdi.fatturapa.adapter;

public class BigDecimal8Adapter extends BigDecimalAdapter {

	/**
	 * Costruttore.
	 */
	public BigDecimal8Adapter() {
		super("##########0.00######");
	}

	/**
	 * Costruttore.
	 *
	 * @param pattern
	 *            pattern
	 */
	public BigDecimal8Adapter(final String pattern) {
		super(pattern);
	}

}
