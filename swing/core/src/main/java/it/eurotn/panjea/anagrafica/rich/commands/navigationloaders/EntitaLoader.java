package it.eurotn.panjea.anagrafica.rich.commands.navigationloaders;

public class EntitaLoader {

	public enum TipoEntitaLoader {
		AZIENDA, ENTITA
	};

	private TipoEntitaLoader tipoEntitaLoader;

	private Object value;

	/**
	 * Costruttore.
	 * 
	 * @param tipoEntitaLoader
	 *            tipo entita
	 * @param value
	 *            valore
	 */
	public EntitaLoader(final TipoEntitaLoader tipoEntitaLoader, final Object value) {
		super();
		this.tipoEntitaLoader = tipoEntitaLoader;
		this.value = value;
	}

	/**
	 * @return the tipoEntitaLoader
	 */
	public TipoEntitaLoader getTipoEntitaLoader() {
		return tipoEntitaLoader;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
}