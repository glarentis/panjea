/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Leonardo
 *
 */
@Entity
@Table(name = "bamm_quote_ammortamento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_QUOTA", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Q")
@NamedQueries({ @NamedQuery(name = "QuotaAmmortamento.deleteOrphans", query = " delete QuotaAmmortamento q where q.consolidata = false and q not in (select pcb.quotaFiscale from PoliticaCalcoloBene pcb) ") })
public abstract class QuotaAmmortamento extends EntityBase {

	private static final long serialVersionUID = 3950064881426517003L;

	public static final String REF = "QuotaAmmortamento";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_CONSOLIDATA = "consolidata";
	public static final String PROP_DIRTY = "dirty";
	public static final String PROP_BENE_AMMORTIZZABILE = "beneAmmortizzabile";
	public static final String PROP_PROGRESSIVO_ANNO_AMMORTAMENTO = "progressivoAnnoAmmortamento";
	public static final String PROP_ANNO_SOLARE_AMMORTAMENTO = "annoSolareAmmortamento";
	public static final String PROP_PERC_QUOTA_AMMORTAMENTO_ORDINARIO = "percQuotaAmmortamentoOrdinario";
	public static final String PROP_IMP_QUOTA_AMMORTAMENTO_ORDINARIO = "impQuotaAmmortamentoOrdinario";
	public static final String PROP_ANNOTAZIONI = "annotazioni";

	@ManyToOne(fetch = FetchType.LAZY)
	private BeneAmmortizzabileLite beneAmmortizzabile;

	private boolean consolidata = false;
	private boolean dirty = false;

	private Integer progressivoAnnoAmmortamento;
	private Integer annoSolareAmmortamento;

	private double percQuotaAmmortamentoOrdinario;

	private BigDecimal impQuotaAmmortamentoOrdinario;
	private String annotazioni;

	// rappresenta l'importo soggetto da ammortamento del bene utilizzato per il
	// calcolo della quota
	@Column(precision = 19, scale = 6)
	private BigDecimal importoSoggettoAmmortamentoBene;

	private boolean percPrimoAnnoApplicata;

	/**
	 * Costruttore di default.
	 */
	public QuotaAmmortamento() {
		initialize();
	}

	/**
	 * @return the annoSolareAmmortamento
	 */
	public Integer getAnnoSolareAmmortamento() {
		return annoSolareAmmortamento;
	}

	/**
	 * @return the annotazioni
	 */
	public String getAnnotazioni() {
		return annotazioni;
	}

	/**
	 * @return the beneAmmortizzabile
	 */
	public BeneAmmortizzabileLite getBeneAmmortizzabile() {
		return beneAmmortizzabile;
	}

	/**
	 * @return the importoSoggettoAmmortamentoBene
	 */
	public BigDecimal getImportoSoggettoAmmortamentoBene() {
		return importoSoggettoAmmortamentoBene;
	}

	/**
	 * @return the impQuotaAmmortamentoOrdinario
	 */
	public BigDecimal getImpQuotaAmmortamentoOrdinario() {
		return impQuotaAmmortamentoOrdinario;
	}

	/**
	 * @return the percQuotaAmmortamentoOrdinario
	 */
	public double getPercQuotaAmmortamentoOrdinario() {
		return percQuotaAmmortamentoOrdinario;
	}

	/**
	 * @return the percQuotaAmmortamentoOrdinarioApplicata
	 */
	public Double getPercQuotaAmmortamentoOrdinarioApplicata() {
		if (percPrimoAnnoApplicata) {
			return new BigDecimal(percQuotaAmmortamentoOrdinario).setScale(6).divide(new BigDecimal(2), 2)
					.doubleValue();
		}
		return percQuotaAmmortamentoOrdinario;
	}

	/**
	 * @return the progressivoAnnoAmmortamento
	 */
	public Integer getProgressivoAnnoAmmortamento() {
		return progressivoAnnoAmmortamento;
	}

	/**
	 * @return the totaleAnno
	 */
	public abstract BigDecimal getTotaleAnno();

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		beneAmmortizzabile = new BeneAmmortizzabileLite();
		impQuotaAmmortamentoOrdinario = BigDecimal.ZERO;
		percQuotaAmmortamentoOrdinario = 0;
		percPrimoAnnoApplicata = false;
	}

	/**
	 * @return the consolidata
	 */
	public boolean isConsolidata() {
		return consolidata;
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @return the percPrimoAnnoApplicata
	 */
	public boolean isPercPrimoAnnoApplicata() {
		return percPrimoAnnoApplicata;
	}

	/**
	 * @param annoSolareAmmortamento
	 *            the annoSolareAmmortamento to set
	 */
	public void setAnnoSolareAmmortamento(Integer annoSolareAmmortamento) {
		this.annoSolareAmmortamento = annoSolareAmmortamento;
	}

	/**
	 * @param annotazioni
	 *            the annotazioni to set
	 */
	public void setAnnotazioni(String annotazioni) {
		this.annotazioni = annotazioni;
	}

	/**
	 * @param beneAmmortizzabile
	 *            the beneAmmortizzabile to set
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabileLite beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
	}

	/**
	 * @param consolidata
	 *            the consolidata to set
	 */
	public void setConsolidata(boolean consolidata) {
		this.consolidata = consolidata;
	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @param importoSoggettoAmmortamentoBene
	 *            the importoSoggettoAmmortamentoBene to set
	 */
	public void setImportoSoggettoAmmortamentoBene(BigDecimal importoSoggettoAmmortamentoBene) {
		this.importoSoggettoAmmortamentoBene = importoSoggettoAmmortamentoBene;
	}

	/**
	 * @param impQuotaAmmortamentoOrdinario
	 *            the impQuotaAmmortamentoOrdinario to set
	 */
	public void setImpQuotaAmmortamentoOrdinario(BigDecimal impQuotaAmmortamentoOrdinario) {
		this.impQuotaAmmortamentoOrdinario = impQuotaAmmortamentoOrdinario;
	}

	/**
	 * @param percPrimoAnnoApplicata
	 *            the percPrimoAnnoApplicata to set
	 */
	public void setPercPrimoAnnoApplicata(boolean percPrimoAnnoApplicata) {
		this.percPrimoAnnoApplicata = percPrimoAnnoApplicata;
	}

	/**
	 * @param percQuotaAmmortamentoOrdinario
	 *            the percQuotaAmmortamentoOrdinario to set
	 */
	public void setPercQuotaAmmortamentoOrdinario(double percQuotaAmmortamentoOrdinario) {
		this.percQuotaAmmortamentoOrdinario = percQuotaAmmortamentoOrdinario;
	}

	/**
	 * @param progressivoAnnoAmmortamento
	 *            the progressivoAnnoAmmortamento to set
	 */
	public void setProgressivoAnnoAmmortamento(Integer progressivoAnnoAmmortamento) {
		this.progressivoAnnoAmmortamento = progressivoAnnoAmmortamento;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("QuotaAmmortamento[");
		buffer.append("annoSolareAmmortamento = ").append(annoSolareAmmortamento);
		buffer.append(" annotazioni = ").append(annotazioni);
		buffer.append(" beneAmmortizzabile = ").append(beneAmmortizzabile != null ? beneAmmortizzabile.getId() : null);
		buffer.append(" consolidata = ").append(consolidata);
		buffer.append(" dirty = ").append(dirty);
		buffer.append(" impQuotaAmmortamentoOrdinario = ").append(impQuotaAmmortamentoOrdinario);
		buffer.append(" percQuotaAmmortamentoOrdinario = ").append(percQuotaAmmortamentoOrdinario);
		buffer.append(" progressivoAnnoAmmortamento = ").append(progressivoAnnoAmmortamento);
		buffer.append("]");
		return buffer.toString();
	}

}
