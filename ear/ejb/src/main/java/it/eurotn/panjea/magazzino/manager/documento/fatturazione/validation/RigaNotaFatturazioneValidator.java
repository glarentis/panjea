/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.fatturazione.validation;

import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

/**
 * @author fattazzo
 *
 */
public class RigaNotaFatturazioneValidator implements IRigaMagazzinoFatturazioneValidator {

	@Override
	public boolean isValid(RigaMagazzino rigaMagazzino, MagazzinoSettings magazzinoSettings) {
		// le righe nota sono sempre valide per la fatturazione
		return true;
	}

}
