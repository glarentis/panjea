package it.eurotn.panjea.mrp.util;

import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Contiene i dati di una rigaOrdine da generare derivate dalle tabelle dei
 * risultati mrp.
 *
 * @author giangi
 * @version 1.0, 09/gen/2014
 */
public class RigheOrdineDaGenerare {

	private Integer id;
	private Integer idArticolo;
	private Integer idDeposito;
	private Integer idFornitore;
	private Integer idTipoDocumento;
	private Integer idRigaDistinta;
	private Date dataConsegna;
	private Date dataDocumento;
	private String righeDaCollegare;
	private String formulaComponente;
	private boolean nuovoOrdine;
	private boolean rilasciaOrdine;
	private boolean distinta;
	private double qta;
	private double qtaR;
	private Integer idConfigurazioneDistinta;
	private Integer idComponente;
	private String ordiniDaCollegare;

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return Returns the dataDocumento.
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the formulaComponente
	 */
	public String getFormulaComponente() {
		return formulaComponente;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public Integer getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return the idComponente
	 */
	public Integer getIdComponente() {
		return idComponente;
	}

	/**
	 * @return the idConfigurazioneDistinta
	 */
	public Integer getIdConfigurazioneDistinta() {
		return idConfigurazioneDistinta;
	}

	/**
	 * @return Returns the idDeposito.
	 */
	public Integer getIdDeposito() {
		return idDeposito;
	}

	/**
	 * @return Returns the idFornitore.
	 */
	public Integer getIdFornitore() {
		return idFornitore;
	}

	/**
	 * @return the idRigaDistinta
	 */
	public Integer getIdRigaDistinta() {
		return idRigaDistinta;
	}

	/**
	 *
	 * @return array con gli id delle righe che hanno generato la riga di
	 *         ordine.
	 */
	public int[] getIdRigheDaCollegare() {
		String[] idRighe = righeDaCollegare.split(",");
		int[] result = new int[idRighe.length];
		int i = 0;
		for (String idRiga : idRighe) {
			result[i++] = NumberUtils.createInteger(idRiga);
		}
		return result;
	}

	/**
	 * @return Returns the idTipoDocumento.
	 */
	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public String getOrdiniDaCollegare() {
		return ordiniDaCollegare;
	}

	/**
	 * @return Returns the qta.
	 */
	public double getQta() {
		return qta;
	}

	/**
	 * @return Returns the qtaR.
	 */
	public double getQtaR() {
		return qtaR;
	}

	/**
	 * @return Returns the righeDaCollegare.
	 */
	public String getRigheDaCollegare() {
		return righeDaCollegare;
	}

	/**
	 * @return the distinta
	 */
	public boolean isDistinta() {
		return distinta;
	}

	/**
	 * @return Returns the nuovoOrdine.
	 */
	public boolean isNuovoOrdine() {
		return nuovoOrdine;
	}

	/**
	 * @return Returns the rilasciaOrdine.
	 */
	public boolean isRilasciaOrdine() {
		return rilasciaOrdine;
	}

	/**
	 * @param dataConsegna
	 *            The dataConsegna to set.
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param distinta
	 *            the distinta to set
	 */
	public void setDistinta(boolean distinta) {
		this.distinta = distinta;
	}

	/**
	 * @param formulaComponente
	 *            the formulaComponente to set
	 */
	public void setFormulaComponente(String formulaComponente) {
		this.formulaComponente = formulaComponente;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idComponente
	 *            the idComponente to set
	 */
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	/**
	 * @param idConfigurazioneDistinta
	 *            the idConfigurazioneDistinta to set
	 */
	public void setIdConfigurazioneDistinta(Integer idConfigurazioneDistinta) {
		this.idConfigurazioneDistinta = idConfigurazioneDistinta;
	}

	/**
	 * @param idDeposito
	 *            The idDeposito to set.
	 */
	public void setIdDeposito(Integer idDeposito) {
		this.idDeposito = idDeposito;
	}

	/**
	 * @param idFornitore
	 *            The idFornitore to set.
	 */
	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	/**
	 * @param idRigaDistinta
	 *            the idRigaDistinta to set
	 */
	public void setIdRigaDistinta(Integer idRigaDistinta) {
		this.idRigaDistinta = idRigaDistinta;
	}

	/**
	 * @param idTipoDocumento
	 *            The idTipoDocumento to set.
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	/**
	 * @param nuovoOrdine
	 *            The nuovoOrdine to set.
	 */
	public void setNuovoOrdine(boolean nuovoOrdine) {
		this.nuovoOrdine = nuovoOrdine;
	}

	public void setOrdiniDaCollegare(String ordiniDaCollegare) {
		this.ordiniDaCollegare = ordiniDaCollegare;
	}

	/**
	 * @param qta
	 *            The qta to set.
	 */
	public void setQta(double qta) {
		this.qta = qta;
	}

	/**
	 * @param qtaR
	 *            The qtaR to set.
	 */
	public void setQtaR(double qtaR) {
		this.qtaR = qtaR;
	}

	/**
	 * @param righeDaCollegare
	 *            The righeDaCollegare to set.
	 */
	public void setRigheDaCollegare(String righeDaCollegare) {
		this.righeDaCollegare = righeDaCollegare;
	}

	/**
	 * @param rilasciaOrdine
	 *            The rilasciaOrdine to set.
	 */
	public void setRilasciaOrdine(boolean rilasciaOrdine) {
		this.rilasciaOrdine = rilasciaOrdine;
	}

	@Override
	public String toString() {
		return "RigheOrdineDaGenerare [id=" + id + ", idArticolo=" + idArticolo + ", idDeposito=" + idDeposito
				+ ", idFornitore=" + idFornitore + ", idTipoDocumento=" + idTipoDocumento + ", idRigaDistinta="
				+ idRigaDistinta + ", dataConsegna=" + dataConsegna + ", dataDocumento=" + dataDocumento
				+ ", righeDaCollegare=" + righeDaCollegare + ", nuovoOrdine=" + nuovoOrdine + ", rilasciaOrdine="
				+ rilasciaOrdine + ", distinta=" + distinta + ", qta=" + qta + ", qtaR=" + qtaR
				+ ", idConfigurazioneDistinta=" + idConfigurazioneDistinta + ", idComponente=" + idComponente + "]";
	}

}
