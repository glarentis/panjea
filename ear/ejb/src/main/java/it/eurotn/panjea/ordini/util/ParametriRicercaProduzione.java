/**
 *
 */
package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author leonardo
 * 
 */
public class ParametriRicercaProduzione implements Serializable {

	private static final long serialVersionUID = -2461564700096533983L;

	private Periodo dataProduzione;
	private boolean effettuaRicerca;

	{
		dataProduzione = new Periodo();
		effettuaRicerca = true;
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaProduzione() {
		super();
	}

	/**
	 * @return the dataProduzione
	 */
	public Periodo getDataProduzione() {
		return dataProduzione;
	}

	/**
	 * @return data produzione finale
	 */
	public Date getDataProduzioneFinale() {
		return dataProduzione.getDataFinale();
	}

	/**
	 * @return data produzione iniziale
	 */
	public Date getDataProduzioneIniziale() {
		return dataProduzione.getDataIniziale();
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param dataProduzione
	 *            the dataProduzione to set
	 */
	public void setDataProduzione(Periodo dataProduzione) {
		this.dataProduzione = dataProduzione;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

}
