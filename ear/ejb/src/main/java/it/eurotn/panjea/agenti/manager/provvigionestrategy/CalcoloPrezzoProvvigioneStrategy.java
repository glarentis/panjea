package it.eurotn.panjea.agenti.manager.provvigionestrategy;

import java.math.BigDecimal;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;

/**
 * @author fattazzo
 *
 */
@Local
public interface CalcoloPrezzoProvvigioneStrategy {

    /**
     * Calcola il prezzo netto provvigionale in base alla riga articolo passata come parametro.
     * 
     * @param rigaArticolo
     *            riga articolo di riferimento
     * @return prezzo provvigionale
     */
    BigDecimal calcolaPrezzoNetto(RigaArticolo rigaArticolo);

    /**
     * Calcola il prezzo unitario provvigionale in base alla riga articolo passata come parametro.
     * 
     * @param rigaArticolo
     *            riga articolo di riferimento
     * @return prezzo provvigionale
     */
    BigDecimal calcolaPrezzoUnitario(RigaArticolo rigaArticolo);

}
