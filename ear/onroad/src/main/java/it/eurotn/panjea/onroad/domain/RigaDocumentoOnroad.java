package it.eurotn.panjea.onroad.domain;

import java.math.BigDecimal;
import java.util.Date;

public class RigaDocumentoOnroad {

	private String codiceUtente;
	private String codiceCliente;
	private String codiceDestinatario;
	private String tipoDocumento;
	private String codiceCessionario;
	private String identificativoDocumento;
	private Date dataDocumento;
	private String oraDocumento;
	private String progressivoRiga;
	private String codiceArticolo;
	private String unitaMisura;
	private String tipologiaVendita;
	private String descrizioneArticolo;
	private Double quantita;
	private Double qtaInevasa;
	private BigDecimal prezzoUVB;
	private BigDecimal prezzoUV;
	private String codiceIVA;
	private BigDecimal sconto1;
	private BigDecimal sconto2;
	private BigDecimal scontoValuta;
	private String codiceRicerca;
	private String codiceListino;
	private String condizioniModificate;
	private Date dataConsegnaIniziale;
	private String prefissoSuffisso;
	private Double quantitaPezzi;
	private Double qtaGestione;
	private String flagPrezzoForzato;
	private BigDecimal valoreRiga;
	private BigDecimal sconto3;
	private BigDecimal sconto4;
	private BigDecimal sconto5;
	private BigDecimal sconto6;
	private String articoloConfezione;
	private String statoDocumento;
	private BigDecimal prezzoOriginale;
	private BigDecimal sconto1Originale;
	private BigDecimal sconto2Originale;
	private BigDecimal sconto3Originale;
	private BigDecimal sconto4Originale;
	private BigDecimal sconto5Originale;
	private String unitaMisura2;
	private Double quantita2;
	private String campo1;
	private String campo2;
	private String codicePromozione;

	/**
	 * Costruttore.
	 */
	public RigaDocumentoOnroad() {
		super();
	}

	/**
	 * @return the articoloConfezione
	 */
	public String getArticoloConfezione() {
		return articoloConfezione;
	}

	/**
	 * @return the campo1
	 */
	public String getCampo1() {
		return campo1;
	}

