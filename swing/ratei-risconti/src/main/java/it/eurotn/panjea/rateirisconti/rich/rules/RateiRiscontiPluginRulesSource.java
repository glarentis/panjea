package it.eurotn.panjea.rateirisconti.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

import it.eurotn.panjea.rules.AbstractPluginRulesSource;

public class RateiRiscontiPluginRulesSource extends AbstractPluginRulesSource {

    @Override
    public List<Rules> getRules() {
        return new ArrayList<Rules>();
    }

}
