package it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;

@Local
public interface RigheInserimentoLoader {

    /**
     * Carica le righe ordine inserimento in base ai parametri specificati.
     *
     * @param parametri
     *            parametri di ricerca
     * @return righe caricate
     */
    List<RigaOrdineInserimento> caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri);
}
