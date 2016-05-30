package it.eurotn.panjea.ordini.manager.interfaces;

import it.eurotn.panjea.ordini.domain.OrdiniSettings;

import javax.ejb.Local;

/**
 * Manager per gestire i settings degli ordini.
 *
 * @author fattazzo
 */
@Local
public interface OrdiniSettingsManager {

	/**
	 * Carica il settings degli ordini.<br/>
	 * Se non esiste ne crea uno, lo salva e lo restituisce.
	 *
	 * @return <code>OrdiniSettings</code> caricato
	 */
	OrdiniSettings caricaOrdiniSettings();

	/**
	 * Salva un {@link OrdiniSettings}.
	 *
	 * @param ordiniSettings
	 *            il settings degli ordini da salvare
	 * @return <code>OrdiniSettings</code> salvato
	 */
	OrdiniSettings salvaOrdiniSettings(OrdiniSettings ordiniSettings);

}
