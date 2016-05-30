/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Rappresenta il codice iva prevalente che verrà utilizzato per creare la riga
 * iva del documento in base al tipo documento e all'entità se specificata.
 * 
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "cont_codici_iva_prevalenti")
@NamedQueries({
		@NamedQuery(name = "CodiceIvaPrevalente.ricercaByTipoAreaContabile", query = "select c from CodiceIvaPrevalente c where c.tipoAreaContabile.id = :paramIdTipoAreaContabile and c.entita is null "),
		@NamedQuery(name = "CodiceIvaPrevalente.ricercaByTipoAreaContabileEntita", query = "select c from CodiceIvaPrevalente c where c.tipoAreaContabile.id = :paramIdTipoAreaContabile and c.entita.id = :paramIdEntita ") })
public class CodiceIvaPrevalente extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = 5151270748254228654L;

	/**
	 * @uml.property name="tipoAreaContabile"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private TipoAreaContabile tipoAreaContabile;

	/**
	 * @uml.property name="entita"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private EntitaLite entita;

	/**
	 * @uml.property name="codiceIva"
	 * @uml.associationEnd
	 */
	@ManyToOne(optional = false)
	private CodiceIva codiceIva;

	/**
	 * Costruttore.
	 * 
	 */
	public CodiceIvaPrevalente() {
		super();
	}

	/**
	 * @return codiceIva
	 * @uml.property name="codiceIva"
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return entita
	 * @uml.property name="entita"
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return tipoAreaContabile
	 * @uml.property name="tipoAreaContabile"
	 */
	public TipoAreaContabile getTipoAreaContabile() {
		return tipoAreaContabile;
	}

	/**
	 * @param codiceIva
	 *            the codiceIva to set
	 * @uml.property name="codiceIva"
	 */
	public void setCodiceIva(CodiceIva codiceIva) {
		this.codiceIva = codiceIva;
	}

	/**
	 * @param entitaLite
	 *            the entitaLite to set
	 * @uml.property name="entita"
	 */
	public void setEntita(EntitaLite entitaLite) {
		this.entita = entitaLite;
	}

	/**
	 * @param tipoAreaContabile
	 *            the tipoAreaContabile to set
	 * @uml.property name="tipoAreaContabile"
	 */
	public void setTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
		this.tipoAreaContabile = tipoAreaContabile;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CodiceIvaPrevalente[");
		buffer.append("codiceIva = ").append(codiceIva);
		buffer.append(", entita = ").append(entita);
		buffer.append(", tipoAreaContabile = ").append(tipoAreaContabile);
		buffer.append("]");
		return buffer.toString();
	}

}
