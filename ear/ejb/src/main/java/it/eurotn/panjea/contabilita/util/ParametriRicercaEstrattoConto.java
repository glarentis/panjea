/**
 * 
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Leonardo
 * @version 1.0, 28/giu/07
 */
@Entity
@Table(name = "para_ricerca_estratto_conto")
public class ParametriRicercaEstrattoConto extends AbstractParametriRicerca implements Serializable, Cloneable {

	private static final long serialVersionUID = -6340077185091597462L;

	@ManyToOne(optional = true)
	private CentroCosto centroCosto = null;

	@ManyToOne(optional = true)
	private SottoConto sottoConto = null;

	private Integer annoCompetenza = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni")) })
	private Periodo dataRegistrazione;

	@CollectionOfElements(targetElement = StatoAreaContabile.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_estratto_conto_stati", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "stato", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<AreaContabile.StatoAreaContabile> statiAreaContabile = null;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoDocumento> tipiDocumento = null;

	private static Logger logger = Logger.getLogger(ParametriRicercaEstrattoConto.class);

	/**
	 * valore di riferimento del numero di tipi documento totali per verificare se stampare tutti i tipi documento nel
	 * metodo getHtmlParameters().<br>
	 */
	private int numeroTotaleTipiDocumento;

	{
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();

		// centro costo rimane a null perchè la search object prevede null value come default e non l'oggetto vuoto
		this.dataRegistrazione = new Periodo();
		this.dataRegistrazione.setDataIniziale(today);
		if (tipiDocumento == null) {
			this.tipiDocumento = new HashSet<TipoDocumento>();
		}
		if (statiAreaContabile == null) {
			this.statiAreaContabile = new HashSet<AreaContabile.StatoAreaContabile>();
		}
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaEstrattoConto() {
	}

	@Override
	public ParametriRicercaEstrattoConto clone() {

		ParametriRicercaEstrattoConto parametriRicercaEstrattoContoCloned = null;

		try {
			parametriRicercaEstrattoContoCloned = (ParametriRicercaEstrattoConto) super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("--> Errore durante il clone della classe ParametriRicercaEstrattoConto.", e);
		}

		return parametriRicercaEstrattoContoCloned;
	}

	/**
	 * @return the annoCompetenza
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
	 * Restituisce la descrizione del conto ricercato. Se la ricerca è per centro di costo verrà restituita la
	 * descrizione del centro.
	 * 
	 * @return descrizione
	 */
	public String getDescrizioneConto() {
		String descrizioneConto = "";
		if (isRicercaConto()) {
			descrizioneConto = this.getSottoConto().getSottoContoCodice() + " - "
					+ this.getSottoConto().getDescrizione();
		}
		if (isRicercaCentroCosto()) {
			descrizioneConto = (!descrizioneConto.isEmpty() ? descrizioneConto + " / " : "")
					+ this.getCentroCosto().getCodice() + " - " + this.getCentroCosto().getDescrizione();
		}
		return descrizioneConto;
	}

	/**
	 * @return parametri principali formattati in html per stampare su report.
	 * 
	 */
	public String getHtmlParameters() {
		Format format = new SimpleDateFormat("dd/MM/yyyy");
		String inizio = getDataRegistrazione().getDataIniziale() != null ? format.format(getDataRegistrazione()
				.getDataIniziale()) : "";
		String fine = getDataRegistrazione().getDataFinale() != null ? format.format(getDataRegistrazione()
				.getDataFinale()) : "";
		String separator = (getDataRegistrazione().getDataIniziale() != null && getDataRegistrazione().getDataFinale() != null) ? " - "
				: "";
		return "<html><b>Periodo:</b> " + inizio + separator + fine + "<br><b>Stato documento:</b> " + getStatiString()
				+ "<br><b>Tipo documento:</b>" + getTipiDocumentoString() + "</html>";
	}

	/**
	 * @return the numeroTotaleTipiDocumento
	 */
	public int getNumeroTotaleTipiDocumento() {
		return numeroTotaleTipiDocumento;
	}

	/**
	 * @return Returns the sottoConto.
	 */
	public SottoConto getSottoConto() {
		return sottoConto;
	}

	/**
	 * @return the statiAreaContabile
	 */
	public Set<AreaContabile.StatoAreaContabile> getStatiAreaContabile() {
		return statiAreaContabile;
	}

	/**
	 * @return restituisce l'ordinal degli stati area contabile presenti separati da virgole per costruire la query di
	 *         ricerca
	 */
	public String getStatiAreaContabileForQuery() {
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (StatoAreaContabile statoArea : getStatiAreaContabile()) {
			sb.append(comma);
			sb.append(statoArea.ordinal());
			comma = ",";
		}
		return sb.toString();
	}

	/**
	 * @return stati in formato stringa
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
	 * @return the tipiDocumento
	 */
	public Set<TipoDocumento> getTipiDocumento() {
		return tipiDocumento;
	}

	/**
	 * @return tipi documento formattati.
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
	 * @return true se ricerco un centro di costo
	 */
	public boolean isRicercaCentroCosto() {
		return centroCosto != null && centroCosto.getId() != null;
	}

	/**
	 * @return true se ricerco un sotto conto
	 */
	public boolean isRicercaConto() {
		return sottoConto != null && sottoConto.getId() != null;
	}

	/**
	 * @param annoCompetenza
	 *            the annoCompetenza to set
	 */
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
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
	 * @param numeroTotaleTipiDocumento
	 *            numer o dei tipi documento selezionati
	 */
	public void setNumeroTotaleTipiDocumento(int numeroTotaleTipiDocumento) {
		this.numeroTotaleTipiDocumento = numeroTotaleTipiDocumento;
	}

	/**
	 * @param sottoConto
	 *            The sottoConto to set.
	 */
	public void setSottoConto(SottoConto sottoConto) {
		this.sottoConto = sottoConto;
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
	 *            the tipiDocumento to set
	 */
	public void setTipiDocumento(Set<TipoDocumento> tipiDocumento) {
		this.tipiDocumento = tipiDocumento;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ParametriRicercaEstrattoConto[");
		buffer.append("periodo = ").append(dataRegistrazione);
		buffer.append(" annoCompetenza = ").append(annoCompetenza);
		buffer.append(" sottoConto = ").append(sottoConto);
		buffer.append(" statiAreaContabile = ").append(statiAreaContabile);
		buffer.append(" tipiDocumento = ").append(tipiDocumento);
		buffer.append("]");
		return buffer.toString();
	}
}
