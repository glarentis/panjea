package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.util.List;

public class DistintaCircolareException extends Exception {
	private List<ArticoloLite> componentiElaborati;

	private static final long serialVersionUID = 8990882774114114217L;

	private ArticoloLite articolo;

	/**
	 * Costruttore.
	 */
	public DistintaCircolareException() {
	}

	/**
	 * @param articolo
	 *            articolo con link circolare.
	 * @param componentiElaborati
	 *            componenti del ramo elaborati. L'ultimo articolo è quello che crea il riferimento circolare.
	 */
	public DistintaCircolareException(final ArticoloLite articolo, final List<ArticoloLite> componentiElaborati) {
		this.articolo = articolo;
		this.componentiElaborati = componentiElaborati;
	}

	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 *
	 * @return componente elaborati del ramo prima della referenza circolare.
	 */
	public List<ArticoloLite> getComponentiElaborati() {
		return componentiElaborati;
	}
}
