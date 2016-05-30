package it.eurotn.panjea.vending.rich.editors.areaordine;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;

public class InstallazionePropertyChangeListener extends AreaOrdineVendingPropertyChangeListener {

    private IVendingDocumentoBD vendingDocumentoBD;

    @Override
    protected AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue) {
        Installazione installazione = (Installazione) propertyValue;
        return vendingDocumentoBD.aggiornaDatiInstallazione(areaRifornimento, installazione);
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public final void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }
}