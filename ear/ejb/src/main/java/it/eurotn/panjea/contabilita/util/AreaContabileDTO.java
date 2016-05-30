/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO utile per le ricerche di {@link AreaContabile}.
 *
 * @author adriano
 * @version 1.0, 14/nov/07
 */
public class AreaContabileDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private StatoAreaContabile statoAreaContabile;

	private Integer annoMovimento;

	private Date dataDocumento;

	private Date dataRegistrazione;

	private Integer idTipoDocumento;

	private CodiceDocumento numeroDocumento;

	private CodiceDocumento numeroProtocollo;

	private String codiceTipoDocumento;

	private Integer idDocumento;

	private EntitaDocumento entitaDocumento;

	private String descrizioneTipoDocumento;

	private String note;

	private BigDecimal importoDare;

	private BigDecimal importoAvere;

	private Integer numeroPaginaGiornale;

	private Importo totale;

	private Integer idEntita;

	private Integer codiceEntita;

	private String ragioneSocialeEntita;

	private boolean notaCreditoEnable;

	private List<RigaContabileDTO> righeContabili;

	private TipoDocumento tipoDocumento;

	private String registroProtocollo;

	private Integer idRapportoBancario;

	private String descrizioneRapportoBancario;

	private String codiceAzienda;

	private TipoEntita tipoEntita;

	/**
	 * Costruttore senza parametri utilizzato nella parte swing.
	 */
	public AreaContabileDTO() {
	}

	public AreaContabileDTO(Integer id, Date dataDocumento, CodiceDocumento numeroDocumento,
			CodiceDocumento numeroProtocollo, Importo totale, Integer idEntita, Integer codiceEntita,
			String ragioneSocialeEntita, Integer numeroPaginaGiornale) {
		super();
		this.id = id;
		this.dataDocumento = dataDocumento;
		this.numeroDocumento = numeroDocumento;
		this.numeroProtocollo = numeroProtocollo;
		this.numeroPaginaGiornale = numeroPaginaGiornale;
		this.totale = totale;
		this.idEntita = idEntita;
		this.codiceEntita = codiceEntita;
		this.ragioneSocialeEntita = ragioneSocialeEntita;
		this.notaCreditoEnable = false;
	}

	public AreaContabileDTO(Integer id, Date dataDocumento, CodiceDocumento numeroDocumento,
			CodiceDocumento numeroProtocollo, Importo totale, Integer idEntita, Integer codiceEntita,
			String ragioneSocialeEntita, Integer numeroPaginaGiornale, boolean notaCreaditoEnable) {
		super();
		this.id = id;
		this.dataDocumento = dataDocumento;
		this.numeroDocumento = numeroDocumento;
		this.numeroProtocollo = numeroProtocollo;
		this.numeroPaginaGiornale = numeroPaginaGiornale;
		this.totale = totale;
		this.idEntita = idEntita;
		this.codiceEntita = codiceEntita;
		this.ragioneSocialeEntita = ragioneSocialeEntita;
		this.notaCreditoEnable = notaCreaditoEnable;
	}

	/**
	 * @param dataRegistrazione
	 * @param numeroDocumento
	 * @param prefissoProtocollo
	 * @param numeroProtocollo
	 * @param codiceTipoAreaContabile
	 * @param descrizioneTipoAreaContabile
	 * @param note
	 * @param registroProtocollo
	 */
	public AreaContabileDTO(Integer id, StatoAreaContabile statoAreaContabile, Integer annoMovimento,
			Date dataDocumento, Date dataRegistrazione, CodiceDocumento numeroDocumento,
			CodiceDocumento numeroProtocollo, String codiceTipoDocumento, String descrizioneTipoDocumento, String note,
			Integer numeroPaginaGiornale, Importo totale, final String registroProtocollo) {
		super();
		this.id = id;
		this.statoAreaContabile = statoAreaContabile;
		this.annoMovimento = annoMovimento;
		this.dataDocumento = dataDocumento;
		this.dataRegistrazione = dataRegistrazione;
		this.numeroDocumento = numeroDocumento;
		this.numeroProtocollo = numeroProtocollo;
		this.codiceTipoDocumento = codiceTipoDocumento;
		this.descrizioneTipoDocumento = descrizioneTipoDocumento;
		this.note = note;
		this.numeroPaginaGiornale = numeroPaginaGiornale;
		this.totale = totale;
		this.registroProtocollo = registroProtocollo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof AreaContabileDTO)) {
			return false;
		}
		return (this.getId().equals(((AreaContabileDTO) obj).getId()));
	}

	/**
	 * @return Returns the annoMovimento.
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @return Returns the codiceAzienda.
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the codiceEntita
	 */
	public Integer getCodiceEntita() {
		return codiceEntita;
	}

	/**
	 * @return Returns the codiceTipoDocumento.
	 */
	public String getCodiceTipoDocumento() {
		return codiceTipoDocumento;
	}

	/**
	 * @return Returns the dataDocumento.
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return Returns the dataRegistrazione.
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return Returns the descrizioneRapportoBancario.
	 */
	public String getDescrizioneRapportoBancario() {
		return descrizioneRapportoBancario;
	}

	/**
	 * @return Returns the descrizioneTipoDocumento.
	 */
	public String getDescrizioneTipoDocumento() {
		return descrizioneTipoDocumento;
	}

	public EntitaDocumento getEntitaDocumento() {
		if (entitaDocumento == null) {
			Documento documento = new Documento();
			documento.setId(idDocumento);
			documento.setEntita(new EntitaLite());
			documento.getEntita().setId(idEntita);
			documento.getEntita().setCodice(codiceEntita);
			documento.getEntita().setDenominazione(ragioneSocialeEntita);
			documento.setRapportoBancarioAzienda(new RapportoBancarioAzienda());
			documento.getRapportoBancarioAzienda().setId(idRapportoBancario);
			documento.getRapportoBancarioAzienda().setDescrizione(descrizioneRapportoBancario);
			documento.setCodiceAzienda(codiceAzienda);
			documento.setTipoDocumento(tipoDocumento);
			entitaDocumento = new EntitaDocumento(documento);
		}
		return entitaDocumento;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return Returns the idDocumento.
	 */
	public Integer getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @return the idEntita
	 */
	public Integer getIdEntita() {
		return idEntita;
	}

	/**
	 * @return Returns the idRapportoBancario.
	 */
	public Integer getIdRapportoBancario() {
		return idRapportoBancario;
	}

	/**
	 * @return Returns the idTipoDocumento.
	 */
	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	/**
	 * @return Returns the importoAvere.
	 */
	public BigDecimal getImportoAvere() {
		return importoAvere;
	}

	/**
	 * @return Returns the importoDare.
	 */
	public BigDecimal getImportoDare() {
		return importoDare;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return Returns the numeroPaginaGiornale.
	 */
	public Integer getNumeroPaginaGiornale() {
		return numeroPaginaGiornale;
	}

	/**
	 * @return the numeroProtocollo
	 */
	public CodiceDocumento getNumeroProtocollo() {
		return numeroProtocollo;
	}

	/**
	 * @return the ragioneSocialeEntita
	 */
	public String getRagioneSocialeEntita() {
		return ragioneSocialeEntita;
	}

	/**
	 * @return Returns the registroProtocollo.
	 */
	public String getRegistroProtocollo() {
		return registroProtocollo;
	}

	/**
	 * @return Returns the righeContabili.
	 */
	public List<RigaContabileDTO> getRigheContabili() {
		if (righeContabili == null) {
			righeContabili = new ArrayList<RigaContabileDTO>();
		}
		return righeContabili;
	}

	/**
	 * @return Returns the sbilancio.
	 */
	public BigDecimal getSbilancio() {
		return importoDare.subtract(importoAvere);
	}

	/**
	 * @return Returns the statoAreaContabile.
	 */
	public StatoAreaContabile getStatoAreaContabile() {
		return statoAreaContabile;
	}

	/**
	 * @return Returns the tipoDocumento.
	 */
	public TipoDocumento getTipoDocumento() {
		if (tipoDocumento == null) {
			tipoDocumento = new TipoDocumento();
			tipoDocumento.setId(idTipoDocumento);
			tipoDocumento.setCodice(codiceTipoDocumento);
			tipoDocumento.setDescrizione(descrizioneTipoDocumento);
			tipoDocumento.setTipoEntita(tipoEntita);
		}

		return tipoDocumento;
	}

	/**
	 * @return Returns the tipoEntita.
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the totale
	 */
	public BigDecimal getTotale() {
		BigDecimal tot = this.totale != null ? totale.getImportoInValutaAzienda() : BigDecimal.ZERO;

		if (tot.compareTo(BigDecimal.ZERO) != 0 && notaCreditoEnable) {
			tot = tot.negate();
		}
		return tot;
	}

	@Override
	public int hashCode() {
		if (getId() != null) {
			String hashStr = this.getClass().getName() + ":" + this.getId();
			return hashStr.hashCode();
		}
		return super.hashCode();
	}

	/**
	 * @param annoMovimento
	 *            The annoMovimento to set.
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.codiceEntita = codiceEntita;
	}

	/**
	 * @param codiceTipoDocumento
	 *            The codiceTipoDocumento to set.
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.codiceTipoDocumento = codiceTipoDocumento;
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataRegistrazione
	 *            The dataRegistrazione to set.
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param descrizioneRapportoBancario
	 *            The descrizioneRapportoBancario to set.
	 */
	public void setDescrizioneRapportoBancario(String descrizioneRapportoBancario) {
		this.descrizioneRapportoBancario = descrizioneRapportoBancario;
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            The descrizioneTipoDocumento to set.
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.descrizioneTipoDocumento = descrizioneTipoDocumento;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param idDocumento
	 *            The idDocumento to set.
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @param idEntita
	 *            The idEntita to set.
	 */
	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
	}

	/**
	 * @param idRapportoBancario
	 *            The idRapportoBancario to set.
	 */
	public void setIdRapportoBancario(Integer idRapportoBancario) {
		this.idRapportoBancario = idRapportoBancario;
	}

	/**
	 * @param idTipoDocumento
	 *            The idTipoDocumento to set.
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	/**
	 * @param importoAvere
	 *            The importoAvere to set.
	 */
	public void setImportoAvere(BigDecimal importoAvere) {
		this.importoAvere = importoAvere;
	}

	/**
	 * @param importoDare
	 *            The importoDare to set.
	 */
	public void setImportoDare(BigDecimal importoDare) {
		this.importoDare = importoDare;
	}

	/**
	 * @param notaCreditoEnable
	 *            The notaCreditoEnable to set.
	 */
	public void setNotaCreditoEnable(boolean notaCreditoEnable) {
		this.notaCreditoEnable = notaCreditoEnable;
	}

	/**
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroDocumento
	 *            The numeroDocumento to set.
	 */
	public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param numeroPaginaGiornale
	 *            The numeroPaginaGiornale to set.
	 */
	public void setNumeroPaginaGiornale(Integer numeroPaginaGiornale) {
		this.numeroPaginaGiornale = numeroPaginaGiornale;
	}

	/**
	 * @param numeroProtocollo
	 *            The numeroProtocollo to set.
	 */
	public void setNumeroProtocollo(CodiceDocumento numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	/**
	 * @param ragioneSocialeEntita
	 *            The ragioneSocialeEntita to set.
	 */
	public void setRagioneSocialeEntita(String ragioneSocialeEntita) {
		this.ragioneSocialeEntita = ragioneSocialeEntita;
	}

	/**
	 * @param registroProtocollo
	 *            The registroProtocollo to set.
	 */
	public void setRegistroProtocollo(String registroProtocollo) {
		this.registroProtocollo = registroProtocollo;
	}

	/**
	 * @param righeContabili
	 *            The righeContabili to set.
	 */
	public void setRigheContabili(List<RigaContabileDTO> righeContabili) {
		this.righeContabili = righeContabili;
	}

	/**
	 * @param statoAreaContabile
	 *            The statoAreaContabile to set.
	 */
	public void setStatoAreaContabile(StatoAreaContabile statoAreaContabile) {
		this.statoAreaContabile = statoAreaContabile;
	}

	/**
	 * @param tipoDocumento
	 *            The tipoDocumento to set.
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipoEntita
	 *            The tipoEntita to set.
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

	/**
	 * @param totale
	 *            The totale to set.
	 */
	public void setTotale(Importo totale) {
		this.totale = totale;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("AreaContabileDTO[");
		buffer.append(" id = ").append(id);
		buffer.append(" annoMovimento = ").append(annoMovimento);
		buffer.append(" codiceTipoDocumento = ").append(codiceTipoDocumento);
		buffer.append(" dataDocumento = ").append(dataDocumento);
		buffer.append(" dataRegistrazione = ").append(dataRegistrazione);
		buffer.append(" descrizioneTipoDocumento = ").append(descrizioneTipoDocumento);
		buffer.append(" importoAvere = ").append(importoAvere);
		buffer.append(" importoDare = ").append(importoDare);
		buffer.append(" note = ").append(note);
		buffer.append(" numeroDocumento = ").append(numeroDocumento);
		buffer.append(" numeroPaginaGiornale = ").append(numeroPaginaGiornale);
		buffer.append(" numeroProtocollo = ").append(numeroProtocollo);
		buffer.append("]");
		return buffer.toString();
	}

}
