package it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;

@Local
public interface AttributiInserimentoLoader {

    /**
     * Aggiunte tutti gli attributi articolo alla riga inserimento.
     *
     * @param rigaOrdineInserimento
     *            riga inserimento
     * @return riga con gli attributi
     */
    RigaOrdineInserimento fillAttributi(RigaOrdineInserimento rigaOrdineInserimento);

}
