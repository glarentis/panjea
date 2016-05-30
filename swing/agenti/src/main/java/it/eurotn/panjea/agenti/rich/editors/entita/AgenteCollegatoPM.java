package it.eurotn.panjea.agenti.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.Entita;

public class AgenteCollegatoPM {
	private Entita agente;

	/**
	 * Costruttore.
	 */
	public AgenteCollegatoPM() {
	}

	/**
	 * Costruttore.
	 *
	 * @param agente
	 *            agente wrappato
	 */
	public AgenteCollegatoPM(final Entita agente) {
		super();
		this.agente = agente;
	}

	/**
	 * @return Returns the agente.
	 */
	public Entita getAgente() {
		return agente;
	}

	/**
	 * @param agente
	 *            The agente to set.
	 */
	public void setAgente(Entita agente) {
		this.agente = agente;
	}
}