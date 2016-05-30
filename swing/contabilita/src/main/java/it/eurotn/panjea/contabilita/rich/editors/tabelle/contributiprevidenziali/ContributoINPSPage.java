package it.eurotn.panjea.contabilita.rich.editors.tabelle.contributiprevidenziali;

import it.eurotn.panjea.contabilita.domain.ContributoINPS;

/**
 * @author fattazzo
 *
 */
public class ContributoINPSPage extends ContributoPrevidenzialePage {

    /**
     * Costruttore.
     */
    public ContributoINPSPage() {
        super("contributoINPSPage", new ContributoPrevidenzialeForm(new ContributoINPS()));
    }
}
