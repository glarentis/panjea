package it.eurotn.panjea.anagrafica.service.exception;

/**
 * Lanciata quando non trovo caricata una preferenza lato server e questa è obbligatoria.
 * 
 * @author giangi
 */
public class PreferenceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="preferenceKey"
	 */
	private String preferenceKey;

	/**
	 * Costruttore.
	 * 
	 * @param preferenceKey
	 *            chiave
	 */
	public PreferenceNotFoundException(final String preferenceKey) {
		super("Preferenza non trovata");
		this.preferenceKey = preferenceKey;
	}

	/**
	 * @return chiave
	 * @uml.property name="preferenceKey"
	 */
	public String getPreferenceKey() {
		return preferenceKey;
	}
}
