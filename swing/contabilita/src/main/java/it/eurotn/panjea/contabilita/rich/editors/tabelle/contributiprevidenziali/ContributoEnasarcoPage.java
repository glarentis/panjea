package it.eurotn.panjea.contabilita.rich.editors.tabelle.contributiprevidenziali;

import it.eurotn.panjea.contabilita.domain.ContributoEnasarco;

/**
 * @author fattazzo
 *
 */
public class ContributoEnasarcoPage extends ContributoPrevidenzialePage {

    /**
     * Costruttore.
     */
    public ContributoEnasarcoPage() {
        super("contributoEnasarcoPage", new ContributoPrevidenzialeForm(new ContributoEnasarco()));
    }
}
