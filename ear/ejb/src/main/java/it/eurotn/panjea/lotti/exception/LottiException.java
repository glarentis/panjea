package it.eurotn.panjea.lotti.exception;

public abstract class LottiException extends Exception {

	private static final long serialVersionUID = -2908697911692205451L;

	/**
	 * Restituisce il messaggio dell'eccezione in formato HTML.
	 * 
	 * @return messaggio
	 */
	public abstract String getHTMLMessage();
}
