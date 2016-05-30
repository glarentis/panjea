package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "anag_lingue", uniqueConstraints = { @UniqueConstraint(columnNames = "codice") })
@NamedQueries({ @NamedQuery(name = "Lingua.caricaLingue", query = "from Lingua l where l.azienda=:azienda") })
public class Lingua extends EntityBase {
	private static final long serialVersionUID = 789817741284258583L;
	/**
	 * @uml.property name="codice"
	 */
	private String codice;
	/**
	 * @uml.property name="azienda"
	 */
	private String azienda;

	/**
	 * @return azienda
	 * @uml.property name="azienda"
	 */
	public String getAzienda() {
		return azienda;
	}

	/**
	 * @return codice
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * 
	 * @return descrizione
	 */
	public String getDescrizione() {
		if (getCodice() == null) {
			return "";
		}
		return new Locale(getCodice()).getDisplayName();
	}

	/**
	 * @param azienda
	 *            the azienda to set
	 * @uml.property name="azienda"
	 */
	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}

	/**
	 * @param codice
	 *            the codice to set
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Lingua[");
		buffer.append(super.toString());
		buffer.append(" codice = ").append(codice);
		buffer.append(" azienda = ").append(azienda);
		buffer.append("]");
		return buffer.toString();
	}
}
