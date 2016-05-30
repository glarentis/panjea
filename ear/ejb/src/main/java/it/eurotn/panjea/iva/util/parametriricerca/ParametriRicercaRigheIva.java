package it.eurotn.panjea.iva.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Parametri per la ricerca delle righe iva.
 *
 * @author fattazzo
 */
@Entity
@Table(name = "para_ricerca_righe_iva")
public class ParametriRicercaRigheIva extends AbstractParametriRicerca implements Serializable {

	private static final long serialVersionUID = -1215315836576093308L;

	@Transient
	private boolean effettuaRicerca = false;

	@Column(length = 10)
	private Integer annoCompetenza = null;

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

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<TipoDocumento> tipiDocumento = null;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	@ManyToOne(optional = true)
	private CodiceIva codiceIva;

	@ManyToOne(optional = true)
	private RegistroIva registroIva;

	{
		this.dataDocumento = new Periodo();
		this.dataRegistrazione = new Periodo();
	}

	/**
	 * Costruttore di default.
	 */
	public ParametriRicercaRigheIva() {
		super();
	}

	/**
	 * @return the annoCompetenza
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return the codiceIva
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
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
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return the tipiDocumento
	 */
	public Set<TipoDocumento> getTipiDocumento() {
		return tipiDocumento;
	}

	/**
	 * @return the effettuaRicerca
	 */
	@Override
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param annoCompetenza
	 *            the annoCompetenza to set
	 */
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	/**
	 * @param codiceIva
	 *            the codiceIva to set
	 */
	public void setCodiceIva(CodiceIva codiceIva) {
		this.codiceIva = codiceIva;
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
	 * @param registroIva
	 *            the registroIva to set
	 */
	public void setRegistroIva(RegistroIva registroIva) {
		this.registroIva = registroIva;
	}

	/**
	 * @param tipiDocumento
	 *            the tipiDocumento to set
	 */
	public void setTipiDocumento(Set<TipoDocumento> tipiDocumento) {
		this.tipiDocumento = tipiDocumento;
	}
}
