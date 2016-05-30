package it.eurotn.panjea.fornodoro.importazione;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

import javax.ejb.EJB;

import org.apache.log4j.Logger;
import org.beanio.StreamFactory;

/**
 * Utilizzata per gestire le setting e caricare i file per IO.
 * 
 * @author giangi
 * @version 1.0, 04/dic/2012
 * 
 */
public abstract class AbstractPreferenceSettings {

	@EJB
	private PreferenceService preferenceService;
	@EJB
	protected PanjeaMessage panjeaMessage;

	private static Logger logger = Logger.getLogger(AbstractPreferenceSettings.class);

	/**
	 * Carica una chiave dalle preference. Se la chiave non esiste viene spedito um messaggio sulla queue.
	 * 
	 * @param key
	 *            chiave delle preference
	 * @return valore della preference. null se non trovato.
	 */
	protected String caricaPreference(String key) {
		String result = null;
		try {
			result = preferenceService.caricaPreference(key).getValore();
		} catch (PreferenceNotFoundException e) {
			logger.error("--> Errore ricerca preference con key " + key, e);
			panjeaMessage.send("Nelle preferenze generali manca la chiave  " + key,
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		}
		return result;
	}

	/**
	 * @param templateFileName
	 *            nome del file di template con il quale costruire il factoryStream
	 * @return factoryStream per l'import. Null se non esiste il file di template o il file di template non è corretto. <br/>
	 *         Eventuali errori vendono spediti sulla coda degli errori
	 */
	protected StreamFactory getStreamFactory(String templateFileName) {
		// Recupero la cartella dove sono i file di template
		// Recupero la cartella dove esportare i file
		Preference preference = null;
		StreamFactory factory = null;
		try {
			preference = preferenceService.caricaPreference("fornodoroDirTemplate");
			String templateFile = new StringBuilder(preference.getValore()).append(templateFileName).toString();
			factory = StreamFactory.newInstance();
			factory.load(templateFile);
		} catch (PreferenceNotFoundException e) {
			logger.error("--> Errore ricerca preference con key fornodoroDirTemplate", e);
			panjeaMessage.send("Nelle preferenze generali manca la chiave fornodoroDirTemplate ",
					PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
		} catch (Exception e) {
			panjeaMessage.send("Errore nel file di template " + e.getMessage(), PanjeaMessage.MESSAGE_GENERIC_SELECTOR);
			logger.error("-->Errore nel file di template ", e);
		}
		return factory;
	}
}
