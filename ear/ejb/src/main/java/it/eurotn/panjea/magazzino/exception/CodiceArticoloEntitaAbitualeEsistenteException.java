/**
 * 
 */
package it.eurotn.panjea.magazzino.exception;

import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;

/**
 * @author fattazzo
 * 
 */
public class CodiceArticoloEntitaAbitualeEsistenteException extends Exception {

	private static final long serialVersionUID = -1958514347751600565L;

	private CodiceArticoloEntita codiceArticoloEntita;

	/**
	 * Costruttore.
	 * 
	 * @param codiceArticoloEntita
	 *            codice articolo entita esistente
	 */
	public CodiceArticoloEntitaAbitualeEsistenteException(final CodiceArticoloEntita codiceArticoloEntita) {
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
