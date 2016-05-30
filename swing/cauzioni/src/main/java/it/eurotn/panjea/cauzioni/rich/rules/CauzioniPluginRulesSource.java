package it.eurotn.panjea.cauzioni.rich.rules;

import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

public class CauzioniPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * Crea le regole per {@link ParametriRicercaSituazioneCauzioni}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaSituazioneCauzioniRules() {
		return new Rules(ParametriRicercaSituazioneCauzioni.class) {
			@Override
			protected void initRules() {
				add("periodo", getPeriodoConstraint(false));
			}
		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();

		listRules.add(createParametriRicercaSituazioneCauzioniRules());

		return listRules;
	}

}
