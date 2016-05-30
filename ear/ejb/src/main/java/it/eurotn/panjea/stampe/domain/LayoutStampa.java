package it.eurotn.panjea.stampe.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.stampe.manager.FormulaNumeroCopieCalculator;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Gestisce i layout di stampa per i vari TipiDocumento.
 *
 * @author giangi
 * @version 1.0, 26/mar/2012
 *
 */
@Entity
@Table(name = "pref_layoutstampe")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_AREA", discriminatorType = DiscriminatorType.STRING, length = 3)
@DiscriminatorValue("LS")
@NamedQuery(name = "LayoutStampa.caricaAll", query = "select l from LayoutStampa l where l.class<>it.eurotn.panjea.stampe.domain.LayoutStampaDocumento", hints = {
		@QueryHint(name = "org.hibernate.cacheable", value = "true"),
		@QueryHint(name = "org.hibernate.cacheRegion", value = "layoutStampa") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "layoutStampa")
public class LayoutStampa extends EntityBase {

	private static final long serialVersionUID = 1705760595437069415L;

	private static Map<String, String[]> possibleVariablesForTipoDocumento;

	static {
		possibleVariablesForTipoDocumento = new HashMap<String, String[]>();
		possibleVariablesForTipoDocumento.put(TipoAreaMagazzino.class.getName(),
				new String[] { AreaMagazzino.FORMULA_VAR_N_COPIE_TIPO_DOCUMENTO });
		possibleVariablesForTipoDocumento.put(TipoAreaOrdine.class.getName(), new String[] {});
		possibleVariablesForTipoDocumento.put(TipoAreaPreventivo.class.getName(), new String[] {});
		possibleVariablesForTipoDocumento = java.util.Collections.unmodifiableMap(possibleVariablesForTipoDocumento);
	}

	protected String reportName;

	private Boolean predefinito;

	private Boolean soloTesto;

	private Boolean batch;

	private String formulaNumeroCopie;

	private Boolean confermaNumeroCopie;

	@Transient
	private String stampante;

	private Boolean preview;

	private boolean mailLayout;

	private boolean interno;

	/**
	 * Costruttore.
	 */
	public LayoutStampa() {
		predefinito = false;
		soloTesto = false;
		batch = false;
		confermaNumeroCopie = false;
		preview = true;
		formulaNumeroCopie = "1";
		mailLayout = false;
		interno = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LayoutStampa other = (LayoutStampa) obj;
		if (reportName == null) {
			if (other.reportName != null) {
				return false;
			}
		} else if (!reportName.equals(other.reportName)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the batch.
	 */
	public Boolean getBatch() {
		return batch;
	}

	/**
	 * Restituisce la chiave di identificazione del layout di stampa.
	 *
	 * @return chiave
	 */
	public String getChiave() {
		StringBuilder sb = new StringBuilder(100);
		if (getReportName() != null) {
			sb.append(getReportName());
		}
		return sb.toString();
	}

	/**
	 *
	 * @return chiave per le caratteristiche di stampa. Proprietà usata per raggruppare i report in base alle
	 *         caratteristiche di stampa
	 */
	public String getChiaveCaratteristicheStampa() {
		return ObjectUtils.defaultIfNull(stampante, "") + Boolean.toString(preview);
	}

	/**
	 * @return Returns the confermaNumeroCopie.
	 */
	public Boolean getConfermaNumeroCopie() {
		return confermaNumeroCopie;
	}

	/**
	 * @return Returns the formulaNumeroCopie.
	 */
	public String getFormulaNumeroCopie() {
		return formulaNumeroCopie;
	}

	/**
	 * @return numero copie da stampare
	 */
	public int getNumeroCopie() {
		return FormulaNumeroCopieCalculator.calcolaNumeroCopie(formulaNumeroCopie, new HashMap<String, Object>());
	}

	/**
	 * @return restituisce le variabili che possono essere utilizzate per comporre la formula per il calcolo del numero
	 *         copie
	 */
	public String[] getPossibleVariables() {
		return new String[] {};
	}

	/**
	 * @return Returns the predefinito.
	 */
	public Boolean getPredefinito() {
		return predefinito;
	}

	/**
	 * @return Returns the preview.
	 */
	public Boolean getPreview() {
		return preview;
	}

	/**
	 * @return Returns the reportName.
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @return Returns the soloTesto.
	 */
	public Boolean getSoloTesto() {
		return soloTesto;
	}

	/**
	 * @return Returns the stampante.
	 */
	public String getStampante() {
		return stampante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((reportName == null) ? 0 : reportName.hashCode());
		return result;
	}

	/**
	 * @return the interno
	 */
	public boolean isInterno() {
		return interno;
	}

	/**
	 * @return the mailLayout
	 */
	public boolean isMailLayout() {
		return mailLayout;
	}

	/**
	 * @param batch
	 *            The batch to set.
	 */
	public void setBatch(Boolean batch) {
		batch = ObjectUtils.defaultIfNull(batch, false);
		this.batch = batch;
	}

	/**
	 * @param confermaNumeroCopie
	 *            The confermaNumeroCopie to set.
	 */
	public void setConfermaNumeroCopie(Boolean confermaNumeroCopie) {
		confermaNumeroCopie = ObjectUtils.defaultIfNull(confermaNumeroCopie, false);
		this.confermaNumeroCopie = confermaNumeroCopie;
	}

	/**
	 * @param formulaNumeroCopie
	 *            The formulaNumeroCopie to set.
	 */
	public void setFormulaNumeroCopie(String formulaNumeroCopie) {
		this.formulaNumeroCopie = formulaNumeroCopie;
	}

	/**
	 * @param interno
	 *            the interno to set
	 */
	public void setInterno(boolean interno) {
		this.interno = interno;
	}

	/**
	 * @param mailLayout
	 *            the mailLayout to set
	 */
	public void setMailLayout(boolean mailLayout) {
		this.mailLayout = mailLayout;
	}

	/**
	 * @param predefinito
	 *            The predefinito to set.
	 */
	public void setPredefinito(Boolean predefinito) {
		predefinito = ObjectUtils.defaultIfNull(predefinito, false);
		this.predefinito = predefinito;
	}

	/**
	 * @param preview
	 *            The preview to set.
	 */
	public void setPreview(Boolean preview) {
		preview = ObjectUtils.defaultIfNull(preview, false);
		this.preview = preview;
	}

	/**
	 * @param reportName
	 *            The reportName to set.
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @param soloTesto
	 *            The soloTesto to set.
	 */
	public void setSoloTesto(Boolean soloTesto) {
		soloTesto = ObjectUtils.defaultIfNull(soloTesto, false);
		this.soloTesto = soloTesto;
	}

	/**
	 * @param stampante
	 *            The stampante to set.
	 */
	public void setStampante(String stampante) {
		this.stampante = stampante;
	}

}
