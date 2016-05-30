package it.eurotn.panjea.tesoreria.util;

import java.util.Date;

public class DataValutaEffetto extends Date {

	private static final long serialVersionUID = -8892115739842550899L;

	/**
	 * Costruttore.
	 * 
	 * @param date
	 *            data
	 */
	public DataValutaEffetto(final Date date) {
		super(date.getTime());
	}
}