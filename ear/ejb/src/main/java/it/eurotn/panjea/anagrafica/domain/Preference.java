/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * Domain Object Preference.
 * 
 * @author adriano
 * @version 1.0, 19/dic/07
 */
@Entity
@Audited
@Table(name = "preference")
@NamedQueries({
		@NamedQuery(name = "Preference.caricaPerUtente", query = " from Preference p where p.nomeUtente = :paramNomeUtente "),
		@NamedQuery(name = "Preference.caricaPerChiave", query = " from Preference p where p.chiave = :paramChiave and p.nomeUtente is null "),
		@NamedQuery(name = "Preference.caricaAll", query = " from Preference p")

})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "preference")
public class Preference extends EntityBase {

	private static final long serialVersionUID = 90952795722197901L;

	public static final String REF = "Preference";
	public static final String PROP_CHIAVE = "chiave";
	public static final String PROP_VALORE = "valore";
	public static final String PROP_NOME_UTENTE = "nomeUtente";

	/**
	 * @uml.property name="chiave"
	 */
	@Column(length = 120, nullable = false)
	private String chiave;

	/**
	 * @uml.property name="nomeUtente"
	 */
	@Column(length = 40)
	private String nomeUtente;

	/**
	 * @uml.property name="valore"
	 */
	@Column(length = 120)
	private String valore;

	/**
	 * @return Returns the chiave.
	 * @uml.property name="chiave"
	 */
	public String getChiave() {
		return chiave;
	}

	/**
	 * @return Returns the nomeUtente.
	 * @uml.property name="nomeUtente"
	 */
	public String getNomeUtente() {
		return nomeUtente;
	}

	/**
	 * @return Returns the valore.
	 * @uml.property name="valore"
	 */
	public String getValore() {
		return valore;
	}

	/**
	 * @param chiave
	 *            The chiave to set.
	 * @uml.property name="chiave"
	 */
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	/**
	 * @param nomeUtente
	 *            The nomeUtente to set.
	 * @uml.property name="nomeUtente"
	 */
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	/**
	 * @param valore
	 *            The valore to set.
	 * @uml.property name="valore"
	 */
	public void setValore(String valore) {
		this.valore = valore;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Preference[");
		buffer.append(super.toString());
		buffer.append(" chiave = ").append(chiave);
		buffer.append(" nomeUtente = ").append(nomeUtente);
		buffer.append(" valore = ").append(valore);
		buffer.append("]");
		return buffer.toString();
	}

}
