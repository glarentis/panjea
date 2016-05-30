package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;

import java.math.BigDecimal;

/**
 * Classe derivata da SottoContoDTO per presentare solo le informazioni del centro costo.
 * 
 * @author Leonardo
 */
public class RigaContoCentroCostoConfronto extends SaldoContoConfronto {

	private static final long serialVersionUID = -4456062065520004741L;

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
	public RigaContoCentroCostoConfronto(final RigaContoCentroCosto saldoConto, final BigDecimal importoDareConfronto,
			final BigDecimal importoAvereConfronto) {
		super(saldoConto, true);
		this.setImportoDare2(importoDareConfronto);
		this.setImportoAvere2(importoAvereConfronto);
	}
}