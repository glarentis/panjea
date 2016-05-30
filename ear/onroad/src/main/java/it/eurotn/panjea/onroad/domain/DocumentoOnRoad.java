package it.eurotn.panjea.onroad.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentoOnRoad {

	/**
	 * Enum per definire l'ordine di importazione dei documenti onroad.
	 * 
	 * @author leonardo
	 */
	public enum OnRoadTipoDocumento {
		DDT, PRN, OR, FT, ACR, NOT, NVA, RFI
	}

	private String codiceUtente;
	private String codiceCliente;
	private String codiceDestinatario;
	private String tipoDocumento;
	private String codiceCessionario;
	private String identificativoDocumento;
	private Date dataDocumento;
	private String oraDocumento;
	private String riferimentoCliente;
	private String dataRifCliente;
	private String codicePagamento;
	private Date dataConsegnaIniziale;
	private String divisa;
	private BigDecimal imponibileNS;
	private BigDecimal imponibile;
	private BigDecimal totaleDocumento;
	private BigDecimal scontoTestata;
	private BigDecimal scontoValuta;
	private String codiceVettore;
	private String codiceSoggettoFiscale;
	private String identificativoDocCessionario;
	private String numeroIncasso;
	private String tipoDDT;
	private String numeroDDTRiferimento;
	private String dataDDTRiferimento;
	private String flagPrefissoSuffisso;
	private String prefissoSuffisso;
	private Integer numeroColli;
	private String codiceMagazzinoPartenza;
	private BigDecimal scontoPagamento;
	private String numeroPagine;
	private String codiceUtenteSostitutivo;
	private String statoDocumento;
	private String dataStampaDocumento;
	private String oraStampaDocumento;
	private String codice1;
	private BigDecimal imponibileCosto;
	private String numeroRighe;
	private String firmatario;
	private String statoPaperless;
	private String identificativoPaper;

	private List<RigaDocumentoOnroad> righe;
	private List<RigaIvaOnRoad> righeIva;

	/**
	 * Costruttore.
	 */
	public DocumentoOnRoad() {
		super();
	}

	/**
	 * @param rigaDocumentoOnroad
	 *            la rigadocumento da aggiungere alla testata
	 * @return true o false
	 */
	public boolean addRigaDocumento(RigaDocumentoOnroad rigaDocumentoOnroad) {
		return getRighe().add(rigaDocumentoOnroad);
	}

	/**
	 * @param rigaIvaOnRoad
	 *            la riga iva da aggiungere alla testata documento
	 * @return true o false
	 */
	public boolean addRigaIva(RigaIvaOnRoad rigaIvaOnRoad) {
		return getRigheIva().add(rigaIvaOnRoad);
	}

	/**
	 * @return l'identificativo della testata documento, uguale per DocumentoOnRoad,RigaDocumentoOnRoad e RigaIvaOnRoad.
	 */
	public String getChiaveDocumento() {
		StringBuilder sb = new StringBuilder();
		sb.append(getCodiceUtente());
		sb.append("#");
		sb.append(getCodiceCliente());
		sb.append("#");
		sb.append(getCodiceDestinatario());
		sb.append("#");
		sb.append(getTipoDocumento());
		sb.append("#");
		sb.append(getCodiceCessionario());
		sb.append("#");
		sb.append(getIdentificativoDocumento());
		sb.append("#");
		sb.append(getDataDocumento().getTime());
		return sb.toString();
	}

	/**
	 * @return the codice1
	 */
	public String getCodice1() {
		return codice1;
	}

	/**
	 * @return the codiceCessionario
	 */
	public String getCodiceCessionario() {
		return codiceCessionario;
	}

	/**
	 * @return the codiceCliente
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

	/**
	 * @return the codiceDestinatario
	 */
	public String getCodiceDestinatario() {
		return codiceDestinatario;
	}

	/**
	 * @return the codiceMagazzinoPartenza
	 */
	public String getCodiceMagazzinoPartenza() {
		return codiceMagazzinoPartenza;
	}

	/**
	 * @return the codicePagamento
	 */
	public String getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return the codiceSoggettoFiscale
	 */
	public String getCodiceSoggettoFiscale() {
		return codiceSoggettoFiscale;
	}

	/**
	 * @return the codiceUtente
	 */
	public String getCodiceUtente() {
		return codiceUtente;
	}

	/**
	 * @return the codiceUtenteSostitutivo
	 */
	public String getCodiceUtenteSostitutivo() {
		return codiceUtenteSostitutivo;
	}

	/**
	 * @return the codiceVettore
	 */
	public String getCodiceVettore() {
		return codiceVettore;
	}

	/**
	 * @return the dataConsegnaIniziale
	 */
	public Date getDataConsegnaIniziale() {
		return dataConsegnaIniziale;
	}

	/**
	 * @return the dataDDTRiferimento
	 */
	public String getDataDDTRiferimento() {
		return dataDDTRiferimento;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the dataRifCliente
	 */
	public String getDataRifCliente() {
		return dataRifCliente;
	}

	/**
	 * @return the dataStampaDocumento
	 */
	public String getDataStampaDocumento() {
		return dataStampaDocumento;
	}

	/**
	 * @return the divisa
	 */
	public String getDivisa() {
		return divisa;
	}

	/**
	 * @return the firmatario
	 */
	public String getFirmatario() {
		return firmatario;
	}

	/**
	 * @return the flagPrefissoSuffisso
	 */
	public String getFlagPrefissoSuffisso() {
		return flagPrefissoSuffisso;
	}

	/**
	 * @return the identificativoDocCessionario
	 */
	public String getIdentificativoDocCessionario() {
		return identificativoDocCessionario;
	}

	/**
	 * @return the identificativoDocumento
	 */
	public String getIdentificativoDocumento() {
		return identificativoDocumento;
	}

	/**
	 * @return the identificativoPaper
	 */
	public String getIdentificativoPaper() {
		return identificativoPaper;
	}

	/**
	 * @return the imponibile
	 */
	public BigDecimal getImponibile() {
		return imponibile;
	}

	/**
	 * @return the imponibileCosto
	 */
	public BigDecimal getImponibileCosto() {
		return imponibileCosto;
	}

	/**
	 * @return the imponibileNS
	 */
	public BigDecimal getImponibileNS() {
		return imponibileNS;
	}

	/**
	 * @return the numeroColli
	 */
	public Integer getNumeroColli() {
		return numeroColli;
	}

	/**
	 * @return the numeroDDTRiferimento
	 */
	public String getNumeroDDTRiferimento() {
		return numeroDDTRiferimento;
	}

	/**
	 * @return the numeroIncasso
	 */
	public String getNumeroIncasso() {
		return numeroIncasso;
	}

	/**
	 * @return the numeroPagine
	 */
	public String getNumeroPagine() {
		return numeroPagine;
	}

	/**
	 * @return the numeroRighe
	 */
	public String getNumeroRighe() {
		return numeroRighe;
	}

	/**
	 * @return the oraDocumento
	 */
	public String getOraDocumento() {
		return oraDocumento;
	}

	/**
	 * @return the oraStampaDocumento
	 */
	public String getOraStampaDocumento() {
		return oraStampaDocumento;
	}

	/**
	 * @return l'ordinal dell'enum associato per avere l'ordinamento di importazione dei tipi documento onroad
	 */
	public Integer getOrdine() {
		String td = getTipoDocumento();
		OnRoadTipoDocumento valueOf = OnRoadTipoDocumento.valueOf(td);
		return new Integer(valueOf.ordinal());
	}

	/**
	 * @return the prefissoSuffisso
	 */
	public String getPrefissoSuffisso() {
		return prefissoSuffisso;
	}

	/**
	 * @return the riferimentoCliente
	 */
	public String getRiferimentoCliente() {
		return riferimentoCliente;
	}

	/**
	 * @return the righe
	 */
	public List<RigaDocumentoOnroad> getRighe() {
		if (righe == null) {
			righe = new ArrayList<RigaDocumentoOnroad>();
		}
		return righe;
	}

	/**
	 * @return the righeIva
	 */
	public List<RigaIvaOnRoad> getRigheIva() {
		if (righeIva == null) {
			righeIva = new ArrayList<RigaIvaOnRoad>();
		}
		return righeIva;
	}

	/**
	 * @return the scontoPagamento
	 */
	public BigDecimal getScontoPagamento() {
		return scontoPagamento;
	}

	/**
	 * @return the scontoTestata
	 */
	public BigDecimal getScontoTestata() {
		return scontoTestata;
	}

	/**
	 * @return the scontoValuta
	 */
	public BigDecimal getScontoValuta() {
		return scontoValuta;
	}

	/**
	 * @return the statoDocumento
	 */
	public String getStatoDocumento() {
		return statoDocumento;
	}

	/**
	 * @return the statoPaperless
	 */
	public String getStatoPaperless() {
		return statoPaperless;
	}

	/**
	 * @return the tipoDDT
	 */
	public String getTipoDDT() {
		return tipoDDT;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the totaleDocumento
	 */
	public BigDecimal getTotaleDocumento() {
		return totaleDocumento;
	}

	/**
	 * @param codice1
	 *            the codice1 to set
	 */
	public void setCodice1(String codice1) {
		this.codice1 = codice1;
	}

	/**
	 * @param codiceCessionario
	 *            the codiceCessionario to set
	 */
	public void setCodiceCessionario(String codiceCessionario) {
		this.codiceCessionario = codiceCessionario;
	}

	/**
	 * @param codiceCliente
	 *            the codiceCliente to set
	 */
	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	/**
	 * @param codiceDestinatario
	 *            the codiceDestinatario to set
	 */
	public void setCodiceDestinatario(String codiceDestinatario) {
		this.codiceDestinatario = codiceDestinatario;
	}

	/**
	 * @param codiceMagazzinoPartenza
	 *            the codiceMagazzinoPartenza to set
	 */
	public void setCodiceMagazzinoPartenza(String codiceMagazzinoPartenza) {
		this.codiceMagazzinoPartenza = codiceMagazzinoPartenza;
	}

	/**
	 * @param codicePagamento
	 *            the codicePagamento to set
	 */
	public void setCodicePagamento(String codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param codiceSoggettoFiscale
	 *            the codiceSoggettoFiscale to set
	 */
	public void setCodiceSoggettoFiscale(String codiceSoggettoFiscale) {
		this.codiceSoggettoFiscale = codiceSoggettoFiscale;
	}

	/**
	 * @param codiceUtente
	 *            the codiceUtente to set
	 */
	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	/**
	 * @param codiceUtenteSostitutivo
	 *            the codiceUtenteSostitutivo to set
	 */
	public void setCodiceUtenteSostitutivo(String codiceUtenteSostitutivo) {
		this.codiceUtenteSostitutivo = codiceUtenteSostitutivo;
	}

	/**
	 * @param codiceVettore
	 *            the codiceVettore to set
	 */
	public void setCodiceVettore(String codiceVettore) {
		this.codiceVettore = codiceVettore;
	}

	/**
	 * @param dataConsegnaIniziale
	 *            the dataConsegnaIniziale to set
	 */
	public void setDataConsegnaIniziale(Date dataConsegnaIniziale) {
		this.dataConsegnaIniziale = dataConsegnaIniziale;
	}

	/**
	 * @param dataDDTRiferimento
	 *            the dataDDTRiferimento to set
	 */
	public void setDataDDTRiferimento(String dataDDTRiferimento) {
		this.dataDDTRiferimento = dataDDTRiferimento;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataRifCliente
	 *            the dataRifCliente to set
	 */
	public void setDataRifCliente(String dataRifCliente) {
		this.dataRifCliente = dataRifCliente;
	}

	/**
	 * @param dataStampaDocumento
	 *            the dataStampaDocumento to set
	 */
	public void setDataStampaDocumento(String dataStampaDocumento) {
		this.dataStampaDocumento = dataStampaDocumento;
	}

	/**
	 * @param divisa
	 *            the divisa to set
	 */
	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	/**
	 * @param firmatario
	 *            the firmatario to set
	 */
	public void setFirmatario(String firmatario) {
		this.firmatario = firmatario;
	}

	/**
	 * @param flagPrefissoSuffisso
	 *            the flagPrefissoSuffisso to set
	 */
	public void setFlagPrefissoSuffisso(String flagPrefissoSuffisso) {
		this.flagPrefissoSuffisso = flagPrefissoSuffisso;
	}

	/**
	 * @param identificativoDocCessionario
	 *            the identificativoDocCessionario to set
	 */
	public void setIdentificativoDocCessionario(String identificativoDocCessionario) {
		this.identificativoDocCessionario = identificativoDocCessionario;
	}

	/**
	 * @param identificativoDocumento
	 *            the identificativoDocumento to set
	 */
	public void setIdentificativoDocumento(String identificativoDocumento) {
		this.identificativoDocumento = identificativoDocumento;
	}

	/**
	 * @param identificativoPaper
	 *            the identificativoPaper to set
	 */
	public void setIdentificativoPaper(String identificativoPaper) {
		this.identificativoPaper = identificativoPaper;
	}

	/**
	 * @param imponibile
	 *            the imponibile to set
	 */
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}

	/**
	 * @param imponibileCosto
	 *            the imponibileCosto to set
	 */
	public void setImponibileCosto(BigDecimal imponibileCosto) {
		this.imponibileCosto = imponibileCosto;
	}

	/**
	 * @param imponibileNS
	 *            the imponibileNS to set
	 */
	public void setImponibileNS(BigDecimal imponibileNS) {
		this.imponibileNS = imponibileNS;
	}

	/**
	 * @param numeroColli
	 *            the numeroColli to set
	 */
	public void setNumeroColli(Integer numeroColli) {
		this.numeroColli = numeroColli;
	}

	/**
	 * @param numeroDDTRiferimento
	 *            the numeroDDTRiferimento to set
	 */
	public void setNumeroDDTRiferimento(String numeroDDTRiferimento) {
		this.numeroDDTRiferimento = numeroDDTRiferimento;
	}

	/**
	 * @param numeroIncasso
	 *            the numeroIncasso to set
	 */
	public void setNumeroIncasso(String numeroIncasso) {
		this.numeroIncasso = numeroIncasso;
	}

	/**
	 * @param numeroPagine
	 *            the numeroPagine to set
	 */
	public void setNumeroPagine(String numeroPagine) {
		this.numeroPagine = numeroPagine;
	}

	/**
	 * @param numeroRighe
	 *            the numeroRighe to set
	 */
	public void setNumeroRighe(String numeroRighe) {
		this.numeroRighe = numeroRighe;
	}

	/**
	 * @param oraDocumento
	 *            the oraDocumento to set
	 */
	public void setOraDocumento(String oraDocumento) {
		this.oraDocumento = oraDocumento;
	}

	/**
	 * @param oraStampaDocumento
	 *            the oraStampaDocumento to set
	 */
	public void setOraStampaDocumento(String oraStampaDocumento) {
		this.oraStampaDocumento = oraStampaDocumento;
	}

	/**
	 * @param prefissoSuffisso
	 *            the prefissoSuffisso to set
	 */
	public void setPrefissoSuffisso(String prefissoSuffisso) {
		this.prefissoSuffisso = prefissoSuffisso;
	}

	/**
	 * @param riferimentoCliente
	 *            the riferimentoCliente to set
	 */
	public void setRiferimentoCliente(String riferimentoCliente) {
		this.riferimentoCliente = riferimentoCliente;
	}

	/**
	 * @param righe
	 *            the righe to set
	 */
	public void setRighe(List<RigaDocumentoOnroad> righe) {
		this.righe = righe;
	}

	/**
	 * @param righeIva
	 *            the righeIva to set
	 */
	public void setRigheIva(List<RigaIvaOnRoad> righeIva) {
		this.righeIva = righeIva;
	}

	/**
	 * @param scontoPagamento
	 *            the scontoPagamento to set
	 */
	public void setScontoPagamento(BigDecimal scontoPagamento) {
		this.scontoPagamento = scontoPagamento;
	}

	/**
	 * @param scontoTestata
	 *            the scontoTestata to set
	 */
	public void setScontoTestata(BigDecimal scontoTestata) {
		this.scontoTestata = scontoTestata;
	}

	/**
	 * @param scontoValuta
	 *            the scontoValuta to set
	 */
	public void setScontoValuta(BigDecimal scontoValuta) {
		this.scontoValuta = scontoValuta;
	}

	/**
	 * @param statoDocumento
	 *            the statoDocumento to set
	 */
	public void setStatoDocumento(String statoDocumento) {
		this.statoDocumento = statoDocumento;
	}

	/**
	 * @param statoPaperless
	 *            the statoPaperless to set
	 */
	public void setStatoPaperless(String statoPaperless) {
		this.statoPaperless = statoPaperless;
	}

	/**
	 * @param tipoDDT
	 *            the tipoDDT to set
	 */
	public void setTipoDDT(String tipoDDT) {
		this.tipoDDT = tipoDDT;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param totaleDocumento
	 *            the totaleDocumento to set
	 */
	public void setTotaleDocumento(BigDecimal totaleDocumento) {
		this.totaleDocumento = totaleDocumento;
	}

}
