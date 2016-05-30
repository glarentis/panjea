package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno.StatoAssegno;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

@Entity
@Table(name = "para_ricerca_assegni")
public class ParametriRicercaAssegni extends AbstractParametriRicerca {

	private static final long serialVersionUID = -8180753248450474024L;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataDocumentoIniziale")),
			@AttributeOverride(name = "dataFinale", column = @Column(name = "dataDocumentoFinale")),
			@AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataDocumentoTipoPeriodo")),
			@AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataDocumentoDataInizialeNull")),
			@AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataDocumentoNumeroGiorni")) })
	private Periodo dataDocumento;

	@CollectionOfElements(targetElement = StatoAssegno.class, fetch = FetchType.EAGER)
	@JoinTable(name = "para_ricerca_assegni_stato_assegno", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "stato", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Set<StatoAssegno> statiAssegno;

	private TipoAreaPartita tipoAreaPartita;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	/**
	 * Il tipo partita mi serve sul client per filtrare i tipiAreaPartita, non considerare questo parametro per la
	 * generazione del/i documento/i.
	 */
	private TipoPartita tipoPartita;

	@Column(length = 20)
	private String numeroAssegno;

	@Transient
	private boolean escludiTipiAreaPartiteDistinta;

	{
		if (tipoPartita == null) {
			tipoPartita = TipoPartita.ATTIVA;
		}
		if (statiAssegno == null) {
			statiAssegno = new HashSet<StatoAssegno>();
		}
		escludiTipiAreaPartiteDistinta = false;
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAssegni() {
		super();
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
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the numeroAssegno
	 */
	public String getNumeroAssegno() {
		return numeroAssegno;
	}

	/**
	 * @return the statiAssegno
	 */
	public Set<StatoAssegno> getStatiAssegno() {
		return statiAssegno;
	}

	/**
	 * @return the tipoAreaPartita
	 */
	public TipoAreaPartita getTipoAreaPartita() {
		return tipoAreaPartita;
	}

	/**
	 * @return the tipoPartita
	 */
	public TipoPartita getTipoPartita() {
		return tipoPartita;
	}

	/**
	 * @return the escludiTipiAreaPartiteDistinta
	 */
	public boolean isEscludiTipiAreaPartiteDistinta() {
		return escludiTipiAreaPartiteDistinta;
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
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param escludiTipiAreaPartiteDistinta
	 *            the escludiTipiAreaPartiteDistinta to set
	 */
	public void setEscludiTipiAreaPartiteDistinta(boolean escludiTipiAreaPartiteDistinta) {
		this.escludiTipiAreaPartiteDistinta = escludiTipiAreaPartiteDistinta;
	}

	/**
	 * @param numeroAssegno
	 *            the numeroAssegno to set
	 */
	public void setNumeroAssegno(String numeroAssegno) {
		this.numeroAssegno = numeroAssegno;
	}

	/**
	 * @param statiAssegno
	 *            the statiAssegno to set
	 */
	public void setStatiAssegno(Set<StatoAssegno> statiAssegno) {
		this.statiAssegno = statiAssegno;
	}

	/**
	 * @param tipoAreaPartita
	 *            the tipoAreaPartita to set
	 */
	public void setTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		this.tipoAreaPartita = tipoAreaPartita;
	}

	/**
	 * @param tipoPartita
	 *            the tipoPartita to set
	 */
	public void setTipoPartita(TipoPartita tipoPartita) {
		this.tipoPartita = tipoPartita;
	}

}
