/**
 * 
 */
package it.eurotn.panjea.pagamenti.util;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.io.Serializable;

/**
 * DTO per avere il pagamento collegato al protocollo dell'areaContabile dello stesso documento, usata per la ricerca
 * pagamenti per visualizzare i documenti di pagamento.
 * 
 * @author Leonardo
 */
public class PagamentoDTO implements IDefProperty, Serializable {

	private static final long serialVersionUID = 4473600308324352613L;

	/**
	 * @uml.property name="protocollo"
	 */
	private Integer protocollo = null;

	/**
	 * @uml.property name="areaChiusura"
	 * @uml.associationEnd
	 */
	private AreaChiusure areaChiusura = null;

	/**
	 * @uml.property name="pagamento"
	 * @uml.associationEnd
	 */
	private Pagamento pagamento = null;

	/**
	 * Costruttore usato per istanziare un pagamento di appoggio senza Pagamento usato.
	 * it.eurotn.panjea.pagamenti.manager.PagamentiManagerBean.ricercaAreePartiteCollegate(ParametriRicercaAreePartite)
	 * 
	 * @param areaChiusura
	 *            areaPagamento del pagamento
	 * @param protocollo
	 *            protocollo associato all'area contabile del pagamento
	 */
	public PagamentoDTO(final AreaPagamenti areaChiusura, final Integer protocollo) {
		this.areaChiusura = areaChiusura;
		this.protocollo = protocollo;
	}

	/**
	 * Costruttore usato per avere l'areaPartite della distinta bancaria o accredito legato al pagamento.
	 * 
	 * @param pagamento
	 *            pagamento nel DTO
	 * @param areaChiusura
	 *            areaPartite del DTO
	 * @param protocollo
	 *            protocollo del DTO
	 */
	public PagamentoDTO(final Pagamento pagamento, final AreaChiusure areaChiusura, final Integer protocollo) {
		this.pagamento = pagamento;
		this.areaChiusura = areaChiusura;
		this.protocollo = protocollo;
	}

	/**
	 * Costruttore usato nella query HQL della ricerca rate.
	 * 
	 * @param pagamento
	 *            pagamento nel DTO
	 * @param protocollo
	 *            protocollo collegato all'area contabile
	 */
	public PagamentoDTO(final Pagamento pagamento, final Integer protocollo) {
		this.pagamento = pagamento;
		this.areaChiusura = pagamento.getAreaChiusure();
		this.protocollo = protocollo;
	}

	/**
	 * @return the areaPartite
	 */
	public AreaPartite getAreaPartite() {
		return areaChiusura;
	}

	@Override
	public String getDomainClassName() {
		return pagamento.getDomainClassName();
	}

	@Override
	public Integer getId() {
		return pagamento.getId();
	}

	/**
	 * @return the rataPartita
	 * @uml.property name="pagamento"
	 */
	public Pagamento getPagamento() {
		return pagamento;
	}

	/**
	 * @return the protocollo
	 * @uml.property name="protocollo"
	 */
	public Integer getProtocollo() {
		return protocollo;
	}

	@Override
	public Integer getVersion() {
		return pagamento.getVersion();
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

}
