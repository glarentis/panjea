package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;

/**
 * Classe di appoggio per visualizzare il nodo padre tipo registro per la treeTable.
 * 
 * @author Leonardo
 */
public class TipoRegistroPM {

	private TipoRegistro tipoRegistro = null;
	private int stato = GiornaleIva.STAMPATO;

	/**
	 * Costruttore di default.
	 * 
	 * @param tipoRegistro
	 *            tipoRegistro
	 */
	public TipoRegistroPM(final TipoRegistro tipoRegistro) {
		setTipoRegistro(tipoRegistro);
	}

	/** @return the stato */
	public int getStato() {
		return stato;
	}

	/** @return the tipoRegistro */
	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param stato
	 *            the stato to set
	 */
	public void setStato(int stato) {
		this.stato = stato;
	}

	/**
	 * @param tipoRegistro
	 *            the tipoRegistro to set
	 */
	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
}