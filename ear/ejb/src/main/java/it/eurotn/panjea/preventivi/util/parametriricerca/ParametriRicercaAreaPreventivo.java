package it.eurotn.panjea.preventivi.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.sicurezza.domain.Utente;

import java.util.ArrayList;
import java.util.Date;
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

@Entity
@Table(name = "para_ricerca_area_preventivo")
public class ParametriRicercaAreaPreventivo extends AbstractParametriRicerca implements ITableHeader {

	public enum STATO_PREVENTIVO {
		TUTTI, NON_PROCESSATO, PROCESSATO
	}

	private String codiceAzienda;

	private static final long serialVersionUID = 1L;

	private Long dataCreazioneTimeStamp;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataDocumentoIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataDocumentoFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataDocumentoTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataDocumentoDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataDocumentoNumeroGiorni")) })
	private Periodo dataDocumento;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "codice", column = @Column(name = "numeroDocumentoIniziale", length = 30)),
			@AttributeOverride(name = "codiceOrder", column = @Column(name = "numeroDocumentoInizialeOrder", length = 60)) })
	private CodiceDocumento numeroDocumentoIniziale = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "codice", column = @Column(name = "numeroDocumentoFinale", length = 30)),
			@AttributeOverride(name = "codiceOrder", column = @Column(name = "numeroDocumentoFinaleOrder", length = 60)) })
	private CodiceDocumento numeroDocumentoFinale = null;

	@ManyToOne(optional = true)
	private Utente utente;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoAreaPreventivo> tipiAreaPreventivo;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	private Integer annoCompetenza;

	@CollectionOfElements(targetElement = StatoAreaPreventivo.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_area_preventivo_stati_area", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "statoArea", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<AreaPreventivo.StatoAreaPreventivo> statiAreaPreventivo;

	private STATO_PREVENTIVO statoPreventivo;

	{
		this.tipiAreaPreventivo = new HashSet<TipoAreaPreventivo>();
		this.numeroDocumentoIniziale = new CodiceDocumento();
		this.numeroDocumentoFinale = new CodiceDocumento();
		this.setStatiAreaPreventivo(new HashSet<AreaPreventivo.StatoAreaPreventivo>());
		statoPreventivo = STATO_PREVENTIVO.TUTTI;
	}

	/**
	 * costruttore.
	 */
	public ParametriRicercaAreaPreventivo() {
	}

	/**
	 * @return Returns the annoCompetenza.
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
	 *
	 * @return codice utente da ricercare
	 */
	public String getCodiceUtente() {
		return utente.getNome();
	}

	/**
	 * @return Returns the dataCreazioneTimeStamp.
	 */
	public Long getDataCreazioneTimeStamp() {
		return dataCreazioneTimeStamp;
	}

	/**
	 * @return the dataDocumento
	 */
	public Periodo getDataDocumento() {
		if (dataDocumento == null) {
			dataDocumento = new Periodo();
		}
		return dataDocumento;
	}

	/**
	 *
	 * @return dataDocumento finale
	 */
	public Date getDataDocumentoFinale() {
		if (dataDocumento == null) {
			dataDocumento = new Periodo();
		}
		return dataDocumento.getDataFinale();
	}

	/**
	 *
	 * @return dataDocumento iniziale
	 */
	public Date getDataDocumentoIniziale() {
		if (dataDocumento == null) {
			dataDocumento = new Periodo();
		}
		return dataDocumento.getDataIniziale();
	}

	/**
	 * @return Returns the entita.
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	@Override
	public List<TableHeaderObject> getHeaderValues() {
		List<TableHeaderObject> values = new ArrayList<TableHeaderObject>();

		values.add(new TableHeaderObject("annoCompetenza", annoCompetenza));

		if (getDataDocumentoIniziale() != null) {
			values.add(new TableHeaderObject("dataDocumentoIniziale", getDataDocumentoIniziale()));
		}

		if (getDataDocumentoFinale() != null) {
			values.add(new TableHeaderObject("dataDocumentoFinale", getDataDocumentoFinale()));
		}

		if (!numeroDocumentoIniziale.isEmpty()) {
			values.add(new TableHeaderObject("numeroDocumentoIniziale", numeroDocumentoIniziale.getCodice()));
		}
		if (!numeroDocumentoFinale.isEmpty()) {
			values.add(new TableHeaderObject("numeroDocumentoFinale", numeroDocumentoFinale.getCodice()));
		}
		if (entita != null && entita.getId() != null) {
			values.add(new TableHeaderObject("entita", entita));
		}
		if (utente != null && utente.getId() != null) {
			values.add(new TableHeaderObject("utente", utente));
		}

		values.add(new TableHeaderObject("statoPreventivo", statoPreventivo));

		if (tipiAreaPreventivo != null && !tipiAreaPreventivo.isEmpty()) {
			values.add(new TableHeaderObject("tipiDocumento", tipiAreaPreventivo));
		}

		if (values.isEmpty()) {
			return null;
		}

		return values;
	}

	/**
	 * @return the numeroDocumentoFinale
	 */
	public CodiceDocumento getNumeroDocumentoFinale() {
		if (numeroDocumentoFinale == null) {
			return new CodiceDocumento();
		}
		return numeroDocumentoFinale;
	}

	/**
	 * @return the numeroDocumentoIniziale
	 */
	public CodiceDocumento getNumeroDocumentoIniziale() {
		if (numeroDocumentoIniziale == null) {
			return new CodiceDocumento();
		}
		return numeroDocumentoIniziale;
	}

	/**
	 * @return the statiAreaPreventivo
	 */
	public Set<AreaPreventivo.StatoAreaPreventivo> getStatiAreaPreventivo() {
		return statiAreaPreventivo;
	}

	/**
	 * @return the statoPreventivo
	 */
	public STATO_PREVENTIVO getStatoPreventivo() {
		return statoPreventivo;
	}

	/**
	 * @return the tipiAreaPreventvo
	 */
	public Set<TipoAreaPreventivo> getTipiAreaPreventivo() {
		return tipiAreaPreventivo;
	}

	/**
	 * @return Returns the utente.
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * @param annoCompetenza
	 *            The annoCompetenza to set.
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
	 * @param dataCreazioneTimeStamp
	 *            The dataCreazioneTimeStamp to set.
	 */
	public void setDataCreazioneTimeStamp(Long dataCreazioneTimeStamp) {
		this.dataCreazioneTimeStamp = dataCreazioneTimeStamp;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Periodo dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param entita
	 *            The entita to set.
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param numeroDocumentoFinale
	 *            the numeroDocumentoFinale to set
	 */
	public void setNumeroDocumentoFinale(CodiceDocumento numeroDocumentoFinale) {
		this.numeroDocumentoFinale = numeroDocumentoFinale;
	}

	/**
	 * @param numeroDocumentoIniziale
	 *            the numeroDocumentoIniziale to set
	 */
	public void setNumeroDocumentoIniziale(CodiceDocumento numeroDocumentoIniziale) {
		this.numeroDocumentoIniziale = numeroDocumentoIniziale;
	}

	/**
	 * @param statiAreaPreventivo
	 *            the statiAreaPreventivo to set
	 */
	public void setStatiAreaPreventivo(Set<AreaPreventivo.StatoAreaPreventivo> statiAreaPreventivo) {
		this.statiAreaPreventivo = statiAreaPreventivo;
	}

	/**
	 * @param statoPreventivo
	 *            the statoPreventivo to set
	 */
	public void setStatoPreventivo(STATO_PREVENTIVO statoPreventivo) {
		this.statoPreventivo = statoPreventivo;
	}

	/**
	 * @param tipiAreaPreventivo
	 *            the tipiAreaPreventivo to set
	 */
	public void setTipiAreaPreventivo(Set<TipoAreaPreventivo> tipiAreaPreventivo) {
		this.tipiAreaPreventivo = tipiAreaPreventivo;
	}

	/**
	 * @param utente
	 *            The utente to set.
	 */
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
}
