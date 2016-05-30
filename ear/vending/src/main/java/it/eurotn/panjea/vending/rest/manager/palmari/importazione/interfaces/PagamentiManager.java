package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import java.math.BigDecimal;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

@Local
public interface PagamentiManager {

    /**
     * Se il tipoAreaMagazzino genera delle rate ed il codice pagamento del cliente (quindi quello
     * dell'area magazzino)<br/>
     * ha un tipoAreaTesoreria per la chiusura automatica prendo l'incassato e chiudo le rate.
     *
     * @param areaMagazzino
     *            amagazzinio
     * @param incasso
     *            da associare al pagamento
     */
    public void creaPagamenti(AreaMagazzino areaMagazzino, BigDecimal incasso);

    /**
     * Crea il pagamento con il valore sul campo incasso dell'area rifornimento
     *
     * @param areaRifornimento
     *            ar
     */
    public void creaPagamenti(AreaRifornimento areaRifornimento);
}
