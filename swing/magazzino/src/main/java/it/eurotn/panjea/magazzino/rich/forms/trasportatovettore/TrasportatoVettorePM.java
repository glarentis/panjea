/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.trasportatovettore;

import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class TrasportatoVettorePM {

	private Periodo periodo;

	private VettoreLite vettore;

	private List<TipoAreaMagazzino> tipiAree;

	{
		tipiAree = new ArrayList<TipoAreaMagazzino>();

		periodo = new Periodo();
		periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
	}

	/**
	 * Costruttore.
	 */
	public TrasportatoVettorePM() {
		super();
	}

	/**
	 * @return the periodo
	 */
	public Periodo getPeriodo() {
		return periodo;
	}

	/**
	 * @return the tipiAree
	 */
	public List<TipoAreaMagazzino> getTipiAree() {
		return tipiAree;
	}

	/**
	 * @return the vettore
	 */
	public VettoreLite getVettore() {
		return vettore;
	}

	/**
	 * @param periodo
	 *            the periodo to set
	 */
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	/**
	 * @param tipiAree
	 *            the tipiAree to set
	 */
	public void setTipiAree(List<TipoAreaMagazzino> tipiAree) {
		this.tipiAree = tipiAree;
	}

	/**
	 * @param vettore
	 *            the vettore to set
	 */
	public void setVettore(VettoreLite vettore) {
		this.vettore = vettore;
	}

}
