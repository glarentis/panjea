/**
 *
 */
package it.eurotn.panjea.pagamenti.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.audit.envers.AuditableProperties;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

/**
 * Classe di dominio per rappresentare il legame tra le {@link SedeEntita} e le sue condizioni di pagamento.
 *
 * @author adriano
 * @version 1.0, 19/dic/2008
 */
@Entity
@Audited
@Table(name = "part_sedi_pagamento")
@NamedQueries({
		@NamedQuery(name = "SedePagamento.caricaBySedeEntita", query = "select sp from SedePagamento sp where sp.sedeEntita.id = :paramIdSedeEntita "),
		@NamedQuery(name = "SedePagamento.caricaPrincipaleByEntita", query = " select sp from SedePagamento sp join fetch sp.sedeEntita where sp.sedeEntita.entita.id = :paramIdEntita and sp.sedeEntita.tipoSede.sedePrincipale=true ")

})
@AuditableProperties(excludeProperties = { "sedeEntita" })
public class SedePagamento extends EntityBase {

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="sedeEntita"
	 * @uml.associationEnd
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private SedeEntita sedeEntita;

	/**
	 * @uml.property name="codicePagamento"
	 * @uml.associationEnd
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private CodicePagamento codicePagamento;

	/**
	 * @return Returns the codicePagamento.
	 * @uml.property name="codicePagamento"
	 */
	public CodicePagamento getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return Returns the sedeEntita.
	 * @uml.property name="sedeEntita"
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @param codicePagamento
	 *            The codicePagamento to set.
	 * @uml.property name="codicePagamento"
	 */
	public void setCodicePagamento(CodicePagamento codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param sedeEntita
	 *            The sedeEntita to set.
	 * @uml.property name="sedeEntita"
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 *
	 * @return a <code>String</code> come risultato di questo oggetto
	 */
	@Override
	public String toString() {
		StringBuffer retValue = new StringBuffer();
		retValue.append("SedePagamento[ ").append(super.toString()).append(" sedeEntita = ")
				.append(this.sedeEntita != null ? this.sedeEntita.getId() : null).append(" codicePagamento = ")
				.append(this.codicePagamento != null ? this.codicePagamento.getId() : null).append(" ]");
		return retValue.toString();
	}

}
