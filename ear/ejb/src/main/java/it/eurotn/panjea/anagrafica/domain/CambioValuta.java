package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.audit.envers.AuditableProperties;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "anag_cambi_valute", uniqueConstraints = @UniqueConstraint(columnNames = { "valuta_id", "data",
		"codice_azienda" }))
@NamedQueries({
		@NamedQuery(name = "CambioValuta.caricaByDate", query = " from CambioValuta cv  join fetch cv.valuta val where val.codiceValuta = :paramCodiceValuta and cv.data >= :paramDataIniziale and cv.data <= :paramDataFinale and cv.codiceAzienda = :paramCodiceAzienda "),
		@NamedQuery(name = "CambioValuta.caricaByUltimaData", query = "from CambioValuta cv  join fetch cv.valuta val where val.codiceValuta = :paramCodiceValuta and cv.data <= :paramData and cv.codiceAzienda = :paramCodiceAzienda order by cv.data desc "),
		@NamedQuery(name = "CambioValuta.caricaByIndex", query = "from CambioValuta cv join fetch cv.valuta val where val.codiceValuta = :paramCodiceValuta and cv.data = :paramData and cv.codiceAzienda = :paramCodiceAzienda ") })
@AuditableProperties(properties = "valuta")
public class CambioValuta extends EntityBase {

	private static final long serialVersionUID = -1393937877219244746L;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(name = "codice_azienda", length = 20)
	private java.lang.String codiceAzienda;

	/**
	 * @uml.property name="data"
	 */
	@Temporal(TemporalType.DATE)
	private Date data;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	private ValutaAzienda valuta;

	/**
	 * @uml.property name="tasso"
	 */
	@Column(precision = 12, scale = 6)
	private BigDecimal tasso;

	/**
	 * Costruttore.
	 */
	public CambioValuta() {
		valuta = new ValutaAzienda();
	}

	/**
	 * @return codiceAzienda
	 * @uml.property name="codiceAzienda"
	 */
	public java.lang.String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return data
	 * @uml.property name="data"
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return Returns the tasso.
	 * @uml.property name="tasso"
	 */
	public BigDecimal getTasso() {
		return tasso;
	}

	/**
	 * @return Returns the valuta.
	 */
	public ValutaAzienda getValuta() {
		return valuta;
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
	 * @param data
	 *            the data to set
	 * @uml.property name="data"
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param tasso
	 *            The tasso to set.
	 * @uml.property name="tasso"
	 */
	public void setTasso(BigDecimal tasso) {
		this.tasso = tasso;
	}

	/**
	 * @param valuta
	 *            The valuta to set.
	 */
	public void setValuta(ValutaAzienda valuta) {
		this.valuta = valuta;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CambioValuta[");
		buffer.append("]");
		return buffer.toString();
	}
}
