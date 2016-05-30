package it.eurotn.panjea.conai.util;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AnalisiConaiDTO implements Serializable {

	private static final long serialVersionUID = 8104285067994038206L;

	private Documento documento;

	private boolean valoriFatturato;

	private ArticoloLite articolo;

	private ConaiMateriale materiale;
	private ConaiTipoImballo tipoImballo;

	private Double qtaRigaArticolo;

	private BigDecimal pesoUnitarioConai;
	private BigDecimal pesoEsenzioneConai;
	private BigDecimal percentualeEsenzioneConai;

	private BigDecimal prezzoNettoConai;

	private Integer numeroDecimaliPesoConai;
	private Integer numeroDecimaliPrezzoConai;

	{
		this.documento = new Documento();
		this.documento.setEntita(new ClienteLite());
		this.valoriFatturato = Boolean.FALSE;
		this.articolo = new ArticoloLite();
	}

	/**
	 * Costruttore.
	 */
	public AnalisiConaiDTO() {
		super();
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the documento.
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return Returns the materiale.
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

	/**
	 * @return Returns the numeroDecimaliPesoConai.
	 */
	public Integer getNumeroDecimaliPesoConai() {
		return numeroDecimaliPesoConai;
	}

	/**
	 * @return Returns the numeroDecimaliPrezzoConai.
	 */
	public Integer getNumeroDecimaliPrezzoConai() {
		return numeroDecimaliPrezzoConai;
	}

	/**
	 * @return Returns the percentualeEsenzioneConai.
	 */
	public BigDecimal getPercentualeEsenzioneConai() {
		return percentualeEsenzioneConai;
	}

	/**
	 * @return Returns the pesoEsenzioneConai.
	 */
	public BigDecimal getPesoEsenzioneConai() {
		return pesoEsenzioneConai;
	}

	public BigDecimal getPesoUnitarioConai() {
		return pesoUnitarioConai;
	}

	/**
	 * @return Returns the pesoUnitarioConai.
	 */
	public BigDecimal getPesoTotaleConai() {
		return pesoUnitarioConai.multiply(BigDecimal.valueOf(qtaRigaArticolo));
	}

	/**
	 * @return Returns the prezzoNettoConai.
	 */
	public BigDecimal getPrezzoNettoConai() {
		return prezzoNettoConai;
	}

	/**
	 * @return Returns the prezzoUnitarioConai.
	 */
	public BigDecimal getPrezzoTotaleConai() {
		return prezzoNettoConai.multiply(getPesoTotaleConai());
	}

	/**
	 * @return Returns the tipoImballo.
	 */
	public ConaiTipoImballo getTipoImballo() {
		return tipoImballo;
	}

	/**
	 * @return Returns the valoriFatturato.
	 */
	public boolean isValoriFatturato() {
		return valoriFatturato;
	}

	/**
	 * @param codiceArticolo
	 *            The codice articolo to set.
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.articolo.setCodice(codiceArticolo);
	}

	/**
	 * @param codiceEntita
	 *            The codice entita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.documento.getEntita().setCodice(codiceEntita);
	}

	/**
	 * @param codiceTipoDocumento
	 *            The codice documento to set.
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.documento.getTipoDocumento().setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataDocumento
	 *            The data documento to set.
	 */
	public void setdataDocumento(Date dataDocumento) {
		this.documento.setDataDocumento(dataDocumento);
	}

	/**
	 * @param denominazioneEntita
	 *            The denominazione entita to set.
	 */
	public void setDenominazioneEntita(String denominazioneEntita) {
		this.documento.getEntita().getAnagrafica().setDenominazione(denominazioneEntita);
	}

	/**
	 * @param descrizioneArticolo
	 *            The descrizione articolo to set.
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.articolo.setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            The descrizione documento to set.
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idArticolo
	 *            The id articolo to set.
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.articolo.setId(idArticolo);
	}

	/**
	 * @param idDocumento
	 *            The id documento to set.
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.documento.setId(idDocumento);
	}

	/**
	 * @param idEntita
	 *            The id entita to set.
	 */
	public void setIdEntita(Integer idEntita) {
		this.documento.getEntita().setId(idEntita);
	}

	/**
	 * @param materiale
	 *            The materiale to set.
	 */
	public void setMateriale(Integer materiale) {
		this.materiale = ConaiMateriale.values()[materiale];
	}

	/**
	 * @param numeroDecimaliPesoConai
	 *            The numeroDecimaliPesoConai to set.
	 */
	public void setNumeroDecimaliPesoConai(Integer numeroDecimaliPesoConai) {
		this.numeroDecimaliPesoConai = numeroDecimaliPesoConai;
	}

	/**
	 * @param numeroDecimaliPrezzoConai
	 *            The numeroDecimaliPrezzoConai to set.
	 */
	public void setNumeroDecimaliPrezzoConai(Integer numeroDecimaliPrezzoConai) {
		this.numeroDecimaliPrezzoConai = numeroDecimaliPrezzoConai;
	}

	/**
	 * @param numeroDocumento
	 *            The numero documento to set.
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.documento.getCodice().setCodice(numeroDocumento);
	}

	/**
	 * @param percentualeEsenzioneConai
	 *            The percentualeEsenzioneConai to set.
	 */
	public void setPercentualeEsenzioneConai(BigDecimal percentualeEsenzioneConai) {
		this.percentualeEsenzioneConai = percentualeEsenzioneConai;
	}

	/**
	 * @param pesoEsenzioneConai
	 *            The pesoEsenzioneConai to set.
	 */
	public void setPesoEsenzioneConai(BigDecimal pesoEsenzioneConai) {
		this.pesoEsenzioneConai = pesoEsenzioneConai;
	}

	/**
	 * @param pesoUnitarioConai
	 *            The pesoUnitarioConai to set.
	 */
	public void setPesoUnitarioConai(BigDecimal pesoUnitarioConai) {
		this.pesoUnitarioConai = pesoUnitarioConai;
	}

	/**
	 * @param prezzoNettoConai
	 *            The prezzoNettoConai to set.
	 */
	public void setPrezzoNettoConai(BigDecimal prezzoNettoConai) {
		this.prezzoNettoConai = prezzoNettoConai;
	}

	/**
	 * @param qtaRigaArticolo
	 *            The qtaRigaArticolo to set.
	 */
	public void setQtaRigaArticolo(Double qtaRigaArticolo) {
		this.qtaRigaArticolo = qtaRigaArticolo;
	}

	/**
	 * @param tipoImballo
	 *            The tipoImballo to set.
	 */
	public void setTipoImballo(Integer tipoImballo) {
		this.tipoImballo = ConaiTipoImballo.values()[tipoImballo];
	}

	/**
	 * @param valoriFatturato
	 *            The valoriFatturato to set.
	 */
	public void setValoriFatturato(boolean valoriFatturato) {
		this.valoriFatturato = valoriFatturato;
	}
}
