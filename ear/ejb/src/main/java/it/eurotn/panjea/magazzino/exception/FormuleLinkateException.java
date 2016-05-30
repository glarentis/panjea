package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;

import java.util.Map;

public class FormuleLinkateException extends Exception {

	private static final long serialVersionUID = -6460331674350612346L;

	private TipoAttributo tipoAttributoDaControllare;
	private FormulaTrasformazione formulaTrasformazioneDaControllare;

	private Map<TipoAttributo, FormulaTrasformazione> tipiAttributoInConflitto;

	/**
	 * Costruttore.
	 * 
	 * @param tipoAttributoDaControllare
	 *            tipo attributo da controllare
	 * @param formulaTrasformazioneDaControllare
	 *            formula da controllare
	 * @param tipiAttributoInConflitto
	 *            tipi attributi in conflitto
	 */
	public FormuleLinkateException(final TipoAttributo tipoAttributoDaControllare,
			final FormulaTrasformazione formulaTrasformazioneDaControllare,
			final Map<TipoAttributo, FormulaTrasformazione> tipiAttributoInConflitto) {
		super();
		this.tipoAttributoDaControllare = tipoAttributoDaControllare;
		this.formulaTrasformazioneDaControllare = formulaTrasformazioneDaControllare;
		this.tipiAttributoInConflitto = tipiAttributoInConflitto;
	}

	/**
	 * @return the formulaTrasformazioneDaControllare
	 */
	public FormulaTrasformazione getFormulaTrasformazioneDaControllare() {
		return formulaTrasformazioneDaControllare;
	}

	/**
	 * @return the tipiAttributoInConflitto
	 */
	public Map<TipoAttributo, FormulaTrasformazione> getTipiAttributoInConflitto() {
		return tipiAttributoInConflitto;
	}

	/**
	 * @return the tipoAttributoDaControllare
	 */
	public TipoAttributo getTipoAttributoDaControllare() {
		return tipoAttributoDaControllare;
	}
}
