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
@DiscriminatorValue("PCSS")
@NamedQueries({ @NamedQuery(name = "PoliticaCalcoloSottoSpecie.caricaPoliticheSimulazione", query = " from PoliticaCalcoloSottoSpecie pc where pc.simulazione.id = :paramIdSimulazione ") })
public class PoliticaCalcoloSottoSpecie extends PoliticaCalcolo {

	private static final long serialVersionUID = -2357090048746651831L;

	@ManyToOne
	private SottoSpecie sottoSpecie;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcoloSottoSpecie() {
		super();
		initialize();
	}

	@Override
	public String getCodiceEntitaPoliticaCalcolo() {
		return getSottoSpecie() != null ? getSottoSpecie().getCodice() : null;
	}

	@Override
	public int getDeep() {
		return DEEP_SOTTOSPECIE;
	}

	@Override
	public String getDescrizioneEntitaPoliticaCalcolo() {
		return getSottoSpecie() != null ? getSottoSpecie().getDescrizione() : null;
	}

	/**
	 * @return the sottoSpecie
	 */
	public SottoSpecie getSottoSpecie() {
		return sottoSpecie;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		sottoSpecie = new SottoSpecie();
	}

	/**
	 * @param sottoSpecie
	 *            the sottoSpecie to set
	 */
	public void setSottoSpecie(SottoSpecie sottoSpecie) {
		this.sottoSpecie = sottoSpecie;
	}

}
