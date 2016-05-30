package it.eurotn.panjea.vending.rich.editors.areamagazzino;

import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;

public class DistributorePropertyChangeListener extends AreaMagazzinoVendingPropertyChangeListener {

    private IVendingDocumentoBD vendingDocumentoBD;

    @Override
    protected AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue) {
        Distributore distributore = (Distributore) propertyValue;
        Integer idDistributore = distributore != null ? distributore.getId() : null;
        return vendingDocumentoBD.aggiornaDistributore(areaRifornimento, idDistributore);
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public final void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }
}