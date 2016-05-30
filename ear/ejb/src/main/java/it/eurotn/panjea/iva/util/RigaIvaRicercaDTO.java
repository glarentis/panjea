/**
 *
 */
package it.eurotn.panjea.iva.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.contabilita.domain.RegistroIva;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fattazzo
 *
 */
public class RigaIvaRicercaDTO implements Serializable {

	private static final long serialVersionUID = 524518326726822225L;

	private Integer idDocumento;
	private CodiceDocumento codiceDocumento;
	private Importo totaleDocumento;
	private Date dataDocumento;
	private Date dataRegistrazione;

	private TipoDocumento tipoDocumento;

	private RegistroIva registroIva;

	private EntitaDocumento entita;

	private CodiceIva codiceIva;

	private Importo imponibileRiga;
	private Importo impostaRiga;

	/**
	 * @return the codiceDocumento
	 */
	public CodiceDocumento getCodiceDocumento() {
		return codiceDocumento;
	}

	/**
	 * @return the codiceIva
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the entita
	 */
	public EntitaDocumento getEntita() {
		return entita;
	}

	/**
	 * @return the idDocumento
	 */
	public Integer getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @return the imponibileRiga
	 */
	public Importo getImponibileRiga() {
		return imponibileRiga;
	}

	/**
	 * @return the impostaRiga
	 */
	public Importo getImpostaRiga() {
		return impostaRiga;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the totaleDocumento
	 */
	public Importo getTotaleDocumento() {
		return totaleDocumento;
	}

	/**
	 * @param codiceCodiceIva
	 *            the codiceCodiceIva to set
	 */
	public void setCodiceCodiceIva(String codiceCodiceIva) {
		this.codiceIva.setCodice(codiceCodiceIva);
	}

	/**
	 * @param codiceDocumento
	 *            the codiceDocumento to set
	 */
	public void setCodiceDocumento(CodiceDocumento codiceDocumento) {
		this.codiceDocumento = codiceDocumento;
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		entita.setCodice(codiceEntita);
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
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param denominazioneEntita
	 *            the denominazioneEntita to set
	 */
	public void setDenominazioneEntita(String denominazioneEntita) {
		entita.setDescrizione(denominazioneEntita);
	}

	/**
	 * @param descrizioneCodiceIva
	 *            the descrizioneCodiceIva to set
	 */
	public void setDescrizioneCodiceIva(String descrizioneCodiceIva) {
		this.codiceIva.setDescrizioneInterna(descrizioneCodiceIva);
	}

	/**
	 * @param descrizioneRegistroIva
	 *            the descrizioneRegistroIva to set
	 */
	public void setDescrizioneRegistroIva(String descrizioneRegistroIva) {
		this.registroIva.setDescrizione(descrizioneRegistroIva);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.tipoDocumento.setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idCodiceIva
	 *            the idCodiceIva to set
	 */
	public void setIdCodiceIva(Integer idCodiceIva) {
		this.codiceIva = new CodiceIva();
		this.codiceIva.setId(idCodiceIva);
	}

	/**
	 * @param idDocumento
	 *            the idDocumento to set
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		entita.setId(idEntita);
	}

	/**
	 * @param idRegistroIva
	 *            the idRegistroIva to set
	 */
	public void setIdRegistroIva(Integer idRegistroIva) {
		this.registroIva = new RegistroIva();
		this.registroIva.setId(idRegistroIva);
	}

	/**
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.tipoDocumento = new TipoDocumento();
		this.tipoDocumento.setId(idTipoDocumento);
	}

	/**
	 * @param imponibileRiga
	 *            the imponibileRiga to set
	 */
	public void setImponibileRiga(Importo imponibileRiga) {
		this.imponibileRiga = imponibileRiga;
	}

	/**
	 * @param impostaRiga
	 *            the impostaRiga to set
	 */
	public void setImpostaRiga(Importo impostaRiga) {
		this.impostaRiga = impostaRiga;
	}

	/**
	 * @param numeroRegistroIva
	 *            the numeroRegistroIva to set
	 */
	public void setNumeroRegistroIva(Integer numeroRegistroIva) {
		this.registroIva.setNumero(numeroRegistroIva);
	}

	/**
	 * @param tipoEntitaParam
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntitaParam) {
		entita = new EntitaDocumento();
		entita.setTipoEntita(tipoEntitaParam);
	}

	/**
	 * @param totaleDocumento
	 *            the totaleDocumento to set
	 */
	public void setTotaleDocumento(Importo totaleDocumento) {
		this.totaleDocumento = totaleDocumento;
	}

}
