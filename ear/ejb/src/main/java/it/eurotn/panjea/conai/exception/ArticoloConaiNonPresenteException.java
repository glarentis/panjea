package it.eurotn.panjea.conai.exception;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

/**
 * Rilanciata se non ho un articolo conai associato al materiale.
 * 
 * @author giangi
 * @version 1.0, 10/ago/2012
 * 
 */
public class ArticoloConaiNonPresenteException extends RuntimeException {
	private static final long serialVersionUID = -2628695716084986869L;
	private ConaiMateriale materiale;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param materiale
	 *            materiale per il quale non ho un articolo associato.
	 */
	public ArticoloConaiNonPresenteException(final ConaiMateriale materiale) {
		super();
		this.materiale = materiale;
	}

	/**
	 * @return Returns the materiale.
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

}
