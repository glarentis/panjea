package it.eurotn.panjea.vending.rich.editors.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.util.Objects;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaFormModel;

public class OperatorePropertyChangeListener implements FormModelPropertyChangeListeners {

    private FormModel formModel;

    private IManutenzioniBD manutenzioniBD;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (formModel.isReadOnly() || ((PanjeaFormModel) formModel).isAdjustingMode()
                || Objects.equals(evt.getOldValue(), evt.getNewValue())) {
            return;
        }

        Operatore operatore = (Operatore) evt.getNewValue();
        DepositoLite deposito = null;
        if (operatore != null) {
            operatore = manutenzioniBD.caricaOperatoreById(operatore.getId());
            if (operatore.getMezzoTrasporto() != null) {
                deposito = operatore.getMezzoTrasporto().getDeposito();
            } else {
                IAnagraficaBD anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
                deposito = anagraficaBD.caricaDepositoPrincipale().creaLite();
            }
        }
        formModel.getValueModel("areaMagazzino.depositoOrigine").setValue(deposito);
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }
}