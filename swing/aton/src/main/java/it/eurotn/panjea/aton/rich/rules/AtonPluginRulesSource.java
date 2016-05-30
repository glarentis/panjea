package it.eurotn.panjea.aton.rich.rules;

import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

/**
 * Rules Source per il plugin di Aton.
 * 
 * @author giangi
 * @version 1.0, 25 Nov. 2010
 * 
 */
public class AtonPluginRulesSource extends AbstractPluginRulesSource {

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		return listRules;
	}

}
