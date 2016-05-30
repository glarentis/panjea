package it.eurotn.panjea.aton.domain;

public class ChiaveCondizRiga {

	private String archivio;
	private String sequenzaRicerca;
	private String codiceRicerca;
	private String descrizione;
	private Integer numeroCampi;

	private String nomeTabella1;
	private String nomeCampo1;

	private String nomeTabella2;
	private String nomeCampo2;

	private String nomeTabella3;
	private String nomeCampo3;

	private String gotoT;
	private String gotoF;

	/**
	 * Corstruttore.
	 */
	public ChiaveCondizRiga() {
		super();

		// di default il valore è questo
		this.archivio = "T_CV_RIG";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ChiaveCondizRiga other = (ChiaveCondizRiga) obj;
		if (codiceRicerca == null) {
			if (other.codiceRicerca != null) {
				return false;
			}
		} else if (!codiceRicerca.equals(other.codiceRicerca)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the archivio
	 */
	public String getArchivio() {
		return archivio;
	}

	/**
	 * @return the codiceRicerca
	 */
	public String getCodiceRicerca() {
		return codiceRicerca;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the gotoF
	 */
	public String getGotoF() {
		return gotoF;
	}

	/**
	 * @return the gotoT
	 */
	public String getGotoT() {
		return gotoT;
	}

	/**
	 * @return the nomeCampo1
	 */
	public String getNomeCampo1() {
		return nomeCampo1;
	}

	/**
	 * @return the nomeCampo2
	 */
	public String getNomeCampo2() {
		return nomeCampo2;
	}

	/**
	 * @return the nomeCampo3
	 */
	public String getNomeCampo3() {
		return nomeCampo3;
	}

	/**
	 * @return the nomeTabella1
	 */
	public String getNomeTabella1() {
		return nomeTabella1;
	}

	/**
	 * @return the nomeTabella2
	 */
	public String getNomeTabella2() {
		return nomeTabella2;
	}

	/**
	 * @return the nomeTabella3
	 */
	public String getNomeTabella3() {
		return nomeTabella3;
	}

	/**
	 * @return the numeroCampi
	 */
	public Integer getNumeroCampi() {
		return numeroCampi;
	}

	/**
	 * @return the sequenzaRicerca
	 */
	public String getSequenzaRicerca() {
		return sequenzaRicerca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceRicerca == null) ? 0 : codiceRicerca.hashCode());
		return result;
	}

	/**
	 * @param archivio
	 *            the archivio to set
	 */
	public void setArchivio(String archivio) {
		this.archivio = archivio;
	}

	/**
	 * @param codiceRicerca
	 *            the codiceRicerca to set
	 */
	public void setCodiceRicerca(String codiceRicerca) {
		this.codiceRicerca = codiceRicerca;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param gotoF
	 *            the gotoF to set
	 */
	public void setGotoF(String gotoF) {
		this.gotoF = gotoF;
	}

	/**
	 * @param gotoT
	 *            the gotoT to set
	 */
	public void setGotoT(String gotoT) {
		this.gotoT = gotoT;
	}

	/**
	 * @param nomeCampo1
	 *            the nomeCampo1 to set
	 */
	public void setNomeCampo1(String nomeCampo1) {
		this.nomeCampo1 = nomeCampo1;
	}

	/**
	 * @param nomeCampo2
	 *            the nomeCampo2 to set
	 */
	public void setNomeCampo2(String nomeCampo2) {
		this.nomeCampo2 = nomeCampo2;
	}

	/**
	 * @param nomeCampo3
	 *            the nomeCampo3 to set
	 */
	public void setNomeCampo3(String nomeCampo3) {
		this.nomeCampo3 = nomeCampo3;
	}

	/**
	 * @param nomeTabella1
	 *            the nomeTabella1 to set
	 */
	public void setNomeTabella1(String nomeTabella1) {
		this.nomeTabella1 = nomeTabella1;
	}

	/**
	 * @param nomeTabella2
	 *            the nomeTabella2 to set
	 */
	public void setNomeTabella2(String nomeTabella2) {
		this.nomeTabella2 = nomeTabella2;
	}

	/**
	 * @param nomeTabella3
	 *            the nomeTabella3 to set
	 */
	public void setNomeTabella3(String nomeTabella3) {
		this.nomeTabella3 = nomeTabella3;
	}

	/**
	 * @param numeroCampi
	 *            the numeroCampi to set
	 */
	public void setNumeroCampi(Integer numeroCampi) {
		this.numeroCampi = numeroCampi;
	}

	/**
	 * @param sequenzaRicerca
	 *            the sequenzaRicerca to set
	 */
	public void setSequenzaRicerca(String sequenzaRicerca) {
		this.sequenzaRicerca = sequenzaRicerca;
	}

}
