package it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;

@Local
public interface RigheInserimentoManager {

    /**
     * Carica le righe ordine inserimento in base ai parametri specificati.
     *
     * @param parametri
     *            parametri di ricerca
     * @return righe caricate
     */
    RigheOrdineInserimento caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri);

    /**
     * Genera tutte righe ordine in base alle righe inserimento.
     *
     * @param righeInserimento
     *            righe inserimento
     * @param areaOrdine
     *            area ordine su cui verranno create le righe
     */
    void generaRigheOrdine(List<RigaOrdineInserimento> righeInserimento, AreaOrdine areaOrdine);

}
