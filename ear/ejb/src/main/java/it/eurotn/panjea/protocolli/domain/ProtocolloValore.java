/**
 * 
 */
package it.eurotn.panjea.protocolli.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import it.eurotn.codice.generator.interfaces.IProtocolloValore;

/**
 * Classe di dominio che rappresenta i protocolli atti alla determinazione di un codice attraverso tale registro.
 * 
 * @author adriano
 * @version 1.0, 05/mag/07
 */
@Entity
@DiscriminatorValue("PV")
@NamedQueries({
		@NamedQuery(name = "ProtocolloValore.caricaByCodice", query = "from ProtocolloValore p where p.codiceAzienda = :paramCodiceAzienda and p.codice = :paramCodice"),
		@NamedQuery(name = "ProtocolloValore.caricaAll", query = "from ProtocolloValore p where p.codiceAzienda = :paramCodiceAzienda ") })
public class ProtocolloValore extends Protocollo implements IProtocolloValore {

	public static final String REF = "ProtocolloValore";
	public static final String PROP_VALORE = "valore";

	private static final long serialVersionUID = -6560076460101519421L;

	@Column
	private Integer valore;

	/**
	 * 
	 */
	public ProtocolloValore() {
		super();
	}

	/**
	 * @return Returns the valore.
	 */
	@Override
	public Integer getValore() {
		return valore;
	}

	/**
	 * @param valore
	 *            The valore to set.
	 */
	@Override
	public void setValore(Integer valore) {
		this.valore = valore;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ProtocolloValore[");
		buffer.append(super.toString());
		buffer.append("valore = ").append(valore);
		buffer.append("]");
		return buffer.toString();
	}

}
