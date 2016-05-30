/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Dati del conto corrente per la sede dell'entità.
 * 
 * @author adriano
 * @version 1.0, 18/dic/07
 */
@Entity
@Audited
@Table(name = "anag_rapporti_bancari")
@DiscriminatorValue("E")
@NamedQueries({ @NamedQuery(name = "RapportoBancarioSedeEntita.caricaBySedeEntita", query = "select rb from RapportoBancarioSedeEntita rb where rb.sedeEntita.id = :paramIdSedeEntita and rb.abilitato = true") })
public class RapportoBancarioSedeEntita extends RapportoBancario {

	private static final long serialVersionUID = 4233883225328955635L;

	public static final String REF = "RapportoBancarioSedeEntita";
	public static final String PROP_ENTITA = "Entita";

	@ManyToOne(fetch = FetchType.LAZY)
	private SedeEntita sedeEntita;

	/**
	 * @return Returns the entita.
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("RapportoBancarioEntita[");
		buffer.append(super.toString());
		buffer.append("]");
		return buffer.toString();
	}

}
