package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

@Local
public interface OrdiniCollegatiManager {

    /**
     * Collega un area rifornimento all'ordine.<br/>
     * Se l'ordine ha una fattura collegata
     * 
     * @param idAreaOrdine
     *            id ordine da collegare
     *
     * @param areaRifornimento
     *            area rifornimento da collegare
     */
    void collega(Integer idAreaOrdine, AreaRifornimento areaRifornimento);

}
