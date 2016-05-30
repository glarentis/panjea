/**
 * 
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fattazzo
 * 
 */
public class DocumentoEntitaDTO implements Serializable {

	private static final long serialVersionUID = -7920936236818738052L;

	private EntitaLite entita;

	private String nazione;

	private TipoDocumento tipoDocumento;

	private Date dataDocumento;

	private String numeroDocumento;

	private BigDecimal totaleImponibile;

	private String codiceIva;

	{
		this.tipoDocumento = new TipoDocumento();
	}

	/**
	 * @return the codiceIva
	 */
	public String getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the nazione
	 */
	public String getNazione() {
		return nazione;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the totaleImponibile
	 */
	public BigDecimal getTotaleImponibile() {
		return totaleImponibile;
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.entita.setCodice(codiceEntita);
	}

	/**
	 * @param codiceFiscale
	 *            the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.entita.getAnagrafica().setCodiceFiscale(codiceFiscale);
	}

	/**
	 * @param codiceIva
	 *            the codiceIva to set
	 */
	public void setCodiceIva(String codiceIva) {
		this.codiceIva = codiceIva;
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.tipoDocumento.setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param denominazioneEntita
	 *            the denominazioneEntita to set
	 */
	public void setDenominazioneEntita(String denominazioneEntita) {
		this.entita.getAnagrafica().setDenominazione(denominazioneEntita);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.tipoDocumento.setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.entita.setId(idEntita);
	}

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param partitaIva
	 *            the partitaIva to set
	 */
	public void setPartitaIva(String partitaIva) {
		this.entita.getAnagrafica().setPartiteIVA(partitaIva);
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(String tipoEntita) {
		if ("C".equals(tipoEntita)) {
			this.entita = new ClienteLite();
		} else {
			this.entita = new FornitoreLite();
		}
	}

	/**
	 * @param totaleImponibile
	 *            the totaleImponibile to set
	 */
	public void setTotaleImponibile(BigDecimal totaleImponibile) {
		this.totaleImponibile = totaleImponibile;
	}
}
