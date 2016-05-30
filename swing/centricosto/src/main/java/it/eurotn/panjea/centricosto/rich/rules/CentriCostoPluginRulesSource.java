package it.eurotn.panjea.centricosto.rich.rules;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

/**
 * Rules Source per il plugin dei centri di costo.
 * 
 * @author giangi
 * @version 1.0, 25 Nov. 2010
 * 
 */
public class CentriCostoPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return Rules per la classe {@link CentroCosto}
	 */
	private Rules createCentroCostoRules() {
		return new Rules(CentroCosto.class) {
			@Override
			protected void initRules() {
				super.initRules();
				add("codice", maxLength(10));
				add("codice", required());
				add("descrizione", maxLength(30));
				add("descrizione", required());
			}
		};
	}

	/**
	 * 
	 * @return regole per la classe {@link CentroCosto}
	 */
	private Rules createRigaCentroCostoRules() {
		return new Rules(RigaCentroCosto.class) {
			@Override
			protected void initRules() {
				super.initRules();
				add("importo", required());
				add("centroCosto", getDomainAttributeRequiredConstraint());
				add("nota", maxLength(30));
			}
		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		listRules.add(createCentroCostoRules());
		listRules.add(createRigaCentroCostoRules());
		return listRules;
	}

}