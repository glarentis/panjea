/**
 * 
 */
package it.eurotn.panjea.anagrafica.service.exception;

/**
 * Eccezione sollevata nel caso in cui dopo la cancellazione di una
 * <code>SedeEntita</code> la <code>SedeAnagrafica</code> associata risulti
 * essere orfana.
 * 
 * @author Aracno
 * @version 1.0, 28-giu-2006
 */
public class SedeAnagraficaOrphanException extends Exception {

	/**
	 * L'oggetto e' un'istanza di <code>SedeAnagraficaVO</code> e rappresenta la
	 * sede anagrafica orfana.
	 * 
	 * @uml.property name="sedeAnagraficaOrphan"
	 */
	private Object sedeAnagraficaOrphan;

	private static final long serialVersionUID = -6903164756760457571L;

	/**
	 * Costruttore.
	 * 
	 * @param arg0
	 *            messaggio
	 * @param sedeAnagrafica
	 *            sede anagrafica orfana
	 */
	public SedeAnagraficaOrphanException(final String arg0, final Object sedeAnagrafica) {
		super(arg0);
		this.sedeAnagraficaOrphan = sedeAnagrafica;
	}

	/**
	 * @return sese anagrafica orfana
	 * @uml.property name="sedeAnagraficaOrphan"
	 */
	public Object getSedeAnagraficaOrphan() {
		return sedeAnagraficaOrphan;
	}

}
