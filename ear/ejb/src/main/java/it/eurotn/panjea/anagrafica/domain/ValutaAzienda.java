package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * Classe di dominio che descrive le Valute gestite dall'azienda. Di default conterrà la Valuta dell'azienda, necessaria
 * per avere il numero di decimali che gestisce (es: l'importo del totale documento) e il carattere simbolo
 * 
 * @author adriano
 * @version 1.0, 18/giu/08
 */
@Entity
@Audited
@Table(name = "anag_valute_azienda")
@NamedQueries({
		@NamedQuery(name = "ValutaAzienda.caricaAll", query = " from ValutaAzienda va where va.codiceAzienda = :paramCodiceAzienda ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "valutaAzienda") }),
		@NamedQuery(name = "ValutaAzienda.caricaByCodice", query = " select va from ValutaAzienda va where va.codiceAzienda = :paramCodiceAzienda and va.codiceValuta = :paramCodiceValuta ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "valutaAzienda") }),
		@NamedQuery(name = "ValutaAzienda.caricaValutaAziendaCorrente", query = " select v from ValutaAzienda v,Azienda a where v.codiceValuta=a.codiceValuta and a.codice=:codiceAzienda ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "valutaAzienda") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "valutaAzienda")
public class ValutaAzienda extends EntityBase {

	public static final String MASCHERA_VALUTA_GENERICA = "###,###,###,##0.00";
	public static final String DEFAULT_FORMATTER = "###,###,###,##0";
	private static final long serialVersionUID = -51154829705836507L;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Index(name = "azienda")
	@Column(name = "codice_azienda", length = 20)
	private java.lang.String codiceAzienda;

	/**
	 * @uml.property name="codiceValuta"
	 */
	@Column(length = 3)
	private String codiceValuta;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "valuta", cascade = CascadeType.REMOVE)
	@Fetch(FetchMode.JOIN)
	private Set<CambioValuta> cambi;

	/**
	 * @uml.property name="numeroDecimali"
	 */
	@Column
	private Integer numeroDecimali;

	/**
	 * @uml.property name="simbolo"
	 */
	@Column(length = 3)
	private String simbolo;

	/**
	 * Costruttore.
	 * 
	 */
	public ValutaAzienda() {
		super();
	}

	/**
	 * @return Returns the cambi.
	 */
	public Set<CambioValuta> getCambi() {
		return cambi;
	}

	/**
	 * @return codiceAzienda
	 * @uml.property name="codiceAzienda"
	 */
	public java.lang.String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return codiceValuta
	 * @uml.property name="codiceValuta"
	 */
	public String getCodiceValuta() {
		return codiceValuta;
	}

	/**
	 * @return Returns the numeroDecimali.
	 * @uml.property name="numeroDecimali"
	 */
	public Integer getNumeroDecimali() {
		return numeroDecimali;
	}

	/**
	 * @return Returns the simbolo.
	 * @uml.property name="simbolo"
	 */
	public String getSimbolo() {
		return simbolo;
	}

	/**
	 * Inizializza i valori di default.
	 */
	protected void initialize() {
		this.numeroDecimali = new Integer(2);
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(java.lang.String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiceValuta
	 *            the codiceValuta to set
	 * @uml.property name="codiceValuta"
	 */
	public void setCodiceValuta(String codiceValuta) {
		this.codiceValuta = codiceValuta;
	}

	/**
	 * @param numeroDecimali
	 *            The numeroDecimali to set.
	 * @uml.property name="numeroDecimali"
	 */
	public void setNumeroDecimali(Integer numeroDecimali) {
		this.numeroDecimali = numeroDecimali;
	}

	/**
	 * @param simbolo
	 *            The simbolo to set.
	 * @uml.property name="simbolo"
	 */
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 * 
	 * @return a <code>String</code> come risultato di questo oggetto
	 */
	@Override
	public String toString() {
		final String spazio = " ";

		StringBuffer retValue = new StringBuffer();

		retValue.append("ValutaAzienda[ ").append(super.toString()).append(spazio).append("codiceAzienda = ")
				.append(this.codiceAzienda).append(spazio).append("codiceValuta = ").append(this.codiceValuta)
				.append(spazio).append("numeroDecimali = ").append(this.numeroDecimali).append(spazio)
				.append("simbolo = ").append(this.simbolo).append(spazio).append("]");

		return retValue.toString();
	}

}
