package it.eurotn.panjea.magazzino.exception;

/**
 * Generata se l'analisi non è presente.
 * 
 * @author giangi
 * @version 1.0, 29/giu/2012
 * 
 */
public class AnalisiNonPresenteException extends Exception {

	private String nomeAnalisi;

	private static final long serialVersionUID = 6006348062395450926L;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param nomeAnalisi
	 *            nome dell'analisi non tocate ne dataStore
	 */
	public AnalisiNonPresenteException(final String nomeAnalisi) {
		this.nomeAnalisi = nomeAnalisi;
	}

	/**
	 * @return Returns the nomeAnalisi.
	 */
	public String getNomeAnalisi() {
		return nomeAnalisi;
	}

}
