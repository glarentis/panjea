package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

/**
 * 
 * Eccezione usata per gestire la quntità maggiore delle righe lotto rispetto alla quantità della riga articolo.
 * 
 * @author fattazzo
 * @version 1.0, 31/ott/2012
 * 
 */
public class QtaLottiMaggioreException extends LottiException {

	private static final long serialVersionUID = -925195445758751342L;

	private Double qtaLotti;

	private RigaArticolo rigaArticolo;

	/**
	 * Costruttore.
	 * 
	 * @param qtaLotti
	 *            quantità assegnata ai lotti
	 * @param rigaArticolo
	 *            riga articolo
	 */
	public QtaLottiMaggioreException(final Double qtaLotti, final RigaArticolo rigaArticolo) {
		super();
		this.qtaLotti = qtaLotti;
		this.rigaArticolo = rigaArticolo;
	}

	@Override
	public String getHTMLMessage() {
		return "";
	}

	/**
	 * @return Returns the qtaLotti.
	 */
	public Double getQtaLotti() {
		return qtaLotti;
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}
}
