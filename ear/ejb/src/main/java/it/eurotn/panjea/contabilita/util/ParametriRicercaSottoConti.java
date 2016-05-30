/**
 * 
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;

import java.io.Serializable;

/**
 * @author adriano
 * @version 1.0, 21/set/07
 */
public class ParametriRicercaSottoConti implements Serializable {

	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum ETipoRicercaSottoConto {
		/**
		 * @uml.property name="cODICE"
		 * @uml.associationEnd
		 */
		CODICE, /**
		 * @uml.property name="dESCRIZIONE"
		 * @uml.associationEnd
		 */
		DESCRIZIONE
	}

	private static final long serialVersionUID = -7241580161045518489L;
	/**
	 * @uml.property name="sottotipoConto"
	 * @uml.associationEnd
	 */
	private SottotipoConto sottotipoConto;
	/**
	 * @uml.property name="tipoRicercaSottoConto"
	 * @uml.associationEnd
	 */
	private ETipoRicercaSottoConto tipoRicercaSottoConto;

	/**
	 * @uml.property name="valoreDaRicercare"
	 */
	private String valoreDaRicercare;

	private Boolean abilitato = null;

	/**
	 * 
	 */
	public ParametriRicercaSottoConti() {
		super();
		initialize();
	}

	/**
	 * @return the abilitato
	 */
	public Boolean getAbilitato() {
		return abilitato;
	}

	/**
	 * @return Returns the sottotipoConto.
	 * @uml.property name="sottotipoConto"
	 */
	public SottotipoConto getSottotipoConto() {
		return sottotipoConto;
	}

	/**
	 * @return Returns the tipoRicercaSottoConto.
	 * @uml.property name="tipoRicercaSottoConto"
	 */
	public ETipoRicercaSottoConto getTipoRicercaSottoConto() {
		return tipoRicercaSottoConto;
	}

	/**
	 * @return Returns the valoreDaRicercare.
	 * @uml.property name="valoreDaRicercare"
	 */
	public String getValoreDaRicercare() {
		return valoreDaRicercare;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.sottotipoConto = null;
		this.tipoRicercaSottoConto = ETipoRicercaSottoConto.CODICE;
		this.valoreDaRicercare = null;
	}

	/**
	 * @param abilitato
	 *            the abilitato to set
	 */
	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param sottotipoConto
	 *            The sottotipoConto to set.
	 * @uml.property name="sottotipoConto"
	 */
	public void setSottotipoConto(SottotipoConto sottotipoConto) {
		this.sottotipoConto = sottotipoConto;
	}

	/**
	 * @param tipoRicercaSottoConto
	 *            The tipoRicerca to set.
	 * @uml.property name="tipoRicercaSottoConto"
	 */
	public void setTipoRicercaSottoConto(ETipoRicercaSottoConto tipoRicercaSottoConto) {
		this.tipoRicercaSottoConto = tipoRicercaSottoConto;
	}

	/**
	 * @param valoreDaRicercare
	 *            The valoreDaRicercare to set.
	 * @uml.property name="valoreDaRicercare"
	 */
	public void setValoreDaRicercare(String valoreDaRicercare) {
		this.valoreDaRicercare = valoreDaRicercare;
	}

}
