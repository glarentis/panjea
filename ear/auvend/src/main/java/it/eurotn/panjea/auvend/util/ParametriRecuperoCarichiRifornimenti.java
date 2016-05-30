/**
 * 
 */
package it.eurotn.panjea.auvend.util;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe di utilita' per impostare i parametri per l'esecuzione del recupero carichi da AuVend.
 * 
 */
public class ParametriRecuperoCarichiRifornimenti implements Serializable {

	private static final long serialVersionUID = 2L;

	private Date dataInizio;
	private Date dataFine;

	/**
	 * @return Returns the dataFine.
	 */
	public Date getDataFine() {
		return dataFine;
	}

	/**
	 * @return Returns the dataInizio.
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * @param dataFine
	 *            The dataFine to set.
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * @param dataInizio
	 *            The dataInizio to set.
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("ParametriRecuperoCarichi[ ").append(super.toString());
		retValue.append(" dataInizio = ").append(this.dataInizio);
		retValue.append(" dataFine = ").append(this.dataFine);
		retValue.append(" ]");

		return retValue.toString();
	}

}
