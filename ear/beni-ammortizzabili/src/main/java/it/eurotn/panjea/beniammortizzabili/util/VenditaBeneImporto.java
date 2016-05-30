/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.util;

import java.math.BigDecimal;

/**
 * 
 * @author adriano
 * @version 1.0, 03/dic/07
 * 
 */
public class VenditaBeneImporto {

	private final Integer idBeneAmmortizzabile;

	private final BigDecimal importoStornoValoreBene;
	private final BigDecimal importoStornoFondoAmmortamento;

	/**
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene
	 * @param paramimportoStornoValoreBene
	 *            importo storico di riferimento
	 * @param paramImportoStornoFondoAmmortamento
	 *            importo storno fondo ammortamento
	 */
	public VenditaBeneImporto(final Integer paramIdBeneAmmortizzabile, final BigDecimal paramimportoStornoValoreBene,
			final BigDecimal paramImportoStornoFondoAmmortamento) {
		super();
		this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
		this.importoStornoValoreBene = paramimportoStornoValoreBene;
		this.importoStornoFondoAmmortamento = paramImportoStornoFondoAmmortamento;
	}

	/**
	 * @return Returns the idBeneAmmortizzabile.
	 */
	public Integer getIdBeneAmmortizzabile() {
		return idBeneAmmortizzabile;
	}

	/**
	 * @return Returns the importoStornoFondoAmmortamento.
	 */
	public BigDecimal getImportoStornoFondoAmmortamento() {
		return importoStornoFondoAmmortamento;
	}

	/**
	 * @return the importoStornoValoreBene
	 */
	public BigDecimal getImportoStornoValoreBene() {
		return importoStornoValoreBene;
	}

}
