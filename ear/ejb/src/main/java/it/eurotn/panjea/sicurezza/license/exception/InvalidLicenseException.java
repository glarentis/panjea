package it.eurotn.panjea.sicurezza.license.exception;

import java.util.Date;

public class InvalidLicenseException extends RuntimeException {

	private static final long serialVersionUID = -7060443780613768681L;

	private Date dataScadenza;

	/**
	 * Costruttore.
	 *
	 * @param dataScadenza
	 *            data scadenza licenza
	 */
	public InvalidLicenseException(final Date dataScadenza) {
		super();
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

}
