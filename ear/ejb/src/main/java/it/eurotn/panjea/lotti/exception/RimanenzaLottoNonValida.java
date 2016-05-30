package it.eurotn.panjea.lotti.exception;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.io.Serializable;

public class RimanenzaLottoNonValida extends Object implements Serializable {

	private static final long serialVersionUID = 7770381407128065208L;

	private ArticoloLite articolo;

	private Lotto lotto;

	private Double rimanenzaCalcolata;

	private Double qtaRichiesta;

	private DepositoLite depositoOrigine;

	{
		this.articolo = new ArticoloLite();
		this.depositoOrigine = new DepositoLite();
		this.lotto = new Lotto();
		this.rimanenzaCalcolata = 0.0;
		this.qtaRichiesta = 0.0;
	}

	/**
	 * Costruttore.
	 */
	public RimanenzaLottoNonValida() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param articolo
	 *            articolo
	 * @param depositoOrigine
	 *            deposito di origine
	 * @param lotto
	 *            lotto
	 * @param rimanenzaCalcolata
	 *            rimanenza
	 * @param qtaRichiesta
	 *            qta richiesta della riga
	 */
	public RimanenzaLottoNonValida(final ArticoloLite articolo, final DepositoLite depositoOrigine, final Lotto lotto,
			final Double rimanenzaCalcolata, final Double qtaRichiesta) {
		super();
		this.articolo = articolo;
		this.depositoOrigine = depositoOrigine;
		this.lotto = lotto;
		this.rimanenzaCalcolata = rimanenzaCalcolata;
		this.qtaRichiesta = qtaRichiesta;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RimanenzaLottoNonValida other = (RimanenzaLottoNonValida) obj;
		if (articolo == null) {
			if (other.articolo != null) {
				return false;
			}
		} else if (!articolo.equals(other.articolo)) {
			return false;
		}
		if (depositoOrigine == null) {
			if (other.depositoOrigine != null) {
				return false;
			}
		} else if (!depositoOrigine.equals(other.depositoOrigine)) {
			return false;
		}
		if (lotto == null) {
			if (other.lotto != null) {
				return false;
			}
		} else if (!lotto.equals(other.lotto)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the deposito.
	 */
	public DepositoLite getDepositoOrigine() {
		return depositoOrigine;
	}

	/**
	 * @return Returns the lotto.
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @return Returns the qtaRichiesta.
	 */
	public Double getQtaRichiesta() {
		return qtaRichiesta;
	}

	/**
	 * @return Returns the rimanenzaCalcolata.
	 */
	public Double getRimanenzaCalcolata() {
		return rimanenzaCalcolata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articolo == null) ? 0 : articolo.hashCode());
		result = prime * result + ((depositoOrigine == null) ? 0 : depositoOrigine.hashCode());
		result = prime * result + ((lotto == null) ? 0 : lotto.hashCode());
		return result;
	}

}
