package it.eurotn.panjea.tesoreria.solleciti;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "part_template_soleciti")
@NamedQueries({ @NamedQuery(name = "TemplateSolleciti.caricaAll", query = "from TemplateSolleciti t where t.codiceAzienda = :codiceAzienda  order by descrizione", hints = {
		@QueryHint(name = "org.hibernate.cacheable", value = "true"),
		@QueryHint(name = "org.hibernate.cacheRegion", value = "templateSolleciti") }) })
public class TemplateSolleciti extends EntityBase {
	private static final Set<TemplatePlaceHolder> PLACE_HOLDERS = new HashSet<TemplatePlaceHolder>();
	private static final long serialVersionUID = -2405671973172442992L;

	/**
	 * @return set non modificabili deli placeholder da poter utilizzare nei template.
	 */
	public static Set<TemplatePlaceHolder> getTemplatePlaceHolders() {
		return Collections.unmodifiableSet(PLACE_HOLDERS);
	}

	{

		PLACE_HOLDERS.add(new TemplatePlaceHolder("Sollecito", "Nota", "nota"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Sollecito", "Residuo", "importo.importoInValutaAzienda"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Sollecito", "ValutaImporto", "importo.codiceValuta"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Sollecito", "DataCreazione", "dataCreazione"));

		PLACE_HOLDERS.add(new TemplatePlaceHolder("Cliente", "denominazione",
				"righeSollecito.rata.areaRate.documento.entita.anagrafica.denominazione"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Cliente", "indirizzo",
				"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.indirizzo"));
		PLACE_HOLDERS
				.add(new TemplatePlaceHolder("Cliente", "cap",
						"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.datiGeografici.descrizioneCap"));
		PLACE_HOLDERS
				.add(new TemplatePlaceHolder("Cliente", "localita",
						"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.datiGeografici.descrizioneLocalita"));
		PLACE_HOLDERS
				.add(new TemplatePlaceHolder(
						"Cliente",
						"provincia",
						"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.datiGeografici.descrizioneLivelloAmministrativo2"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Cliente", "numeroCivico",
				"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.numeroCivico"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Cliente", "telefono",
				"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.telefono"));
		PLACE_HOLDERS.add(new TemplatePlaceHolder("Cliente", "fax",
				"righeSollecito.rata.areaRate.documento.entita.anagrafica.sedeAnagrafica.fax"));
	}

	public static final String REPORT_PATH = "Tesoreria/Solleciti";

	@Column(length = 30, nullable = true)
	private String descrizione;

	@Lob
	private String testo;

	@Lob
	private String testoFooter;

	@Column(length = 120)
	private String oggettoMail;

	@Lob
	private String testoMail;

	@Index(name = "azienda")
	private java.lang.String codiceAzienda;

	private String reportName;

	private String reportNameMail;

	/**
	 * costrutore.
	 */
	public TemplateSolleciti() {

	}

	/**
	 * Dal testo del template sostituisco i placeholder con i valori nell'oggetto rata.
	 * 
	 * @param rata
	 *            rata per la quale devo creare il testo del sollecito.<br/>
	 *            I valori verranno presi da questa rata .
	 * @return testo del sollecito.
	 */
	public String creaTestoSollecito(Rata rata) {
		String result = descrizione;
		for (TemplatePlaceHolder placeHolder : PLACE_HOLDERS) {
			result = result.replaceAll(placeHolder.getCodice(), placeHolder.getValue(rata));
		}
		return result;
	}

	/**
	 * Formatta il testo html.
	 * 
	 * @param text
	 *            testo
	 * @return testo formattato
	 */
	private String formatText(String text) {
		String testoTmp = text;
		if (testoTmp == null) {
			testoTmp = "";
		}
		testoTmp = testoTmp.replaceAll("\\n      ", "");
		testoTmp = testoTmp.replaceAll("\\n    ", "");
		testoTmp = testoTmp.replaceAll("\\n  ", "");
		// cambio i tab in 7 spaz
		testoTmp = testoTmp.replaceAll("\\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		// converto gli spazi che rimangono
		testoTmp = testoTmp.replaceAll(" ", "&nbsp;");
		return testoTmp;
	}

	/**
	 * @return the codiceAzienda
	 */
	public java.lang.String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the oggettoMail
	 */
	public String getOggettoMail() {
		return oggettoMail;
	}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @return the reportNameMail
	 */
	public String getReportNameMail() {
		return reportNameMail;
	}

	/**
	 * @return the testo
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * @return the testoFooter
	 */
	public String getTestoFooter() {
		return testoFooter;
	}

	/**
	 * @return the testoMail
	 */
	public String getTestoMail() {
		return testoMail;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(java.lang.String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param oggettoMail
	 *            the oggettoMail to set
	 */
	public void setOggettoMail(String oggettoMail) {
		this.oggettoMail = oggettoMail;
	}

	/**
	 * @param reportName
	 *            the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @param reportNameMail
	 *            the reportNameMail to set
	 */
	public void setReportNameMail(String reportNameMail) {
		this.reportNameMail = reportNameMail;
	}

	/**
	 * @param testo
	 *            the testo to set
	 */
	public void setTesto(String testo) {
		this.testo = formatText(testo);
	}

	/**
	 * @param testoFooter
	 *            the testoFooter to set
	 */
	public void setTestoFooter(String testoFooter) {
		this.testoFooter = formatText(testoFooter);
	}

	/**
	 * @param testoMail
	 *            the testoMail to set
	 */
	public void setTestoMail(String testoMail) {
		this.testoMail = testoMail;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TemplateSoleciti[");
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
