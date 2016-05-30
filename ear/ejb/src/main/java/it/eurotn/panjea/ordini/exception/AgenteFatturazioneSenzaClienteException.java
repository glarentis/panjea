package it.eurotn.panjea.ordini.exception;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;

public class AgenteFatturazioneSenzaClienteException extends Exception {

	private static final long serialVersionUID = 3427370392482922590L;

	private AgenteLite agenteLite;

	/**
	 * Costruttore.
	 * 
	 * @param agenteLite
	 *            agente
	 */
	public AgenteFatturazioneSenzaClienteException(final AgenteLite agenteLite) {
		super();
		this.agenteLite = agenteLite;
	}

	/**
	 * @return Returns the agenteLite.
	 */
	public AgenteLite getAgenteLite() {
		return agenteLite;
	}

}
