package it.eurotn.panjea.giroclienti.manager.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;

@Local
public interface GiroClientiAreaOrdineManager {

    /**
     * Crea un'area ordine.
     *
     * @param rigaGiroCliente
     *            riga giro cliente
     * @param documento
     *            documento
     * @param tipoAreaOrdine
     *            tipo area ordine
     * @param deposito
     *            deposito
     * @return area ordine creata
     */
    AreaOrdine creaAreaOrdine(RigaGiroCliente rigaGiroCliente, Documento documento, TipoAreaOrdine tipoAreaOrdine,
            DepositoLite deposito);

    /**
     * Crea un documento in base alla riga giro cliente specificata.
     *
     * @param rigaGiroCliente
     *            riga giro cliente
     * @param tipoDocumento
     *            tipo documento
     * @return documento creato
     */
    Documento creaDocumento(RigaGiroCliente rigaGiroCliente, TipoDocumento tipoDocumento);
}
