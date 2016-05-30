/**
 *
 */
package it.eurotn.panjea.anagrafica.util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 *
 */
public class StatisticaEntitaDTO implements Serializable {

	private static final long serialVersionUID = -1562422347402552619L;

	private Integer idEntita;

	private BigDecimal importoFatturatoAnnoCorrente;
	private BigDecimal importoFatturatoAnnoPrecedente;

	private BigDecimal importoRateAperte;
	private BigDecimal importoRatePagateAnnoCorrente;

	private BigDecimal importoOrdinatoAnnoCorrente;
	private BigDecimal importoOrdinatoAnnoPrecedente;
	private BigDecimal importoOrdinatoDaEvadere;
	private BigDecimal numeroOrdiniDaEvadere;

	/**
	 * @return the idEntita
	 */
	public Integer getIdEntita() {
		return idEntita;
	}

	/**
	 * @return the importoFatturatoAnnoCorrente
	 */
	public BigDecimal getImportoFatturatoAnnoCorrente() {
		return importoFatturatoAnnoCorrente;
	}

	/**
	 * @return the importoFatturatoAnnoPrecedente
	 */
	public BigDecimal getImportoFatturatoAnnoPrecedente() {
		return importoFatturatoAnnoPrecedente;
	}

	/**
	 * @return the importoOrdinatoAnnoCorrente
	 */
	public BigDecimal getImportoOrdinatoAnnoCorrente() {
		return importoOrdinatoAnnoCorrente;
	}

	/**
	 * @return the importoOrdinatoAnnoPrecedente
	 */
	public BigDecimal getImportoOrdinatoAnnoPrecedente() {
		return importoOrdinatoAnnoPrecedente;
	}

	/**
	 * @return the importoOrdinatoDaEvadere
	 */
	public BigDecimal getImportoOrdinatoDaEvadere() {
		return importoOrdinatoDaEvadere;
	}

	/**
	 * @return the importoRateAperte
	 */
	public BigDecimal getImportoRateAperte() {
		return importoRateAperte;
	}

	/**
	 * @return the importoRatePagateAnnoCorrente
	 */
	public BigDecimal getImportoRatePagateAnnoCorrente() {
		return importoRatePagateAnnoCorrente;
	}

	/**
	 * @return the numeroOrdiniDaEvadere
	 */
	public BigDecimal getNumeroOrdiniDaEvadere() {
		return numeroOrdiniDaEvadere;
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
	}

	/**
	 * @param importoFatturatoAnnoCorrente
	 *            the importoFatturatoAnnoCorrente to set
	 */
	public void setImportoFatturatoAnnoCorrente(BigDecimal importoFatturatoAnnoCorrente) {
		this.importoFatturatoAnnoCorrente = importoFatturatoAnnoCorrente;
	}

	/**
	 * @param importoFatturatoAnnoPrecedente
	 *            the importoFatturatoAnnoPrecedente to set
	 */
	public void setImportoFatturatoAnnoPrecedente(BigDecimal importoFatturatoAnnoPrecedente) {
		this.importoFatturatoAnnoPrecedente = importoFatturatoAnnoPrecedente;
	}

	/**
	 * @param importoOrdinatoAnnoCorrente
	 *            the importoOrdinatoAnnoCorrente to set
	 */
	public void setImportoOrdinatoAnnoCorrente(BigDecimal importoOrdinatoAnnoCorrente) {
		this.importoOrdinatoAnnoCorrente = importoOrdinatoAnnoCorrente;
	}

	/**
	 * @param importoOrdinatoAnnoPrecedente
	 *            the importoOrdinatoAnnoPrecedente to set
	 */
	public void setImportoOrdinatoAnnoPrecedente(BigDecimal importoOrdinatoAnnoPrecedente) {
		this.importoOrdinatoAnnoPrecedente = importoOrdinatoAnnoPrecedente;
	}

	/**
	 * @param importoOrdinatoDaEvadere
	 *            the importoOrdinatoDaEvadere to set
	 */
	public void setImportoOrdinatoDaEvadere(BigDecimal importoOrdinatoDaEvadere) {
		this.importoOrdinatoDaEvadere = importoOrdinatoDaEvadere;
	}

	/**
	 * @param importoRateAperte
	 *            the importoRateAperte to set
	 */
	public void setImportoRateAperte(BigDecimal importoRateAperte) {
		this.importoRateAperte = importoRateAperte;
	}

	/**
	 * @param importoRatePagateAnnoCorrente
	 *            the importoRatePagateAnnoCorrente to set
	 */
	public void setImportoRatePagateAnnoCorrente(BigDecimal importoRatePagateAnnoCorrente) {
		this.importoRatePagateAnnoCorrente = importoRatePagateAnnoCorrente;
	}

	/**
	 * @param numeroOrdiniDaEvadere
	 *            the numeroOrdiniDaEvadere to set
	 */
	public void setNumeroOrdiniDaEvadere(BigDecimal numeroOrdiniDaEvadere) {
		this.numeroOrdiniDaEvadere = numeroOrdiniDaEvadere;
	}
}
