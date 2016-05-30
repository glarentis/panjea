/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.rules;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

/**
 * 
 * @author adriano
 * @version 1.0, 11/mag/07
 */
public class ProtocolliPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * Crea le regole per {@link ProtocolloAnno}.
	 * 
	 * @return regole create
	 */
	private Rules createProtocolloAnnoRules() {
		return new Rules(ProtocolloAnno.class) {

			@Override
			protected void initRules() {
				add(ProtocolloAnno.PROP_ANNO, getRequiredConstraint());
				add(ProtocolloAnno.PROP_ANNO, getMaxCharConstraint(4));
				add(ProtocolloAnno.PROP_VALORE, getRequiredConstraint());
			}

		};
	}

	/**
	 * Crea le regole per {@link Protocollo}.
	 * 
	 * @return regole create
	 */
	private Rules createProtocolloRules() {
		return new Rules(Protocollo.class) {

			@Override
			protected void initRules() {
				add(Protocollo.PROP_CODICE, getRequiredConstraint());
				add(Protocollo.PROP_DESCRIZIONE, getRequiredConstraint());
				add(Protocollo.PROP_CODICE, getMaxCharConstraint(10));
				add(Protocollo.PROP_DESCRIZIONE, getMaxCharConstraint(60));
			}

		};

	}

	/**
	 * Crea le regole per {@link ProtocolloValore}.
	 * 
	 * @return regole create
	 */
	private Rules createProtocolloValoreRules() {
		return new Rules(ProtocolloValore.class) {

			@Override
			protected void initRules() {
				add(Protocollo.PROP_CODICE, getRequiredConstraint());
				add(Protocollo.PROP_CODICE, getMaxCharConstraint(5));
				add(Protocollo.PROP_DESCRIZIONE, getMaxCharConstraint(60));
				add(Protocollo.PROP_DESCRIZIONE, getRequiredConstraint());
				add(ProtocolloValore.PROP_VALORE, getRequiredConstraint());
			}

		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> rules = new ArrayList<Rules>();
		rules.add(createProtocolloRules());
		rules.add(createProtocolloAnnoRules());
		rules.add(createProtocolloValoreRules());
		return rules;
	}
}
