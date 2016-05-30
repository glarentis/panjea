package it.eurotn.panjea.ordini.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;

/**
 * Mappa le righe degli ordini che sono stati importati da una procedura esterna.<br/>
 *
 * @author giangi
 * @version 1.0, 06/dic/2010
 */
@Entity
@Table(name = "ordi_righe_ordine_importate")
public class RigaOrdineImportata extends EntityBase {

	private static final long serialVersionUID = 8112922387135197438L;

	@ManyToOne(optional = false)
	private OrdineImportato ordine;

	private Integer numeroRiga;

	@Transient
	private Date dataConsegna;

	@Transient
	private Date dataProduzione;

	/**
	 * Mantengo gli attributi importati in una stringa (nome=valore).
	 */
	private String attributi;

	private String attributo1;

	@Transient
	private ProvenienzaPrezzo provenienzaPrezzo;// se null non calcola il prezzo

	@Column(precision = 5, scale = 2)
	private BigDecimal percProvvigione;

	@ManyToOne
	private ArticoloLite articolo;

	private String codiceArticolo;

	@Column(length = 255)
	private String note;

	private Boolean omaggio;

	@Column(precision = 18, scale = 6, nullable = true)
	private Double qta;

	@Column(precision = 19, scale = 6, nullable = true)
	private BigDecimal prezzo;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto1;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto2;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto3;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto4;

	@Column(precision = 19, scale = 6, nullable = true)
	private BigDecimal prezzoDeterminato;

	@Column(precision = 19, scale = 6, nullable = true)
	private BigDecimal prezzoUnitarioDeterminato;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto1Determinato;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto2Determinato;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto3Determinato;

	@Column(precision = 5, scale = 2)
	private BigDecimal sconto4Determinato;

	private boolean assortimento;

	@Transient
	private boolean selezionata;

	private BigDecimal prezzoUnitario;

	/**
	 * Se la riga è stata generata da altre righe (ad esempio un ordineFornitore da un ordineCliente) collega le righe.
	 */
	@Transient
	private int[] idRigheDaCollegare;

	@Transient
	private Integer idRigaDistinta;

	@Transient
	private Integer idDistintaConfigurazione;

	@Transient
	private boolean distinta;

	private Double qtaR;

	@Transient
	private Set<RigaOrdineImportata> righeComponente;

	@Transient
	private String formulaComponente;

	/**
	 * Costruttore.
	 */
	public RigaOrdineImportata() {
		omaggio = false;
		assortimento = true;

		prezzoUnitario = BigDecimal.ZERO;
		prezzo = BigDecimal.ZERO;
		sconto1 = BigDecimal.ZERO;
		sconto2 = BigDecimal.ZERO;
		sconto3 = BigDecimal.ZERO;
		sconto4 = BigDecimal.ZERO;

		prezzoUnitarioDeterminato = BigDecimal.ZERO;
		prezzoDeterminato = BigDecimal.ZERO;
		sconto1Determinato = BigDecimal.ZERO;
		sconto2Determinato = BigDecimal.ZERO;
		sconto3Determinato = BigDecimal.ZERO;
		sconto4Determinato = BigDecimal.ZERO;

		selezionata = false;

		righeComponente = new HashSet<RigaOrdineImportata>();
	}

	/**
	 * @param notaOrdine
	 *            aggiunge una nota alla riga. Se già presente la aggiunge
	 */
	public void addNota(NotaOrdineImportata notaOrdine) {
		if (note != null) {
			note += " ";
		} else {
			note = "";
		}
		this.note += notaOrdine.getNota();
	}

