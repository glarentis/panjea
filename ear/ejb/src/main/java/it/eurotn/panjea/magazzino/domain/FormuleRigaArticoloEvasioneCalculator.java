package it.eurotn.panjea.magazzino.domain;

/**
 * Calcola gli attributi della riga articolo durante l'evasione.
 *
 * @author giangi
 * @version 1.0, 19/feb/2013
 *
 */
public class FormuleRigaArticoloEvasioneCalculator extends FormuleRigaArticoloCalculator {
    @Override
    protected boolean attributoCalcolato(AttributoRigaArticolo attributoRiga) {
        boolean result = super.attributoCalcolato(attributoRiga);
        return result && attributoRiga.isRicalcolaInEvasione();
    }

    @Override
    protected AttributoRiga creaAttributoQta(IRigaArticoloDocumento rigaArticoloDocumento) {
        // In evasione la formula della qta non deve essere considerata perchè utilizzo la qtaDaEvadere
        AttributoRiga attributoQta = super.creaAttributoQta(rigaArticoloDocumento);
        attributoQta.setValore(rigaArticoloDocumento.getQta().toString().replace(".", ","));
        attributoQta.setFormula(null);
        return attributoQta;
    }
}
