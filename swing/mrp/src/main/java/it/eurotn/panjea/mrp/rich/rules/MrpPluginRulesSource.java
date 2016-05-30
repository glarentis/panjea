package it.eurotn.panjea.mrp.rich.rules;

import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

public class MrpPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return regole per la validazione del {@link ParametriCalcoloMrp}
	 */
	private Rules createParametriCalcoloMrpRules() {
		return new Rules(ParametriMrpRisultato.class) {
			@Override
			protected void initRules() {
				add("dataInizio", getRequiredConstraint());
				add("numBucket", getRequiredConstraint());
			}
		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> rules = new ArrayList<Rules>();
		rules.add(createParametriCalcoloMrpRules());
		return rules;
	}

}
