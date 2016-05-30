package it.eurotn.panjea.vega.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

import it.eurotn.panjea.rules.AbstractPluginRulesSource;

/**
 * Rules Source per il plugin di AuVend.
 *
 * @author adriano
 * @version 1.0, 30/dic/2008
 *
 */
public class VegaPluginRulesSource extends AbstractPluginRulesSource {

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		return listRules;
	}

}
