package it.eurotn.panjea.magazzino.exception;

/**
 * Eccezione rilanciata quando una riga magazzino non passa delle regole di validazione.<br>
 * 
 * @author giangi
 */
public class EsportaDocumentoCassaException extends Exception {
	// mesaggio di errore
	/**
	 * @uml.property name="errore"
	 */
	private String errore = new String();
	/**
	 * @uml.property name="descrizione"
	 */
	private String descrizione = new String();

	private static final long serialVersionUID = -8949812972491240119L;

	/**
	 * 
	 * @param e
	 *            errore
	 * @param descrizioneArticolo
	 *            descrizione dell'articolo
	 */
	public EsportaDocumentoCassaException(final String e, final String descrizioneArticolo) {
		setErrore(e);
		setDescrizione(descrizioneArticolo);
	}

	/**
	 * @return descrizione
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return .
	 * @uml.property name="errore"
	 */
	public String getErrore() {
		return errore;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param errore
	 *            = menssagio di errore .
	 * @uml.property name="errore"
	 */
	public void setErrore(String errore) {
		this.errore = errore;
	}
}
