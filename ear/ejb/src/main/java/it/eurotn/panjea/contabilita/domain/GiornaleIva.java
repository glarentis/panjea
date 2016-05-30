/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "cont_libri_giornale_iva")
@NamedQueries({
		@NamedQuery(name = "GiornaleIva.caricaGiornaliIva", query = "select g from GiornaleIva g where g.annoCompetenza = :paramAnnoCompetenza and g.codiceAzienda = :paramCodiceAzienda"),
		@NamedQuery(name = "GiornaleIva.caricaGiornaliSuccessivi", query = "select g from GiornaleIva g where ((g.anno = :paramAnno and g.mese > :paramMese) or (g.anno > :paramAnno)) and g.registroIva.id = :paramIdRegistroIva and g.codiceAzienda = :paramCodiceAzienda"),
		@NamedQuery(name = "GiornaleIva.contaGiornaliByRegistroIva", query = "select count(g.id) from GiornaleIva g where g.codiceAzienda = :paramCodiceAzienda and g.registroIva.id = :paramIdRegistroIva "),
		@NamedQuery(name = "GiornaleIva.caricaDefinitivoByAnnoEMese", query = "select g from GiornaleIva g where g.codiceAzienda = :paramCodiceAzienda and g.anno = :paramAnno and g.mese = :paramMese and g.stato = 1 "),
		@NamedQuery(name = "GiornaleIva.caricaGiornaleByRegistroIva", query = "select g from GiornaleIva g where g.registroIva.id = :paramIdRegistroIva and g.anno = :paramAnno and g.mese = :paramMese and g.codiceAzienda = :paramCodiceAzienda") })
public class GiornaleIva extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = 7589551000707752632L;

	public static final int STAMPATO = 0;

	public static final int NON_STAMPATO = 1;

	public static final int NON_VALIDO = 2;
	@Column(length = 20, nullable = false)
	private String codiceAzienda;
	private Integer anno;

	private Integer annoCompetenza;

	private Integer mese;
	private int stato = GiornaleIva.NON_STAMPATO;
	@Temporal(TemporalType.DATE)
	private Date dataUltimoDocumento;
	private BigDecimal imponibile;

	private BigDecimal imposta;

	private BigDecimal totaleDocumento;

	@Column(length = 30)
	private String protocolloMovimento;
	private Integer numeroPagina;

	private BigDecimal percTrimestraleValore;
	private BigDecimal percTrimestraleImporto;

	@ManyToOne
	private RegistroIva registroIva;
	private BigDecimal minimaleIVA;

	@Transient
	private boolean stampabile;

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the annoCompetenza
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the dataUltimoDocumento
	 */
	public Date getDataUltimoDocumento() {
		return dataUltimoDocumento;
	}

	/**
	 * @return the imponibile
	 */
	public BigDecimal getImponibile() {
		return imponibile;
	}

	/**
	 * @return the imposta
	 */
	public BigDecimal getImposta() {
		return imposta;
	}

	/**
	 * @return the mese
	 */
	public Integer getMese() {
		return mese;
	}

	/**
	 * @return the minimaleIVA
	 */
	public BigDecimal getMinimaleIVA() {
		return minimaleIVA;
	}

	/**
	 * @return the numeroPagina
	 */
	public Integer getNumeroPagina() {
		return numeroPagina;
	}

	/**
	 * @return the percTrimestraleImporto
	 */
	public BigDecimal getPercTrimestraleImporto() {
		return percTrimestraleImporto;
	}

	/**
	 * @return the percTrimestraleValore
	 */
	public BigDecimal getPercTrimestraleValore() {
		return percTrimestraleValore;
	}

	/**
	 * @return the protocolloMovimento
	 */
	public String getProtocolloMovimento() {
		return protocolloMovimento;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return the valido
	 */
	public int getStato() {
		return stato;
	}

	/**
	 * @return the totaleDocumento
	 */
	public BigDecimal getTotaleDocumento() {
		return totaleDocumento;
	}

	/**
	 * @return the stampabile
	 */
	public boolean isStampabile() {
		return stampabile;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param annoCompetenza
	 *            the annoCompetenza to set
	 */
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param dataUltimoDocumento
	 *            the dataUltimoDocumento to set
	 */
	public void setDataUltimoDocumento(Date dataUltimoDocumento) {
		this.dataUltimoDocumento = dataUltimoDocumento;
	}

	/**
	 * @param imponibile
	 *            the imponibile to set
	 */
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}

	/**
	 * @param imposta
	 *            the imposta to set
	 */
	public void setImposta(BigDecimal imposta) {
		this.imposta = imposta;
	}

	/**
	 * @param mese
	 *            the mese to set
	 */
	public void setMese(Integer mese) {
		this.mese = mese;
	}

	/**
	 * @param minimaleIVA
	 *            the minimaleIVA to set
	 */
	public void setMinimaleIVA(BigDecimal minimaleIVA) {
		this.minimaleIVA = minimaleIVA;
	}

	/**
	 * @param numeroPagina
	 *            the numeroPagina to set
	 */
	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	/**
	 * @param percTrimestraleImporto
	 *            the percTrimestraleImporto to set
	 */
	public void setPercTrimestraleImporto(BigDecimal percTrimestraleImporto) {
		this.percTrimestraleImporto = percTrimestraleImporto;
	}

	/**
	 * @param percTrimestraleValore
	 *            the percTrimestraleValore to set
	 */
	public void setPercTrimestraleValore(BigDecimal percTrimestraleValore) {
		this.percTrimestraleValore = percTrimestraleValore;
	}

	/**
	 * @param protocolloMovimento
	 *            the protocolloMovimento to set
	 */
	public void setProtocolloMovimento(String protocolloMovimento) {
		this.protocolloMovimento = protocolloMovimento;
	}

	/**
	 * @param registroIva
	 *            the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @param stampabile
	 *            the stampabile to set
	 */
	public void setStampabile(boolean stampabile) {
		this.stampabile = stampabile;
	}

	/**
	 * @param stato
	 *            the stato to set
	 */
	public void setStato(int stato) {
		this.stato = stato;
	}

	/**
	 * @param totaleDocumento
	 *            the totaleDocumento to set
	 */
	public void setTotaleDocumento(BigDecimal totaleDocumento) {
		this.totaleDocumento = totaleDocumento;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("GiornaleIva[");
		buffer.append(" id = ").append(getId());
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" anno = ").append(anno);
		buffer.append(" annoCompetenza = ").append(annoCompetenza);
		buffer.append(" mese = ").append(mese);
		buffer.append(" stato = ").append(stato);
		buffer.append(" dataUltimoDocumento = ").append(dataUltimoDocumento);
		buffer.append(" imponibile = ").append(imponibile);
		buffer.append(" imposta = ").append(imposta);
		buffer.append(" totaleDocumento = ").append(totaleDocumento);
		buffer.append(" numeroMovimento = ").append(protocolloMovimento);
		buffer.append(" numeroPagina = ").append(numeroPagina);
		buffer.append(" registroIva = ").append(registroIva != null ? registroIva.getId() : "null");
		buffer.append("]");
		return buffer.toString();
	}

}
