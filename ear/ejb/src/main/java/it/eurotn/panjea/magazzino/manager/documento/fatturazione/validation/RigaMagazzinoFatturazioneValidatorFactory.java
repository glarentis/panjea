/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.fatturazione.validation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaTestata;

/**
 * @author fattazzo
 *
 */
public class RigaMagazzinoFatturazioneValidatorFactory {

	private RigaArticoloFatturazioneValidator rigaArticoloFatturazioneValidator = new RigaArticoloFatturazioneValidator();
	private RigaNotaFatturazioneValidator rigaNotaFatturazioneValidator = new RigaNotaFatturazioneValidator();
	private RigaTestataFatturazioneValidator rigaTestataFatturazioneValidator = new RigaTestataFatturazioneValidator();

	/**
	 *
	 * Costruttore.
	 */
	public RigaMagazzinoFatturazioneValidatorFactory() {

	}

	/**
	 * Restituisce il validator corretto da utilizzare per la riga magazzino durante la fatturazione.
	 *
	 * @param rigaMagazzino
	 *            riga magazzino
	 * @return validator
	 */
	public IRigaMagazzinoFatturazioneValidator getValidator(RigaMagazzino rigaMagazzino) {

		IRigaMagazzinoFatturazioneValidator validator = null;

		if (rigaMagazzino instanceof RigaArticolo) {
			validator = rigaArticoloFatturazioneValidator;
		} else if (rigaMagazzino.getClass().getName().equals(RigaNota.class.getName())) {
			validator = rigaNotaFatturazioneValidator;
		} else if (rigaMagazzino.getClass().getName().equals(RigaTestata.class.getName())) {
			validator = rigaTestataFatturazioneValidator;
		}

		return validator;
	}
}
