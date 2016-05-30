/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.manutenzionelistino.updater.interfaces;

import it.eurotn.panjea.magazzino.domain.VersioneListino;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface ListinoManutenzioneUpdater {

	/**
	 * Aggiorna tutte le righe listino della versione specificata con quelle presenti nella manutenzione listino.
	 * 
	 * @param versioneListino
	 *            versione listino
	 */
	void aggiornaRigheListinoDaManutenzione(VersioneListino versioneListino);

	/**
	 * Aggiunge alla versione listino tutte le righe degli articoli della manutenzione che non sono presenti.
	 * 
	 * @param versioneListino
	 *            versione listino
	 */
	void aggiungiRigheListinoMancantiDaManutenzione(VersioneListino versioneListino);

}
