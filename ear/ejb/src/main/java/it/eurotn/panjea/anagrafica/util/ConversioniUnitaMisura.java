package it.eurotn.panjea.anagrafica.util;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.magazzino.service.exception.ConversioneUnitaMisuraAssenteException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversioniUnitaMisura {

	private Map<String, ConversioneUnitaMisura> conversioni = null;

	/**
	 * ConversioniUnitaMisura.
	 * 
	 * @param conversioniUM
	 *            la lista di conversioni da gestire
	 */
	public ConversioniUnitaMisura(final List<ConversioneUnitaMisura> conversioniUM) {
		super();

		// preparo una mappa con chiave u.m.origine-u.m.destinazione per
		// accedere alle conversioni disponibili
		conversioni = new HashMap<String, ConversioneUnitaMisura>();
		for (ConversioneUnitaMisura conversioneUnitaMisura : conversioniUM) {
			String key = conversioneUnitaMisura.getUnitaMisuraOrigine().getCodice() + "-"
					+ conversioneUnitaMisura.getUnitaMisuraDestinazione().getCodice();
			key = key.toLowerCase();
			conversioni.put(key, conversioneUnitaMisura);
		}
	}

	/**
	 * Data la chiave, restituisce la conversione specifica.
	 * 
	 * @param umOrigine
	 *            u.m.origine
	 * @param umDestinazione
	 *            u.m.destinazione
	 * @return ConversioneUnitaMisura associata alla chiave specificata
	 */
	public ConversioneUnitaMisura getConversione(String umOrigine, String umDestinazione) {

		// creo la chiave con: u.m.riga-u.m.destinazione e la cerco nella mappa
		// conversioni u.m. disponibili
		String key = umOrigine + "-" + umDestinazione;
		key = key.toLowerCase();

		return conversioni.get(key);
	}

	/**
	 * Restituisce l'eccezione (senza rilanciarla) che identifica la combinazione mancante per la conversione tra u.m.
	 * diverse.
	 * 
	 * @param umOrigine
	 *            u.m.origine
	 * @param umDestinazione
	 *            u.m.destinazione
	 * @return ConversioneUnitaMisuraAssenteException che rappresenta la combinazione mancante
	 */
	public ConversioneUnitaMisuraAssenteException getConversioneUnitaMisuraAssenteException(String umOrigine,
			String umDestinazione) {
		ConversioneUnitaMisuraAssenteException conversioneUnitaMisuraAssenteException = new ConversioneUnitaMisuraAssenteException();
		conversioneUnitaMisuraAssenteException.setUnitaMisuraOrigine(umOrigine);
		conversioneUnitaMisuraAssenteException.setUnitaMisuraDestinazione(umDestinazione);
		return conversioneUnitaMisuraAssenteException;
	}

}
