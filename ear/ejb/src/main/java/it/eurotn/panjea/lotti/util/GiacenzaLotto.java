package it.eurotn.panjea.lotti.util;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.io.Serializable;

public class GiacenzaLotto implements Serializable {

	private static final long serialVersionUID = 2097158717936922897L;

	private ArticoloLite articolo;

	private CategoriaLite categoria;

	private DepositoLite deposito;

	private Double giacenzaMagazzino = 0.0;

	private Double giacenzaLotti = 0.0;

	{
		this.articolo = new ArticoloLite();
		this.categoria = new CategoriaLite();
		this.deposito = new DepositoLite();
		this.giacenzaLotti = 0.0;
		this.giacenzaMagazzino = 0.0;
	}

	/**
	 * Costruttore.
	 */
	public GiacenzaLotto() {
		super();
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the categoria.
	 */
	public CategoriaLite getCategoria() {
		return categoria;
	}

	/**
	 * @return Returns the deposito.
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return Returns the giacenzaLotti.
	 */
	public Double getGiacenzaLotti() {
		return giacenzaLotti;
	}

	/**
	 * @return Returns the giacenzaMagazzino.
	 */
	public Double getGiacenzaMagazzino() {
		return giacenzaMagazzino;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param categoria
	 *            The categoria to set.
	 */
	public void setCategoria(CategoriaLite categoria) {
		this.categoria = categoria;
	}

	/**
	 * @param deposito
	 *            The deposito to set.
	 */
	public void setDeposito(DepositoLite deposito) {
		this.deposito = deposito;
	}

	/**
	 * @param giacenzaLotti
	 *            The giacenzaLotti to set.
	 */
	public void setGiacenzaLotti(Double giacenzaLotti) {
		this.giacenzaLotti = giacenzaLotti;
	}

	/**
	 * @param giacenzaMagazzino
	 *            The giacenzaMagazzino to set.
	 */
	public void setGiacenzaMagazzino(Double giacenzaMagazzino) {
		this.giacenzaMagazzino = giacenzaMagazzino;
	}

}
