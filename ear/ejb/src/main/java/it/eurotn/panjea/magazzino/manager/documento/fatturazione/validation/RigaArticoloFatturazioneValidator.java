/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.fatturazione.validation;

import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

/**
 * @author fattazzo
 *
 */
public class RigaArticoloFatturazioneValidator implements IRigaMagazzinoFatturazioneValidator {

	@Override
	public boolean isValid(RigaMagazzino rigaMagazzino, MagazzinoSettings magazzinoSettings) {
		return !((RigaArticolo) rigaMagazzino).isChiusa();
	}

}
