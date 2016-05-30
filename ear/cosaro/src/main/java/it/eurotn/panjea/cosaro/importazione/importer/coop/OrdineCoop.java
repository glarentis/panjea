package it.eurotn.panjea.cosaro.importazione.importer.coop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdineCoop {
	private String codicePiattaforma;
	private String numOrdine;
	private Date dataConsegna;
	private Date dataOraPrevistaPiattaforma;
	private Date dataBolla;
	private Integer numBolla;
	private String codiceFornitore;
	private Date dataOrdine;
	private List<RigaOrdineCoop> righe = new ArrayList<RigaOrdineCoop>();

	/**
	 * @return Returns the codiceFornitore.
	 */
	public String getCodiceFornitore() {
		return codiceFornitore;
	}

	/**
	 * @return Returns the codicePiattaforma.
	 */
	public String getCodicePiattaforma() {
		return codicePiattaforma;
	}

	/**
	 * @return Returns the dataBolla.
	 */
	public Date getDataBolla() {
		return dataBolla;
	}

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return Returns the dataOraPrevistaPiattaforma.
	 */
	public Date getDataOraPrevistaPiattaforma() {
		return dataOraPrevistaPiattaforma;
	}

	/**
	 * @return Returns the dataOrdine.
	 */
	public Date getDataOrdine() {
		return dataOrdine;
	}

	/**
	 * @return Returns the numBolla.
	 */
	public Integer getNumBolla() {
		return numBolla;
	}

	/**
	 * @return Returns the numOrdine.
	 */
	public String getNumOrdine() {
		return numOrdine;
	}

	/**
	 * @return Returns the righe.
	 */
	public List<RigaOrdineCoop> getRighe() {
		return righe;
	}

	/**
	 * @param codiceFornitore
	 *            The codiceFornitore to set.
	 */
	public void setCodiceFornitore(String codiceFornitore) {
		this.codiceFornitore = codiceFornitore;
	}

	/**
	 * @param codicePiattaforma
	 *            The codicePiattaforma to set.
	 */
	public void setCodicePiattaforma(String codicePiattaforma) {
		this.codicePiattaforma = codicePiattaforma;
	}

	/**
	 * @param dataBolla
	 *            The dataBolla to set.
	 */
	public void setDataBolla(Date dataBolla) {
		this.dataBolla = dataBolla;
	}

	/**
	 * @param dataConsegna
	 *            The dataConsegna to set.
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataOraPrevistaPiattaforma
	 *            The dataOraPrevistaPiattaforma to set.
	 */
	public void setDataOraPrevistaPiattaforma(Date dataOraPrevistaPiattaforma) {
		this.dataOraPrevistaPiattaforma = dataOraPrevistaPiattaforma;
	}

	/**
	 * @param dataOrdine
	 *            The dataOrdine to set.
	 */
	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	/**
	 * @param numBolla
	 *            The numBolla to set.
	 */
	public void setNumBolla(Integer numBolla) {
		this.numBolla = numBolla;
	}

	/**
	 * @param numOrdine
	 *            The numOrdine to set.
	 */
	public void setNumOrdine(String numOrdine) {
		try {
			Integer intNumero = Integer.parseInt(numOrdine);
			this.numOrdine = intNumero.toString();
		} catch (NumberFormatException e) {
			this.numOrdine = numOrdine;
		}
	}

	/**
	 * @param righe
	 *            The righe to set.
	 */
	public void setRighe(List<RigaOrdineCoop> righe) {
		this.righe = righe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrdineCoop [codicePiattaforma=" + codicePiattaforma + ", numOrdine=" + numOrdine + ", dataConsegna="
				+ dataConsegna + ", dataOraPrevistaPiattaforma=" + dataOraPrevistaPiattaforma + ", dataBolla="
				+ dataBolla + ", numBolla=" + numBolla + ", codiceFornitore=" + codiceFornitore + ", dataOrdine="
				+ dataOrdine + " , num righe  " + righe.size() + "]";
	}
}
