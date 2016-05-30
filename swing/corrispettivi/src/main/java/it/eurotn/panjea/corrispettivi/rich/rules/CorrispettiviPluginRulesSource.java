package it.eurotn.panjea.corrispettivi.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

public class CorrispettiviPluginRulesSource extends AbstractPluginRulesSource {

    /**
     * crea le regole di validazione per {@link Corrispettivo}.
     *
     * @return regole create
     */
    private Rules createCorrispettivoRules() {
        return new Rules(Corrispettivo.class) {

            @Override
            protected void initRules() {
                add("totale", getRequiredConstraint());
            };
        };
    }

    @Override
    public List<Rules> getRules() {

        List<Rules> rules = new ArrayList<Rules>();
        rules.add(createCorrispettivoRules());

        return rules;
    }
}
