package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.math.BigDecimal;

public class GiacenzaOnRoad {

	private ArticoloLite articolo;

	private BigDecimal giacenza;

	private DepositoLite deposito;

	/**
	 * Costruttore.
	 * 
	 * @param articolo
	 *            articolo di riferimento
	 * @param deposito
	 *            deposito di riferimento
	 * @param giacenza
	 *            valore della giacenza
	 */
	public GiacenzaOnRoad(final ArticoloLite articolo, final DepositoLite deposito, final BigDecimal giacenza) {
		super();
		this.articolo = articolo;
		this.giacenza = giacenza;
		this.deposito = deposito;
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the deposito
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return the giacenza
	 */
	public BigDecimal getGiacenza() {
		return giacenza;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param deposito
	 *            the deposito to set
	 */
	public void setDeposito(DepositoLite deposito) {
		this.deposito = deposito;
	}

	/**
	 * @param giacenza
	 *            the giacenza to set
	 */
	public void setGiacenza(BigDecimal giacenza) {
		this.giacenza = giacenza;
	}

}
