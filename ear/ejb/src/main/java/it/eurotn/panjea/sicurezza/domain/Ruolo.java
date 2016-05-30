/**
 * 
 */
package it.eurotn.panjea.sicurezza.domain;

import it.eurotn.entity.EntityBase;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Domain Object di Ruolo.
 * 
 * @author adriano
 * @version 1.0, 20/dic/07
 */
@Entity
@Table(name = "sicu_ruoli", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }))
@NamedQueries({
		@NamedQuery(name = "Ruolo.caricaAll", query = "from Ruolo"),
		@NamedQuery(name = "Ruolo.caricaByAzienda", query = " from Ruolo r where r.codiceAzienda = :paramCodiceAzienda ") })
public class Ruolo extends EntityBase {

	private static final long serialVersionUID = -8124881630391130848L;

	public static final String REF = "Ruolo";
	public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";

	/**
	 * @uml.property name="codice"
	 */
	@Column(length = 50, nullable = false)
	private String codice;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 50, nullable = false)
	private String codiceAzienda;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 50)
	private String descrizione;

	/**
	 * @uml.property name="permessi"
	 */
	@ManyToMany
	@OrderBy("ordinamento ASC")
	private Set<Permesso> permessi;

	/**
	 * Costruttore.
	 * 
	 */
	public Ruolo() {
		initialize();
	}

	/**
	 * @return Returns the codice.
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceAzienda.
	 * @uml.property name="codiceAzienda"
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the permessi.
	 * @uml.property name="permessi"
	 */
	public Set<Permesso> getPermessi() {
		return permessi;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.permessi = new TreeSet<Permesso>();
	}

	/**
	 * @param codice
	 *            The codice to set.
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param permessi
	 *            The permessi to set.
	 * @uml.property name="permessi"
	 */
	public void setPermessi(Set<Permesso> permessi) {
		this.permessi = permessi;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Ruolo[");
		buffer.append(super.toString());
		buffer.append(" codice = ").append(codice);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