	/**
	 * @param rigaComponente
	 *            rigaComponente
	 * @return true or false
	 */
	public boolean addRigaComponente(RigaOrdineImportata rigaComponente) {
		return righeComponente.add(rigaComponente);
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
		RigaOrdineImportata other = (RigaOrdineImportata) obj;
		if (codiceArticolo == null) {
			if (other.codiceArticolo != null) {
				return false;
			}
		} else if (!codiceArticolo.equals(other.codiceArticolo)) {
			return false;
		}
		if (numeroRiga == null) {
			if (other.numeroRiga != null) {
				return false;
			}
		} else if (!numeroRiga.equals(other.numeroRiga)) {
			return false;
		}
		if (ordine == null) {
			if (other.ordine != null) {
				return false;
			}
		} else if (!ordine.equals(other.ordine)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the articoloLite.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the attributi.
	 */
	public String getAttributi() {
		return attributi;
	}

	/**
	 * @return Returns the attributo1.
	 */
	public String getAttributo1() {
		return attributo1;
	}

	/**
	 * @return Returns the codiceArticolo.
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return dataProduzione
	 */
	public Date getDataProduzione() {
		return dataProduzione;
	}

	/**
	 * @return the formulaComponente
	 */
	public String getFormulaComponente() {
		return formulaComponente;
	}

	/**
	 * @return the idDistintaConfigurazione
	 */
	public Integer getIdDistintaConfigurazione() {
		return idDistintaConfigurazione;
	}

	/**
	 * @return the idRigaDistinta
	 */
	public Integer getIdRigaDistinta() {
		return idRigaDistinta;
	}

	/**
	 * @return Returns the idiRgheDaCollegare.
	 */
	public int[] getIdRigheDaCollegare() {
		return idRigheDaCollegare;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return Returns the numeroRiga.
	 */
	public Integer getNumeroRiga() {
		return numeroRiga;
	}

	/**
	 * @return Returns the omaggio.
	 */
	public Boolean getOmaggio() {
		return omaggio;
	}

	/**
	 * @return Returns the ordine.
	 */
	public OrdineImportato getOrdine() {
		return ordine;
	}

	/**
	 * @return Returns the percProvvigione.
	 */
	public BigDecimal getPercProvvigione() {
		return percProvvigione;
	}

	/**
	 * @return Returns the prezzo.
	 */
	public BigDecimal getPrezzo() {
		return prezzo;
	}

	/**
	 * @return the prezzoDeterminato
	 */
	public BigDecimal getPrezzoDeterminato() {
		if (prezzoDeterminato == null) {
			return BigDecimal.ZERO;
		}
		return prezzoDeterminato;
	}

	/**
	 * @return the prezzoForzato
	 */
	public BigDecimal getPrezzoForzato() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the prezzoUnitario
	 */
	public BigDecimal getPrezzoUnitario() {
		return prezzoUnitario;
	}

	/**
	 * @return the prezzoUnitarioDeterminato
	 */
	public BigDecimal getPrezzoUnitarioDeterminato() {
		return prezzoUnitarioDeterminato;
	}

	/**
	 * @return Returns the provenienzaPrezzo.
	 */
	public ProvenienzaPrezzo getProvenienzaPrezzo() {
		return provenienzaPrezzo;
	}

	/**
	 * @return Returns the qta.
	 */
	public Double getQta() {
		return qta;
	}

	/**
	 * @return Returns the qtaR.
	 */
	public Double getQtaR() {
		return qtaR;
	}

	/**
	 * @return the righeComponente
	 */
	public Set<RigaOrdineImportata> getRigheComponente() {
		return righeComponente;
	}

	/**
	 * @return the sconto1
	 */
	public BigDecimal getSconto1() {
		return sconto1;
	}

	/**
	 * @return the sconto1Determinato
	 */
	public BigDecimal getSconto1Determinato() {
		return sconto1Determinato;
	}

	/**
	 * @return the sconto2
	 */
	public BigDecimal getSconto2() {
		return sconto2;
	}

	/**
	 * @return the sconto2Determinato
	 */
	public BigDecimal getSconto2Determinato() {
		return sconto2Determinato;
	}

	/**
	 * @return the sconto3
	 */
	public BigDecimal getSconto3() {
		return sconto3;
	}

	/**
	 * @return the sconto3Determinato
	 */
	public BigDecimal getSconto3Determinato() {
		return sconto3Determinato;
	}

	/**
	 * @return the sconto4
	 */
	public BigDecimal getSconto4() {
		return sconto4;
	}

	/**
	 * @return the sconto4Determinato
	 */
	public BigDecimal getSconto4Determinato() {
		return sconto4Determinato;
	}

	/**
	 *
	 * @return sconto determinato.
	 */
	public Sconto getScontoDeterminato() {
		return new Sconto(sconto1Determinato, sconto2Determinato, sconto3Determinato, sconto4Determinato);
	}

	/**
	 * @return scostamento tra prezzo e prezzo determinato in percentuale
	 */
	public BigDecimal getScostamentoPrezzo() {
		// non posso dividere per 0 quindi nel caso in cui il prezzo determinato non ci sia lo imposto a 1
		BigDecimal prezzoDet = getTotaleRigaDeterminato();
		if (prezzoDet.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		return getTotaleRiga().multiply(Importo.HUNDRED).divide(prezzoDet, 2, RoundingMode.HALF_UP)
				.subtract(Importo.HUNDRED);
	}

	/**
	 * @return totale della riga importata
	 */
	public BigDecimal getTotaleRiga() {
		return getPrezzo().multiply(BigDecimal.valueOf(getQta()));
	}

	/**
	 * @return totale della riga importata
	 */
	public BigDecimal getTotaleRigaDeterminato() {
		return getPrezzoDeterminato().multiply(BigDecimal.valueOf(getQta()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codiceArticolo == null) ? 0 : codiceArticolo.hashCode());
		result = prime * result + ((numeroRiga == null) ? 0 : numeroRiga.hashCode());
		result = prime * result + ((ordine == null) ? 0 : ordine.hashCode());
		return result;
	}

	/**
	 * postload callback.
	 */
	@PostLoad
	private void init() {
		if (!getOrdine().isBloccaOrdine() && getOrdine().getPagamento() != null) {
			getOrdine().setBloccaOrdine(!getOrdine().getPagamento().equals(getOrdine().getPagamentoDeterminato()));
		}
		if (!getOrdine().isBloccaOrdine()) {
			getOrdine().setBloccaOrdine(
					prezzoDeterminato.compareTo(prezzo) != 0 && (omaggio == null || (omaggio != null && !omaggio)));
		}
		if (getArticolo() == null
				|| (getOrdine() != null && (getOrdine().getPagamento() == null || getOrdine().getAgente() == null))) {
			getOrdine().setBloccaOrdine(true);
		}
	}

	/**
	 * @return the assortimento
	 */
	public boolean isAssortimento() {
		return assortimento;
	}

	/**
	 * @return the distinta
	 */
	public boolean isDistinta() {
		return distinta;
	}

	/**
	 * @return the selezionata
	 */
	public boolean isSelezionata() {
		return selezionata;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param assortimento
	 *            the assortimento to set
	 */
	public void setAssortimento(boolean assortimento) {
		this.assortimento = assortimento;
	}

	/**
	 * @param attributi
	 *            The attributi to set.
	 */
	public void setAttributi(String attributi) {
		this.attributi = attributi;
	}

	/**
	 * @param attributo1
	 *            The attributo1 to set.
	 */
	public void setAttributo1(String attributo1) {
		this.attributo1 = attributo1;
	}

	/**
	 * @param codiceArticolo
	 *            The codiceArticolo to set.
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		codiceArticolo = codiceArticolo.replace(" ", "");
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param dataConsegna
	 *            The dataConsegna to set.
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataProduzione
	 *            the dataProduziuone to set
	 */
	public void setDataProduzione(Date dataProduzione) {
		this.dataProduzione = dataProduzione;
	}

	/**
	 * @param distinta
	 *            the distinta to set
	 */
	public void setDistinta(boolean distinta) {
		this.distinta = distinta;
	}

	/**
	 * @param formulaComponente
	 *            the formulaComponente to set
	 */
	public void setFormulaComponente(String formulaComponente) {
		this.formulaComponente = formulaComponente;
	}

	/**
	 * @param idDistintaConfigurazione
	 *            the idDistintaConfigurazione to set
	 */
	public void setIdDistintaConfigurazione(Integer idDistintaConfigurazione) {
		this.idDistintaConfigurazione = idDistintaConfigurazione;
	}

	/**
	 * @param idRigaDistinta
	 *            the idRigaDistinta to set
	 */
	public void setIdRigaDistinta(Integer idRigaDistinta) {
		this.idRigaDistinta = idRigaDistinta;
	}

	/**
	 * @param idiRgheDaCollegare
	 *            The idiRgheDaCollegare to set.
	 */
	public void setIdRigheDaCollegare(int[] idiRgheDaCollegare) {
		this.idRigheDaCollegare = idiRgheDaCollegare;
	}

	/**
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroRiga
	 *            The numeroRiga to set.
	 */
	public void setNumeroRiga(Integer numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	/**
	 * @param omaggio
	 *            The omaggio to set.
	 */
	public void setOmaggio(Boolean omaggio) {
		this.omaggio = omaggio;
		updateValoriDeterminati();
	}

	/**
	 * @param ordine
	 *            The ordine to set.
	 */
	public void setOrdine(OrdineImportato ordine) {
		this.ordine = ordine;
	}

	/**
	 * @param percProvvigione
	 *            The percProvvigione to set.
	 */
	public void setPercProvvigione(BigDecimal percProvvigione) {
		this.percProvvigione = percProvvigione;
	}

	/**
	 * @param prezzo
	 *            The prezzo to set.
	 */
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

	/**
	 * @param prezzoDeterminato
	 *            the prezzoDeterminato to set
	 */
	public void setPrezzoDeterminato(BigDecimal prezzoDeterminato) {
		this.prezzoDeterminato = prezzoDeterminato;
	}

	/**
	 * @param prezzoForzato
	 *            the prezzoForzato to set
	 */
	public void setPrezzoForzato(BigDecimal prezzoForzato) {
		if (prezzoForzato.compareTo(BigDecimal.ZERO) != 0) {
			this.prezzoUnitario = prezzoForzato;
		}
	}

	/**
	 * @param prezzoUnitario
	 *            the prezzoUnitario to set
	 */
	public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}

	/**
	 * @param prezzoUnitarioDeterminato
	 *            the prezzoUnitarioDeterminato to set
	 */
	public void setPrezzoUnitarioDeterminato(BigDecimal prezzoUnitarioDeterminato) {
		this.prezzoUnitarioDeterminato = prezzoUnitarioDeterminato;
		updateValoriDeterminati();
	}

	/**
	 * @param provenienzaPrezzo
	 *            The provenienzaPrezzo to set.
	 */
	public void setProvenienzaPrezzo(ProvenienzaPrezzo provenienzaPrezzo) {
		this.provenienzaPrezzo = provenienzaPrezzo;
	}

	/**
	 * @param qta
	 *            The qta to set.
	 */
	public void setQta(Double qta) {
		this.qta = qta;
	}

	/**
	 * @param qtaR
	 *            the qtaR to set
	 */
	public void setQtaR(Double qtaR) {
		this.qtaR = qtaR;
	}

	/**
	 * @param righeComponente
	 *            the righeComponente to set
	 */
	public void setRigheComponente(Set<RigaOrdineImportata> righeComponente) {
		this.righeComponente = righeComponente;
	}

	/**
	 * @param sconto1
	 *            the sconto1 to set
	 */
	public void setSconto1(BigDecimal sconto1) {
		this.sconto1 = sconto1;
	}

	/**
	 * @param sconto1Determinato
	 *            the sconto1Determinato to set
	 */
	public void setSconto1Determinato(BigDecimal sconto1Determinato) {
		this.sconto1Determinato = sconto1Determinato;
		updateValoriDeterminati();
	}

	/**
	 * @param sconto2
	 *            the sconto2 to set
	 */
	public void setSconto2(BigDecimal sconto2) {
		this.sconto2 = sconto2;
	}

	/**
	 * @param sconto2Determinato
	 *            the sconto2Determinato to set
	 */
	public void setSconto2Determinato(BigDecimal sconto2Determinato) {
		this.sconto2Determinato = sconto2Determinato;
		updateValoriDeterminati();
	}

	/**
	 * @param sconto3
	 *            the sconto3 to set
	 */
	public void setSconto3(BigDecimal sconto3) {
		this.sconto3 = sconto3;
	}

	/**
	 * @param sconto3Determinato
	 *            the sconto3Determinato to set
	 */
	public void setSconto3Determinato(BigDecimal sconto3Determinato) {
		this.sconto3Determinato = sconto3Determinato;
		updateValoriDeterminati();
	}

	/**
	 * @param sconto4
	 *            the sconto4 to set
	 */
	public void setSconto4(BigDecimal sconto4) {
		this.sconto4 = sconto4;
	}

	/**
	 * @param sconto4Determinato
	 *            the sconto4Determinato to set
	 */
	public void setSconto4Determinato(BigDecimal sconto4Determinato) {
		this.sconto4Determinato = sconto4Determinato;
		updateValoriDeterminati();
	}

	/**
	 *
	 * @param sconto
	 *            sconto da impostare
	 */
	public void setScontoDeterminato(Sconto sconto) {
		sconto1Determinato = sconto.getSconto1();
		sconto2Determinato = sconto.getSconto2();
		sconto3Determinato = sconto.getSconto3();
		sconto4Determinato = sconto.getSconto4();
		updateValoriDeterminati();
	}

	/**
	 * @param selezionata
	 *            the selezionata to set
	 */
	public void setSelezionata(boolean selezionata) {
		this.selezionata = selezionata;
	}

	/**
	 * Sostituisce il prezzo determinato e sconti con quelli unitari.
	 */
	public void sostituisciPrezzoDeterminato() {

		this.prezzoDeterminato = this.prezzo;

		this.sconto1Determinato = this.sconto1;
		this.sconto2Determinato = this.sconto2;
		this.sconto3Determinato = this.sconto3;
		this.sconto4Determinato = this.sconto4;

		this.prezzoUnitarioDeterminato = this.prezzoUnitario;
	}

	/**
	 * Aggiorna il valore del prezzo determinato in base agli sconti.
	 */
	private void updateValoriDeterminati() {

		if (omaggio) {
			sconto1Determinato = Importo.HUNDRED.negate();
			sconto2Determinato = BigDecimal.ZERO;
			sconto3Determinato = BigDecimal.ZERO;
			sconto4Determinato = BigDecimal.ZERO;
		}

		// Creo uno sconto "al volo" ed utilizzo il suo metodo per applicarlo
		Sconto sconto = new Sconto();
		sconto.setSconto1(sconto1Determinato);
		sconto.setSconto2(sconto2Determinato);
		sconto.setSconto3(sconto3Determinato);
		sconto.setSconto4(sconto4Determinato);

		prezzoDeterminato = sconto.applica(prezzoUnitarioDeterminato, 2);
	}

}
