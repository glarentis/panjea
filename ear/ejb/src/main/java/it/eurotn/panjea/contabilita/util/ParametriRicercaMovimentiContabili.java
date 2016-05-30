/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author adriano
 * @version 1.0, 20/giu/07
 */
@Entity
@Table(name = "para_ricerca_area_contabile")
public class ParametriRicercaMovimentiContabili extends AbstractParametriRicerca implements ITableHeader {

	private static final long serialVersionUID = -8514414199102847082L;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "codice", column = @Column(name = "daNumeroDocumento", length = 30)),
		@AttributeOverride(name = "codiceOrder", column = @Column(name = "daNumeroDocumentoOrder", length = 60)) })
	private CodiceDocumento daNumeroDocumento = null;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "codice", column = @Column(name = "aNumeroDocumento", length = 30)),
		@AttributeOverride(name = "codiceOrder", column = @Column(name = "aNumeroDocumentoOrder", length = 60)) })
	private CodiceDocumento aNumeroDocumento = null;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataDocumentoIniziale")),
		@AttributeOverride(name = "dataFinale", column = @Column(name = "dataDocumentoFinale")),
		@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataDocumentoTipoPeriodo")),
		@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataDocumentoDataInizialeNull")),
		@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataDocumentoNumeroGiorni")) })
	private Periodo dataDocumento;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale")),
		@AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale")),
		@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo")),
		@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull")),
		@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni")) })
	private Periodo dataRegistrazione;

	@Column(length = 10)
	private String annoCompetenza = null;

	private BigDecimal totale = null;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoDocumento> tipiDocumento = null;

	@CollectionOfElements(targetElement = StatoAreaContabile.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_area_contabile_stati", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "stato", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<AreaContabile.StatoAreaContabile> statiAreaContabile = null;

	private Boolean escludiMovimentiStampati = null;

	/**
	 * parametro utilizzato per la ricerca dei movimenti contabili partendo dalle righe iva per la stampa registri iva.
	 */
	@ManyToOne(optional = true)
	private RegistroIva registroIva = null;

	/**
	 * valore di riferimento del numero di tipi documento totali per verificare se stampare tutti i tipi documento nel
	 * metodo getHtmlParameters().<br>
	 */
	private int numeroTotaleTipiDocumento;

	private boolean filtraAbilitatiStampaGiornale = false;

	private boolean filtraAbilitatiStampaRegistroIva = false;

	{
		if (tipiDocumento == null) {
			this.tipiDocumento = new HashSet<TipoDocumento>();
		}
		if (statiAreaContabile == null) {
			this.statiAreaContabile = new HashSet<AreaContabile.StatoAreaContabile>();
		}
		this.escludiMovimentiStampati = Boolean.TRUE;
		this.annoCompetenza = "";
		this.dataDocumento = new Periodo();
		this.dataRegistrazione = new Periodo();

		this.aNumeroDocumento = new CodiceDocumento();
		this.daNumeroDocumento = new CodiceDocumento();
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaMovimentiContabili() {
	}

	/**
	 * @return the annoCompetenza
	 */
	public String getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return the aNumeroDocumento
	 */
	public CodiceDocumento getANumeroDocumento() {
		if (aNumeroDocumento == null) {
			return new CodiceDocumento();
		}
		return aNumeroDocumento;
	}

	/**
	 * @return the daNumeroDocumento
	 */
	public CodiceDocumento getDaNumeroDocumento() {
		if (daNumeroDocumento == null) {
			return new CodiceDocumento();
		}
		return daNumeroDocumento;
	}

	/**
	 * @return the dataDocumento
	 */
	public Periodo getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Periodo getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return Returns the movimentiStampati.
	 */
	public Boolean getEscludiMovimentiStampati() {
		return escludiMovimentiStampati;
	}

	@Override
	public List<TableHeaderObject> getHeaderValues() {

		List<TableHeaderObject> values = new ArrayList<TableHeaderObject>();

		if (annoCompetenza != null && !annoCompetenza.isEmpty()) {
			values.add(new TableHeaderObject("annoCompetenza", annoCompetenza));
		}
		// if (daDataRegistrazione != null) {
		// values.add(new TableHeaderObject("daDataRegistrazione", daDataRegistrazione));
		// }
		// if (aDataRegistrazione != null) {
		// values.add(new TableHeaderObject("aDataRegistrazione", aDataRegistrazione));
		// }

		if (values.isEmpty()) {
			return null;
		} else {
			return values;
		}
	}

	/**
	 * Restituisce i parametri principali formattati in html per stampare su report.<br>
	 * Posso non impostare le date.
	 *
	 * @return String
	 */
	public String getHtmlParameters() {
		Format format = new SimpleDateFormat("dd/MM/yyyy");
		String inizio = getDataRegistrazione().getDataIniziale() != null ? format.format(getDataRegistrazione()
				.getDataIniziale()) : "";
		String fine = getDataRegistrazione().getDataFinale() != null ? format.format(getDataRegistrazione()
				.getDataFinale()) : "";
		String separator = (getDataRegistrazione().getDataIniziale() != null && getDataRegistrazione().getDataFinale() != null) ? " - "
				: "";
		return "<html><b>Periodo:</b> " + inizio + separator + fine + "<br><b>Stato:</b> " + getStatiString()
				+ "<br><b>Tipo documento:</b>" + getTipiDocumentoString() + "</html>";
	}

	/**
	 * @return the numeroTotaleTipiDocumento
	 */
	public int getNumeroTotaleTipiDocumento() {
		return numeroTotaleTipiDocumento;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return List<AreaContabile.StatoAreaContabile>
	 */
	public Set<AreaContabile.StatoAreaContabile> getStatiAreaContabile() {
		return statiAreaContabile;
	}

	/**
	 * @return gli stati selezionati sotto forma di string formattata
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
	 * @return Returns the tipiDocumento.
	 */
	public Set<TipoDocumento> getTipiDocumento() {
		return tipiDocumento;
	}

	/**
	 * @return i tipi documento selezionati sotto forma di string formattata
	 */
	private String getTipiDocumentoString() {
		String tipiDoc = new String();
		if (tipiDocumento != null) {
			if (this.numeroTotaleTipiDocumento != tipiDocumento.size() && !tipiDocumento.isEmpty()) {
				for (TipoDocumento tipoDocumento : tipiDocumento) {
					tipiDoc = tipiDoc + tipoDocumento.getCodice() + ",";
				}
				tipiDoc = tipiDoc.substring(0, tipiDoc.length() - 1);
			} else {
				tipiDoc = "Tutti";
			}
		}
		return tipiDoc;
	}

	/**
	 * @return the totale
	 */
	public BigDecimal getTotale() {
		return totale;
	}

	/**
	 * @return filtraAbilitatiStampaGiornale
	 */
	public boolean isFiltraAbilitatiStampaGiornale() {
		return filtraAbilitatiStampaGiornale;
	}

	/**
	 * @return filtraAbilitatiStampaRegistroIva
	 */
	public boolean isFiltraAbilitatiStampaRegistroIva() {
		return filtraAbilitatiStampaRegistroIva;
	}

	/**
	 * @param annoCompetenza
	 *            the annoCompetenza to set
	 */
	public void setAnnoCompetenza(String annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	/**
	 * @param aNumeroDocumento
	 *            the aNumeroDocumento to set
	 */
	public void setaNumeroDocumento(CodiceDocumento aNumeroDocumento) {
		this.aNumeroDocumento = aNumeroDocumento;
	}

	/**
	 * @param daNumeroDocumento
	 *            the daNumeroDocumento to set
	 */
	public void setDaNumeroDocumento(CodiceDocumento daNumeroDocumento) {
		this.daNumeroDocumento = daNumeroDocumento;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Periodo dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Periodo dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param escludiMovimentiStampati
	 *            The movimentiStampati to set.
	 */
	public void setEscludiMovimentiStampati(Boolean escludiMovimentiStampati) {
		this.escludiMovimentiStampati = escludiMovimentiStampati;
	}

	/**
	 * @param filtraAbilitatiStampaGiornale
	 *            The filtraAbilitatiStampaGiornale to set.
	 */
	public void setFiltraAbilitatiStampaGiornale(boolean filtraAbilitatiStampaGiornale) {
		this.filtraAbilitatiStampaGiornale = filtraAbilitatiStampaGiornale;
	}

	/**
	 * @param filtraAbilitatiStampaRegistroIva
	 *            The filtraAbilitatiStampaRegistroIva to set.
	 */
	public void setFiltraAbilitatiStampaRegistroIva(boolean filtraAbilitatiStampaRegistroIva) {
		this.filtraAbilitatiStampaRegistroIva = filtraAbilitatiStampaRegistroIva;
	}

	/**
	 * @param numeroTotaleTipiDocumento
	 *            the numeroTotaleTipiDocumento to set
	 */
	public void setNumeroTotaleTipiDocumento(int numeroTotaleTipiDocumento) {
		this.numeroTotaleTipiDocumento = numeroTotaleTipiDocumento;
	}

	/**
	 * @param registroIva
	 *            the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @param statiAreaContabile
	 *            the statiAreaContabile to set
	 */
	public void setStatiAreaContabile(Set<AreaContabile.StatoAreaContabile> statiAreaContabile) {
		this.statiAreaContabile = statiAreaContabile;
	}

	/**
	 * @param tipiDocumento
	 *            The tipiDocumento to set.
	 */
	public void setTipiDocumento(Set<TipoDocumento> tipiDocumento) {
		this.tipiDocumento = tipiDocumento;
	}

	/**
	 * @param totale
	 *            the totale to set
	 */
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ParametriRicercaMovimentiContabili[");
		buffer.append(" dataDocumento = ").append(dataDocumento);
		buffer.append(" dataRegistrazione = ").append(dataRegistrazione);
		buffer.append(" annoCompetenza = ").append(annoCompetenza);
		buffer.append(" escludiMovimentiStampati = ").append(escludiMovimentiStampati);
		buffer.append(" tipiDocumento = ").append(tipiDocumento);
		buffer.append(" totale = ").append(totale);
		buffer.append("]");
		return buffer.toString();
	}
}
