package it.eurotn.panjea.magazzino.manager.articolo;

public class ArticoloValorizzazioneBOM {
	private int idArticolo;
	private int numDecimaliPrezzo;
	private String codiciAttributo;
	private String valoriAttributo;
	private String codice;
	private String descrizione;

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiciAttributo.
	 */
	public String getCodiciAttributo() {
		return codiciAttributo;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the numDecimaliPrezzo.
	 */
	public int getNumDecimaliPrezzo() {
		return numDecimaliPrezzo;
	}

	/**
	 * @return Returns the valoriAttributo.
	 */
	public String getValoriAttributo() {
		return valoriAttributo;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiciAttributo
	 *            The codiciAttributo to set.
	 */
	public void setCodiciAttributo(String codiciAttributo) {
		this.codiciAttributo = codiciAttributo;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(int idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param numDecimaliPrezzo
	 *            The numDecimaliPrezzo to set.
	 */
	public void setNumDecimaliPrezzo(int numDecimaliPrezzo) {
		this.numDecimaliPrezzo = numDecimaliPrezzo;
	}

	/**
	 * @param valoriAttributo
	 *            The valoriAttributo to set.
	 */
	public void setValoriAttributo(String valoriAttributo) {
		this.valoriAttributo = valoriAttributo;
	}
}
