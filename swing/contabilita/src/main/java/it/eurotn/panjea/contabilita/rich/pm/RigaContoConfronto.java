package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;

import java.math.BigDecimal;

/**
 * Classe derivata da SaldoContoConfronto per presentare solo le informazioni del conto.
 * 
 * @author Leonardo
 */
public class RigaContoConfronto extends SaldoContoConfronto {

	private static final long serialVersionUID = -2422700662629326273L;

	/**
	 * Costruttore.
	 * 
	 * @param saldoConto
	 *            saldo conto
	 * @param importoDareConfronto
	 *            importo dare
	 * @param importoAvereConfronto
	 *            importo avere
	 */
	public RigaContoConfronto(final RigaConto saldoConto, final BigDecimal importoDareConfronto,
			final BigDecimal importoAvereConfronto) {
		super(saldoConto, true);
		this.setImportoDare2(importoDareConfronto);
		this.setImportoAvere2(importoAvereConfronto);
	}
}