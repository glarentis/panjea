/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.fatturazione.validation;

import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

/**
 * @author fattazzo
 *
 */
public class RigaTestataFatturazioneValidator implements IRigaMagazzinoFatturazioneValidator {

	@Override
	public boolean isValid(RigaMagazzino rigaMagazzino, MagazzinoSettings magazzinoSettings) {
		RigaTestata rigaTestata = (RigaTestata) rigaMagazzino;

		boolean testateOrdineOk = (rigaTestata.getAreaCollegata() instanceof AreaOrdine)
				&& magazzinoSettings.isRiportaTestateOrdineInFatturazione();

		return !rigaTestata.isRigaTestataDocumento() || testateOrdineOk;
	}

}
