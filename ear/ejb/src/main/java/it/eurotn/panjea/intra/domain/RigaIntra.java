/**
 *
 */
package it.eurotn.panjea.intra.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "intr_righe_intra")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_RIGA", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("R")
public abstract class RigaIntra extends EntityBase {

	private static final long serialVersionUID = 3140116373424837994L;

	@ManyToOne(fetch = FetchType.LAZY)
	protected AreaIntra areaIntra;

	@Embedded
	protected Importo importo;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public RigaIntra() {
		importo = new Importo();
	}

	/**
	 * @return the areaIntra
	 */
	public AreaIntra getAreaIntra() {
		return areaIntra;
	}

	/**
	 * @return the importo
	 */
	public Importo getImporto() {
		return importo;
	}

	/**
	 * @param areaIntra
	 *            the areaIntra to set
	 */
	public void setAreaIntra(AreaIntra areaIntra) {
		this.areaIntra = areaIntra;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(Importo importo) {
		this.importo = importo;
	}

}