	/**
	 * @return the campo2
	 */
	public String getCampo2() {
		return campo2;
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
	 * @return the codiceArticolo
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
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
	 * @return the codiceIVA
	 */
	public String getCodiceIVA() {
		return codiceIVA;
	}

	/**
	 * @return the codiceListino
	 */
	public String getCodiceListino() {
		return codiceListino;
	}

	/**
	 * @return the codicePromozione
	 */
	public String getCodicePromozione() {
		return codicePromozione;
	}

	/**
	 * @return the codiceRicerca
	 */
	public String getCodiceRicerca() {
		return codiceRicerca;
	}

	/**
	 * @return the codiceUtente
	 */
	public String getCodiceUtente() {
		return codiceUtente;
	}

	/**
	 * @return the condizioniModificate
	 */
	public String getCondizioniModificate() {
		return condizioniModificate;
	}

	/**
	 * @return the dataConsegnaIniziale
	 */
	public Date getDataConsegnaIniziale() {
		return dataConsegnaIniziale;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the descrizioneArticolo
	 */
	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}

	/**
	 * @return the flagPrezzoForzato
	 */
	public String getFlagPrezzoForzato() {
		return flagPrezzoForzato;
	}

	/**
	 * @return the identificativoDocumento
	 */
	public String getIdentificativoDocumento() {
		return identificativoDocumento;
	}

	/**
	 * @return the oraDocumento
	 */
	public String getOraDocumento() {
		return oraDocumento;
	}

	/**
	 * @return the prefissoSuffisso
	 */
	public String getPrefissoSuffisso() {
		return prefissoSuffisso;
	}

	/**
	 * @return the prezzoOriginale
	 */
	public BigDecimal getPrezzoOriginale() {
		return prezzoOriginale;
	}

	/**
	 * @return the prezzoUV
	 */
	public BigDecimal getPrezzoUV() {
		return prezzoUV;
	}

	/**
	 * @return the prezzoUVB
	 */
	public BigDecimal getPrezzoUVB() {
		return prezzoUVB;
	}

	/**
	 * @return the progressivoRiga
	 */
	public String getProgressivoRiga() {
		return progressivoRiga;
	}

	/**
	 * @return the qtaGestione
	 */
	public Double getQtaGestione() {
		return qtaGestione;
	}

	/**
	 * @return the qtaInevasa
	 */
	public Double getQtaInevasa() {
		return qtaInevasa;
	}

	/**
	 * @return the quantita
	 */
	public Double getQuantita() {
		return quantita;
	}

	/**
	 * @return the quantita2
	 */
	public Double getQuantita2() {
		return quantita2;
	}

	/**
	 * @return the quantitaPezzi
	 */
	public Double getQuantitaPezzi() {
		return quantitaPezzi;
	}

	/**
	 * @return the sconto1
	 */
	public BigDecimal getSconto1() {
		return sconto1;
	}

	/**
	 * @return the sconto1Originale
	 */
	public BigDecimal getSconto1Originale() {
		return sconto1Originale;
	}

	/**
	 * @return the sconto2
	 */
	public BigDecimal getSconto2() {
		return sconto2;
	}

	/**
	 * @return the sconto2Originale
	 */
	public BigDecimal getSconto2Originale() {
		return sconto2Originale;
	}

	/**
	 * @return the sconto3
	 */
	public BigDecimal getSconto3() {
		return sconto3;
	}

	/**
	 * @return the sconto3Originale
	 */
	public BigDecimal getSconto3Originale() {
		return sconto3Originale;
	}

	/**
	 * @return the sconto4
	 */
	public BigDecimal getSconto4() {
		return sconto4;
	}

	/**
	 * @return the sconto4Originale
	 */
	public BigDecimal getSconto4Originale() {
		return sconto4Originale;
	}

	/**
	 * @return the sconto5
	 */
	public BigDecimal getSconto5() {
		return sconto5;
	}

	/**
	 * @return the sconto5Originale
	 */
	public BigDecimal getSconto5Originale() {
		return sconto5Originale;
	}

	/**
	 * @return the sconto6
	 */
	public BigDecimal getSconto6() {
		return sconto6;
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
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the tipologiaVendita
	 */
	public String getTipologiaVendita() {
		return tipologiaVendita;
	}

	/**
	 * @return the unitaMisura
	 */
	public String getUnitaMisura() {
		return unitaMisura;
	}

	/**
	 * @return the unitaMisura2
	 */
	public String getUnitaMisura2() {
		return unitaMisura2;
	}

	/**
	 * @return the valoreRiga
	 */
	public BigDecimal getValoreRiga() {
		return valoreRiga;
	}

	/**
	 * @param articoloConfezione
	 *            the articoloConfezione to set
	 */
	public void setArticoloConfezione(String articoloConfezione) {
		this.articoloConfezione = articoloConfezione;
	}

	/**
	 * @param campo1
	 *            the campo1 to set
	 */
	public void setCampo1(String campo1) {
		this.campo1 = campo1;
	}

	/**
	 * @param campo2
	 *            the campo2 to set
	 */
	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
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
	 * @param codiceIVA
	 *            the codiceIVA to set
	 */
	public void setCodiceIVA(String codiceIVA) {
		this.codiceIVA = codiceIVA;
	}

	/**
	 * @param codiceListino
	 *            the codiceListino to set
	 */
	public void setCodiceListino(String codiceListino) {
		this.codiceListino = codiceListino;
	}

	/**
	 * @param codicePromozione
	 *            the codicePromozione to set
	 */
	public void setCodicePromozione(String codicePromozione) {
		this.codicePromozione = codicePromozione;
	}

	/**
	 * @param codiceRicerca
	 *            the codiceRicerca to set
	 */
	public void setCodiceRicerca(String codiceRicerca) {
		this.codiceRicerca = codiceRicerca;
	}

	/**
	 * @param codiceUtente
	 *            the codiceUtente to set
	 */
	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	/**
	 * @param condizioniModificate
	 *            the condizioniModificate to set
	 */
	public void setCondizioniModificate(String condizioniModificate) {
		this.condizioniModificate = condizioniModificate;
	}

	/**
	 * @param dataConsegnaIniziale
	 *            the dataConsegnaIniziale to set
	 */
	public void setDataConsegnaIniziale(Date dataConsegnaIniziale) {
		this.dataConsegnaIniziale = dataConsegnaIniziale;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}

	/**
	 * @param flagPrezzoForzato
	 *            the flagPrezzoForzato to set
	 */
	public void setFlagPrezzoForzato(String flagPrezzoForzato) {
		this.flagPrezzoForzato = flagPrezzoForzato;
	}

	/**
	 * @param identificativoDocumento
	 *            the identificativoDocumento to set
	 */
	public void setIdentificativoDocumento(String identificativoDocumento) {
		this.identificativoDocumento = identificativoDocumento;
	}

	/**
	 * @param oraDocumento
	 *            the oraDocumento to set
	 */
	public void setOraDocumento(String oraDocumento) {
		this.oraDocumento = oraDocumento;
	}

	/**
	 * @param prefissoSuffisso
	 *            the prefissoSuffisso to set
	 */
	public void setPrefissoSuffisso(String prefissoSuffisso) {
		this.prefissoSuffisso = prefissoSuffisso;
	}

	/**
	 * @param prezzoOriginale
	 *            the prezzoOriginale to set
	 */
	public void setPrezzoOriginale(BigDecimal prezzoOriginale) {
		this.prezzoOriginale = prezzoOriginale;
	}

	/**
	 * @param prezzoUV
	 *            the prezzoUV to set
	 */
	public void setPrezzoUV(BigDecimal prezzoUV) {
		this.prezzoUV = prezzoUV;
	}

	/**
	 * @param prezzoUVB
	 *            the prezzoUVB to set
	 */
	public void setPrezzoUVB(BigDecimal prezzoUVB) {
		this.prezzoUVB = prezzoUVB;
	}

	/**
	 * @param progressivoRiga
	 *            the progressivoRiga to set
	 */
	public void setProgressivoRiga(String progressivoRiga) {
		this.progressivoRiga = progressivoRiga;
	}

	/**
	 * @param qtaGestione
	 *            the qtaGestione to set
	 */
	public void setQtaGestione(Double qtaGestione) {
		this.qtaGestione = qtaGestione;
	}

	/**
	 * @param qtaInevasa
	 *            the qtaInevasa to set
	 */
	public void setQtaInevasa(Double qtaInevasa) {
		this.qtaInevasa = qtaInevasa;
	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param quantita2
	 *            the quantita2 to set
	 */
	public void setQuantita2(Double quantita2) {
		this.quantita2 = quantita2;
	}

	/**
	 * @param quantitaPezzi
	 *            the quantitaPezzi to set
	 */
	public void setQuantitaPezzi(Double quantitaPezzi) {
		this.quantitaPezzi = quantitaPezzi;
	}

	/**
	 * @param sconto1
	 *            the sconto1 to set
	 */
	public void setSconto1(BigDecimal sconto1) {
		this.sconto1 = sconto1;
	}

	/**
	 * @param sconto1Originale
	 *            the sconto1Originale to set
	 */
	public void setSconto1Originale(BigDecimal sconto1Originale) {
		this.sconto1Originale = sconto1Originale;
	}

	/**
	 * @param sconto2
	 *            the sconto2 to set
	 */
	public void setSconto2(BigDecimal sconto2) {
		this.sconto2 = sconto2;
	}

	/**
	 * @param sconto2Originale
	 *            the sconto2Originale to set
	 */
	public void setSconto2Originale(BigDecimal sconto2Originale) {
		this.sconto2Originale = sconto2Originale;
	}

	/**
	 * @param sconto3
	 *            the sconto3 to set
	 */
	public void setSconto3(BigDecimal sconto3) {
		this.sconto3 = sconto3;
	}

	/**
	 * @param sconto3Originale
	 *            the sconto3Originale to set
	 */
	public void setSconto3Originale(BigDecimal sconto3Originale) {
		this.sconto3Originale = sconto3Originale;
	}

	/**
	 * @param sconto4
	 *            the sconto4 to set
	 */
	public void setSconto4(BigDecimal sconto4) {
		this.sconto4 = sconto4;
	}

	/**
	 * @param sconto4Originale
	 *            the sconto4Originale to set
	 */
	public void setSconto4Originale(BigDecimal sconto4Originale) {
		this.sconto4Originale = sconto4Originale;
	}

	/**
	 * @param sconto5
	 *            the sconto5 to set
	 */
	public void setSconto5(BigDecimal sconto5) {
		this.sconto5 = sconto5;
	}

	/**
	 * @param sconto5Originale
	 *            the sconto5Originale to set
	 */
	public void setSconto5Originale(BigDecimal sconto5Originale) {
		this.sconto5Originale = sconto5Originale;
	}

	/**
	 * @param sconto6
	 *            the sconto6 to set
	 */
	public void setSconto6(BigDecimal sconto6) {
		this.sconto6 = sconto6;
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
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipologiaVendita
	 *            the tipologiaVendita to set
	 */
	public void setTipologiaVendita(String tipologiaVendita) {
		this.tipologiaVendita = tipologiaVendita;
	}

	/**
	 * @param unitaMisura
	 *            the unitaMisura to set
	 */
	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}

	/**
	 * @param unitaMisura2
	 *            the unitaMisura2 to set
	 */
	public void setUnitaMisura2(String unitaMisura2) {
		this.unitaMisura2 = unitaMisura2;
	}

	/**
	 * @param valoreRiga
	 *            the valoreRiga to set
	 */
	public void setValoreRiga(BigDecimal valoreRiga) {
		this.valoreRiga = valoreRiga;
	}

}
