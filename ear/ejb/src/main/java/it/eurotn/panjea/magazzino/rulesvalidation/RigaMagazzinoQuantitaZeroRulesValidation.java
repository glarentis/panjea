/**
 * 
 */
package it.eurotn.panjea.magazzino.rulesvalidation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;

import org.apache.log4j.Logger;

/**
 * Esegue la validazione sul campo quantità della riga articolo dell'area magazzino. Solo se la riga ha quantità non
 * nulli la validazione ha successo.
 * 
 * @author fattazzo
 * 
 */
public class RigaMagazzinoQuantitaZeroRulesValidation extends AbstractRigaArticoloRulesValidation {

	private static final long serialVersionUID = 2524475876602581939L;

	private static Logger logger = Logger.getLogger(RigaMagazzinoQuantitaZeroRulesValidation.class);

	/**
	 * Costruttore di default.
	 */
	public RigaMagazzinoQuantitaZeroRulesValidation() {
		super("Qta zero");
	}

	@Override
	public boolean valida(RigaArticolo rigaArticolo) {
		return rigaArticolo.getQta() != 0;
	}

	@Override
	public boolean valida(RigaArticoloLite rigaArticoloLite) {
		logger.debug("--> Eseguo valida");
		if (rigaArticoloLite.getQta() == null) {
			return false;
		}
		return rigaArticoloLite.getQta() != 0;
	}

}
