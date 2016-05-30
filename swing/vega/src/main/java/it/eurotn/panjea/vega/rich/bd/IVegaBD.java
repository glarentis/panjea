package it.eurotn.panjea.vega.rich.bd;

import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

public interface IVegaBD {

    /**
     *
     * @return lista di entità con codice esterno da importare.
     */
    List<EntitaLite> caricaEntitaConCodiceEsternoDaConfermare();

    /**
     *
     * @return num clienti da verificare.
     */
    long verificaClientiDaImportare();

}
