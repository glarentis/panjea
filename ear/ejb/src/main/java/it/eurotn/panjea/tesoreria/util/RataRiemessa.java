package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.util.PanjeaEJBUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RataRiemessa extends Rata implements Serializable {

	private static final long serialVersionUID = 9066296280610321315L;

	private List<Rata> rateDaCreare;

	private BigDecimal importoRateRiemesse = null;

	/**
	 * Costruttore.
	 */
	public RataRiemessa() {
	}

	/**
	 *
	 * @param rata
	 *            rata da riemettere.
	 */
	public RataRiemessa(final Rata rata) {
		PanjeaEJBUtil.copyProperties(this, rata);
		rateDaCreare = new ArrayList<Rata>();
	}

	/**
	 *
	 * @return importo rimanente dell'insoluto da riemettere
	 */
	public BigDecimal getImportoDaRiemettere() {
		return getImporto().getImportoInValutaAzienda().subtract(importoRateRiemesse)
				.subtract(getImportoRateDaCreare());
	}

	/**
	 *
	 * @return importo delle rate da creare per l'insoluto.
	 */
	public BigDecimal getImportoRateDaCreare() {
		BigDecimal result = BigDecimal.ZERO;
		for (Rata rata : rateDaCreare) {
			if (rata.getImporto().getImportoInValuta() != null) {
				result = result.add(rata.getImporto().getImportoInValuta());
			}
		}
		return result;
	}

	public BigDecimal getImportoRateRiemesse() {
		return importoRateRiemesse;
	}

	/**
	 * @return Returns the rateDaCreare.
	 */
	public List<Rata> getRateDaCreare() {
		return rateDaCreare;
	}

	/**
	 *
	 * @return true se ho le rate emesse settate.
	 */
	public boolean isInizialized() {
		return importoRateRiemesse != null;
	}

	/**
	 * @param rateDaCreare
	 *            The rateDaCreare to set.
	 */
	public void setRateDaCreare(List<Rata> rateDaCreare) {
		this.rateDaCreare = rateDaCreare;
	}

	/**
	 *
	 * @param rateRiemesse
	 *            rateRiemesse per la rata.
	 */
	public void setRateRiemesse(final List<Rata> rateRiemesse) {
		importoRateRiemesse = BigDecimal.ZERO;
		for (Rata rata : rateRiemesse) {
			if (rata.getImporto().getImportoInValuta() != null) {
				importoRateRiemesse = importoRateRiemesse.add(rata.getImporto().getImportoInValuta());
			}
		}
	}
}
