package it.eurotn.panjea.bi.rich.editors.analisi.converter;

import com.jidesoft.converter.ConverterContext;

public class NumberPivotContext extends ConverterContext {
	private static final long serialVersionUID = 1L;
	private int numeroDecimali;
	private boolean separatore;
	private String postFisso;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public NumberPivotContext() {
		super("numberPivotContext");
		this.numeroDecimali = 0;
		this.separatore = false;
		this.postFisso = "";
	}

	/**
	 *
	 * Costruttore.
	 *
	 * @param numeroDecimali
	 *            decimali da visualizzare
	 * @param separatore
	 *            true per visualizzare il separatore delle migliaia
	 * @param postFisso
	 *            postFisso da visualizzare
	 */

	public NumberPivotContext(final int numeroDecimali, final boolean separatore, final String postFisso) {
		super("numberPivotContext");
		this.numeroDecimali = numeroDecimali;
		this.separatore = separatore;
		this.postFisso = postFisso;
	}

	/**
	 * @return Returns the numeroDecimali.
	 */
	public int getNumeroDecimali() {
		return numeroDecimali;
	}

	/**
	 * @return Returns the postFisso.
	 */
	public String getPostFisso() {
		return postFisso;
	}

	/**
	 * @return Returns the separatore.
	 */
	public boolean isSeparatoreVisible() {
		return separatore;
	}

	/**
	 * @param numeroDecimali
	 *            The numeroDecimali to set.
	 */
	public void setNumeroDecimali(int numeroDecimali) {
		this.numeroDecimali = numeroDecimali;
	}

	/**
	 * @param postFisso
	 *            The postFisso to set.
	 */
	public void setPostFisso(String postFisso) {
		this.postFisso = postFisso;
	}
}
