/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 * 
 */
@Entity
@Audited
@Table(name = "bamm_ubicazioni")
@NamedQueries({
		@NamedQuery(name = "Ubicazione.caricaAll", query = "from Ubicazione u where u.codiceAzienda = :paramCodiceAzienda "),
		@NamedQuery(name = "Ubicazione.caricaByCodice", query = "from Ubicazione u where u.codiceAzienda = :paramCodiceAzienda and u.codice = :paramCodice ") })
public class Ubicazione extends EntityBase {

	private static final long serialVersionUID = 1057299932487122798L;

	public static final String REF = "Ubicazione";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda = null;

	@Column(nullable = false, unique = true)
	@Index(name = "index_codice")
	private String codice = null;

	@Column(nullable = false, unique = true)
	private String descrizione = null;

	/**
	 * Costruttore di default.
	 */
	public Ubicazione() {
		initialize();
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {

	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Ubicazione[");
		buffer.append("codice = ").append(codice);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
