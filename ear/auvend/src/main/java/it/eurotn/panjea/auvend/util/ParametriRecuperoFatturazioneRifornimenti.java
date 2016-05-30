package it.eurotn.panjea.auvend.util;

import java.io.Serializable;
import java.util.Date;

public class ParametriRecuperoFatturazioneRifornimenti implements Serializable {

	private static final long serialVersionUID = 5321949825020066187L;

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

}
