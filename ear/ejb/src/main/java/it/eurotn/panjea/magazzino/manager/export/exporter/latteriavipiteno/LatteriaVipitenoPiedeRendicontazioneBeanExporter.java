package it.eurotn.panjea.magazzino.manager.export.exporter.latteriavipiteno;

import java.util.Date;

public class LatteriaVipitenoPiedeRendicontazioneBeanExporter {

	private int numeroProgressivo;

	private String codiceCliente;

	private Date dataCreazione;

	private Double quantitaTotale;

	/**
	 * Costruttore.
	 * 
	 * @param codiceCliente
	 *            codice cliente
	 * @param dataCreazione
	 *            data di creazione
	 * @param progressivo
	 *            progressivo
	 * @param quantitaTotale
	 *            quantità totale
	 */
	public LatteriaVipitenoPiedeRendicontazioneBeanExporter(final String codiceCliente, final Date dataCreazione,
			final int progressivo, final Double quantitaTotale) {
		super();
		this.codiceCliente = codiceCliente;
		this.dataCreazione = dataCreazione;
		this.numeroProgressivo = progressivo;
		this.quantitaTotale = quantitaTotale;
	}

	/**
	 * @return Returns the carattereChiusura.
	 */
	public String getCarattereChiusura() {
		return "x";
	}

	/**
	 * @return Returns the codiceCliente.
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

	/**
	 * @return Returns the dataCreazione.
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}

	/**
	 * @return Returns the numeroProgressivo.
	 */
	public int getNumeroProgressivo() {
		return numeroProgressivo;
	}

	/**
	 * @return Returns the quantitaTotale.
	 */
	public Double getQuantitaTotale() {
		return quantitaTotale;
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public int getTipoRecord() {
		return 9;
	}

	/**
	 * @param carattereChiusura
	 *            The carattereChiusura to set.
	 */
	public void setCarattereChiusura(String carattereChiusura) {
	}

	/**
	 * @param codiceCliente
	 *            The codiceCliente to set.
	 */
	public void setCodiceCliente(String codiceCliente) {
	}

	/**
	 * @param dataCreazione
	 *            The dataCreazione to set.
	 */
	public void setDataCreazione(Date dataCreazione) {
	}

	/**
	 * @param numeroProgressivo
	 *            The numeroProgressivo to set.
	 */
	public void setNumeroProgressivo(int numeroProgressivo) {
	}

	/**
	 * @param quantitaTotale
	 *            The quantitaTotale to set.
	 */
	public void setQuantitaTotale(Double quantitaTotale) {
	}

	/**
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(int tipoRecord) {
	}

}
