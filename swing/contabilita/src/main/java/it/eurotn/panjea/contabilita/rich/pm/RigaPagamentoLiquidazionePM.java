package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import java.math.BigDecimal;

public class RigaPagamentoLiquidazionePM {

	private TipoDocumento tipoDocumento = null;
	private BigDecimal totale = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param tipoDocumento
	 *            tipo documento
	 * @param totale
	 *            il totale per il tipo documento
	 */
	public RigaPagamentoLiquidazionePM(final TipoDocumento tipoDocumento, final BigDecimal totale) {
		super();
		this.tipoDocumento = tipoDocumento;
		this.totale = totale;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the totale
	 */
	public BigDecimal getTotale() {
		return totale;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param totale
	 *            the totale to set
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

}
