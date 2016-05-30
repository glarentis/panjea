package it.eurotn.panjea.intra.domain.dichiarazione;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SoggettoDelegato implements Serializable {
	private static final long serialVersionUID = -2658019908771570714L;
	@Column(length = 20)
	private String partitaIvaSoggettoDelegato;
	@Column(length = 100)
	private String denominazioneSoggettoDelegato;

	/**
	 * @return Returns the denominazioneSoggettoDelegato.
	 */
	public String getDenominazioneSoggettoDelegato() {
		return denominazioneSoggettoDelegato;
	}

	/**
	 * @return Returns the partitaivaSoggettoDelegato.
	 */
	public String getPartitaIvaSoggettoDelegato() {
		return partitaIvaSoggettoDelegato;
	}

	/**
	 * @param denominazioneSoggettoDelegato
	 *            The denominazioneSoggettoDelegato to set.
	 */
	public void setDenominazioneSoggettoDelegato(String denominazioneSoggettoDelegato) {
		this.denominazioneSoggettoDelegato = denominazioneSoggettoDelegato;
	}

	/**
	 * @param partitaivaSoggettoDelegato
	 *            The partitaivaSoggettoDelegato to set.
	 */
	public void setPartitaIvaSoggettoDelegato(String partitaivaSoggettoDelegato) {
		this.partitaIvaSoggettoDelegato = partitaivaSoggettoDelegato;
	}

}
