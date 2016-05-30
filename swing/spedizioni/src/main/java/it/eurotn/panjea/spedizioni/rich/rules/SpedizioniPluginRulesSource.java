package it.eurotn.panjea.spedizioni.rich.rules;

import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaRendicontazione;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

public class SpedizioniPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * Crea le regole per {@link ParametriCreazioneEtichette}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriCreazioneEtichetteRules() {
		return new Rules(ParametriCreazioneEtichette.class) {
			@Override
			protected void initRules() {
				add("numeroColli", getRequiredConstraint());
				add("peso", getRequiredConstraint());
				add("modalitaIncasso", getRequiredConstraint());
				add("consegna", getRequiredConstraint());
				add(new ModalitaIncassoPropertyConstraint("modalitaIncasso", "importoContrassegno"));
			}
		};
	}

	/**
	 * Crea le regole per {@link ParametriRicercaRendicontazione}.
	 * 
	 * @return regole create
	 */
	private Rules createParametriRicercaRendicontazioneRules() {
		return new Rules(ParametriRicercaRendicontazione.class) {
			@Override
			protected void initRules() {
				add("vettore", getDomainAttributeRequiredConstraint());
			}
		};
	}

	@Override
	public List<Rules> getRules() {

		List<Rules> listRules = new ArrayList<Rules>();

		listRules.add(createParametriCreazioneEtichetteRules());

		listRules.add(createParametriRicercaRendicontazioneRules());

		return listRules;
	}
}
