/**
 * 
 */
package it.eurotn.panjea.sicurezza.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Domain Object Permesso.
 * 
 * @author adriano
 * @version 1.0, 20/dic/07
 */
@Entity
@Table(name = "sicu_permessi", uniqueConstraints = @UniqueConstraint(columnNames = { "codice" }))
@NamedQueries({ @NamedQuery(name = "Permesso.caricaAll", query = " from Permesso order by ordinamento "),
		@NamedQuery(name = "Permesso.caricaByModulo", query = " from Permesso p where p.modulo = :paramModulo ") })
public class Permesso extends EntityBase {

	private static final long serialVersionUID = 5191875713826044736L;

	public static final String REF = "Permesso";
	public static final String PROP_MODULO = "modulo";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_SOTTOMODULO = "sottoModulo";
	public static final String PROP_ORDINAMENTO = "ordinamento";

	/**
	 * @uml.property name="codice"
	 */
	@Column(length = 50, nullable = false)
	private String codice;
	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 50)
	private String descrizione;
	/**
	 * @uml.property name="modulo"
	 */
	@Column(length = 50)
	private String modulo;
	/**
	 * @uml.property name="sottoModulo"
	 */
	@Column(length = 50)
	private String sottoModulo;

	/**
	 * @uml.property name="permessiCollegati"
	 */
	@Column(length = 200)
	private String permessiCollegati;

	/**
	 * @uml.property name="ordinamento"
	 */
	private int ordinamento;

	/**
	 * @return Returns the codice.
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the modulo.
	 * @uml.property name="modulo"
	 */
	public String getModulo() {
		return modulo;
	}

	/**
	 * @return ordinamento
	 * @uml.property name="ordinamento"
	 */
	public int getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @return Returns the permessiCollegati.
	 * @uml.property name="permessiCollegati"
	 */
	public String getPermessiCollegati() {
		return permessiCollegati;
	}

	/**
	 * @return sottoModulo
	 * @uml.property name="sottoModulo"
	 */
	public String getSottoModulo() {
		return sottoModulo;
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
	 * @param descrizione
	 *            The descrizione to set.
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param modulo
	 *            The modulo to set.
	 * @uml.property name="modulo"
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	/**
	 * @param ordinamento
	 *            the ordinamento to set
	 * @uml.property name="ordinamento"
	 */
	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * @param permessiCollegati
	 *            The permessiCollegati to set.
	 * @uml.property name="permessiCollegati"
	 */
	public void setPermessiCollegati(String permessiCollegati) {
		this.permessiCollegati = permessiCollegati;
	}

	/**
	 * @param sottoModulo
	 *            the sottoModulo to set
	 * @uml.property name="sottoModulo"
	 */
	public void setSottoModulo(String sottoModulo) {
		this.sottoModulo = sottoModulo;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Permesso[");
		buffer.append(super.toString());
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" modulo = ").append(modulo);
		buffer.append(" sottoModulo = ").append(sottoModulo);
		buffer.append(" ordinamento = ").append(ordinamento);
		buffer.append("]");
		return buffer.toString();
	}
}
