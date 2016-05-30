package it.eurotn.panjea.magazzino.manager.export.exporter.latteriavipiteno;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;

import java.util.Date;

public class LatteriaVipitenoRigaRendicontazioneBeanExporter {

	private RigaArticolo rigaArticolo;

	private String codiceCliente;

	private Date dataCreazione;

	private int numeroProgressivo;

	private Double quantita;

	private String codiceLotto;

	/**
	 * Costruttore.
	 * 
	 * @param codiceCliente
	 *            codice cliente
	 * @param dataCreazione
	 *            data di creazione
	 * @param progressivo
	 *            progressivo
	 * @param rigaArticolo
	 *            riga articolo
	 * @param quantita
	 *            quantità della riga
	 * @param codiceLotto
	 *            codice del lotto
	 */
	public LatteriaVipitenoRigaRendicontazioneBeanExporter(final String codiceCliente, final Date dataCreazione,
			final int progressivo, final RigaArticolo rigaArticolo, final Double quantita, final String codiceLotto) {
		super();
		this.numeroProgressivo = progressivo;
		this.rigaArticolo = rigaArticolo;
		this.codiceCliente = codiceCliente;
		this.dataCreazione = dataCreazione;
		this.quantita = quantita;
		this.codiceLotto = codiceLotto;
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return rigaArticolo.getArticolo();
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
	 * @return Returns the codiceEntita.
	 */
	public Integer getCodiceEntita() {
		return rigaArticolo.getAreaMagazzino().getDocumento().getEntita().getCodice();
	}

	/**
	 * @return Returns the codiceFiscaleEntita.
	 */
	public String getCodiceFiscaleEntita() {

		String codiceFiscale = "";

		if (getPartitaIvaEntita() == null || getPartitaIvaEntita().isEmpty()) {
			codiceFiscale = rigaArticolo.getAreaMagazzino().getDocumento().getEntita().getAnagrafica()
					.getCodiceFiscale();
		}

		return codiceFiscale;
	}

	/**
	 * @return the codiceLotto
	 */
	public String getCodiceLotto() {
		return codiceLotto;
	}

	/**
	 * @return Returns the codiceSedeEntita.
	 */
	public String getCodiceSedeEntita() {
		return rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getCodice();
	}

	/**
	 * @return Returns the dataCreazione.
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}

	/**
	 * @return Returns the documento.
	 */
	public Documento getDocumento() {
		return rigaArticolo.getAreaMagazzino().getDocumento();
	}

	/**
	 * @return Returns the numeroProgressivo.
	 */
	public int getNumeroProgressivo() {
		return numeroProgressivo;
	}

	/**
	 * @return Returns the partitaIvaEntita.
	 */
	public String getPartitaIvaEntita() {
		return rigaArticolo.getAreaMagazzino().getDocumento().getEntita().getAnagrafica().getPartiteIVA();
	}

	/**
	 * @return Returns the documento.
	 */
	public Double getQuantita() {
		return quantita;
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public int getTipoRecord() {
		return 1;
	}

	/**
	 * @return Returns the tipoVendita.
	 */
	public String getTipoVendita() {
		// vendita o omaggio
		String result = "VEN";

		if (rigaArticolo.isOmaggio()) {
			result = "OMA";
		}

		return result;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setarticolo(ArticoloLite articolo) {
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
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
	}

	/**
	 * @param codiceFiscaleEntita
	 *            The codiceFiscaleEntita to set.
	 */
	public void setCodiceFiscaleEntita(String codiceFiscaleEntita) {
	}

	/**
	 * @param codiceLotto
	 *            the codiceLotto to set
	 */
	public void setCodiceLotto(String codiceLotto) {
		this.codiceLotto = codiceLotto;
	}

	/**
	 * @param codiceSedeEntita
	 *            The codiceSedeEntita to set.
	 */
	public void setCodiceSedeEntita(String codiceSedeEntita) {
	}

	/**
	 * @param dataCreazione
	 *            The dataCreazione to set.
	 */
	public void setDataCreazione(Date dataCreazione) {
	}

	/**
	 * @param documento
	 *            The documento to set.
	 */
	public void setDocumento(Documento documento) {
	}

	/**
	 * @param numeroProgressivo
	 *            The numeroProgressivo to set.
	 */
	public void setNumeroProgressivo(int numeroProgressivo) {
	}

	/**
	 * @param partitaIvaEntita
	 *            The partitaIvaEntita to set.
	 */
	public void setPartitaIvaEntita(String partitaIvaEntita) {
	}

	/**
	 * @param quantita
	 *            The quantita to set.
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(int tipoRecord) {
	}

	/**
	 * @param tipoVendita
	 *            The tipoVendita to set.
	 */
	public void setTipoVendita(String tipoVendita) {
	}
}
