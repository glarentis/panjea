package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.Listino;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.EqualTo;
import org.springframework.rules.constraint.property.PropertiesConstraint;

public class ListinoValutaPropertiesConstraint extends PropertiesConstraint {

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param listinoPropertyName
	 *            proprietà del listino
	 * @param codiceValutaConfrontoPropertyName
	 *            proprietà del codice valuta sul quale effettuare il ocnfronto
	 */
	public ListinoValutaPropertiesConstraint(final String listinoPropertyName,
			final String codiceValutaConfrontoPropertyName) {
		super(listinoPropertyName, EqualTo.instance(), codiceValutaConfrontoPropertyName);
	}

	@Override
	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		Listino listino = (Listino) domainObjectAccessStrategy.getPropertyValue(getPropertyName());
		String codiceValuta = (String) domainObjectAccessStrategy.getPropertyValue(getOtherPropertyName());
		boolean testResult = true;

		if (listino != null && listino.getId() != null) {
			testResult = getConstraint().test(listino.getCodiceValuta(), codiceValuta);
		}

		return testResult;
	}

	@Override
	public String toString() {
		return RcpSupport.getMessage("listinoValutaPropertiesConstraint");
	}
}
