/**
 *
 */
package it.eurotn.panjea.partite.util;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Parametri per la ricerca delle rate.<br>
 * Da tenere in considerazione:<br>
 * <ul>
 * <li>il tipo partita puo' essere solo ATTIVA o PASSIVA
 * <li>se {@link TipoPartita} =ATTIVA allora {@link EntitaLite} = {@link FornitoreLite} e {@link TipoEntita} =FORNITORE</li>
 * <li>se {@link TipoPartita} =PASSIVA allora {@link EntitaLite} = {@link ClienteLite} e {@link TipoEntita} =CLIENTE</li>
 * </ul>
 * 
 * @author Leonardo
 */
@Entity
@Table(name = "para_ricerca_rate")
public class ParametriRicercaRate extends AbstractParametriRicerca {

	private static final long serialVersionUID = 4786530519842272834L;

	/**
	 * Crea i parametri per la ricerca delle rate aperte di un' entità.
	 * 
	 * @param cliente
	 *            entità interessata. Normalmente è il cliente
	 * @return parametri impostati
	 */
	public static ParametriRicercaRate creaParametriRicercaRateAperte(EntitaLite cliente) {
		Set<StatoRata> stati = new TreeSet<StatoRata>();
		stati.add(StatoRata.APERTA);
		stati.add(StatoRata.PAGAMENTO_PARZIALE);
		stati.add(StatoRata.RIEMESSA);
		stati.add(StatoRata.IN_RIASSEGNAZIONE);
		stati.add(StatoRata.ANTICIPO_FATTURA);
		List<TipoPagamento> tipiPagamento = Arrays.asList(TipoPagamento.values());

		ParametriRicercaRate parametriRicercaRate = new ParametriRicercaRate();
		parametriRicercaRate.setTipiPagamento(new TreeSet<TipoPagamento>());
		parametriRicercaRate.getTipiPagamento().addAll(tipiPagamento);
		parametriRicercaRate.setStatiRata(stati);
		parametriRicercaRate.setEntita(cliente);
		parametriRicercaRate.setEffettuaRicerca(true);
		parametriRicercaRate.setDataScadenza(new Periodo());
		parametriRicercaRate.getDataScadenza().setTipoPeriodo(TipoPeriodo.NESSUNO);
		parametriRicercaRate.setSoloEntita(true);
		return parametriRicercaRate;
	}

	/**
	 * Crea i parametri per la ricerca delle rate aperte.
	 * 
	 * @param tipoPartita
	 *            tipoPartita per la quale cercare le rate aperte.
	 * @return parametri impostati
	 */
	public static ParametriRicercaRate creaParametriRicercaRateApertePerEntita(TipoPartita tipoPartita) {
		ParametriRicercaRate parametriRicercaRate = new ParametriRicercaRate();
		parametriRicercaRate.setTipoPartita(tipoPartita);
		Set<StatoRata> stati = new TreeSet<StatoRata>();
		stati.add(StatoRata.APERTA);
		stati.add(StatoRata.PAGAMENTO_PARZIALE);
		stati.add(StatoRata.RIEMESSA);
		stati.add(StatoRata.IN_RIASSEGNAZIONE);
		stati.add(StatoRata.IN_LAVORAZIONE);
		stati.add(StatoRata.ANTICIPO_FATTURA);
		List<TipoPagamento> tipiPagamento = Arrays.asList(TipoPagamento.values());
		parametriRicercaRate.setTipiPagamento(new TreeSet<TipoPagamento>());
		parametriRicercaRate.getTipiPagamento().addAll(tipiPagamento);
		// se il tipo partita è PASSIVO (fornitore) vado a togliere il tipo pagamento F24
		if (tipoPartita == TipoPartita.PASSIVA) {
			parametriRicercaRate.getTipiPagamento().remove(TipoPagamento.F24);
		}
		parametriRicercaRate.setStatiRata(stati);
		parametriRicercaRate.setEffettuaRicerca(true);
		parametriRicercaRate.setDataScadenza(new Periodo());
		parametriRicercaRate.getDataScadenza().setTipoPeriodo(TipoPeriodo.NESSUNO);
		parametriRicercaRate.setSoloEntita(true);
		return parametriRicercaRate;
	}

