package it.eurotn.panjea.rate.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.CategoriaRata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "part_rate")
@NamedQueries({ @NamedQuery(name = "Rata.caricaRateCollegate", query = "select r from Rata r where r.rataRiemessa.id = :paramIdRataRiemessa") })
public class Rata extends EntityBase implements Cloneable {

	public static class Ratacomparator implements Comparator<Rata>, Serializable {

		private static final long serialVersionUID = -854547086808422332L;

		/**
		 * Compara la rata tramite il numero rata.
		 * 
		 * @param o1
		 *            rata1
		 * @param o2
		 *            rata2
		 * @return risultato compare
		 */
		@Override
		public int compare(Rata o1, Rata o2) {
			return o1.getNumeroRata().compareTo(o2.getNumeroRata());
		}
	}

	/**
	 * Stato della rata.
	 * <ul>
	 * <li>APERTA: se la rata e' stata generata ma non ha generato nessun pagamento/effetto</li>
	 * <li>IN_LAVORAZIONE: e' stato creato un effetto e pagamento, ma il pagamento non ha ancora settata la
	 * dataPagamento</li>
	 * <li>CHIUSA: viene assegnata la dataPagamento al pagamento</li>
	 * <li>PAGAMENTO_PARZIALE: identifica un pagamento associato ad una rata che pero' copre solo in parte il totale
	 * rata</li>
	 * <li>IN_RIASSEGNAZIONE: definisce una rata senza data scadenza</li>
	 * <li>RIEMESSA: rata insoluta da cui si sono create delle nuove rate ad essa collegate</li>
	 * </ul>
	 * 
	 * @author
	 */
	public enum StatoRata {
		APERTA, IN_LAVORAZIONE, CHIUSA, PAGAMENTO_PARZIALE, IN_RIASSEGNAZIONE, RIEMESSA, ANTICIPO_FATTURA
	}

	private static Logger logger = Logger.getLogger(Rata.class);

	/**
	 * scale da utilizzare per il calcolo delle partite e dei pagamenti.
	 */
	private static final long serialVersionUID = -4435716059043201622L;

	/**
	 * Calcola lo stato di una rata.
	 * 
	 * @param rata
	 *            rata con i dati per calcolarne lo stato
	 * @param numPagamenti
	 *            numero di pagamenti presenti
	 * @param dataPagamentoPresente
	 *            indica se è presente almento un pagamento con la data pagamento
	 * @return stato delal rata
	 */
	public static StatoRata calcolaStatoRata(Rata rata, Integer numPagamenti, boolean dataPagamentoPresente) {
		if (rata.getDataScadenza() == null
				&& BigDecimal.ZERO.compareTo(rata.getImportoInValutaAziendaRateCollegate()) != 0) {
			return StatoRata.RIEMESSA;
		}

		if (rata.getDataScadenza() == null && !rata.isRitenutaAcconto()) {
			return StatoRata.IN_RIASSEGNAZIONE;
		}

		// il totale paga per la rata è uguale al totale dei pagamenti + l'importo forzato
		Importo totalePagatoRata = rata.getTotalePagato().add(rata.getImportoForzato(), 2);

		if (totalePagatoRata.getImportoInValuta().compareTo(BigDecimal.ZERO) == 0 && numPagamenti != null
				&& numPagamenti.compareTo(1) == 0) {
			return StatoRata.ANTICIPO_FATTURA;
		}

		if (totalePagatoRata.getImportoInValuta().abs().compareTo(BigDecimal.ZERO) == 0) {
			return StatoRata.APERTA;
		}

		if (totalePagatoRata.abs().compareTo(rata.getImporto().abs()) < 0) {
			return StatoRata.PAGAMENTO_PARZIALE;
		}

		if (totalePagatoRata.abs().compareTo(rata.getImporto().abs()) == 0) {
			if (dataPagamentoPresente) {
				return StatoRata.CHIUSA;
			} else {
				return StatoRata.IN_LAVORAZIONE;
			}
		}

		if (totalePagatoRata.abs().compareTo(rata.getImporto().abs()) > 0) {
			if (dataPagamentoPresente) {
				return StatoRata.APERTA;
			} else {
				return StatoRata.IN_LAVORAZIONE;
			}
		}

		throw new IllegalStateException("Determinazione stato rata non valido");
		//
		// if (rataConPagamento && rataConPagamentoPagato) {
		// BigDecimal importoPagatoTotale = totalePagato.getImportoInValuta().add(
		// importoForzato.getImportoInValuta());
		// BigDecimal residuoRata = importo.getImportoInValuta().subtract(importoPagatoTotale);
		// if (residuoRata.compareTo(BigDecimal.ZERO) == 0) {
		// stato = StatoRata.CHIUSA;
		// } else if (residuoRata.compareTo(BigDecimal.ZERO) == -1) {
		// stato = StatoRata.APERTA;
		// } else {
		// stato = StatoRata.PAGAMENTO_PARZIALE;
		// }
		// } else if (rataConPagamento && !rataConPagamentoPagato) {
		// stato = StatoRata.IN_LAVORAZIONE;
		// }
		// if (dataScadenza == null && !this.ritenutaAcconto) {
		// stato = StatoRata.IN_RIASSEGNAZIONE;
		// }
		// if (BigDecimal.ZERO.compareTo(importoInValutaRateCollegate) != 0) {
		// stato = StatoRata.RIEMESSA;
		// }
	}

