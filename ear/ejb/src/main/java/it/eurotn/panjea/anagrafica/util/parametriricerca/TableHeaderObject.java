package it.eurotn.panjea.anagrafica.util.parametriricerca;

/**
 * Coppie di chiavi valore da stampare come header del report. Utilizzata da JideTableWidget
 * 
 * @author giangi
 * @version 1.0, 19/ago/2010
 */
public class TableHeaderObject {

	/**
	 * @uml.property name="key"
	 */
	private final String key;

	/**
	 * @uml.property name="value"
	 */
	private final Object value;

	/**
	 * Costruttore.
	 * 
	 * @param key
	 *            chiave
	 * @param value
	 *            valore
	 */
	public TableHeaderObject(final String key, final Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 * @uml.property name="key"
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value
	 * @uml.property name="value"
	 */
	public Object getValue() {
		return value;
	}

}