	/**
	 * 
	 * @param idEntita
	 *            id dell'entità per la quale caricare le rate da poter utilizzare per l'acconto.
	 * @param tipoPartita
	 *            tipo partita ATTIVA/PASSIVA
	 * @return parametri per la ricerca delle rate da poter utilizzare per l'acconto.
	 */
	public static ParametriRicercaRate creaParametriRicercaRateDaUtilizzarePerAcconto(Integer idEntita,
			TipoPartita tipoPartita) {

		ParametriRicercaRate parametriRicercaRate = new ParametriRicercaRate();

		Set<StatoRata> statiRata = new TreeSet<StatoRata>();
		statiRata.add(StatoRata.APERTA);
		statiRata.add(StatoRata.PAGAMENTO_PARZIALE);
		parametriRicercaRate.setStatiRata(statiRata);

		parametriRicercaRate.setTipoPartita(tipoPartita);

		EntitaLite entitaLite = new EntitaLite();
		entitaLite.setId(idEntita);
		parametriRicercaRate.setEntita(entitaLite);
		parametriRicercaRate.setImportoIniziale(BigDecimal.valueOf(0.01));
		parametriRicercaRate.setEscludiPagate(true);
		Set<TipoPagamento> tipiPagamentoDaEscludere = new TreeSet<TipoPagamento>();
		tipiPagamentoDaEscludere.add(TipoPagamento.F24);
		parametriRicercaRate.setTipiPagamentoEsclusi(tipiPagamentoDaEscludere);
		parametriRicercaRate.setTipiPagamento(null);
		return parametriRicercaRate;
	}

	private Boolean soloEntita;

	/**
	 * Servono per preimpostare dei parametri di default per la creazione delle aree chisure.
	 */
	@Transient
	private ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure;

	private String codiceValuta;

	// flag utilizzato per sapere se effettuare la ricerca o no
	private boolean effettuaRicerca = false;

	@Embedded
	private Periodo dataScadenza;

	@ManyToOne(optional = true)
	private CategoriaEntita categoriaEntita;

	private BigDecimal importoIniziale;

	private BigDecimal importoFinale;

	private TipoEntita tipoEntita;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	@ManyToOne(optional = true)
	private SedeEntita sedeEntita;

	@ManyToOne(optional = true)
	private RapportoBancarioAzienda rapportoBancarioAzienda;

	@ManyToOne(optional = true)
	private CategoriaRata categoriaRata;

