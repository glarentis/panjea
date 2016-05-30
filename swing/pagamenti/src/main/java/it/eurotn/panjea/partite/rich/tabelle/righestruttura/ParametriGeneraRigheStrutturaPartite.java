package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;

/**
 * Parametri per la generazione di {@link RigaStrutturaPartite}.
 * 
 * @author leonardo
 */
public class ParametriGeneraRigheStrutturaPartite {

	private Integer numeroRate;
	private Integer intervallo;

	/**
	 * Costruttore di default.
	 */
	public ParametriGeneraRigheStrutturaPartite() {
		super();
	}

	/**
	 * @return the intervallo
	 */
	public Integer getIntervallo() {
		return intervallo;
	}

	/**
	 * @return the nRate
	 */
	public Integer getNumeroRate() {
		return numeroRate;
	}

	/**
	 * @param intervallo
	 *            the intervallo to set
	 */
	public void setIntervallo(Integer intervallo) {
		this.intervallo = intervallo;
	}

	/**
	 * @param rate
	 *            the nRate to set
	 */
	public void setNumeroRate(Integer rate) {
		numeroRate = rate;
	}

}
