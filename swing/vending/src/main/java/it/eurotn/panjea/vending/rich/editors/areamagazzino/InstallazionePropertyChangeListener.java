package it.eurotn.panjea.vending.rich.editors.areamagazzino;

import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;

public class InstallazionePropertyChangeListener extends AreaMagazzinoVendingPropertyChangeListener {

    private IVendingDocumentoBD vendingDocumentoBD;

    @Override
    protected AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue) {
        Installazione installazione = (Installazione) propertyValue;

        List<DepositoLite> depositiAggiuntivi = new ArrayList<>();
        if (installazione != null && !installazione.isNew() && installazione.getPozzetto() != null
                && !installazione.getPozzetto().isNew()) {
            depositiAggiuntivi.add(installazione.getPozzetto().creaLite());
        }
        formModel.getValueModel("depositiAggiuntivi").setValue(depositiAggiuntivi);

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