package it.eurotn.panjea.contabilita.manager.ivasospesa;

public class IvaSospesaCalculatorStrategy {

	/**
	 * Restituisce il {@link IvaSospesaCalculator} adatto a seconda del parametro passato. Cambia il metodo di calcolo a
	 * seconda che il pagamento sia l'ultimo nel periodo o meno.<br>
	 * Normalmente vanno calcolati imposta e imponibile in percentuale pagata, nel caso in cui invece sia l'ultimo
	 * pagamento devo fare la differenza tra il totale imposta e imponibile e la somma di imposta e imponibile in
	 * percentuale.
	 * 
	 * @param isUltimoPagamento
	 *            isUltimoPagamento
	 * @return {@link IvaSospesaCalculator}
	 */
	public IvaSospesaCalculator getCalculator(boolean isUltimoPagamento) {
		if (isUltimoPagamento) {
			return new IvaSospesaPerDifferenzaCalculator();
		} else {
			return new IvaSospesaPerPercentualeCalculator();
		}
	}

}
