package it.eurotn.panjea.ordini.util.parametriricerca;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author fattazzo
 */
@Entity(name = "ParametriRicercaMovimentazioneOrdini")
@Table(name = "para_ricerca_movimentazione_ordini")
public class ParametriRicercaMovimentazione extends AbstractParametriRicerca implements Serializable {

	/**
	 * @author fattazzo
	 */
	public enum ESTATORIGA {
		EVASA, NON_EVASA, TUTTE
	}

	private static final long serialVersionUID = 5431072541940319015L;

	@ManyToOne(optional = true)
	private ArticoloLite articoloLite = null;

	@ManyToOne(optional = true)
	private DepositoLite depositoLite = null;

	@ManyToOne(optional = true)
	private EntitaLite entitaLite = null;

	@Enumerated
	private TipoEntita tipoEntita = null;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni")) })
	private Periodo dataRegistrazione;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataConsegnaIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataConsegnaFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataConsegnaTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataConsegnaDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataConsegnaNumeroGiorni")) })
	private Periodo dataConsegna;

	private boolean effettuaRicerca = false;

	private boolean righeOmaggio = false;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoAreaOrdine> tipiAreaOrdine = null;

	@Column(length = 255)
	private String noteRiga = null;

	@Enumerated
	private ESTATORIGA statoRiga = ESTATORIGA.TUTTE;

	@ManyToOne(optional = true)
	private AgenteLite agente;

	{
		this.effettuaRicerca = false;
		this.tipoEntita = TipoEntita.CLIENTE;
		this.statoRiga = ESTATORIGA.TUTTE;

		if (tipiAreaOrdine == null) {
			this.tipiAreaOrdine = new TreeSet<TipoAreaOrdine>();
		}

		if (dataRegistrazione == null) {
			dataRegistrazione = new Periodo();
			dataRegistrazione.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
		}
		if (dataConsegna == null) {
			dataConsegna = new Periodo();
			dataConsegna.setTipoPeriodo(TipoPeriodo.NESSUNO);
		}
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaMovimentazione() {
		super();
	}

	/**
	 * @return the agente
	 */
	public AgenteLite getAgente() {
		return agente;
	}

	/**
	 * @return the articoloLite
	 * @uml.property name="articoloLite"
	 */
	public ArticoloLite getArticoloLite() {
		return articoloLite;
	}

	/**
	 * @return the dataConsegna
	 */
	public Periodo getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Periodo getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the depositoLite
	 * @uml.property name="depositoLite"
	 */
	public DepositoLite getDepositoLite() {
		return depositoLite;
	}

	/**
	 * @return the entitaLite
	 * @uml.property name="entitaLite"
	 */
	public EntitaLite getEntitaLite() {
		return entitaLite;
	}

	/**
	 * @return the noteRiga
	 * @uml.property name="noteRiga"
	 */
	public String getNoteRiga() {
		return noteRiga;
	}

	/**
	 * @return the statoRiga
	 * @uml.property name="statoRiga"
	 */
	public ESTATORIGA getStatoRiga() {
		return statoRiga;
	}

	/**
	 * @return the tipiAreaOrdine
	 * @uml.property name="tipiAreaOrdine"
	 */
	public Set<TipoAreaOrdine> getTipiAreaOrdine() {
		return tipiAreaOrdine;
	}

	/**
	 * @return the tipoEntita
	 * @uml.property name="tipoEntita"
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the effettuaRicerca
	 * @uml.property name="effettuaRicerca"
	 */
	@Override
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @return Returns the righeOmaggio.
	 */
	public boolean isRigheOmaggio() {
		return righeOmaggio;
	}

	/**
	 * @param agente
	 *            the agente to set
	 */
	public void setAgente(AgenteLite agente) {
		this.agente = agente;
	}

	/**
	 * @param articoloLite
	 *            the articoloLite to set
	 * @uml.property name="articoloLite"
	 */
	public void setArticoloLite(ArticoloLite articoloLite) {
		this.articoloLite = articoloLite;
	}

	/**
	 * @param dataConsegna
	 *            the dataConsegna to set
	 */
	public void setDataConsegna(Periodo dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Periodo dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param depositoLite
	 *            the depositoLite to set
	 * @uml.property name="depositoLite"
	 */
	public void setDepositoLite(DepositoLite depositoLite) {
		this.depositoLite = depositoLite;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 * @uml.property name="effettuaRicerca"
	 */
	@Override
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param entitaLite
	 *            the entitaLite to set
	 * @uml.property name="entitaLite"
	 */
	public void setEntitaLite(EntitaLite entitaLite) {
		this.entitaLite = entitaLite;
	}

	/**
	 * @param noteRiga
	 *            the noteRiga to set
	 * @uml.property name="noteRiga"
	 */
	public void setNoteRiga(String noteRiga) {
		this.noteRiga = noteRiga;
	}

	/**
	 * @param righeOmaggio
	 *            The righeOmaggio to set.
	 */
	public void setRigheOmaggio(boolean righeOmaggio) {
		this.righeOmaggio = righeOmaggio;
	}

	/**
	 * @param statoRiga
	 *            the statoRiga to set
	 * @uml.property name="statoRiga"
	 */
	public void setStatoRiga(ESTATORIGA statoRiga) {
		this.statoRiga = statoRiga;
	}

	/**
	 * @param tipiAreaOrdine
	 *            the tipiAreaOrdine to set
	 * @uml.property name="tipiAreaOrdine"
	 */
	public void setTipiAreaOrdine(Set<TipoAreaOrdine> tipiAreaOrdine) {
		this.tipiAreaOrdine = tipiAreaOrdine;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 * @uml.property name="tipoEntita"
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

}
