package it.eurotn.panjea.preventivi.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;

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
@Entity(name = "ParametriRicercaMovimentazionePreventivi")
@Table(name = "para_ricerca_movimentazione_preventivi")
public class ParametriRicercaMovimentazione extends AbstractParametriRicerca implements Serializable {

	/**
	 * @author fattazzo
	 */
	public enum EStatoRiga {
		PROCESSATE, NON_PROCESSATE, TUTTE
	}

	private static final long serialVersionUID = 5431072541940319015L;

	@ManyToOne(optional = true)
	private ArticoloLite articoloLite = null;

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

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoAreaPreventivo> tipiAreaPreventivo = null;

	private EStatoRiga statoRiga;

	{
		this.effettuaRicerca = false;
		this.tipoEntita = TipoEntita.CLIENTE;

		if (tipiAreaPreventivo == null) {
			this.tipiAreaPreventivo = new TreeSet<TipoAreaPreventivo>();
		}

		if (dataRegistrazione == null) {
			dataRegistrazione = new Periodo();
			dataRegistrazione.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
		}
		if (dataConsegna == null) {
			dataConsegna = new Periodo();
			dataConsegna.setTipoPeriodo(TipoPeriodo.NESSUNO);
		}
		statoRiga = EStatoRiga.TUTTE;
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaMovimentazione() {
		super();
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
	 * @return the entitaLite
	 * @uml.property name="entitaLite"
	 */
	public EntitaLite getEntitaLite() {
		return entitaLite;
	}

	/**
	 * @return the statoRiga
	 */
	public EStatoRiga getStatoRiga() {
		return statoRiga;
	}

	/**
	 * @return the tipiAreaPreventivo
	 */
	public Set<TipoAreaPreventivo> getTipiAreaPreventivo() {
		return tipiAreaPreventivo;
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
	 * @param statoRiga
	 *            the statoRiga to set
	 */
	public void setStatoRiga(EStatoRiga statoRiga) {
		this.statoRiga = statoRiga;
	}

	/**
	 * @param tipiAreaPreventivo
	 *            the tipiAreaPreventivo to set
	 */
	public void setTipiAreaPreventivo(Set<TipoAreaPreventivo> tipiAreaPreventivo) {
		this.tipiAreaPreventivo = tipiAreaPreventivo;
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
