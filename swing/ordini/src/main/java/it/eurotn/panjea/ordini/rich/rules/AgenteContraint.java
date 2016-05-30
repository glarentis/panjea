package it.eurotn.panjea.ordini.rich.rules;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.plugin.PluginManager;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.Required;
import org.springframework.rules.constraint.property.RequiredIfTrue;

public class AgenteContraint extends RequiredIfTrue {

	private PluginManager pluginManager = null;

	{
		pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	/**
	 * Costruttore.
	 * 
	 * @param propertyName
	 *            property name
	 * @param predicate
	 *            constraint di test
	 */
	public AgenteContraint(final String propertyName, final Constraint predicate) {
		super(propertyName, predicate);
	}

	@Override
	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		Object object = domainObjectAccessStrategy.getPropertyValue(getPropertyName());

		boolean testResult = Boolean.TRUE;

		if (getConstraint().test(domainObjectAccessStrategy)) {

			testResult = Boolean.FALSE;

			// solo se il plugin degli agenti è presente eseguo il test,
			// altrimenti l'agente non è mai richiesto.
			if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI) && object != null) {
				testResult = Required.instance().test(((IDefProperty) object).getId());
				testResult = testResult && ((IDefProperty) object).getId().intValue() != -1;
			}
		}
		return testResult;
	}

	@Override
	public String toString() {
		return RcpSupport.getMessage("agente") + " " + RcpSupport.getMessage("required");
	}

}
