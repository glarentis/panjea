/**
 * 
 */
package it.eurotn.panjea.iva.rich.rules;

import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.property.ConditionalPropertyConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;

/**
 * PluginRulesSource per l'area iva.
 * 
 * @author adriano.
 * @version 1.0, 03/ott/2008.
 * 
 */
public class IvaPluginRulesSource extends AbstractPluginRulesSource {
	/**
	 * 
	 * @return .
	 */
	private Rules createRigaIvaRules() {
		return new Rules(RigaIva.class) {

			@Override
			protected void initRules() {
				// add("imponibileVisualizzato", getImportoConstraint());
				add(getImponibileRigaIvaConstraint());
				add("codiceIva", getDomainAttributeRequiredConstraint());
				// add("impostaVisualizzata", REQUIRED_CONSTRAINT);
			}
		};
	}

	/**
	 * L'imponibile della riga iva deve essere diverso da 0 solo nel caso in cui il documento abbia importo a 0.
	 * 
	 * @return imponibile constraint
	 */
	private PropertyConstraint getImponibileRigaIvaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"areaIva.documento.totale.importoInValutaAzienda", or(gt(BigDecimal.ZERO), lt(BigDecimal.ZERO)));
		PropertyConstraint domainObjectIfTrue = new ConditionalPropertyConstraint(propertyConstraint,
				new PropertyValueConstraint("imponibileVisualizzato", getImportoConstraint()));
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("imponibileVisualizzatoRequired");
		return propertyResolvableConstraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eurotn.panjea.rules.AbstractPluginRulesSource#getRules()
	 */
	@Override
	public List<Rules> getRules() {
		List<Rules> rules = new ArrayList<Rules>();
		rules.add(createRigaIvaRules());
		return rules;
	}

}
