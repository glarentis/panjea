package it.eurotn.panjea.corrispettivi.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "cont_righe_corrispettivi")
public class RigaCorrispettivo extends EntityBase implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7378852527170750856L;

	/**
	 * @uml.property name="codiceIva"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private CodiceIva codiceIva;

	/**
	 * @uml.property name="importo"
	 */
	private BigDecimal importo;

	/**
	 * @return codiceIva
	 * @uml.property name="codiceIva"
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return importo
	 * @uml.property name="importo"
	 */
	public BigDecimal getImporto() {
		return importo;
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
	 * @param importo
	 *            the importo to set
	 * @uml.property name="importo"
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
}
