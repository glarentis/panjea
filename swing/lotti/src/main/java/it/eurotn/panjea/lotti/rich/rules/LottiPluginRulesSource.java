package it.eurotn.panjea.lotti.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.util.ParametriRicercaLotti;
import it.eurotn.panjea.lotti.util.ParametriRicercaScadenzaLotti;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

/**
 * Rules Source per il plugin della gestione lotti.
 *
 * @author giangi
 * @version 1.0, 25 Nov. 2010
 *
 */
public class LottiPluginRulesSource extends AbstractPluginRulesSource {

    /**
     * @return regole per la validazione del {@link Lotto}
     */
    private Rules createLottoRules() {
        return new Rules(Lotto.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("priorita", getRequiredConstraint());
                add("articolo", getDomainAttributeRequiredConstraint());
                add("articolo.tipoLotto", not(eq(TipoLotto.NESSUNO)));
            }
        };
    }

    /**
     * @return regole per la validazione del {@link ParametriRicercaLotti}
     */
    private Rules createParametriRicercaLottiRules() {
        return new Rules(ParametriRicercaLotti.class) {
            @Override
            protected void initRules() {
                add("lotto", getRequiredConstraint());
            }
        };
    }

    /**
     * @return regole per la validazione del {@link ParametriRicercaScadenzaLotti}
     */
    private Rules createParametriRicercaScadenzaLottiRules() {
        return new Rules(ParametriRicercaScadenzaLotti.class) {
            @Override
            protected void initRules() {
                add("deposito", getDomainAttributeRequiredConstraint());
                add("dataScadenza", getRequiredConstraint());
            }
        };
    }

    /**
     * @return regole per la validazione della {@link RigaLotto}
     */
    private Rules createRigaLottoRules() {
        return new Rules(RigaLotto.class) {
            @Override
            protected void initRules() {
                add("lotto", getRequiredConstraint());
                add("quantita", new Constraint[] { getRequiredConstraint(), gt(0.0) });
            }
        };
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        listRules.add(createRigaLottoRules());
        listRules.add(createLottoRules());
        listRules.add(createParametriRicercaLottiRules());
        listRules.add(createParametriRicercaScadenzaLottiRules());

        return listRules;
    }

}