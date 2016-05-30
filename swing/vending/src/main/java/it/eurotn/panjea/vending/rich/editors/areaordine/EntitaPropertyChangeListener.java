package it.eurotn.panjea.vending.rich.editors.areaordine;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;

public class EntitaPropertyChangeListener extends AreaOrdineVendingPropertyChangeListener {

    private IVendingDocumentoBD vendingDocumentoBD;

    @Override
    protected AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue) {
        EntitaLite entita = (EntitaLite) propertyValue;
        Integer idEntita = entita != null ? entita.getId() : null;
        return vendingDocumentoBD.aggiornaEntita(areaRifornimento, idEntita);
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public final void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }
}