package it.eurotn.panjea.centricosto.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * 
 * Contiene i dati per il centro di costo.
 * 
 * @author giangi
 * @version 1.0, 18/dic/2010
 * 
 */
@Entity
@Audited
@Table(name = "cont_centri_costo", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "centroCosto")
public class CentroCosto extends EntityBase {

	private static final long serialVersionUID = -2488243767173772697L;

	@Column(nullable = false, length = 30)
	private String descrizione;

	@Column(nullable = false, length = 15)
	@Index(name = "codice_idx")
	private String codice;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "centroCosto")
	private List<SottoConto> sottoConti;

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceAzienda.
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the sottoConti.
	 */
	public List<SottoConto> getSottoConti() {
		return sottoConti;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param sottoConti
	 *            The sottoConti to set.
	 */
	public void setSottoConti(List<SottoConto> sottoConti) {
		this.sottoConti = sottoConti;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CentroCosto [descrizione=" + descrizione + ", codice=" + codice + ", codiceAzienda=" + codiceAzienda
				+ "]";
	}
}
