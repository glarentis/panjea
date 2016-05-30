package it.eurotn.panjea.intra.manager.interfaces;

import it.eurotn.panjea.intra.domain.IntraSettings;

public interface IntraSettingsManager {

	/**
	 * Carica il settings della gestione intra.<br/>
	 * Se non esiste ne crea uno, lo salva e lo restituisce.
	 * 
	 * @return <code>IntraSettings</code> caricato
	 */
	IntraSettings caricaIntraSettings();

	/**
	 * Salva un {@link IntraSettings}.
	 * 
	 * @param intraSettingsToSave
	 *            il settings dell'intra da salvare
	 * @return <code>IntraSettings</code> salvato
	 */
	IntraSettings salvaIntraSettings(IntraSettings intraSettingsToSave);

}
