package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Parametri per recuperare la SituazioneEP.
 * 
 * @author giangi
 */
@Entity
@Table(name = "para_ricerca_situazione_ep")
public class ParametriRicercaSituazioneEP extends AbstractParametriRicerca implements Serializable {

	private static final long serialVersionUID = -3984368131494055207L;

	@ManyToOne(optional = true)
	private CentroCosto centroCosto = null;

	private Integer annoCompetenza;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni")) })
	private Periodo dataRegistrazione = null;

	@CollectionOfElements(targetElement = StatoAreaContabile.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_situazione_ep_stati", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "stato", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<AreaContabile.StatoAreaContabile> statiAreaContabile = null;

	private Boolean stampaConto;
	private Boolean stampaSottoConto;
	private Boolean stampaClienti;
	private Boolean stampaFornitori;
	private Boolean stampaCentriCosto;

	{
		if (statiAreaContabile == null) {
			statiAreaContabile = new HashSet<AreaContabile.StatoAreaContabile>();
		}
		stampaConto = false;
		stampaSottoConto = false;
		stampaClienti = false;
		stampaFornitori = false;
		stampaCentriCosto = false;
		dataRegistrazione = new Periodo();
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaSituazioneEP() {
		super();
	}

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return the centroCosto
	 */
	public CentroCosto getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Periodo getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * Restituisce i parametri principali formattati in html per stampare su report.
	 * 
	 * @return parametri
	 */
	public String getHtmlParameters() {
		Format format = new SimpleDateFormat("dd/MM/yyyy");
		String inizio = getDataRegistrazione().getDataIniziale() != null ? format.format(getDataRegistrazione()
				.getDataIniziale()) : "";
		String fine = getDataRegistrazione().getDataFinale() != null ? format.format(getDataRegistrazione()
				.getDataFinale()) : "";
		String separator = (getDataRegistrazione().getDataIniziale() != null && getDataRegistrazione().getDataFinale() != null) ? " - "
				: "";
		StringBuffer sb = new StringBuffer();
		sb.append("<html><b>Periodo:</b> ");
		sb.append(inizio);
		sb.append(separator);
		sb.append(fine);
		sb.append("<br><b>Stato:</b>");
		sb.append(getStatiString());
		if (centroCosto != null && centroCosto.getId() != null) {
			sb.append("<br><b>Centro di costo</b>");
			sb.append(" - ");
			sb.append(centroCosto.getCodice());
			sb.append(" - ");
			sb.append(centroCosto.getDescrizione());
		}
		return sb.toString();
	}

	/**
	 * @return the stampaCentriCosto
	 */
	public Boolean getStampaCentriCosto() {
		return stampaCentriCosto;
	}

	/**
	 * @return stampaClienti
	 */
	public Boolean getStampaClienti() {
		return stampaClienti;
	}

	/**
	 * @return stampaConto
	 */
	public Boolean getStampaConto() {
		return stampaConto;
	}

	/**
	 * @return stampaFornitori
	 */
	public Boolean getStampaFornitori() {
		return stampaFornitori;
	}

	/**
	 * @return stampaSottoConto
	 */
	public Boolean getStampaSottoConto() {
		return stampaSottoConto;
	}

	/**
	 * @return statiAreaContabile
	 */
	public Set<AreaContabile.StatoAreaContabile> getStatiAreaContabile() {
		return statiAreaContabile;
	}

	/**
	 * 
	 * @return stati
	 */
	private String getStatiString() {
		String stati = new String();
		if (statiAreaContabile != null) {
			for (StatoAreaContabile statoAreaContabile : statiAreaContabile) {
				stati = stati + statoAreaContabile.name().substring(0, 1) + ",";
			}
			stati = stati.substring(0, stati.length() - 1);
		}
		return stati;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnnoCompetenza(Integer anno) {
		this.annoCompetenza = anno;
	}

	/**
	 * @param centroCosto
	 *            the centroCosto to set
	 */
	public void setCentroCosto(CentroCosto centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Periodo dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param stampaCentriCosto
	 *            the stampaCentriCosto to set
	 */
	public void setStampaCentriCosto(Boolean stampaCentriCosto) {
		this.stampaCentriCosto = stampaCentriCosto;
	}

	/**
	 * @param stampaClienti
	 *            the stampaClienti to set
	 */
	public void setStampaClienti(Boolean stampaClienti) {
		this.stampaClienti = stampaClienti;
	}

	/**
	 * @param stampaConto
	 *            the stampaConto to set
	 */
	public void setStampaConto(Boolean stampaConto) {
		this.stampaConto = stampaConto;
	}

	/**
	 * @param stampaFornitori
	 *            the stampaFornitori to set
	 */
	public void setStampaFornitori(Boolean stampaFornitori) {
		this.stampaFornitori = stampaFornitori;
	}

	/**
	 * @param stampaSottoConto
	 *            the stampaSottoConto to set
	 */
	public void setStampaSottoConto(Boolean stampaSottoConto) {
		this.stampaSottoConto = stampaSottoConto;
	}

	/**
	 * @param statiAreaContabile
	 *            the statiAreaContabile to set
	 */
	public void setStatiAreaContabile(Set<AreaContabile.StatoAreaContabile> statiAreaContabile) {
		this.statiAreaContabile = statiAreaContabile;
	}

	@Override
	public String toString() {
		return statiAreaContabile.toString();
	}

}
