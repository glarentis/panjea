/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.IRigaComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;

import java.util.Set;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.EqualTo;
import org.springframework.rules.constraint.property.PropertiesConstraint;

/**
 * @author fattazzo
 * 
 */
public class QtaComponentiConstraint extends PropertiesConstraint {

	/**
	 * Costruttore.
	 * 
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public QtaComponentiConstraint(String componentiPropertyName, String classeTipoDocumentoPropertyName) {
		super(componentiPropertyName, EqualTo.instance(), classeTipoDocumentoPropertyName);
	}

	@Override
	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		Set<IRigaComponente> componenti = (Set<IRigaComponente>) domainObjectAccessStrategy
				.getPropertyValue(getPropertyName());

		boolean testResult = true;
		if (componenti != null) {
			for (IRigaComponente rigaComponente : componenti) {
				RigaArticolo rigaArticolo = (RigaArticolo) domainObjectAccessStrategy.getDomainObject();
				((RigaArticoloComponente) rigaComponente).setAreaMagazzino(rigaArticolo.getAreaMagazzino());
				QtaRigaArticoloConstraint classeConstraint = new QtaRigaArticoloConstraint(getOtherPropertyName(),
						"qta");
				testResult = classeConstraint.test(rigaComponente);
				if (!testResult) {
					break;
				}
			}
		}

		return testResult;
	}

	@Override
	public String toString() {
		return RcpSupport.getMessage("qtaComponentiConstraint");
	}

}
