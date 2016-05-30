/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.anagrafica.util;

import it.eurotn.panjea.anagrafica.domain.ETipologiaPagamento;

import java.io.Serializable;

/**
 * Raccoglie gli attributi utilizzati durante la generazione delle rate
 * nell'inserimento del codice pagamento.
 * 
 * @author adriano
 */
public class PoliticaGenerazioneRate implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="tipologiaPagamentoDefault"
	 * @uml.associationEnd
	 */
	private ETipologiaPagamento tipologiaPagamentoDefault;

	/**
	 * @uml.property name="numeroRate"
	 */
	private int numeroRate;

	/**
	 * @uml.property name="intervalloRata"
	 */
	private int intervalloRata;
	/**
	 * @uml.property name="percentualeIvaSuPrimaRata"
	 */
	private boolean percentualeIvaSuPrimaRata;

	/**
	 * get di intervallo rata per le loro generazione.
	 * 
	 * @return intervallo
	 * @uml.property name="intervalloRata"
	 */
	public int getIntervalloRata() {
		return intervalloRata;
	}

	/**
	 * Numero rate da generare durante l'inserimento di CodicePagamento.
	 * 
	 * @return Returns the numeroRate.
	 * @uml.property name="numeroRate"
	 */
	public int getNumeroRate() {
		return numeroRate;
	}

	/**
	 * getter della tipologia di pagamento di default per la generazione delle
	 * rate.
	 * 
	 * @return tipologiaPagamentoDefault
	 * @uml.property name="tipologiaPagamentoDefault"
	 */
	public ETipologiaPagamento getTipologiaPagamentoDefault() {
		return tipologiaPagamentoDefault;
	}

	/**
	 * indicatore di generazione rata con percentuale iva su prima rata.
	 * 
	 * @return percentualeIvaSuPrimaRata
	 * @uml.property name="percentualeIvaSuPrimaRata"
	 */
	public boolean isPercentualeIvaSuPrimaRata() {
		return percentualeIvaSuPrimaRata;
	}

	/**
	 * @param intervalloRata
	 *            the intervalloRata to set
	 * @uml.property name="intervalloRata"
	 */
	public void setIntervalloRata(int intervalloRata) {
		this.intervalloRata = intervalloRata;
	}

	/**
	 * @param numeroRate
	 *            the numeroRate to set
	 * @uml.property name="numeroRate"
	 */
	public void setNumeroRate(int numeroRate) {
		this.numeroRate = numeroRate;
	}

	/**
	 * @param percentualeIvaSuPrimaRata
	 *            the percentualeIvaSuPrimaRata to set
	 * @uml.property name="percentualeIvaSuPrimaRata"
	 */
	public void setPercentualeIvaSuPrimaRata(boolean percentualeIvaSuPrimaRata) {
		this.percentualeIvaSuPrimaRata = percentualeIvaSuPrimaRata;
	}

	/**
	 * @param tipologiaPagamentoDefault
	 *            the tipologiaPagamentoDefault to set
	 * @uml.property name="tipologiaPagamentoDefault"
	 */
	public void setTipologiaPagamentoDefault(ETipologiaPagamento tipologiaPagamentoDefault) {
		this.tipologiaPagamentoDefault = tipologiaPagamentoDefault;
	}

}