	@ManyToOne(fetch = FetchType.EAGER)
	private AreaRate areaRate;

	private Integer numeroRata;

	@Temporal(TemporalType.DATE)
	@Index(name = "dataScadenza")
	private Date dataScadenza;

	@Transient
	private Importo importoForzato;

	@Enumerated
	@Transient
	private StatoRata statoRata;

	@Enumerated
	private TipoPagamento tipoPagamento;

	@ManyToOne
	private CategoriaRata categoriaRata;

	@Enumerated
	private TipologiaPartita tipologiaPartita;

	@ManyToOne
	private RapportoBancarioAzienda rapportoBancarioAzienda;

	@ManyToOne
	private RapportoBancarioSedeEntita rapportoBancarioEntita;

	@Embedded
	private Importo importo;

	/**
	 * Indica se dovrà essere generata la stampa per la richiesta di versamento.
	 */
	private boolean stampaRV;

	@Transient
	private Importo totalePagato;

	@Transient
	private Importo residuo;

	@Transient
	private Importo residuoConSegno;

	@Formula("(select coalesce(SUM(r.importoInValuta),0) from part_rate r where r.rataRiemessa_id = id)")
	@NotAudited
	private BigDecimal importoInValutaRateCollegate;

	@Formula("(select coalesce(SUM(r.importoInValutaAzienda),0) from part_rate r where r.rataRiemessa_id = id)")
	@NotAudited
	private BigDecimal importoInValutaAziendaRateCollegate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rata")
	@Fetch(FetchMode.JOIN)
	@BatchSize(size = 20)
	@OrderBy("dataPagamento")
	private Set<Pagamento> pagamenti;

	@Transient
	private boolean initialized;

	@Column(length = 100)
	private String note;

	@ManyToOne
	private Rata rataRiemessa;

	@Transient
	private Importo importoRateCollegate;

	private boolean ritenutaAcconto;

	@Embedded
	private DatiRitenutaAccontoRata datiRitenutaAccontoRata;

	{
		importoInValutaRateCollegate = BigDecimal.ZERO;
		importoInValutaAziendaRateCollegate = BigDecimal.ZERO;
		this.areaRate = new AreaRate();
		this.statoRata = StatoRata.APERTA;
		this.tipologiaPartita = TipologiaPartita.NORMALE;
		this.tipoPagamento = TipoPagamento.RIBA;
		this.pagamenti = new HashSet<Pagamento>();
		this.importo = new Importo();
		this.importoRateCollegate = new Importo();
		this.totalePagato = new Importo();
		this.residuo = new Importo();
		this.residuoConSegno = new Importo();
		this.initialized = false;
	}

