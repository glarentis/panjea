/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Leonardo
 *
 */
@Entity
@DiscriminatorValue("PCS")
@NamedQueries({ @NamedQuery(name = "PoliticaCalcoloSpecie.caricaPoliticheSimulazione", query = " from PoliticaCalcoloSpecie pc where pc.simulazione.id = :paramIdSimulazione ") })
public class PoliticaCalcoloSpecie extends PoliticaCalcolo {

	private static final long serialVersionUID = 4371262063935805754L;

	@ManyToOne
	private Specie specie;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcoloSpecie() {
		super();
		initialize();
	}

	@Override
	public String getCodiceEntitaPoliticaCalcolo() {
		return getSpecie() != null ? getSpecie().getCodice() : null;
	}

	@Override
	public int getDeep() {
		return DEEP_SPECIE;
	}

	@Override
	public String getDescrizioneEntitaPoliticaCalcolo() {
		return getSpecie() != null ? getSpecie().getDescrizione() : null;
	}

	/**
	 * @return the specie
	 */
	public Specie getSpecie() {
		return specie;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		specie = new Specie();
	}

	/**
	 * @param specie
	 *            the specie to set
	 */
	public void setSpecie(Specie specie) {
		this.specie = specie;
	}

}
