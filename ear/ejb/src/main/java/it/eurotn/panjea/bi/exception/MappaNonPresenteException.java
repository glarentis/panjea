/**
 *
 */
package it.eurotn.panjea.bi.exception;

/**
 * @author fattazzo
 *
 */
public class MappaNonPresenteException extends Exception {

	private static final long serialVersionUID = -4731288574729211198L;

	private String nomeFileMappa;

	/**
	 * Costruttore.
	 *
	 * @param nomeFileMappa
	 *            nome del file della mappa
	 */
	public MappaNonPresenteException(final String nomeFileMappa) {
		super();
		this.nomeFileMappa = nomeFileMappa;
	}

	/**
	 * @return the nomeFileMappa
	 */
	public String getNomeFileMappa() {
		return nomeFileMappa;
	}

}
