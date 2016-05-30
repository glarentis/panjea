/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Parametri ricerca per il calcolo di un bilancio a confronto.
 *
 * @author Leonardo
 */
@Entity
@Table(name = "para_ricerca_bilancio_confronto")
public class ParametriRicercaBilancioConfronto extends ParametriRicercaBilancio {

	private static final long serialVersionUID = -8709564812816188294L;

	// dati per il periodo a confronto risetto al base
	private Integer annoCompetenza2 = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale2")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale2")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo2")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull2")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni2")) })
	private Periodo dataRegistrazione2 = null;

	/**
	 *
	 */
	public ParametriRicercaBilancioConfronto() {
		super();
		this.dataRegistrazione2 = new Periodo();
	}

	/**
	 * @return the annoCompetenzaConfronto
	 */
	public Integer getAnnoCompetenza2() {
		return annoCompetenza2;
	}

	/**
	 * @return the dataRegistrazione2
	 */
	public Periodo getDataRegistrazione2() {
		return dataRegistrazione2;
	}

	/**
	 * Restituisce i parametri principali formattati in html.
	 *
	 * @return parametri in formato html da stampare nel report.
	 */
	@Override
	public String getHtmlParameters() {
		Format format = new SimpleDateFormat("dd/MM/yyyy");
		String inizio = getDataRegistrazione().getDataIniziale() != null ? format.format(getDataRegistrazione()
				.getDataIniziale()) : "";
		String fine = getDataRegistrazione().getDataFinale() != null ? format.format(getDataRegistrazione()
				.getDataFinale()) : "";
		String inizio2 = getDataRegistrazione2().getDataIniziale() != null ? format.format(getDataRegistrazione2()
				.getDataIniziale()) : "";
		String fine2 = getDataRegistrazione2().getDataFinale() != null ? format.format(getDataRegistrazione2()
				.getDataFinale()) : "";
		String separator = (getDataRegistrazione().getDataIniziale() != null && getDataRegistrazione().getDataFinale() != null) ? " - "
				: "";
		String separator2 = (getDataRegistrazione2().getDataIniziale() != null && getDataRegistrazione2()
				.getDataFinale() != null) ? " - " : "";

		StringBuffer sb = new StringBuffer();
		sb.append("<html><b>Periodo 1:</b> ");
		sb.append(inizio);
		sb.append(separator);
		sb.append(fine);
		sb.append("<br><b>Periodo 2:</b> ");
		sb.append(inizio2);
		sb.append(separator2);
		sb.append(fine2);
		sb.append("<br><b>Stato:</b>");
		sb.append(getStatiString());
		if (getCentroCosto() != null && getCentroCosto().getId() != null) {
			sb.append("<br><b>Centro di costo</b>");
			sb.append(" - ");
			sb.append(getCentroCosto().getCodice());
			sb.append(" - ");
			sb.append(getCentroCosto().getDescrizione());
		}
		return sb.toString();
	}

	/**
	 *
	 * @return Parametri per il calcolo del bilancio per il primo periodo
	 */
	public ParametriRicercaBilancio getParametriRicercaBilancio1() {
		ParametriRicercaBilancio parametriRicercaBilancio = new ParametriRicercaBilancio();
		parametriRicercaBilancio.getDataRegistrazione().setDataFinale(getDataRegistrazione().getDataFinale());
		parametriRicercaBilancio.setAnnoCompetenza(getAnnoCompetenza());
		parametriRicercaBilancio.getDataRegistrazione().setDataIniziale(getDataRegistrazione().getDataIniziale());
		parametriRicercaBilancio.setStampaClienti(getStampaClienti());
		parametriRicercaBilancio.setStampaFornitori(getStampaFornitori());
		parametriRicercaBilancio.setStampaConto(getStampaConto());
		parametriRicercaBilancio.setStampaCentriCosto(getStampaCentriCosto());
		parametriRicercaBilancio.setStampaSottoConto(getStampaSottoConto());
		parametriRicercaBilancio.setStatiAreaContabile(getStatiAreaContabile());
		parametriRicercaBilancio.setCentroCosto(getCentroCosto());
		parametriRicercaBilancio.setVisualizzaSaldiCFAzero(true);
		return parametriRicercaBilancio;
	}

	/**
	 *
	 * @return Parametri per il calcolo del bilancio per il secondo periodo
	 */
	public ParametriRicercaBilancio getParametriRicercaBilancio2() {
		ParametriRicercaBilancio parametriRicercaBilancio = new ParametriRicercaBilancio();
		parametriRicercaBilancio.getDataRegistrazione().setDataFinale(getDataRegistrazione2().getDataFinale());
		parametriRicercaBilancio.setAnnoCompetenza(getAnnoCompetenza2());
		parametriRicercaBilancio.getDataRegistrazione().setDataIniziale(getDataRegistrazione2().getDataIniziale());
		parametriRicercaBilancio.setStampaClienti(getStampaClienti());
		parametriRicercaBilancio.setStampaFornitori(getStampaFornitori());
		parametriRicercaBilancio.setStampaConto(getStampaConto());
		parametriRicercaBilancio.setStampaCentriCosto(getStampaCentriCosto());
		parametriRicercaBilancio.setStampaSottoConto(getStampaSottoConto());
		parametriRicercaBilancio.setStatiAreaContabile(getStatiAreaContabile());
		parametriRicercaBilancio.setCentroCosto(getCentroCosto());
		parametriRicercaBilancio.setVisualizzaSaldiCFAzero(true);
		return parametriRicercaBilancio;
	}

	/**
	 *
	 * @return stringa con la descrizione degli stati
	 */
	private String getStatiString() {
		String stati = new String();
		if (getStatiAreaContabile() != null) {
			for (StatoAreaContabile statoAreaContabile : getStatiAreaContabile()) {
				stati = stati + statoAreaContabile.name().substring(0, 1) + ",";
			}
			stati = stati.substring(0, stati.length() - 1);
		}
		return stati;
	}

	/**
	 * @param annoCompetenzaConfronto
	 *            the annoCompetenzaConfronto to set
	 */
	public void setAnnoCompetenza2(Integer annoCompetenzaConfronto) {
		this.annoCompetenza2 = annoCompetenzaConfronto;
	}

	/**
	 * @param dataRegistrazione2
	 *            the dataRegistrazione2 to set
	 */
	public void setDataRegistrazione2(Periodo dataRegistrazione2) {
		this.dataRegistrazione2 = dataRegistrazione2;
	}

}
