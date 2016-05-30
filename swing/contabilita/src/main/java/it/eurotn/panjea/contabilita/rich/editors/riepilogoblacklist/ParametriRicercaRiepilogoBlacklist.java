/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.riepilogoblacklist;

import java.util.Date;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaRiepilogoBlacklist {

	private Date dataIniziale;

	private Date dataFinale;

	/**
	 * @return the dataFinale
	 */
	public Date getDataFinale() {
		return dataFinale;
	}

	/**
	 * @return the dataIniziale
	 */
	public Date getDataIniziale() {
		return dataIniziale;
	}

	/**
	 * @param dataFinale
	 *            the dataFinale to set
	 */
	public void setDataFinale(Date dataFinale) {
		this.dataFinale = dataFinale;
	}

	/**
	 * @param dataIniziale
	 *            the dataIniziale to set
	 */
	public void setDataIniziale(Date dataIniziale) {
		this.dataIniziale = dataIniziale;
	}

}
