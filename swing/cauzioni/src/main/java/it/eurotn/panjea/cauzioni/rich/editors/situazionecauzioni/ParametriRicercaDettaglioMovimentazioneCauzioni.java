package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import java.util.Set;

public class ParametriRicercaDettaglioMovimentazioneCauzioni {

	private Set<Integer> idEntita;

	private Set<Integer> idSedeEntita;

	private Set<Integer> idArticolo;

	/**
	 * Costruttore.
	 * 
	 * @param idEntita
	 *            entità
	 * @param idSedeEntita
	 *            sede
	 * @param idArticolo
	 *            articolo
	 */
	public ParametriRicercaDettaglioMovimentazioneCauzioni(final Set<Integer> idEntita,
			final Set<Integer> idSedeEntita, final Set<Integer> idArticolo) {
		super();
		this.idEntita = idEntita;
		this.idSedeEntita = idSedeEntita;
		this.idArticolo = idArticolo;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public Set<Integer> getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idEntita.
	 */
	public Set<Integer> getIdEntita() {
		return idEntita;
	}

	/**
	 * @return Returns the idSedeEntita.
	 */
	public Set<Integer> getIdSedeEntita() {
		return idSedeEntita;
	}

}
