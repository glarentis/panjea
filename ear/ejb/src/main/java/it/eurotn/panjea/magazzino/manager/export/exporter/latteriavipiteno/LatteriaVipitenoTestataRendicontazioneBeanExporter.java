package it.eurotn.panjea.magazzino.manager.export.exporter.latteriavipiteno;

import java.util.Date;

public class LatteriaVipitenoTestataRendicontazioneBeanExporter {

	private String codiceCliente;

	private Date dataCreazione;

	/**
	 * Costruttore.
	 * 
	 * @param codiceCliente
	 *            codice cliente
	 * @param dataCreazione
	 *            data di creazione
	 */
	public LatteriaVipitenoTestataRendicontazioneBeanExporter(final String codiceCliente, final Date dataCreazione) {
		super();
		this.codiceCliente = codiceCliente;
		this.dataCreazione = dataCreazione;
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
		return 0;
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public int getTipoRecord() {
		return 0;
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
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(int tipoRecord) {
	}

}