	/**
	 * Metodo che verifica l'uguaglianza di this rispetto ad un'altra rata considerando le proprietà dataScadenza,
	 * importo, tipoPagamento; non sovrascrivo la equals perchè serve di default sulle tabelle per l'ordinamento.
	 * 
	 * @param rata
	 *            rada da controllare rispetto a this
	 * @return true se uguali, false se almeno uno dei valori delle proprietà considerate risulta diversa
	 */
	public boolean checkEqualForDomainValues(Rata rata) {

		int thisTipoPagOrdinal = this.tipoPagamento == null ? -1 : this.tipoPagamento.ordinal();
		int rataTipoPagOrdinal = rata.getTipoPagamento() == null ? -1 : rata.getTipoPagamento().ordinal();

		return this.dataScadenza.equals(rata.getDataScadenza()) && this.importo.equals(rata.getImporto())
				&& thisTipoPagOrdinal == rataTipoPagOrdinal;
	}

	@Override
	public Rata clone() {
		try {
			Rata rata = (Rata) super.clone();
			rata.setImporto(this.getImporto().clone());
			return rata;
		} catch (CloneNotSupportedException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Errore durante la clonazione della rata. " + e);
			}
		}
		return null;
	}

	/**
	 * @return the areaPartite
	 */
	public AreaRate getAreaRate() {
		return areaRate;
	}

	/**
	 * @return the categoriaRata
	 */
	public CategoriaRata getCategoriaRata() {
		return categoriaRata;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the datiRitenutaAccontoRata
	 */
	public DatiRitenutaAccontoRata getDatiRitenutaAccontoRata() {
		if (datiRitenutaAccontoRata == null) {
			datiRitenutaAccontoRata = new DatiRitenutaAccontoRata();
		}
		return datiRitenutaAccontoRata;
	}

	/**
	 * @return Returns the importo.
	 */
	public Importo getImporto() {
		return importo;
	}

	/**
	 * @return Returns the importoForzato.
	 */
	public Importo getImportoForzato() {
		return importoForzato;
	}

	/**
	 * @return Returns the importoInValutaAziendaRateCollegate.
	 */
	public BigDecimal getImportoInValutaAziendaRateCollegate() {
		return importoInValutaAziendaRateCollegate;
	}

	/**
	 * @return Returns the importoInValutaRateCollegate.
	 */
	public BigDecimal getImportoInValutaRateCollegate() {
		return importoInValutaRateCollegate;
	}

	/**
	 * @return valore in valuta del totale documento scontato della % conto cassa. <br/>
	 *         <b>NB:</b>Ritorna il valore anche se i giorni limite non sono validi.
	 */
	public BigDecimal getImportoInValutaScontoFinanziario() {
		valorizzaTransientValues();
		if (areaRate.getPercentualeSconto() == null || areaRate.getPercentualeSconto() != null
				&& areaRate.getPercentualeSconto().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal percScontoDiv100 = BigDecimal.ONE.subtract(areaRate.getPercentualeSconto().divide(Importo.HUNDRED,
				2, RoundingMode.HALF_UP));

		BigDecimal importoScontato = getResiduo().getImportoInValuta().multiply(percScontoDiv100);
		return importoScontato;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the numeroRata
	 */
	public Integer getNumeroRata() {
		return numeroRata;
	}

	/**
	 * @return the pagamenti
	 */
	public Set<Pagamento> getPagamenti() {
		valorizzaTransientValues();
		return Collections.unmodifiableSet(pagamenti);
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return the rapportoBancarioEntita
	 */
	public RapportoBancarioSedeEntita getRapportoBancarioEntita() {
		return rapportoBancarioEntita;
	}

	/**
	 * @return Returns the rataRiemessa.
	 */
	public Rata getRataRiemessa() {
		return rataRiemessa;
	}

	/**
	 * @return the residuo
	 */
	public Importo getResiduo() {
		valorizzaTransientValues();
		return residuo;
	}

	/**
	 * @return the residuoConSegno
	 */
	public Importo getResiduoConSegno() {
		valorizzaTransientValues();
		return residuoConSegno;
	}

	/**
	 * @return the stato
	 */
	public StatoRata getStatoRata() {
		valorizzaTransientValues();
		return statoRata;
	}

	/**
	 * @return the tipologiaRata
	 */
	public TipologiaPartita getTipologiaPartita() {
		return tipologiaPartita;
	}

	/**
	 * @return the tipoPagamento
	 */
	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	/**
	 * @return the totalePagato calcolando i transient values se non inizializzati
	 */
	public Importo getTotalePagato() {
		valorizzaTransientValues();
		return totalePagato;
	}

	/**
	 * @return the ritenutaAcconto
	 */
	public boolean isRitenutaAcconto() {
		return ritenutaAcconto;
	}

	/**
	 * @return the stampaRV
	 */
	public boolean isStampaRV() {
		return stampaRV;
	}

	/**
	 * @param areaRate
	 *            the areaRate to set
	 */
	public void setAreaRate(AreaRate areaRate) {
		this.areaRate = areaRate;
	}

	/**
	 * @param categoriaRata
	 *            the categoriaRata to set
	 */
	public void setCategoriaRata(CategoriaRata categoriaRata) {
		this.categoriaRata = categoriaRata;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param datiRitenutaAccontoRata
	 *            the datiRitenutaAccontoRata to set
	 */
	public void setDatiRitenutaAccontoRata(DatiRitenutaAccontoRata datiRitenutaAccontoRata) {
		this.datiRitenutaAccontoRata = datiRitenutaAccontoRata;
	}

	/**
	 * @param importo
	 *            The importo to set.
	 */
	public void setImporto(Importo importo) {
		this.importo = importo;
	}

	/**
	 * @param importoInValutaAziendaRateCollegate
	 *            The importoInValutaAziendaRateCollegate to set.
	 */
	public void setImportoInValutaAziendaRateCollegate(BigDecimal importoInValutaAziendaRateCollegate) {
		this.importoInValutaAziendaRateCollegate = importoInValutaAziendaRateCollegate;
		importoRateCollegate.setImportoInValutaAzienda(importoInValutaAziendaRateCollegate);
	}

	/**
	 * @param importoInValutaRateCollegate
	 *            The importoInValutaRateCollegate to set.
	 */
	public void setImportoInValutaRateCollegate(BigDecimal importoInValutaRateCollegate) {
		this.importoInValutaRateCollegate = importoInValutaRateCollegate;
		importoRateCollegate.setImportoInValuta(importoInValutaRateCollegate);
	}

	/**
	 * @param initialized
	 *            the initialized to set
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroRata
	 *            the numeroRata to set
	 */
	public void setNumeroRata(Integer numeroRata) {
		this.numeroRata = numeroRata;
	}

	/**
	 * @param pagamenti
	 *            the pagamenti to set
	 */
	public void setPagamenti(Set<Pagamento> pagamenti) {
		this.initialized = false;
		this.pagamenti = pagamenti;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param rapportoBancarioEntita
	 *            the rapportoBancarioEntita to set
	 */
	public void setRapportoBancarioEntita(RapportoBancarioSedeEntita rapportoBancarioEntita) {
		this.rapportoBancarioEntita = rapportoBancarioEntita;
	}

	/**
	 * @param rataRiemessa
	 *            The rataRiemessa to set.
	 */
	public void setRataRiemessa(Rata rataRiemessa) {
		this.rataRiemessa = rataRiemessa;
	}

	/**
	 * @param residuo
	 *            the residuo to set
	 */
	public void setResiduo(Importo residuo) {
		this.residuo = residuo;
	}

	/**
	 * @param residuoConSegno
	 *            the residuoConSegno to set
	 */
	public void setResiduoConSegno(Importo residuoConSegno) {
		this.residuoConSegno = residuoConSegno;
	}

	/**
	 * @param ritenutaAcconto
	 *            the ritenutaAcconto to set
	 */
	public void setRitenutaAcconto(boolean ritenutaAcconto) {
		this.ritenutaAcconto = ritenutaAcconto;
	}

	/**
	 * @param stampaRV
	 *            the stampaRV to set
	 */
	public void setStampaRV(boolean stampaRV) {
		this.stampaRV = stampaRV;
	}

	/**
	 * @param tipologiaPartita
	 *            the tipologiaPartita to set
	 */
	public void setTipologiaPartita(TipologiaPartita tipologiaPartita) {
		this.tipologiaPartita = tipologiaPartita;
	}

	/**
	 * @param tipoPagamento
	 *            the tipoPagamento to set
	 */
	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	/**
	 * @param totalePagato
	 *            the totalePagato to set
	 */
	public void setTotalePagato(Importo totalePagato) {
		this.totalePagato = totalePagato;
	}

	/**
	 * To string di RataPartita.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Rata[");
		buffer.append("areaRate = ").append(areaRate != null ? areaRate.getId() : "null");
		buffer.append(" dataScadenza = ").append(dataScadenza);
		buffer.append(" importo = ").append(importo);
		buffer.append(" numeroRata = ").append(numeroRata);
		buffer.append(" rapportoBancarioAzienda = ").append(
				rapportoBancarioAzienda != null ? rapportoBancarioAzienda.getId() : null);
		buffer.append(" tipologiaPartita = ").append(tipologiaPartita);
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * Calcola i valori transient per la rata.
	 */
	private void valorizzaTransientValues() {
		if (!initialized) {
			totalePagato = new Importo();
			importoForzato = new Importo();

			totalePagato.setCodiceValuta(this.importo.getCodiceValuta());
			importoForzato.setCodiceValuta(this.importo.getCodiceValuta());
			boolean rataConPagamento = false;
			for (Pagamento pagamento : this.pagamenti) {
				if (!pagamento.isInsoluto()) {
					Importo imp = pagamento.getImporto();

					totalePagato.setImportoInValuta(totalePagato.getImportoInValuta().add(imp.getImportoInValuta()));
					importoForzato.setImportoInValuta(importoForzato.getImportoInValuta().add(
							pagamento.getImportoForzato().getImportoInValuta()));

					totalePagato.setImportoInValutaAzienda(totalePagato.getImportoInValutaAzienda().add(
							imp.getImportoInValutaAzienda()));
					importoForzato.setImportoInValutaAzienda(importoForzato.getImportoInValuta().add(
							pagamento.getImportoForzato().getImportoInValutaAzienda()));

					if (pagamento.getDataPagamento() != null) {
						rataConPagamento = true;
					}
				}
			}

			importoRateCollegate.setCodiceValuta(importo.getCodiceValuta());
			importoRateCollegate.setTassoDiCambio(importo.getTassoDiCambio());

			this.residuo = this.importo.abs()
					.subtract(this.totalePagato.abs(), importo.getImportoInValutaAzienda().scale()).abs()
					.subtract(importoRateCollegate, importo.getImportoInValutaAzienda().scale());
			this.residuoConSegno = this.importo
					.subtract(this.totalePagato, importo.getImportoInValutaAzienda().scale()).subtract(
							importoRateCollegate, importo.getImportoInValutaAzienda().scale());
			initialized = true;
			this.statoRata = Rata.calcolaStatoRata(this, this.pagamenti.size(), rataConPagamento);
		}
	}
}
