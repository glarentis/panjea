package it.eurotn.panjea.anagrafica.service.exception;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ContattoOrphanException extends Exception {

	private static final long serialVersionUID = 5414380461856621575L;

	/**
	 * l'attributo e' un istanza di ContattoVO e rappresenta il contatto orfano.
	 * 
	 * @uml.property name="contattoOrphan"
	 */
	private Object contattoOrphan;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param contattoOrphan
	 *            contatto orfano
	 */
	public ContattoOrphanException(final String message, final Object contattoOrphan) {
		super(message);
		this.contattoOrphan = contattoOrphan;
	}

	/**
	 * restituisce contattoOrphan.
	 * 
	 * @return contattoOrphan contatto orfano
	 * @uml.property name="contattoOrphan"
	 */
	public Object getContattoOrphan() {
		return contattoOrphan;
	}

}
