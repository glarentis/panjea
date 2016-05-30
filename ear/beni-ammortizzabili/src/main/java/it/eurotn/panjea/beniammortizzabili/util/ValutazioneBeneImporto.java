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
public class ValutazioneBeneImporto {

	private final Integer idBeneAmmortizzabile;
	private final BigDecimal importoValutazioneBene;
	private final BigDecimal importoValutazioneFondo;

	/**
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene
	 * @param paramImportoValutazioneBene
	 *            importo valutazione bene
	 * @param paramImportoValutazioneFondo
	 *            importo valutazione fondo
	 */
	public ValutazioneBeneImporto(final Integer paramIdBeneAmmortizzabile,
			final BigDecimal paramImportoValutazioneBene, final BigDecimal paramImportoValutazioneFondo) {
		super();
		this.idBeneAmmortizzabile = paramIdBeneAmmortizzabile;
		this.importoValutazioneBene = paramImportoValutazioneBene;
		this.importoValutazioneFondo = paramImportoValutazioneFondo;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getIdBeneAmmortizzabile() {
		return idBeneAmmortizzabile;
	}

	/**
	 * @return Returns the importoValutazioneBene.
	 */
	public BigDecimal getImportoValutazioneBene() {
		return importoValutazioneBene;
	}

	/**
	 * @return Returns the importoValutazioneFondo.
	 */
	public BigDecimal getImportoValutazioneFondo() {
		return importoValutazioneFondo;
	}

}
