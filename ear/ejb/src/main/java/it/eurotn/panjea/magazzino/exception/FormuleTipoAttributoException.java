package it.eurotn.panjea.magazzino.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FormuleTipoAttributoException extends Exception {

	private static final long serialVersionUID = -2203472334474632798L;

	private Map<String, Exception> errori = null;

	/**
	 * Costruttore.
	 */
	public FormuleTipoAttributoException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 */
	public FormuleTipoAttributoException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 * @param cause
	 *            cause
	 */
	public FormuleTipoAttributoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            cause
	 */
	public FormuleTipoAttributoException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Aggiunge un errore di formula per il tipo attributo.
	 * 
	 * @param codiceTipoAttributo
	 *            codiceTipoAttributo
	 * @param exceptionFormula
	 *            exceptionFormula
	 */
	public void addErroreAttributo(String codiceTipoAttributo, Exception exceptionFormula) {
		getErrori().put(codiceTipoAttributo, exceptionFormula);
	}

	/**
	 * @return la mappa con gli errori per tipo attributo
	 */
	public Map<String, Exception> getErrori() {
		if (errori == null) {
			errori = new HashMap<String, Exception>();
		}
		return errori;
	}

	/**
	 * @return true se non ci sono errori, false altrimenti
	 */
	public boolean isEmpty() {
		return getErrori().size() == 0;
	}

	/**
	 * Analizza l'eccezione e ritorna un stringa da visualizzare.
	 * 
	 * @param exception
	 *            exception
	 * @return descrizione dell'errore
	 */
	private String parseFormulaException(Exception exception) {
		String result = "";

		if (exception instanceof NullPointerException) {
			result = "<b>Errore: è stato inserito un valore nullo in un campo della formula</>";
		} else if (exception.getMessage() != null && exception.getMessage().equals("Infinite or NaN")) {
			result = "<B>Divisione per 0.<B>";
		} else if (exception.getMessage() != null && exception.getMessage().contains("ReferenceError")) {
			String msg = exception.getMessage();
			int start = msg.indexOf("$");
			String firstPartMessage = "";
			if (start > -1) {
				int end = msg.lastIndexOf("$");
				firstPartMessage = msg.substring(start + 1, end);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("Non è possibile risolvere la variabile: ");
			sb.append("<B>" + firstPartMessage + "</B><BR>");

			result = sb.toString();
		}
		return result;
	}

	/**
	 * Restituisce la stringa con le informazioni riguardanti il codice tipo attributo ed il tipo di errore associato.
	 * 
	 * @return la stringa con le informazioni sugli errori formule
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML>");
		for (Entry<String, Exception> errore : errori.entrySet()) {
			sb.append("Impossibile eseguire la formula: ");
			sb.append("<B>" + errore.getKey() + "</B><BR>");
			sb.append(parseFormulaException(errore.getValue()));
			sb.append("<BR/>");
		}
		sb.append("</HTML>");
		return sb.toString();
	}

}
