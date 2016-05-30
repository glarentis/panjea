package it.eurotn.panjea.ordini.domain;

public class NotaOrdineImportata {

	private String numeroRiga;

	private OrdineImportato ordine;

	private String nota;

	/**
	 * Costruttore.
	 * 
	 */
	public NotaOrdineImportata() {
		super();
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @return Returns the numeroRiga.
	 */
	public String getNumeroRiga() {
		return numeroRiga;
	}

	/**
	 * @return the ordine
	 */
	public OrdineImportato getOrdine() {
		return ordine;
	}

	/**
	 * 
	 * @return true se è una nota di testata, false se nota di riga
	 */
	public boolean isNotaTestata() {
		return numeroRiga.equals("0");
	}

	/**
	 * @param nota
	 *            the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota.trim();
	}

	/**
	 * @param numeroRiga
	 *            The numeroRiga to set.
	 */
	public void setNumeroRiga(String numeroRiga) {
		this.numeroRiga = numeroRiga.trim();
	}

	/**
	 * @param ordine
	 *            the ordine to set
	 */
	public void setOrdine(OrdineImportato ordine) {
		this.ordine = ordine;
	}

}
