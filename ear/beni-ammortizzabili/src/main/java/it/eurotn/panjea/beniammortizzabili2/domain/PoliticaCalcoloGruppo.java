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
@DiscriminatorValue("PCG")
@NamedQueries({ @NamedQuery(name = "PoliticaCalcoloGruppo.caricaPoliticheSimulazione", query = " from PoliticaCalcoloGruppo pc where pc.simulazione.id = :paramIdSimulazione ") })
public class PoliticaCalcoloGruppo extends PoliticaCalcolo {

	private static final long serialVersionUID = 1014367989118328459L;

	@ManyToOne
	private Gruppo gruppo;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcoloGruppo() {
		super();
		initialize();
	}

	@Override
	public String getCodiceEntitaPoliticaCalcolo() {
		return getGruppo() != null ? getGruppo().getCodice() : null;
	}

	@Override
	public int getDeep() {
		return DEEP_GRUPPO;
	}

	@Override
	public String getDescrizioneEntitaPoliticaCalcolo() {
		return getGruppo() != null ? getGruppo().getDescrizione() : null;
	}

	/**
	 * @return the gruppo
	 */
	public Gruppo getGruppo() {
		return gruppo;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		gruppo = new Gruppo();
	}

	/**
	 * @param gruppo
	 *            the gruppo to set
	 */
	public void setGruppo(Gruppo gruppo) {
		this.gruppo = gruppo;
	}

}
