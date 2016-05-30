/**
 * 
 */
package it.eurotn.panjea.magazzino.rulesvalidation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;

/**
 * Esegue la validazione del campo del prezzo determinato sulla riga articolo. Se il prezzo determinato è diverso dal
 * prezzo unitario la validazione non ha successo.
 * 
 * @author fattazzo
 * 
 */
public class RigaMagazzinoPrezzoDeterminatoDiversoRulesValidation extends AbstractRigaArticoloRulesValidation {

	private static final long serialVersionUID = 976121526499272422L;

	/**
	 * Costruttore di default.
	 */
	public RigaMagazzinoPrezzoDeterminatoDiversoRulesValidation() {
		super("Prezzo determinato diverso");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation#valida(it.eurotn.panjea.magazzino
	 * .domain.RigaArticolo)
	 */
	@Override
	public boolean valida(RigaArticolo rigaArticolo) {
		if (rigaArticolo.getPrezzoDeterminato() != null
				&& (rigaArticolo.getPrezzoDeterminato().compareTo(
						rigaArticolo.getPrezzoUnitario().getImportoInValutaAzienda()) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation#valida(it.eurotn.panjea.magazzino
	 * .domain.RigaArticoloLite)
	 */
	@Override
	public boolean valida(RigaArticoloLite rigaArticoloLite) {
		if (rigaArticoloLite.getPrezzoDeterminato() != null
				&& (rigaArticoloLite.getPrezzoDeterminato().compareTo(
						rigaArticoloLite.getPrezzoUnitario().getImportoInValutaAzienda()) == 0)) {
			return true;
		} else {
			return false;
		}
	}

}
