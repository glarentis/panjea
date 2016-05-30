package it.eurotn.panjea.fido.rich.rules;

import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

public class FidoPluginRulesSource extends AbstractPluginRulesSource {

	@Override
	public List<Rules> getRules() {
		return new ArrayList<Rules>();
	}

}
