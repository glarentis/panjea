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
@DiscriminatorValue("I")
public class ContributoINPS extends ContributoPrevidenziale {

	private static final long serialVersionUID = -7259477268935072571L;

	@Override
	public ETipoContoBase getTipoContoBase() {
		return ETipoContoBase.QUOTA_INPS;
	}

}
