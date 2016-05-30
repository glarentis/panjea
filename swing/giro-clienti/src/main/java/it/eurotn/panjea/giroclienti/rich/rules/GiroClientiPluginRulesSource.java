package it.eurotn.panjea.giroclienti.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;

import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.rich.editors.riepilogo.RiepilogoGiornalieroPM;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.GiroSedeClientePM;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.header.copiascheda.SchedaCopiaPM;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

public class GiroClientiPluginRulesSource extends AbstractPluginRulesSource {

    /**
     * @return regole per {@link GiroSedeClientePM}
     */
    private Rules createGiroSedeClientePMRules() {
        return new Rules(GiroSedeClientePM.class) {
            @Override
            protected void initRules() {
                add("giroSedeCliente.giorno", getRequiredConstraint());
                add("giroSedeCliente.ora", getRequiredConstraint());
                add("giroSedeCliente.sedeEntita", getDomainAttributeRequiredConstraint());
                add("giroSedeCliente.utente", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * @return regole per {@link GiroSedeCliente}
     */
    private Rules createGiroSedeClienteRules() {
        return new Rules(GiroSedeCliente.class) {
            @Override
            protected void initRules() {

                add("giorno", getRequiredConstraint());
                add("ora", getRequiredConstraint());
                add("sedeEntita", getDomainAttributeRequiredConstraint());
                add("utente", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * @return regole per {@link RiepilogoGiornalieroPM}
     */
    private Rules createRiepilogoGiornalieroPMRules() {
        return new Rules(RiepilogoGiornalieroPM.class) {
            @Override
            protected void initRules() {
                add("data", getRequiredConstraint());
                add("utenti", getCollectionRequiredRequired());
            }
        };
    }

    /**
     * @return regole per {@link SchedaCopiaPM}
     */
    private Rules createSchedaCopiaPMRules() {
        return new Rules(SchedaCopiaPM.class) {
            @Override
            protected void initRules() {
                add("utente", getRequiredConstraint());
                add("giorno", getRequiredConstraint());
                add("utenteDestinazione", getRequiredConstraint());
                add("giornoDestinazione", getRequiredConstraint());
                add("modalitaCopia", getRequiredConstraint());
            }
        };
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();
        listRules.add(createGiroSedeClienteRules());
        listRules.add(createGiroSedeClientePMRules());
        listRules.add(createRiepilogoGiornalieroPMRules());
        listRules.add(createSchedaCopiaPMRules());

        return listRules;
    }

}
