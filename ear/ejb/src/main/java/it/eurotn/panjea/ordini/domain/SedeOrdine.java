package it.eurotn.panjea.ordini.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "ordi_sedi_ordini")
@NamedQueries({
	@NamedQuery(name = "SedeOrdine.caricaBySedeEntita", query = "select so from SedeOrdine so where so.sedeEntita.id = :paramIdSedeEntita "),
	@NamedQuery(name = "SedeOrdine.caricaPrincipaleByEntita", query = " select so from SedeOrdine so where so.sedeEntita.entita.id = :paramIdEntita and so.sedeEntita.tipoSede.sedePrincipale=true ")

})
@AuditableProperties(excludeProperties = { "sedeEntita" })
public class SedeOrdine extends EntityBase {

	private static final long serialVersionUID = 4035508134634276029L;

	/**
	 * @uml.property name="sedeEntita"
	 * @uml.associationEnd
	 */
	@OneToOne
	private SedeEntita sedeEntita;

	/**
	 * @uml.property name="tipoAreaEvasione"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private TipoAreaMagazzino tipoAreaEvasione;

	/**
	 * Costruttore.
	 *
	 */
	public SedeOrdine() {
		super();
	}

	/**
	 * @return the sedeEntita
	 * @uml.property name="sedeEntita"
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the tipoAreaEvasione
	 * @uml.property name="tipoAreaEvasione"
	 */
	public TipoAreaMagazzino getTipoAreaEvasione() {
		return tipoAreaEvasione;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 * @uml.property name="sedeEntita"
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param tipoAreaEvasione
	 *            the tipoAreaEvasione to set
	 * @uml.property name="tipoAreaEvasione"
	 */
	public void setTipoAreaEvasione(TipoAreaMagazzino tipoAreaEvasione) {
		this.tipoAreaEvasione = tipoAreaEvasione;
	}

}
