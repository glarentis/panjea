package it.eurotn.panjea.ordini.util.parametriricerca;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
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
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author fattazzo
 */
@Entity
@Table(name = "para_ricerca_area_ordine")
public class ParametriRicercaAreaOrdine extends AbstractParametriRicerca implements ITableHeader {

	/**
	 * @author fattazzo
	 */
	public enum STATO_ORDINE {
		TUTTI, EVASO, NON_EVASO
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

	@Transient
	private String numeroDocumento = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "codice", column = @Column(name = "numeroDocumentoFinale", length = 30)),
			@AttributeOverride(name = "codiceOrder", column = @Column(name = "numeroDocumentoFinaleOrder", length = 60)) })
	private CodiceDocumento numeroDocumentoFinale = null;

	private String numeroOrdineCliente = "";

	@ManyToOne(optional = true)
	private Utente utente = null;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoAreaOrdine> tipiAreaOrdine = null;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	@ManyToOne(optional = true)
	private AgenteLite agente;

	@Transient
	private List<Deposito> depositiSorgente;

	private Integer annoCompetenza;

	@CollectionOfElements(targetElement = StatoAreaOrdine.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_area_ordine_stati_area", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "statoArea", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<AreaOrdine.StatoAreaOrdine> statiAreaOrdine = null;

	private STATO_ORDINE statoOrdine;

	{
		if (tipiAreaOrdine == null) {
			tipiAreaOrdine = new HashSet<TipoAreaOrdine>();
		}
		if (statiAreaOrdine == null) {
			statiAreaOrdine = new HashSet<AreaOrdine.StatoAreaOrdine>();
		}

		numeroDocumentoIniziale = new CodiceDocumento();
		numeroDocumentoFinale = new CodiceDocumento();
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAreaOrdine() {
		super();
	}

	/**
	 * @return Returns the agente.
	 */
	public AgenteLite getAgente() {
		return agente;
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
	 * @return Returns the depositiSorgente.
	 */
	public List<Deposito> getDepositiSorgente() {
		return depositiSorgente;
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

		if (numeroDocumentoIniziale != null && !numeroDocumentoIniziale.isEmpty()) {
			values.add(new TableHeaderObject("numeroDocumentoIniziale", numeroDocumentoIniziale.getCodice()));
		}
		if (numeroDocumentoFinale != null && !numeroDocumentoFinale.isEmpty()) {
			values.add(new TableHeaderObject("numeroDocumentoFinale", numeroDocumentoFinale.getCodice()));
		}
		if (entita != null && entita.getId() != null) {
			values.add(new TableHeaderObject("entita", entita));
		}
		if (agente != null && agente.getId() != null) {
			values.add(new TableHeaderObject("agente", agente));
		}
		if (utente != null && utente.getId() != null) {
			values.add(new TableHeaderObject("utente", utente));
		}
		if (statiAreaOrdine != null && !statiAreaOrdine.isEmpty()
				&& statiAreaOrdine.size() != StatoAreaOrdine.values().length) {
			values.add(new TableHeaderObject("statiAreaOrdine", statiAreaOrdine));
		}
		values.add(new TableHeaderObject("statoOrdine", statoOrdine));

		if (tipiAreaOrdine != null && !tipiAreaOrdine.isEmpty()) {
			values.add(new TableHeaderObject("tipiDocumento", tipiAreaOrdine));
		}

		if (values.isEmpty()) {
			return null;
		} else {
			return values;
		}
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
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
	 * @return Returns the numeroOrdineCliente.
	 */
	public String getNumeroOrdineCliente() {
		return numeroOrdineCliente;
	}

	/**
	 * @return the statiAreaOrdine
	 */
	public Set<AreaOrdine.StatoAreaOrdine> getStatiAreaOrdine() {
		return statiAreaOrdine;
	}

	/**
	 * @return Returns the statoOrdine.
	 */
	public STATO_ORDINE getStatoOrdine() {
		return statoOrdine;
	}

	/**
	 * @return the tipiAreaOrdine
	 */
	public Set<TipoAreaOrdine> getTipiAreaOrdine() {
		return tipiAreaOrdine;
	}

	/**
	 * @return Returns the utente.
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * @param agente
	 *            The agente to set.
	 */
	public void setAgente(AgenteLite agente) {
		this.agente = agente;
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
	 * @param depositiSorgente
	 *            The depositiSorgente to set.
	 */
	public void setDepositiSorgente(List<Deposito> depositiSorgente) {
		this.depositiSorgente = depositiSorgente;
	}

	/**
	 * @param entita
	 *            The entita to set.
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
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
	 * @param numeroOrdineCliente
	 *            The numeroOrdineCliente to set.
	 */
	public void setNumeroOrdineCliente(String numeroOrdineCliente) {
		this.numeroOrdineCliente = numeroOrdineCliente;
	}

	/**
	 * @param statiAreaOrdine
	 *            the statiAreaOrdine to set
	 */
	public void setStatiAreaOrdine(Set<AreaOrdine.StatoAreaOrdine> statiAreaOrdine) {
		this.statiAreaOrdine = statiAreaOrdine;
	}

	/**
	 * @param statoOrdine
	 *            The statoOrdine to set.
	 */
	public void setStatoOrdine(STATO_ORDINE statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	/**
	 * @param tipiAreaOrdine
	 *            the tipiAreaOrdine to set
	 */
	public void setTipiAreaOrdine(Set<TipoAreaOrdine> tipiAreaOrdine) {
		this.tipiAreaOrdine = tipiAreaOrdine;
	}

	/**
	 * @param utente
	 *            The utente to set.
	 */
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
}