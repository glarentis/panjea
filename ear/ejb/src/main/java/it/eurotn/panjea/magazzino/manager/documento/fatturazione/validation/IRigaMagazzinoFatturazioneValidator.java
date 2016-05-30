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
public interface IRigaMagazzinoFatturazioneValidator{

	/**
	 * Verifica se la riga magazzino è valida per essere utilizzata nella fatturazione.
	 *
	 * @param rigaMagazzino
	 *            riga magazzino
	 * @param magazzinoSettings
	 *            settings del magazzino
	 * @return <code>true</code> se valida
	 */
	boolean isValid(RigaMagazzino rigaMagazzino, MagazzinoSettings magazzinoSettings);
}
