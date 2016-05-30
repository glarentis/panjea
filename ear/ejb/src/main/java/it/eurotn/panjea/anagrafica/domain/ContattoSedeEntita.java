/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Object Domain della mansione di {@link Contatto} all'interno di
 * {@link SedeEntita}.
 * 
 * @author adriano
 * @version 1.0, 17/dic/07
 */
@Entity
@Audited
@Table(name = "anag_contatti_sede_entita")
@NamedQueries({
		@NamedQuery(name = "ContattoSedeEntita.caricaPerSedeEntita", query = " from ContattoSedeEntita c where c.sedeEntita.id = :paramIdSedeEntita "),
		@NamedQuery(name = "ContattoSedeEntita.caricaPerEntita", query = " from ContattoSedeEntita c where c.sedeEntita.entita.id = :paramIdEntita ") })
public class ContattoSedeEntita extends EntityBase {

	private static final long serialVersionUID = 1571981179140466774L;

	public static final String REF = "MansioneSede";
	public static final String PROP_CONTATTO = "contatto";
	public static final String PROP_SEDE_ENTITA = "sedeEntita";
	public static final String PROP_MANSIONE = "mansione";
	public static final String PROP_ID = "id";

	/**
	 * @uml.property name="contatto"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private Contatto contatto;
	/**
	 * @uml.property name="mansione"
	 * @uml.associationEnd
	 */
	@ManyToOne(optional = false)
	private Mansione mansione;
	/**
	 * @uml.property name="sedeEntita"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private SedeEntita sedeEntita;
	/**
	 * @uml.property name="codiceImportazione"
	 */
	@Column(length = 6)
	private String codiceImportazione;

	/**
	 * Costruttore.
	 * 
	 */
	public ContattoSedeEntita() {
		this.initialize();
	}

	/**
	 * @return codiceImportazione
	 * @uml.property name="codiceImportazione"
	 */
	public String getCodiceImportazione() {
		return codiceImportazione;
	}

	/**
	 * @return Returns the contatto.
	 * @uml.property name="contatto"
	 */
	public Contatto getContatto() {
		return contatto;
	}

	/**
	 * @return Returns the mansione.
	 * @uml.property name="mansione"
	 */
	public Mansione getMansione() {
		return mansione;
	}

	/**
	 * @return Returns the sedeEntita.
	 * @uml.property name="sedeEntita"
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.contatto = new Contatto();
		this.mansione = new Mansione();
		this.sedeEntita = new SedeEntita();
	}

	/**
	 * @param codiceImportazione
	 *            the codiceImportazione to set
	 * @uml.property name="codiceImportazione"
	 */
	public void setCodiceImportazione(String codiceImportazione) {
		this.codiceImportazione = codiceImportazione;
	}

	/**
	 * @param contatto
	 *            The contatto to set.
	 * @uml.property name="contatto"
	 */
	public void setContatto(Contatto contatto) {
		this.contatto = contatto;
	}

	/**
	 * @param mansione
	 *            The mansione to set.
	 * @uml.property name="mansione"
	 */
	public void setMansione(Mansione mansione) {
		this.mansione = mansione;
	}

	/**
	 * @param sedeEntita
	 *            The sedeEntita to set.
	 * @uml.property name="sedeEntita"
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ContattoSedeEntita[");
		buffer.append(super.toString());
		buffer.append(" contatto = ").append(contatto != null ? contatto.getId() : null);
		buffer.append(" mansione = ").append(mansione != null ? mansione.getId() : null);
		buffer.append(" sedeEntita = ").append(sedeEntita != null ? sedeEntita.getId() : null);
		buffer.append("]");
		return buffer.toString();
	}
}
