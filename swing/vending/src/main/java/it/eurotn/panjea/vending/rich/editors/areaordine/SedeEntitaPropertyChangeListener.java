package it.eurotn.panjea.vending.rich.editors.areaordine;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;

public class SedeEntitaPropertyChangeListener extends AreaOrdineVendingPropertyChangeListener {

    private IVendingDocumentoBD vendingDocumentoBD;

    @Override
    protected AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue) {
        SedeEntita sedeEntita = (SedeEntita) propertyValue;
        return vendingDocumentoBD.aggiornaSedeEntita(areaRifornimento, sedeEntita);
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public final void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }
}