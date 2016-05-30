package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.EqualTo;
import org.springframework.rules.constraint.property.PropertiesConstraint;

public class QtaRigaArticoloConstraint extends PropertiesConstraint {

	/**
	 * Costruttore.
	 * 
	 * @param propertyName
	 *            propertyName
	 * @param otherPropertyName
	 *            otherPropertyName
	 */
	public QtaRigaArticoloConstraint(final String propertyName, final String otherPropertyName) {
		super(propertyName, EqualTo.instance(), otherPropertyName);
	}

	@Override
	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		return ((RigaArticolo) domainObjectAccessStrategy.getDomainObject()).isQtaValid();
	}

	@Override
	public String toString() {
		return RcpSupport.getMessage("qtaRigaArticoloConstraint");
	}

}
