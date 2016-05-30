/**
 * 
 */
package it.eurotn.panjea.magazzino.rulesvalidation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Esegue la validazione sul campo prezzo della riga articolo dell'area
 * magazzino. Solo se la riga ha prezzo non nullo e maggiore di 0 la validazione
 * ha successo.
 * 
 * @author fattazzo
 * 
 */
public class RigaMagazzinoPrezzoZeroRulesValidation extends AbstractRigaArticoloRulesValidation {

	private static final long serialVersionUID = 2878621552212730924L;

	private static Logger logger = Logger.getLogger(RigaMagazzinoPrezzoZeroRulesValidation.class);

	/**
	 * Costruttore di default.
	 */
	public RigaMagazzinoPrezzoZeroRulesValidation() {
		super("Prezzo zero");
	}

	@Override
	public boolean valida(RigaArticolo rigaArticolo) {
		if (rigaArticolo.getPrezzoUnitario() != null
				&& rigaArticolo.getPrezzoUnitario().getImportoInValutaAzienda().compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> Exit valida");
			return true;
		} else {
			logger.debug("--> Exit valida");
			return false;
		}
	}

	@Override
	public boolean valida(RigaArticoloLite rigaArticoloLite) {
		logger.debug("--> Enter valida");

		if (rigaArticoloLite.getPrezzoUnitario() != null
				&& rigaArticoloLite.getPrezzoUnitario().getImportoInValutaAzienda().compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> Exit valida");
			return true;
		} else {
			logger.debug("--> Exit valida");
			return false;
		}
	}

}
