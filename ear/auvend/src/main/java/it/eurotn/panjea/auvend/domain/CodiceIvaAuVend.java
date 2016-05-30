/**
 * 
 */
package it.eurotn.panjea.auvend.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Classe di dominio utilizzata per indicare quale {@link CodiceIva} puo' essere utilizzato per le acquisizioni di
 * AuVend.
 * 
 * @author adriano
 * @version 1.0, 04/feb/2009
 * 
 */
@Entity
@Table(name = "avend_codici_iva")
@NamedQueries({ @NamedQuery(name = "CodiceIvaAuVend.caricaAll", query = "select cIva from CodiceIvaAuVend cIva where cIva.codiceIva.codiceAzienda = :paramCodiceAzienda ") })
public class CodiceIvaAuVend extends EntityBase {

	private static final long serialVersionUID = 1L;

	@OneToOne
	@Fetch(FetchMode.SELECT)
	private CodiceIva codiceIva;

	/**
	 * @return Returns the codiceIva.
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @param codiceIva
	 *            The codiceIva to set.
	 */
	public void setCodiceIva(CodiceIva codiceIva) {
		this.codiceIva = codiceIva;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("CodiceIvaAuVend[ ").append(super.toString());
		retValue.append(" codiceIva = ").append(this.codiceIva != null ? this.codiceIva.getId() : null);
		retValue.append(" ]");

		return retValue.toString();
	}

}
