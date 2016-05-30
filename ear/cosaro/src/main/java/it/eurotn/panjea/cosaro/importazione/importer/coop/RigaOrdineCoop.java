package it.eurotn.panjea.cosaro.importazione.importer.coop;

public class RigaOrdineCoop {
	private String codicePiattaforma;
	private String numeroOrdine;
	private String codiceArticolo;
	private String descrizioneArticolo;
	private Integer colli;
	private Integer pezzi;
	private Integer peso;
	private String linea;
	private String prezzo;
	private String codiceArticoloCoop;

	/**
	 * @return Returns the codiceArticolo.
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	/**
	 * @return Returns the codiceArticoloCoop.
	 */
	public String getCodiceArticoloCoop() {
		return codiceArticoloCoop;
	}

	/**
	 * @return Returns the codicePiattaforma.
	 */
	public String getCodicePiattaforma() {
		return codicePiattaforma;
	}

	/**
	 * @return Returns the colli.
	 */
	public Integer getColli() {
		if (colli == null) {
			return 0;
		}
		return colli;
	}

	/**
	 * @return Returns the descrizioneArticolo.
	 */
	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}

	/**
	 * @return Returns the linea.
	 */
	public String getLinea() {
		return linea;
	}

	/**
	 * @return Returns the numeroOrdine.
	 */
	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @return Returns the peso.
	 */
	public Integer getPeso() {
		if (peso == null) {
			return 0;
		}
		return peso;
	}

	/**
	 * @return Returns the pezzi.
	 */
	public Integer getPezzi() {
		if (pezzi == null) {
			return 0;
		}
		return pezzi;
	}

	/**
	 * @return Returns the prezzo.
	 */
	public String getPrezzo() {
		return prezzo;
	}

	/**
	 * @param codiceArticolo
	 *            The codiceArticolo to set.
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param codiceArticoloCoop
	 *            The codiceArticoloCoop to set.
	 */
	public void setCodiceArticoloCoop(String codiceArticoloCoop) {
		this.codiceArticoloCoop = codiceArticoloCoop;
	}

	/**
	 * @param codicePiattaforma
	 *            The codicePiattaforma to set.
	 */
	public void setCodicePiattaforma(String codicePiattaforma) {
		this.codicePiattaforma = codicePiattaforma;
	}

	/**
	 * @param colli
	 *            The colli to set.
	 */
	public void setColli(Integer colli) {
		this.colli = colli;
	}

	/**
	 * @param descrizioneArticolo
	 *            The descrizioneArticolo to set.
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}

	/**
	 * @param linea
	 *            The linea to set.
	 */
	public void setLinea(String linea) {
		this.linea = linea;
	}

	/**
	 * @param numeroOrdine
	 *            The numeroOrdine to set.
	 */
	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	/**
	 * @param peso
	 *            The peso to set.
	 */
	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	/**
	 * @param pezzi
	 *            The pezzi to set.
	 */
	public void setPezzi(Integer pezzi) {
		this.pezzi = pezzi;
	}

	/**
	 * @param prezzo
	 *            The prezzo to set.
	 */
	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}
}
