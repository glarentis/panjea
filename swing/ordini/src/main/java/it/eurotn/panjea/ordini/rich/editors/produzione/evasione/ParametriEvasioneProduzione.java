/**
 *
 */
package it.eurotn.panjea.ordini.rich.editors.produzione.evasione;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

/**
 * @author leonardo
 */
public class ParametriEvasioneProduzione {

	private TipoAreaMagazzino tipoAreaMagazzino;
	private boolean contoTerzi;

	/**
	 * Costruttore.
	 */
	public ParametriEvasioneProduzione() {
		super();
		contoTerzi = false;
	}

	/**
	 * @return the tipoAreaMagazzino
	 */
	public TipoAreaMagazzino getTipoAreaMagazzino() {
		return tipoAreaMagazzino;
	}

	/**
	 * @return the contoTerzi
	 */
	public boolean isContoTerzi() {
		return contoTerzi;
	}

	/**
	 * @param contoTerzi
	 *            the contoTerzi to set
	 */
	public void setContoTerzi(boolean contoTerzi) {
		this.contoTerzi = contoTerzi;
	}

	/**
	 * @param tipoAreaMagazzino
	 *            the tipoAreaMagazzino to set
	 */
	public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
		this.tipoAreaMagazzino = tipoAreaMagazzino;
	}

}
