package it.eurotn.panjea.fornodoro.evasione;

import java.util.Date;

public class RigaEvasione {
	private String chiaveOrdine;
	private int numRiga;
	private String codiceArticolo;
	private Double qta;
	private Integer statoRiga;
	private int tipoRecord;
	private String numLotto;
	private Date dataScadenza;
	private String pezziEvasi;
	private String colliEvasi;

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		RigaEvasione other = (RigaEvasione) obj;
		return (getChiave().equals(other.getChiave()));
	}

	/**
	 *
	 * @return chiave per riconoscere la rigaEvasione
	 */
	public String getChiave() {
		StringBuilder sb = new StringBuilder();
		sb.append(chiaveOrdine).append("#").append(numRiga);
		return sb.toString();
	}

	/**
	 * @return Returns the chiaveOrdine.
	 */
	public String getChiaveOrdine() {
		return chiaveOrdine;
	}

	/**
	 * @return Returns the codiceArticolo.
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	/**
	 * @return Returns the colliEvasi.
	 */
	public String getColliEvasi() {
		return colliEvasi;
	}

	/**
	 * @return Returns the dataScadenza.
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 *
	 * @return numeroOrdine
	 */
	public Integer getNumeroOrdine() {
		return Integer.parseInt(chiaveOrdine.substring(4, 10));
	}

	/**
	 * @return Returns the numLotto.
	 */
	public String getNumLotto() {
		return numLotto;
	}

	/**
	 * @return Returns the numRiga.
	 */
	public int getNumRiga() {
		return numRiga;
	}

	/**
	 * @return Returns the pezziEvasi.
	 */
	public String getPezziEvasi() {
		return pezziEvasi.replace('.', ',');
	}

	/**
	 * @return Returns the qta.
	 */
	public Double getQta() {
		return qta;
	}

	/**
	 * @return Returns the statoRiga.
	 */
	public Integer getStatoRiga() {
		return statoRiga;
	}

	/**
	 *
	 * @return codice tipoDocumento
	 */
	public String getTipoDocumento() {
		return chiaveOrdine.substring(2, 4);
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public int getTipoRecord() {
		return tipoRecord;
	}

	@Override
	public int hashCode() {
		return getChiave().hashCode();
	}

	/**
	 * @param chiaveOrdine
	 *            The chiaveOrdine to set.
	 */
	public void setChiaveOrdine(String chiaveOrdine) {
		this.chiaveOrdine = chiaveOrdine;
	}

	/**
	 * @param codiceArticolo
	 *            The codiceArticolo to set.
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param colliEvasi
	 *            The colliEvasi to set.
	 */
	public void setColliEvasi(String colliEvasi) {
		this.colliEvasi = colliEvasi;
	}

	/**
	 * @param dataScadenza
	 *            The dataScadenza to set.
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param numLotto
	 *            The numLotto to set.
	 */
	public void setNumLotto(String numLotto) {
		this.numLotto = numLotto;
	}

	/**
	 * @param numRiga
	 *            The numRiga to set.
	 */
	public void setNumRiga(int numRiga) {
		this.numRiga = numRiga;
	}

	/**
	 * @param pezziEvasi
	 *            The pezziEvasi to set.
	 */
	public void setPezziEvasi(String pezziEvasi) {
		this.pezziEvasi = pezziEvasi;
	}

	/**
	 * @param qta the qta to set
	 */
	public void setQta(Double qta) {
		this.qta = qta;
	}

	/**
	 * @param statoRiga
	 *            The statoRiga to set.
	 */
	public void setStatoRiga(Integer statoRiga) {
		if (statoRiga == null) {
			statoRiga = 0;
		}
		this.statoRiga = statoRiga;
	}

	/**
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(int tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RigaEvasione [chiaveOrdine=" + chiaveOrdine + ", numRiga=" + numRiga + ", codiceArticolo="
				+ codiceArticolo + ", qta=" + qta + ", statoRiga=" + statoRiga + ", tipoRecord=" + tipoRecord
				+ ", numLotto=" + numLotto + ", dataScadenza=" + dataScadenza + "]";
	}

}
