package it.eurotn.panjea.rate.util;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class RataRV {

	private BigDecimal importo;
	private Date dataScadenza;

	private String documenti;

	private String numeroDocumento;
	private Date dataDocumento;
	private String codicePaese;
	private String checkDigit;
	private String descrizioneBanca;
	private String cin;

	private String abi;

	private String cab;
	private String numero;
	private Integer entitaId;
	private String entitaDescrizione;
	private String entitaIndirizzo;
	private String entitaCap;
	private String entitaLocalita;

	private LivelloAmministrativo1 entitaLivelloAmministrativo1;
	private LivelloAmministrativo2 entitaLivelloAmministrativo2;
	private LivelloAmministrativo3 entitaLivelloAmministrativo3;
	private LivelloAmministrativo4 entitaLivelloAmministrativo4;

	private String entitaNazione;

	private String aziendaDescrizione;

	private String aziendaIndirizzo;

	private String aziendaCap;

	private String aziendaLocalita;

	private LivelloAmministrativo1 aziendaLivelloAmministrativo1;

	private LivelloAmministrativo2 aziendaLivelloAmministrativo2;

	private LivelloAmministrativo3 aziendaLivelloAmministrativo3;

	private LivelloAmministrativo4 aziendaLivelloAmministrativo4;

	private String aziendaNazione;

	/**
	 * @return the abi
	 */
	public String getAbi() {
		return abi;
	}

	/**
	 * @return the aziendaCap
	 */
	public String getAziendaCap() {
		return aziendaCap;
	}

	/**
	 * @return the aziendaDescrizione
	 */
	public String getAziendaDescrizione() {
		return aziendaDescrizione;
	}

	/**
	 * @return the aziendaIndirizzo
	 */
	public String getAziendaIndirizzo() {
		return aziendaIndirizzo;
	}

	/**
	 * @return the aziendaLivelloAmministrativo1
	 */
	public LivelloAmministrativo1 getAziendaLivelloAmministrativo1() {
		return aziendaLivelloAmministrativo1;
	}

	/**
	 * @return the aziendaLivelloAmministrativo2
	 */
	public LivelloAmministrativo2 getAziendaLivelloAmministrativo2() {
		return aziendaLivelloAmministrativo2;
	}

	/**
	 * @return the aziendaLivelloAmministrativo3
	 */
	public LivelloAmministrativo3 getAziendaLivelloAmministrativo3() {
		return aziendaLivelloAmministrativo3;
	}

	/**
	 * @return the aziendaLivelloAmministrativo4
	 */
	public LivelloAmministrativo4 getAziendaLivelloAmministrativo4() {
		return aziendaLivelloAmministrativo4;
	}

	/**
	 * @return the aziendaLocalita
	 */
	public String getAziendaLocalita() {
		return aziendaLocalita;
	}

	/**
	 * @return the aziendaNazione
	 */
	public String getAziendaNazione() {
		return aziendaNazione;
	}

	/**
	 * @return the cab
	 */
	public String getCab() {
		return cab;
	}

	/**
	 * @return the checkDigit
	 */
	public String getCheckDigit() {
		return checkDigit;
	}

	/**
	 * @return the cin
	 */
	public String getCin() {
		return cin;
	}

	/**
	 * @return the codicePaese
	 */
	public String getCodicePaese() {
		return codicePaese;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return Returns the descrizioneBanca.
	 */
	public String getDescrizioneBanca() {
		return descrizioneBanca;
	}

	/**
	 * @return the documenti
	 */
	public String getDocumenti() {
		return documenti;
	}

	/**
	 * @return the entitaCap
	 */
	public String getEntitaCap() {
		return entitaCap;
	}

	/**
	 * @return the entitaDescrizione
	 */
	public String getEntitaDescrizione() {
		return entitaDescrizione;
	}

	/**
	 * @return the entitaId
	 */
	public Integer getEntitaId() {
		return entitaId;
	}

	/**
	 * @return the entitaIndirizzo
	 */
	public String getEntitaIndirizzo() {
		return entitaIndirizzo;
	}

	/**
	 * @return the entitaLivelloAmministrativo1
	 */
	public LivelloAmministrativo1 getEntitaLivelloAmministrativo1() {
		return entitaLivelloAmministrativo1;
	}

	/**
	 * @return the entitaLivelloAmministrativo2
	 */
	public LivelloAmministrativo2 getEntitaLivelloAmministrativo2() {
		return entitaLivelloAmministrativo2;
	}

	/**
	 * @return the entitaLivelloAmministrativo3
	 */
	public LivelloAmministrativo3 getEntitaLivelloAmministrativo3() {
		return entitaLivelloAmministrativo3;
	}

	/**
	 * @return the entitaLivelloAmministrativo4
	 */
	public LivelloAmministrativo4 getEntitaLivelloAmministrativo4() {
		return entitaLivelloAmministrativo4;
	}

	/**
	 * @return the entitaLocalita
	 */
	public String getEntitaLocalita() {
		return entitaLocalita;
	}

	/**
	 * @return the entitaNazione
	 */
	public String getEntitaNazione() {
		return entitaNazione;
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param abi
	 *            the abi to set
	 */
	public void setAbi(String abi) {
		this.abi = abi;
	}

	/**
	 * @param aziendaCap
	 *            the aziendaCap to set
	 */
	public void setAziendaCap(String aziendaCap) {
		this.aziendaCap = aziendaCap;
	}

	/**
	 * @param aziendaDescrizione
	 *            the aziendaDescrizione to set
	 */
	public void setAziendaDescrizione(String aziendaDescrizione) {
		this.aziendaDescrizione = aziendaDescrizione;
	}

	/**
	 * @param aziendaIndirizzo
	 *            the aziendaIndirizzo to set
	 */
	public void setAziendaIndirizzo(String aziendaIndirizzo) {
		this.aziendaIndirizzo = aziendaIndirizzo;
	}

	/**
	 * @param aziendaLivelloAmministrativo1
	 *            the aziendaLivelloAmministrativo1 to set
	 */
	public void setAziendaLivelloAmministrativo1(LivelloAmministrativo1 aziendaLivelloAmministrativo1) {
		this.aziendaLivelloAmministrativo1 = aziendaLivelloAmministrativo1;
	}

	/**
	 * @param aziendaLivelloAmministrativo2
	 *            the aziendaLivelloAmministrativo2 to set
	 */
	public void setAziendaLivelloAmministrativo2(LivelloAmministrativo2 aziendaLivelloAmministrativo2) {
		this.aziendaLivelloAmministrativo2 = aziendaLivelloAmministrativo2;
	}

	/**
	 * @param aziendaLivelloAmministrativo3
	 *            the aziendaLivelloAmministrativo3 to set
	 */
	public void setAziendaLivelloAmministrativo3(LivelloAmministrativo3 aziendaLivelloAmministrativo3) {
		this.aziendaLivelloAmministrativo3 = aziendaLivelloAmministrativo3;
	}

	/**
	 * @param aziendaLivelloAmministrativo4
	 *            the aziendaLivelloAmministrativo4 to set
	 */
	public void setAziendaLivelloAmministrativo4(LivelloAmministrativo4 aziendaLivelloAmministrativo4) {
		this.aziendaLivelloAmministrativo4 = aziendaLivelloAmministrativo4;
	}

	/**
	 * @param aziendaLocalita
	 *            the aziendaLocalita to set
	 */
	public void setAziendaLocalita(String aziendaLocalita) {
		this.aziendaLocalita = aziendaLocalita;
	}

	/**
	 * @param aziendaNazione
	 *            the aziendaNazione to set
	 */
	public void setAziendaNazione(String aziendaNazione) {
		this.aziendaNazione = aziendaNazione;
	}

	/**
	 * @param cab
	 *            the cab to set
	 */
	public void setCab(String cab) {
		this.cab = cab;
	}

	/**
	 * @param checkDigit
	 *            the checkDigit to set
	 */
	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}

	/**
	 * @param cin
	 *            the cin to set
	 */
	public void setCin(String cin) {
		this.cin = cin;
	}

	/**
	 * @param codicePaese
	 *            the codicePaese to set
	 */
	public void setCodicePaese(String codicePaese) {
		this.codicePaese = codicePaese;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param descrizioneBanca
	 *            The descrizioneBanca to set.
	 */
	public void setDescrizioneBanca(String descrizioneBanca) {
		this.descrizioneBanca = descrizioneBanca;
	}

	/**
	 * @param documenti
	 *            the documenti to set
	 */
	public void setDocumenti(String documenti) {
		this.documenti = documenti;
	}

	/**
	 * @param entitaCap
	 *            the entitaCap to set
	 */
	public void setEntitaCap(String entitaCap) {
		this.entitaCap = entitaCap;
	}

	/**
	 * @param entitaDescrizione
	 *            the entitaDescrizione to set
	 */
	public void setEntitaDescrizione(String entitaDescrizione) {
		this.entitaDescrizione = entitaDescrizione;
	}

	/**
	 * @param entitaId
	 *            the entitaId to set
	 */
	public void setEntitaId(Integer entitaId) {
		this.entitaId = entitaId;
	}

	/**
	 * @param entitaIndirizzo
	 *            the entitaIndirizzo to set
	 */
	public void setEntitaIndirizzo(String entitaIndirizzo) {
		this.entitaIndirizzo = entitaIndirizzo;
	}

	/**
	 * @param entitaLivelloAmministrativo1
	 *            the entitaLivelloAmministrativo1 to set
	 */
	public void setEntitaLivelloAmministrativo1(LivelloAmministrativo1 entitaLivelloAmministrativo1) {
		this.entitaLivelloAmministrativo1 = entitaLivelloAmministrativo1;
	}

	/**
	 * @param entitaLivelloAmministrativo2
	 *            the entitaLivelloAmministrativo2 to set
	 */
	public void setEntitaLivelloAmministrativo2(LivelloAmministrativo2 entitaLivelloAmministrativo2) {
		this.entitaLivelloAmministrativo2 = entitaLivelloAmministrativo2;
	}

	/**
	 * @param entitaLivelloAmministrativo3
	 *            the entitaLivelloAmministrativo3 to set
	 */
	public void setEntitaLivelloAmministrativo3(LivelloAmministrativo3 entitaLivelloAmministrativo3) {
		this.entitaLivelloAmministrativo3 = entitaLivelloAmministrativo3;
	}

	/**
	 * @param entitaLivelloAmministrativo4
	 *            the entitaLivelloAmministrativo4 to set
	 */
	public void setEntitaLivelloAmministrativo4(LivelloAmministrativo4 entitaLivelloAmministrativo4) {
		this.entitaLivelloAmministrativo4 = entitaLivelloAmministrativo4;
	}

	/**
	 * @param entitaLocalita
	 *            the entitaLocalita to set
	 */
	public void setEntitaLocalita(String entitaLocalita) {
		this.entitaLocalita = entitaLocalita;
	}

	/**
	 * @param entitaNazione
	 *            the entitaNazione to set
	 */
	public void setEntitaNazione(String entitaNazione) {
		this.entitaNazione = entitaNazione;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RataRV [abi=");
		builder.append(abi);
		builder.append(", aziendaCap=");
		builder.append(aziendaCap);
		builder.append(", aziendaLocalita=");
		builder.append(aziendaLocalita);
		builder.append(", aziendaDescrizione=");
		builder.append(aziendaDescrizione);
		builder.append(", aziendaIndirizzo=");
		builder.append(aziendaIndirizzo);
		builder.append(", aziendaLivelloAmministrativo1=");
		builder.append(aziendaLivelloAmministrativo1);
		builder.append(", aziendaLivelloAmministrativo2=");
		builder.append(aziendaLivelloAmministrativo2);
		builder.append(", aziendaLivelloAmministrativo3=");
		builder.append(aziendaLivelloAmministrativo3);
		builder.append(", aziendaLivelloAmministrativo4=");
		builder.append(aziendaLivelloAmministrativo4);
		builder.append(", cab=");
		builder.append(cab);
		builder.append(", checkDigit=");
		builder.append(checkDigit);
		builder.append(", cin=");
		builder.append(cin);
		builder.append(", codicePaese=");
		builder.append(codicePaese);
		builder.append(", dataDocumento=");
		builder.append(dataDocumento);
		builder.append(", dataScadenza=");
		builder.append(dataScadenza);
		builder.append(", entitaCap=");
		builder.append(entitaCap);
		builder.append(", entitaLocalita=");
		builder.append(entitaLocalita);
		builder.append(", entitaDescrizione=");
		builder.append(entitaDescrizione);
		builder.append(", entitaId=");
		builder.append(entitaId);
		builder.append(", entitaIndirizzo=");
		builder.append(entitaIndirizzo);
		builder.append(", entitaLivelloAmministrativo1=");
		builder.append(entitaLivelloAmministrativo1);
		builder.append(", entitaLivelloAmministrativo2=");
		builder.append(entitaLivelloAmministrativo2);
		builder.append(", entitaLivelloAmministrativo3=");
		builder.append(entitaLivelloAmministrativo3);
		builder.append(", entitaLivelloAmministrativo4=");
		builder.append(entitaLivelloAmministrativo4);
		builder.append(", importo=");
		builder.append(importo);
		builder.append(", numero=");
		builder.append(numero);
		builder.append(", numeroDocumento=");
		builder.append(numeroDocumento);
		builder.append("]");
		return builder.toString();
	}
}
