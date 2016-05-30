package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "cont_area_contabile")
@NamedQueries({ @NamedQuery(name = "AreaContabileLite.ricercaAreaByDocumento", query = "select a from AreaContabileLite a  where a.documento.id = :paramIdDocumento") })
public class AreaContabileLite extends EntityBase {
	private static final long serialVersionUID = -4424816656231179742L;

	private StatoAreaContabile statoAreaContabile;

	@Embedded
	private CodiceDocumento codice;

	@OneToOne(fetch = FetchType.LAZY)
	private Documento documento;

	/**
	 * @return the codice
	 */
	public CodiceDocumento getCodice() {
		return codice;
	}

	/**
	 * @return documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return statoAreaContabile
	 */
	public StatoAreaContabile getStatoAreaContabile() {
		return statoAreaContabile;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(CodiceDocumento codice) {
		this.codice = codice;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param statoAreaContabile
	 *            the statoAreaContabile to set
	 */
	public void setStatoAreaContabile(StatoAreaContabile statoAreaContabile) {
		this.statoAreaContabile = statoAreaContabile;
	}
}
