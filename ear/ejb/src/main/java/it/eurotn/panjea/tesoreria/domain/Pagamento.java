/**
 * 
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "part_pagamenti")
@NamedQueries({ @NamedQuery(name = "Pagamento.ricercaByAreaChiusura", query = " select p from Pagamento p inner join fetch p.rata where p.areaChiusure.id = :paramAreaChiusureId ") })
public class Pagamento extends EntityBase implements Cloneable {

	/**
	 * Identifica il possibile collegamento di un pagamento con il rispettivo documento.<br>
	 * <ul>
	 * <li>ACCREDITO_ASSEGNO: areaAccreditoAssegno</li>
	 * <li>ACCONTO: areaAcconto</li>
	 * <li>CHIUSURA: areaChiusure</li>
	 * <li>LIQUIDAZIONE: tutte le aree null</li>
	 * </ul>
	 * 
	 * @author leonardo
	 */
	public enum TipoPagamentoDocumento {
		CHIUSURA, ACCREDITO_ASSEGNO, ACCONTO, LIQUIDAZIONE
	}

	private static final long serialVersionUID = -275074096228055785L;

	@Temporal(TemporalType.DATE)
	private Date dataCreazione;

	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	@Embedded
	private Importo importo;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaForzato", precision = 19, scale = 6)),
			@AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaForzato", precision = 19, scale = 6)),
			@AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioForzato", precision = 12, scale = 6)),
			@AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaForzato", length = 3)) })
	private Importo importoForzato;

	@Column
	private boolean insoluto;

	private boolean chiusuraForzataRata;

	private boolean scontoFinanziario;

	@ManyToOne(fetch = FetchType.LAZY)
	private Rata rata;

	@ManyToOne(fetch = FetchType.LAZY)
	private AreaAcconto areaAcconto;

	/**
	 * Link verso l'Area accredito per la gestione assegni.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private AreaAccreditoAssegno areaAccreditoAssegno;

	/**
	 * Questa proprieta' rappresenta l'area del documento di pagamento.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private AreaChiusure areaChiusure;

	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Effetto effetto;

	/**
	 * Inizializzazione con removeNullValue.
	 */
	{
		chiusuraForzataRata = false;
		scontoFinanziario = false;
		insoluto = false;
		importo = new Importo();
		importoForzato = new Importo();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Pagamento pagamento = new Pagamento();
		pagamento.setId(getId() != null ? getId().intValue() : null);
		pagamento.setVersion(getVersion() != null ? getVersion().intValue() : 0);
		pagamento.setChiusuraForzataRata(this.getChiusuraForzataRata());
		// pagamento.setAreaPartite(this.getAreaPartite());
		pagamento.setDataCreazione(this.getDataCreazione());
		pagamento.setDataPagamento(this.getDataPagamento());
		pagamento.setEffetto(this.getEffetto());
		pagamento.setImporto(this.getImporto());
		pagamento.setImportoForzato(getImportoForzato().clone());
		pagamento.setRata(this.getRata());
		return pagamento;
	}

	/**
	 * @return the areaAcconto
	 */
	public AreaAcconto getAreaAcconto() {
		return areaAcconto;
	}

	/**
	 * @return the areaAccreditoAssegno
	 */
	public AreaAccreditoAssegno getAreaAccreditoAssegno() {
		return areaAccreditoAssegno;
	}

	/**
	 * @return il riferimento all'area pagamenti che contiene this
	 */
	public AreaChiusure getAreaChiusure() {
		return areaChiusure;
	}

	/**
	 * @return se la rata e' stata forzata in chiusura
	 */
	public boolean getChiusuraForzataRata() {
		return chiusuraForzataRata;
	}

	/**
	 * @return the dataCreazione
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @return the effetto
	 */
	public Effetto getEffetto() {
		return effetto;
	}

	/**
	 * @return the importo
	 */
	public Importo getImporto() {
		return importo;
	}

	/**
	 * @return the importoForzato
	 * @uml.property name="importoForzato"
	 */
	public Importo getImportoForzato() {
		return importoForzato;
	}

	/**
	 * @return the rataPartita
	 */
	public Rata getRata() {
		return rata;
	}

	/**
	 * Identifica, a seconda dell'areaTesoreria associata, il tipo di documento di pagamento.
	 * 
	 * @return TipoPagamentoDocumento
	 */
	public TipoPagamentoDocumento getTipoPagamentoDocumento() {
		if (areaChiusure != null) {
			return TipoPagamentoDocumento.CHIUSURA;
		} else if (areaAcconto != null) {
			return TipoPagamentoDocumento.ACCONTO;
		} else if (areaAccreditoAssegno != null) {
			return TipoPagamentoDocumento.ACCREDITO_ASSEGNO;
		} else {
			return TipoPagamentoDocumento.LIQUIDAZIONE;
		}
	}

	/**
	 * @return the insoluto
	 */
	public boolean isInsoluto() {
		return insoluto;
	}

	/**
	 * @return indica se il pagamento è stato creato da un documento di liquidazione e quindi non ha area chiusura o
	 *         areaAcconto collegato
	 */
	public boolean isLiquidazione() {
		return areaAcconto == null && areaChiusure == null;
	}

	/**
	 * 
	 * @return true se al pagamento è stato applicato lo sconto finanziario.Nel campo importoForzato c'è l'importo dello
	 *         sconto.
	 */
	public boolean isScontoFinanziario() {
		return scontoFinanziario;
	}

	/**
	 * @param areaAcconto
	 *            the areaAcconto to set
	 */
	public void setAreaAcconto(AreaAcconto areaAcconto) {
		this.areaAcconto = areaAcconto;
	}

	/**
	 * @param areaAccreditoAssegno
	 *            the areaAccreditoAssegno to set
	 */
	public void setAreaAccreditoAssegno(AreaAccreditoAssegno areaAccreditoAssegno) {
		this.areaAccreditoAssegno = areaAccreditoAssegno;
	}

	/**
	 * @param areaChiusure
	 *            the areaChiusure to set
	 */
	public void setAreaChiusure(AreaChiusure areaChiusure) {
		this.areaChiusure = areaChiusure;
	}

	/**
	 * @param chiusuraForzataRata
	 *            se il pagamento forza la chiusura rata
	 */
	public void setChiusuraForzataRata(boolean chiusuraForzataRata) {
		this.chiusuraForzataRata = chiusuraForzataRata;
	}

	/**
	 * @param dataCreazione
	 *            the dataCreazione to set
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @param effetto
	 *            the effetto to set
	 */
	public void setEffetto(Effetto effetto) {
		this.effetto = effetto;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(Importo importo) {
		this.importo = importo;
	}

	/**
	 * @param importoForzato
	 *            the importoForzato to set
	 */
	public void setImportoForzato(Importo importoForzato) {
		this.importoForzato = importoForzato;
	}

	/**
	 * @param insoluto
	 *            the insoluto to set
	 */
	public void setInsoluto(boolean insoluto) {
		this.insoluto = insoluto;
	}

	/**
	 * @param rata
	 *            the rata to set
	 */
	public void setRata(Rata rata) {
		this.rata = rata;
	}

	/**
	 * @param scontoFinanziario
	 *            setta se il pagamento ha uno sconto finanziario
	 */
	public void setScontoFinanziario(boolean scontoFinanziario) {
		this.scontoFinanziario = scontoFinanziario;
	}
}
