/**
 * 
 */
package it.eurotn.panjea.protocolli.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * Classe di dominio per rappresentare la ripartizione di un protocollo nei vari anni.
 * 
 * @author adriano
 * @version 1.0, 05/mag/07
 */
@Entity
@Table(name = "code_protocolli")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_protocollo", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("P")
@NamedQueries({
		@NamedQuery(name = "Protocollo.caricaByCodice", query = "from Protocollo p where p.codiceAzienda = :paramCodiceAzienda and p.codice like :paramCodice "),
		@NamedQuery(name = "Protocollo.caricaAll", query = "from Protocollo p where p.codiceAzienda = :paramCodiceAzienda ") })
public class Protocollo extends EntityBase {

	public static final String REF = "Protocollo";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";

	private static final long serialVersionUID = 7553551863487361506L;
	@Column(length = 10, nullable = false)
	@Index(name = "codice_azienda")
	private String codiceAzienda;
	@Column(length = 10)
	private String codice;
	@Column(length = 60)
	private String descrizione;

	/**
	 * 
	 */
	public Protocollo() {
		super();
	}

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

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Protocollo[");
		buffer.append(" codice = ").append(codice);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}
}
