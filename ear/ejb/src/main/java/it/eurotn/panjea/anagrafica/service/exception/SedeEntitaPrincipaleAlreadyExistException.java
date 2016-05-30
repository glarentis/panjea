/**
 * 
 */
package it.eurotn.panjea.anagrafica.service.exception;

/**
 * Eccezione sollevata nel caso si verificasse l'aggiunta di una
 * <code>SedeEntita</code> principale SedeEntita.getTipoSede.isSedePrincipale ==
 * true.
 * 
 * @author Adry
 * @version 1.0, 13-lug-2006
 */
public class SedeEntitaPrincipaleAlreadyExistException extends Exception {

	private static final long serialVersionUID = -4436485743922776208L;

	/**
	 * sedeEntita contiene un istanza di <code>SedeEntitaVO</code> come sede
	 * principale corrente.
	 * 
	 * @uml.property name="sedeEntitaPrincipale"
	 */
	private Object sedeEntitaPrincipale;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param sedeEntitaPrincipale
	 *            sede
	 */
	public SedeEntitaPrincipaleAlreadyExistException(final String message, final Object sedeEntitaPrincipale) {
		super(message);
		this.sedeEntitaPrincipale = sedeEntitaPrincipale;
	}

	/**
	 * @return sedeEntita come sede entita principale corrente
	 * @uml.property name="sedeEntitaPrincipale"
	 */
	public Object getSedeEntitaPrincipale() {
		return sedeEntitaPrincipale;
	}

}
