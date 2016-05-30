package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@DiscriminatorValue("AA")
public class AreaAcconto extends AreaTesoreria {

	private static final long serialVersionUID = 8188610244629136467L;

	@Audited
	@Column(length = 60)
	private String note;

	@Audited
	private boolean automatico;

	@Formula("( select COALESCE(SUM(pag.importoInValuta),0) from part_pagamenti pag where pag.areaAcconto_id = id)")
	private BigDecimal importoUtilizzato;

	@Audited
	@ManyToOne
	private RapportoBancarioAzienda rapportoBancarioAzienda;

	/**
	 * Init delle proprieta' base di Effetto.
	 */
	{
		automatico = false;
		this.importoUtilizzato = BigDecimal.ZERO;
		this.documento = new Documento();
		this.codicePagamento = null;
	}

	/**
	 * @return the importoUtilizzato
	 */
	public BigDecimal getImportoUtilizzato() {
		return importoUtilizzato;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return the residuo
	 */
	public BigDecimal getResiduo() {
		if (getImportoUtilizzato() == null) {
			return getDocumento().getTotale().getImportoInValuta();
		} else {
			return getDocumento().getTotale().getImportoInValuta().subtract(getImportoUtilizzato());
		}
	}

	/**
	 * @return the automatico
	 */
	public boolean isAutomatico() {
		return automatico;
	}

	/**
	 * @param automatico
	 *            the automatico to set
	 */
	public void setAutomatico(boolean automatico) {
		this.automatico = automatico;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

}
