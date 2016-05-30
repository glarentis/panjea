package it.eurotn.panjea.sicurezza.license.exception;

public class NumeroInserimentiSuperatiException extends RuntimeException {

	private static final long serialVersionUID = -7049930060025008524L;

	private Class<?> entityClass;

	private int maxInserimenti;

	/**
	 * Costruttore.
	 *
	 * @param entityClass
	 *            classe
	 * @param maxInserimenti
	 *            numero massimo di inserimenti consentiti
	 */
	public NumeroInserimentiSuperatiException(final Class<?> entityClass, final int maxInserimenti) {
		super();
		this.entityClass = entityClass;
		this.maxInserimenti = maxInserimenti;
	}

	/**
	 * @return the entityClass
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return the maxInserimenti
	 */
	public int getMaxInserimenti() {
		return maxInserimenti;
	}

}
