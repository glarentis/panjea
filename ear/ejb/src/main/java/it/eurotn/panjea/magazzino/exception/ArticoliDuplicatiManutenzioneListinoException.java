package it.eurotn.panjea.magazzino.exception;

import java.util.HashMap;
import java.util.Map;

public class ArticoliDuplicatiManutenzioneListinoException extends Exception {

	private static final long serialVersionUID = 2381333463456014182L;

	private Map<String, Integer> articoliDuplicati;

	{
		articoliDuplicati = new HashMap<String, Integer>();
	}

	/**
	 * Costruttore.
	 * 
	 * @param articoliDuplicati
	 *            articoli duplicati. La chiave rappresenta l'articolo ( codice + descrizione) e il valore il numero di
	 *            volte in cui l'articolo è presente
	 */
	public ArticoliDuplicatiManutenzioneListinoException(final Map<String, Integer> articoliDuplicati) {
		super();
		this.articoliDuplicati = articoliDuplicati;
	}

	/**
	 * @return Returns the articoliDuplicati.
	 */
	public Map<String, Integer> getArticoliDuplicati() {
		return articoliDuplicati;
	}

}
