package it.eurotn.panjea.ordini.manager.documento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.RigaOrdine;

@Local
public interface RigheCollegateManager {

    /**
     * Rimuove il collegamento fra due righe ordine
     * 
     * @param rigaOrdineOrigine
     *            id riga origine
     * @param rigaOrdineDestinazione
     *            id riga destinazione
     */
    void cancellaRigheCollegate(int rigaOrdineOrigine, int rigaOrdineDestinazione);

    /**
     * Carica tutte le righe collegate alle riga ordine passata come parametro.
     * 
     * @param rigaOrdine
     *            riga ordine di riferimento
     * @return righe collegate caricate
     */
    List<RigaDestinazione> caricaRigheCollegate(RigaOrdine rigaOrdine);
}
