package it.eurotn.panjea.exporter.exception;

import org.beanio.BeanIOException;
import org.beanio.BeanReaderIOException;

public class ImportException extends Exception {

	private static final long serialVersionUID = -645985494698794176L;

	private String nomeFile;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            BeanIOException
	 * @param nomeFile
	 *            nome file
	 */
	public ImportException(final BeanIOException exception, final String nomeFile) {
		super(exception.getMessage().concat(
				(exception.getCause() != null && exception.getCause().getMessage() != null ? exception.getCause()
						.getMessage() : "")));
		this.nomeFile = nomeFile;
	}

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione da wrappare
	 * @param nomeFile
	 *            nome del file di riferimento
	 */
	public ImportException(final BeanReaderIOException exception, final String nomeFile) {
		super(exception.getRecordContext().getRecordErrors().toString());
		this.nomeFile = nomeFile;
	}

	/**
	 * @return the nomeFile
	 */
	public String getNomeFile() {
		return nomeFile;
	}

}
