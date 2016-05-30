package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;

public class CodiceArticoloEntitaContoTerziEsistenteException extends Exception {

	private static final long serialVersionUID = 1988095641729196391L;

	private CodiceArticoloEntita codiceArticoloEntita;

	/**
	 * Costruttore.
	 * 
	 * @param codiceArticoloEntita
	 *            {@link CodiceArticoloEntita}
	 */
	public CodiceArticoloEntitaContoTerziEsistenteException(final CodiceArticoloEntita codiceArticoloEntita) {
		super();
		this.codiceArticoloEntita = codiceArticoloEntita;
	}

	/**
	 * @return the codiceArticoloEntita
	 */
	public CodiceArticoloEntita getCodiceArticoloEntita() {
		return codiceArticoloEntita;
	}
}
