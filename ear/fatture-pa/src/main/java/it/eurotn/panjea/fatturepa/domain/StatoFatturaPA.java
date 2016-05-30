package it.eurotn.panjea.fatturepa.domain;

import java.util.Set;
import java.util.TreeSet;

public enum StatoFatturaPA {

    /**
     * Da inviare.
     */
    _DI(false), /**
                 * Inviata.
                 */
    _IN(false), /**
                 * Ricevuta di consegna.
                 */
    RC(false), /**
                * Notifica di scarto.
                */
    NS(true), /**
               * Notifica di mancata consegna.
               */
    MC(false), /**
                * Notifica esito cedente/prestatore.
                */
    NE(true), /**
               * Notifica decorrenza termini.
               */
    DT(true), /**
               * Attestazione di avvenuta trasmissione della fattura con impossibilità di recapito.
               */
    AT(true);

    // indica che lo stato chiude anche il ciclo degli esiti della fattura PA
    private boolean statoDiChiusura;

    /**
     * Costruttore.
     *
     * @param statoDiChiusura
     *            indica che lo stato chiude anche il ciclo degli esiti della fattura PA
     */
    private StatoFatturaPA(final boolean statoDiChiusura) {
        this.statoDiChiusura = statoDiChiusura;
    }

    /**
     * Restituisce tutti gli stati che indicano che la fattura PA risulta chiusa.
     *
     * @return stati
     */
    public static final Set<StatoFatturaPA> getStatiChiusura() {
        Set<StatoFatturaPA> result = new TreeSet<StatoFatturaPA>();
        for (StatoFatturaPA statoFatturaPA : StatoFatturaPA.values()) {
            if (statoFatturaPA.isStatoDiChiusura()) {
                result.add(statoFatturaPA);
            }
        }
        return result;
    }

    /**
     * @return the statoDiChiusura
     */
    public boolean isStatoDiChiusura() {
        return statoDiChiusura;
    }

}
