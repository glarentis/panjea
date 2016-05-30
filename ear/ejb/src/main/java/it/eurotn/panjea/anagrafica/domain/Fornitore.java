package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.audit.envers.AuditableProperties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@DiscriminatorValue("F")
@AuditableProperties(properties = { "anagrafica", "anagrafica.sedeAnagrafica" })
public class Fornitore extends Entita {

	private static final long serialVersionUID = -7844553161046100262L;

	@Embedded
	private DatiRitenutaAccontoEntita datiRitenutaAcconto;

	private static int ordine = 2;

	/**
	 * Costruttore.
	 *
	 */
	public Fornitore() {
		super();
	}

	/**
	 * @return the datiRitenutaAcconto
	 */
	public DatiRitenutaAccontoEntita getDatiRitenutaAcconto() {
		if (datiRitenutaAcconto == null) {
			datiRitenutaAcconto = new DatiRitenutaAccontoEntita();
		}
		return datiRitenutaAcconto;
	}

	/**
	 * @return ordine
	 */
	@Override
	public int getOrdine() {
		return ordine;
	}

	@Override
	public TipoEntita getTipo() {
		return TipoEntita.FORNITORE;
	}

	/**
	 * @param datiRitenutaAcconto
	 *            the datiRitenutaAcconto to set
	 */
	public void setDatiRitenutaAcconto(DatiRitenutaAccontoEntita datiRitenutaAcconto) {
		this.datiRitenutaAcconto = datiRitenutaAcconto;
	}

}
