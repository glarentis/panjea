/**
 *
 */
package it.eurotn.panjea.intra.domain;

/**
 * @author leonardo
 */
public enum NaturaTransazione {

	ACQUISTO_VENDITA_BARATTO("A", "1"), RESTITUZIONE_SOSTITUZIONE("B", "2"), AIUTI_PUBBLICI_PRIVATI("C", "3"), OPERAZIONE_PRE_CONTOTERZI(
			"D", "4"), OPERAZIONE_POST_CONTOTERZI("E", "5"), OPERAZIONE_SENZA_TRASF_PROPRIETA("F", "6"), PROGETTI_PROTEZIONE_INTERGOVERNATIVI(
			"G", "7"), FORNITURE_PER_COSTRUZIONI("H", "8"), ALTRE_TRANSAZIONI("I", "9");

	private String alfa;
	private String numeric;

	/**
	 * La natura della transazione con il relativo codice alfabetico e numerico associato.
	 * 
	 * @param alfa
	 *            il codice alfabetico associato
	 * @param numeric
	 *            il codice numerico associato
	 */
	NaturaTransazione(final String alfa, final String numeric) {
		this.alfa = alfa;
		this.numeric = numeric;
	}

	/**
	 * @return the alfa
	 */
	public String getAlfa() {
		return alfa;
	}

	/**
	 * @return the numeric
	 */
	public String getNumeric() {
		return numeric;
	}

}
