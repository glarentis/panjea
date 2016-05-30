package it.eurotn.panjea.cosaro.sync.exporter;

import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

public class RigaDocumentoCosaro {

	private RigaLotto rigaLotto;
	private RigaArticolo rigaArticolo;

	/**
	 * Costruttore.
	 */
	public RigaDocumentoCosaro() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param rigaArticolo
	 *            rigaArticolo
	 * @param rigaLotto
	 *            rigaLotto
	 */
	public RigaDocumentoCosaro(final RigaArticolo rigaArticolo, final RigaLotto rigaLotto) {
		super();
		this.rigaArticolo = rigaArticolo;
		this.rigaLotto = rigaLotto;
	}

	/**
	 * @return il valore dell'attributo confezioni (codice conf)
	 */
	public Double getConfezioni() {
		Double value = 0.0;
		AttributoRiga attributo = rigaArticolo.getAttributo("conf");
		if (attributo != null) {
			Double val = (Double) attributo.getValoreTipizzato();
			value = val != null ? val : 0.0;
		}
		return value;
	}

	/**
	 * @return la qta di riga articolo o riga lotto se presente
	 */
	public Double getQuantita() {
		Double qta = rigaArticolo.getQta();
		if (rigaLotto != null) {
			qta = rigaLotto.getQuantita();
		}
		return qta;
	}

	/**
	 * @return the rigaArticolo
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * @return the rigaLotto
	 */
	public RigaLotto getRigaLotto() {
		return rigaLotto;
	}

	/**
	 * @param confezioni
	 *            the confezioni to set
	 */
	public void setConfezioni(Double confezioni) {

	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {

	}

	/**
	 * @param rigaArticolo
	 *            the rigaArticolo to set
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * @param rigaLotto
	 *            the rigaLotto to set
	 */
	public void setRigaLotto(RigaLotto rigaLotto) {
		this.rigaLotto = rigaLotto;
	}
}
