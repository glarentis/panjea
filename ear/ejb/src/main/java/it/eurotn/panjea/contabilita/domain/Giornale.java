package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "cont_libri_giornale")
@NamedQueries({
		@NamedQuery(name = "Giornale.caricaGiornali", query = "select g from Giornale g where g.annoCompetenza = :paramAnnoCompetenza and g.codiceAzienda = :paramCodiceAzienda"),
		@NamedQuery(name = "Giornale.caricaGiornaleByMeseEAnnoCompetenza", query = "select g from Giornale g where g.annoCompetenza = :paramAnnoCompetenza and g.mese = :paramMese and g.codiceAzienda = :paramCodiceAzienda"),
		@NamedQuery(name = "Giornale.caricaGiornaleByMeseEAnno", query = "select g from Giornale g where g.anno = :paramAnno and g.mese = :paramMese and g.codiceAzienda = :paramCodiceAzienda"),
		@NamedQuery(name = "Giornale.contaGiornali", query = "select count(g.id) from Giornale g where g.codiceAzienda = :paramCodiceAzienda"),
		@NamedQuery(name = "Giornale.caricaGiornaliSuccessivi", query = "select g from Giornale g where (g.anno = :paramAnno and g.mese > :paramMese) and g.codiceAzienda = :paramCodiceAzienda") })
public class Giornale extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = -7254512853143128560L;

	@Column(length = 20, nullable = false)
	private String codiceAzienda;

	private Integer anno;

	private Integer annoCompetenza;

	private Integer mese;

	private Boolean valido;

	@Temporal(TemporalType.DATE)
	private Date dataUltimoDocumento;

	private BigDecimal saldoDare;

	private BigDecimal saldoAvere;

	private Integer numeroMovimento;

	private Integer numeroPagina;

	/*
	 * @OneToMany+@JoinColumn When a collection is mapped using these two annotations, Hibernate doesn't generate a join
	 * table. Envers, however, has to do this, so that when you read the revisions in which the related entity has
	 * changed, you don't get false results.
	 * 
	 * Aggiungo @AuditJoinTable per personalizzare il nome della tabella che in automatico viene chiamata
	 * Giornale_NotaGiornale_aud.
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "giornale_id")
	@AuditJoinTable(name = "cont_libri_giornale_cont_libri_giornale_note_aud")
	private List<NotaGiornale> noteGiornale;

	@Transient
	private boolean stampabile;

	/**
	 * @return anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return annoCompetenza
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return dataUltimoDocumento
	 */
	public Date getDataUltimoDocumento() {
		return dataUltimoDocumento;
	}

	/**
	 * @return mese
	 */
	public Integer getMese() {
		return mese;
	}

	/**
	 * @return noteGiornale
	 */
	public List<NotaGiornale> getNoteGiornale() {
		return noteGiornale;
	}

	/**
	 * @return numeroMovimento
	 */
	public Integer getNumeroMovimento() {
		return numeroMovimento;
	}

	/**
	 * @return numeroPagina
	 */
	public Integer getNumeroPagina() {
		return numeroPagina;
	}

	/**
	 * @return saldoAvere
	 */
	public BigDecimal getSaldoAvere() {
		return saldoAvere;
	}

	/**
	 * @return saldoDare
	 */
	public BigDecimal getSaldoDare() {
		return saldoDare;
	}

	/**
	 * @return valido
	 */
	public Boolean getValido() {
		return valido;
	}

	/**
	 * @return stampabile
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
	 * @param mese
	 *            the mese to set
	 */
	public void setMese(Integer mese) {
		this.mese = mese;
	}

	/**
	 * @param noteGiornale
	 *            the noteGiornale to set
	 */
	public void setNoteGiornale(List<NotaGiornale> noteGiornale) {
		this.noteGiornale = noteGiornale;
	}

	/**
	 * @param numeroMovimento
	 *            the numeroMovimento to set
	 */
	public void setNumeroMovimento(Integer numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}

	/**
	 * @param numeroPagina
	 *            the numeroPagina to set
	 */
	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	/**
	 * @param saldoAvere
	 *            the saldoAvere to set
	 */
	public void setSaldoAvere(BigDecimal saldoAvere) {
		this.saldoAvere = saldoAvere;
	}

	/**
	 * @param saldoDare
	 *            the saldoDare to set
	 */
	public void setSaldoDare(BigDecimal saldoDare) {
		this.saldoDare = saldoDare;
	}

	/**
	 * @param stampabile
	 *            the stampabile to set
	 */
	public void setStampabile(boolean stampabile) {
		this.stampabile = stampabile;
	}

	/**
	 * @param valido
	 *            the valido to set
	 */
	public void setValido(Boolean valido) {
		this.valido = valido;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Giornale[");
		buffer.append("anno = ").append(anno);
		buffer.append(" dataUltimoDocumento = ").append(dataUltimoDocumento);
		buffer.append(" mese = ").append(mese);
		buffer.append(" numeroMovimento = ").append(numeroMovimento);
		buffer.append(" numeroPagina = ").append(numeroPagina);
		buffer.append(" saldoAvere = ").append(saldoAvere);
		buffer.append(" saldoDare = ").append(saldoDare);
		buffer.append(" valido = ").append(valido);
		buffer.append("]");
		return buffer.toString();
	}
}
