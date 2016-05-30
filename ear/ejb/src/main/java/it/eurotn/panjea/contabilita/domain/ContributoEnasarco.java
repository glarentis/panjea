/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 * 
 */
@Entity
@Audited
@DiscriminatorValue("E")
public class ContributoEnasarco extends ContributoPrevidenziale {

	private static final long serialVersionUID = -833353478848492240L;

	@Override
	public ETipoContoBase getTipoContoBase() {
		return ETipoContoBase.QUOTA_ENASARCO;
	}

}
