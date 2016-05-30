package it.eurotn.panjea.conai.domain;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;

import java.math.BigDecimal;

/**
 * Classe DTO per gestire le statistiche del conai.
 * 
 * @author giangi
 * @version 1.0, 07/mag/2012
 */
public class StatisticaTipoImballo {

	private BigDecimal pesoTotale;
	private BigDecimal pesoPerEsenzione;
	private ConaiMateriale materiale;
	private ConaiTipoImballo tipoImballo;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StatisticaTipoImballo other = (StatisticaTipoImballo) obj;
		if (tipoImballo != other.tipoImballo) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the materiale.
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

	/**
	 * @return peso assogettato alla tassa
	 */
	public BigDecimal getPeso() {
		return pesoTotale.subtract(pesoPerEsenzione);
	}

	/**
	 * @return Returns the pesoPerEsenzione.
	 */
	public BigDecimal getPesoPerEsenzione() {
		return pesoPerEsenzione;
	}

	/**
	 * @return Returns the pesoTotale.
	 */
	public BigDecimal getPesoTotale() {
		return pesoTotale;
	}

	/**
	 * @return Returns the tipoImballo.
	 */
	public ConaiTipoImballo getTipoImballo() {
		return tipoImballo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipoImballo == null) ? 0 : tipoImballo.hashCode());
		return result;
	}

	/**
	 * @param materiale
	 *            The materiale to set.
	 */
	public void setMateriale(ConaiMateriale materiale) {
		this.materiale = materiale;
	}

	/**
	 * @param pesoPerEsenzione
	 *            The pesoPerEsenzione to set.
	 */
	public void setPesoPerEsenzione(BigDecimal pesoPerEsenzione) {
		this.pesoPerEsenzione = pesoPerEsenzione;
	}

	/**
	 * @param pesoTotale
	 *            The pesoTotale to set.
	 */
	public void setPesoTotale(BigDecimal pesoTotale) {
		this.pesoTotale = pesoTotale;
	}

	/**
	 * @param tipoImballo
	 *            The tipoImballo to set.
	 */
	public void setTipoImballo(ConaiTipoImballo tipoImballo) {
		this.tipoImballo = tipoImballo;
	}
}
