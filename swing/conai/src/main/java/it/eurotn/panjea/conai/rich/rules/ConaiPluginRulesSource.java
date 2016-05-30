package it.eurotn.panjea.conai.rich.rules;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiEsenzione;
import it.eurotn.panjea.conai.domain.ConaiListino;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

public class ConaiPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return regole per la validazione del {@link ConaiArticolo}
	 */
	private Rules createConaiArticoloRules() {
		return new Rules(ConaiArticolo.class) {
			@Override
			protected void initRules() {
				add("articolo", getDomainAttributeRequiredConstraint());
			}
		};
	}

	private Rules createConaiComponenteRules() {
		return new Rules(ConaiComponente.class) {
			@Override
			protected void initRules() {
				add("materiale", getRequiredConstraint());
				add("tipoImballo", getRequiredConstraint());
			}
		};
	}

	/**
	 * @return regole per la validazione del {@link ConaiEsenzione}
	 */
	private Rules createConaiEsenzioneRules() {
		return new Rules(ConaiEsenzione.class) {
			@Override
			protected void initRules() {
				add("entita", getDomainAttributeRequiredConstraint());
				add("percentualeEsenzione", getRequiredConstraint());
			}
		};
	}

	/**
	 * @return regole per la validazione del {@link ConaiListino}
	 */
	private Rules createConaiListinoRules() {
		return new Rules(ConaiListino.class) {
			@Override
			protected void initRules() {
				add("dataInizio", getRequiredConstraint());
				add("dataFine", getRequiredConstraint());
				add("prezzo", getRequiredConstraint());
			}
		};
	}

	/**
	 * @return regole per la validazione del {@link ConaiParametriCreazione}
	 */
	private Rules createConaiParametriCreazioneRules() {
		return new Rules(ConaiParametriCreazione.class) {
			@Override
			protected void initRules() {
				add("pathCreazione", getRequiredConstraint());
				add("data", getRequiredConstraint());
				add("anno", getRequiredConstraint());
			}
		};
	}

	/**
	 * Crea le regole per {@link ParametriRicercaAnalisi}.
	 *
	 * @return regole create
	 */
	private Rules createParametriRicercaAnalisiRules() {
		return new Rules(ParametriRicercaAnalisi.class) {
			@Override
			protected void initRules() {
				add("periodo", getPeriodoConstraint(false));
			}
		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();

		listRules.add(createConaiParametriCreazioneRules());
		listRules.add(createConaiArticoloRules());
		listRules.add(createConaiListinoRules());
		listRules.add(createConaiEsenzioneRules());
		listRules.add(createParametriRicercaAnalisiRules());
		listRules.add(createConaiComponenteRules());

		return listRules;
	}

}