	@CollectionOfElements(targetElement = StatoRata.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_rate_stati_rata", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "stato", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<Rata.StatoRata> statiRata;

	private TipoPartita tipoPartita;

	@CollectionOfElements(targetElement = TipoPagamento.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_rate_tipi_pagamento", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "tipoPagamento", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<TipoPagamento> tipiPagamento;

	@CollectionOfElements(targetElement = TipoPagamento.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "para_ricerca_rate_tipi_pagamento_esclusi", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "tipoPagamentoEscluso", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<TipoPagamento> tipiPagamentoEsclusi;

	private Boolean escludiPagate;

	private Boolean stampaDettaglio;

	@ManyToOne(optional = true)
	private Banca bancaEntita;

	@ManyToOne(optional = true)
	private AgenteLite agente;

	@ManyToOne(optional = true)
	private CodicePagamento codicePagamento;

	@ManyToOne(optional = true)
	private ZonaGeografica zonaGeografica;

	private Boolean compensazione;

	@ManyToOne(optional = true)
	private AnagraficaLite anagrafica;

	@Transient
	private List<SituazioneRata> rate;

	{
		setTipoPartita(TipoPartita.ATTIVA);
		this.statiRata = new TreeSet<StatoRata>();
		this.statiRata.add(StatoRata.APERTA);
		this.statiRata.add(StatoRata.PAGAMENTO_PARZIALE);
		this.statiRata.add(StatoRata.ANTICIPO_FATTURA);
		this.tipiPagamento = new TreeSet<TipoPagamento>();
		this.tipiPagamento.addAll(Arrays.asList(TipoPagamento.values()));
		this.escludiPagate = false;
		this.stampaDettaglio = null;
		this.compensazione = Boolean.FALSE;
		this.rate = null;
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaRate() {
		super();
	}

	/**
	 * @return Returns the agente.
	 */
	public AgenteLite getAgente() {
		return agente;
	}

	/**
	 * @return the anagrafica
	 */
	public AnagraficaLite getAnagrafica() {
		return anagrafica;
	}

	/**
	 * @return the bancaEntita
	 */
	public Banca getBancaEntita() {
		return bancaEntita;
	}

	/**
	 * @return Returns the categoriaEntita.
	 */
	public CategoriaEntita getCategoriaEntita() {
		return categoriaEntita;
	}

	/**
	 * @return the categoriaRata
	 */
	public CategoriaRata getCategoriaRata() {
		return categoriaRata;
	}

	/**
	 * @return the codicePagamento
	 */
	public CodicePagamento getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return Returns the codiceValuta.
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return the compensazione
	 */
	public Boolean getCompensazione() {
		return compensazione;
	}

	/**
	 * @return Returns the dataScadenza.
	 */
	public Periodo getDataScadenza() {
		if (dataScadenza == null) {
			dataScadenza = new Periodo();
		}

		return dataScadenza;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the escludiPagate
	 */
	public Boolean getEscludiPagate() {
		return escludiPagate;
	}

	/**
	 * @return Returns the importoFinale.
	 */
	public BigDecimal getImportoFinale() {
		return importoFinale;
	}

	/**
	 * @return Returns the importoIniziale.
	 */
	public BigDecimal getImportoIniziale() {
		return importoIniziale;
	}

	/**
	 * @return Returns the parametriCreazioneAreaChiusure.
	 */
	public ParametriCreazioneAreaChiusure getParametriCreazioneAreaChiusure() {
		return parametriCreazioneAreaChiusure;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return the rate
	 */
	public List<SituazioneRata> getRate() {
		return rate;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return Returns the soloEntita.
	 */
	public Boolean getSoloEntita() {
		return soloEntita;
	}

	/**
	 * @return the stampaDettaglio
	 */
	public Boolean getStampaDettaglio() {
		return stampaDettaglio;
	}

	/**
	 * @return the statiRata
	 */
	public Set<StatoRata> getStatiRata() {
		return statiRata;
	}

	/**
	 * @return restituisce l'ordinal degli stati rata presenti separati da virgole per costruire la query di ricerca
	 */
	public String getStatiRataForQuery() {
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (StatoRata statoRata : getStatiRata()) {
			sb.append(comma);
			sb.append(statoRata.ordinal());
			comma = ",";
		}
		return sb.toString();
	}

	/**
	 * @return the tipiPagamento
	 */
	public Set<TipoPagamento> getTipiPagamento() {
		return tipiPagamento;
	}

	/**
	 * @return the tipiPagamentoEsclusi
	 */
	public Set<TipoPagamento> getTipiPagamentoEsclusi() {
		return tipiPagamentoEsclusi;
	}

	/**
	 * @return restituisce l'ordinal dei tipi pagamento presenti separati da virgole per costruire la query di ricerca
	 */
	public String getTipiPagamentoForQuery() {
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (TipoPagamento tipoPagamento : getTipiPagamento()) {
			sb.append(comma);
			sb.append(tipoPagamento.ordinal());
			comma = ",";
		}
		return sb.toString();
	}

	/**
	 * @return the tipoEntita
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the tipoPartita
	 */
	public TipoPartita getTipoPartita() {
		return tipoPartita;
	}

	/**
	 * @return the zonaGeografica
	 */
	public ZonaGeografica getZonaGeografica() {
		return zonaGeografica;
	}

	/**
	 * @return the effettuaRicerca
	 */
	@Override
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param agente
	 *            The agente to set.
	 */
	public void setAgente(AgenteLite agente) {
		this.agente = agente;
	}

	/**
	 * @param anagrafica
	 *            the anagrafica to set
	 */
	public void setAnagrafica(AnagraficaLite anagrafica) {
		this.anagrafica = anagrafica;
	}

	/**
	 * @param bancaEntita
	 *            the bancaEntita to set
	 */
	public void setBancaEntita(Banca bancaEntita) {
		this.bancaEntita = bancaEntita;
	}

	/**
	 * @param categoriaEntita
	 *            The categoriaEntita to set.
	 */
	public void setCategoriaEntita(CategoriaEntita categoriaEntita) {
		this.categoriaEntita = categoriaEntita;
	}

	/**
	 * @param categoriaRata
	 *            the categoriaRata to set
	 */
	public void setCategoriaRata(CategoriaRata categoriaRata) {
		this.categoriaRata = categoriaRata;
	}

	/**
	 * @param codicePagamento
	 *            the codicePagamento to set
	 */
	public void setCodicePagamento(CodicePagamento codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param codiceValuta
	 *            The codiceValuta to set.
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param compensazione
	 *            the compensazione to set
	 */
	public void setCompensazione(Boolean compensazione) {
		this.compensazione = compensazione;
	}

	/**
	 * @param dataScadenza
	 *            The dataScadenza to set.
	 */
	public void setDataScadenza(Periodo dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	@Override
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param escludiPagate
	 *            the escludiPagate to set
	 */
	public void setEscludiPagate(Boolean escludiPagate) {
		this.escludiPagate = escludiPagate;
	}

	/**
	 * @param importoFinale
	 *            The importoFinale to set.
	 */
	public void setImportoFinale(BigDecimal importoFinale) {
		this.importoFinale = importoFinale;
	}

	/**
	 * @param importoIniziale
	 *            The importoIniziale to set.
	 */
	public void setImportoIniziale(BigDecimal importoIniziale) {
		this.importoIniziale = importoIniziale;
	}

	/**
	 * @param parametriCreazioneAreaChiusure
	 *            The parametriCreazioneAreaChiusure to set.
	 */
	public void setParametriCreazioneAreaChiusure(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) {
		this.parametriCreazioneAreaChiusure = parametriCreazioneAreaChiusure;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(List<SituazioneRata> rate) {
		this.rate = rate;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * 
	 * @param soloEntita
	 *            the soloEntita to set
	 */
	public void setSoloEntita(Boolean soloEntita) {
		this.soloEntita = soloEntita;
	}

	/**
	 * @param stampaDettaglio
	 *            the stampaDettaglio to set
	 */
	public void setStampaDettaglio(Boolean stampaDettaglio) {
		this.stampaDettaglio = stampaDettaglio;
	}

	/**
	 * @param statiRata
	 *            the statiRata to set
	 */
	public void setStatiRata(Set<StatoRata> statiRata) {
		this.statiRata = statiRata;
	}

	/**
	 * @param tipiPagamento
	 *            the tipiPagamento to set
	 */
	public void setTipiPagamento(Set<TipoPagamento> tipiPagamento) {
		this.tipiPagamento = tipiPagamento;
	}

	/**
	 * @param tipiPagamentoEsclusi
	 *            the tipiPagamentoEsclusi to set
	 */
	public void setTipiPagamentoEsclusi(Set<TipoPagamento> tipiPagamentoEsclusi) {
		this.tipiPagamentoEsclusi = tipiPagamentoEsclusi;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

	/**
	 * @param tipoPartita
	 *            the tipoPartita to set
	 */
	public void setTipoPartita(TipoPartita tipoPartita) {
		this.tipoPartita = tipoPartita;
		if (tipoPartita.equals(TipoPartita.ATTIVA)) {
			this.tipoEntita = TipoEntita.CLIENTE;
		} else if (tipoPartita.equals(TipoPartita.PASSIVA)) {
			this.tipoEntita = TipoEntita.FORNITORE;
		}
	}

	/**
	 * @param zonaGeografica
	 *            the zonaGeografica to set
	 */
	public void setZonaGeografica(ZonaGeografica zonaGeografica) {
		this.zonaGeografica = zonaGeografica;
	}

}
