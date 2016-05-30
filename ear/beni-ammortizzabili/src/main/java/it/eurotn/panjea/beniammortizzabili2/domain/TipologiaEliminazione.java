/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 * 
 */
@Entity
@Audited
@Table(name = "bamm_tipologie_eliminazione")
public class TipologiaEliminazione extends EntityBase {

	private static final long serialVersionUID = -7190284917333065475L;

	public static final String REF = "TipologiaEliminazione";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";

	@Column(length = 10, nullable = false, unique = true)
	@Index(name = "index_codice")
	private String codice = null;

	@Column(length = 50, nullable = false, unique = true)
	private String descrizione = null;

	/**
	 * Costruttore di default.
	 */
	public TipologiaEliminazione() {
		initialize();
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
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
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TipologiaEliminazione[");
		buffer.append("codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}

}
