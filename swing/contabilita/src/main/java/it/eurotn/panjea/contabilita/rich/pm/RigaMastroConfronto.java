package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;

import java.math.BigDecimal;

/**
 * Classe derivata da SaldoContoConfronto per presentare solo le informazioni del mastro.
 * 
 * @author Leonardo
 */
public class RigaMastroConfronto extends SaldoContoConfronto {

	private static final long serialVersionUID = -8948757482157294283L;

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
	public RigaMastroConfronto(final RigaMastro saldoConto, final BigDecimal importoDareConfronto,
			final BigDecimal importoAvereConfronto) {
		super(saldoConto, true);
		this.setImportoDare2(importoDareConfronto);
		this.setImportoAvere2(importoAvereConfronto);
	}
}