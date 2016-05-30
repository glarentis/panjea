package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.domain.RegistroIva;

import java.math.BigDecimal;

public class RigaTotaliLiquidazionePM {

	private RegistroIva registroIva;
	private BigDecimal importoADebito;

	private BigDecimal importoACredito;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param registroIva
	 *            registro iva
	 * @param importo
	 *            importo
	 */
	public RigaTotaliLiquidazionePM(final RegistroIva registroIva, final BigDecimal importo) {
		super();
		this.registroIva = registroIva;

		switch (this.registroIva.getTipoRegistro()) {
		case ACQUISTO:
			this.importoACredito = importo;
			this.importoADebito = BigDecimal.ZERO;
			break;
		default:
			this.importoADebito = importo;
			this.importoACredito = BigDecimal.ZERO;
			break;
		}
	}

	/**
	 * @return the importoACredito
	 */
	public BigDecimal getImportoACredito() {
		return importoACredito;
	}

	/**
	 * @return the importoADebito
	 */
	public BigDecimal getImportoADebito() {
		return importoADebito;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

}
