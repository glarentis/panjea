/**
 * 
 */
package it.eurotn.panjea.conai.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * Il listino costi associato al materiale in un periodo determinato.
 * 
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_conai_righe_listino")
public class ConaiListino extends EntityBase {

	private static final long serialVersionUID = -6782585877570274791L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private ConaiArticolo conaiArticolo;

	@Temporal(TemporalType.DATE)
	private Date dataInizio;

	@Temporal(TemporalType.DATE)
	private Date dataFine;

	@Column(precision = 19, scale = 6)
	private BigDecimal prezzo;

	/**
	 * Costruttore.
	 */
	public ConaiListino() {
		super();
	}

	/**
	 * @return the conaiArticolo
	 */
	public ConaiArticolo getConaiArticolo() {
		return conaiArticolo;
	}

	/**
	 * @return the dataFine
	 */
	public Date getDataFine() {
		return dataFine;
	}

	/**
	 * @return the dataInizio
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * @return the prezzo
	 */
	public BigDecimal getPrezzo() {
		return prezzo;
	}

	/**
	 * @param conaiArticolo
	 *            the conaiArticolo to set
	 */
	public void setConaiArticolo(ConaiArticolo conaiArticolo) {
		this.conaiArticolo = conaiArticolo;
	}

	/**
	 * @param dataFine
	 *            the dataFine to set
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * @param dataInizio
	 *            the dataInizio to set
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * @param prezzo
	 *            the prezzo to set
	 */
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}

}
