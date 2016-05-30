package it.eurotn.panjea.magazzino.rich.forms.listino;

import java.math.BigDecimal;
import java.util.Date;

public class PrezzoAuditBean extends Object {

	private Date dataRevisione;

	private BigDecimal prezzo;
	private Integer numeroDecimaliPrezzo;

	/**
	 * Costruttore. Creato solamente per essere usato dal beanwrapper del table format
	 */
	public PrezzoAuditBean() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param dataRevisione
	 *            data revisione
	 * @param prezzo
	 *            prezzo nella revisione
	 * @param numeroDecimaliPrezzo
	 *            numero decimali del prezzo
	 */
	public PrezzoAuditBean(final Date dataRevisione, final BigDecimal prezzo, final Integer numeroDecimaliPrezzo) {
		super();
		this.dataRevisione = dataRevisione;
		this.prezzo = prezzo;
		this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
	}

	/**
	 * @return Returns the dataRevisione.
	 */
	public Date getDataRevisione() {
		return dataRevisione;
	}

	/**
	 * @return Returns the numeroDecimaliPrezzo.
	 */
	public Integer getNumeroDecimaliPrezzo() {
		return numeroDecimaliPrezzo;
	}

	/**
	 * @return Returns the prezzo.
	 */
	public BigDecimal getPrezzo() {
		return prezzo;
	}
}