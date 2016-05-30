package it.eurotn.panjea.lotti.util;

import java.io.Serializable;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoRendicontazione;

public class RigaLottoRendicontazione implements Serializable {

	private static final long serialVersionUID = 113963771212235586L;

	private RigaMagazzinoRendicontazione rigaMagazzino;

	private Lotto lotto;

	private Double quantita;

	private int progressivo;

	/**
	 * @return the lotto
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @return the progressivo
	 */
	public int getProgressivo() {
		return progressivo;
	}

	/**
	 * @return the quantita
	 */
	public Double getQuantita() {
		return quantita;
	}

	/**
	 * @return the rigaMagazzino
	 */
	public RigaMagazzinoRendicontazione getRigaMagazzino() {
		return rigaMagazzino;
	}

	/**
	 * @param lotto
	 *            the lotto to set
	 */
	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}

	/**
	 * @param progressivo
	 *            the progressivo to set
	 */
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param rigaMagazzino
	 *            the rigaMagazzino to set
	 */
	public void setRigaMagazzino(RigaMagazzinoRendicontazione rigaMagazzino) {
		this.rigaMagazzino = rigaMagazzino;
	}
}
