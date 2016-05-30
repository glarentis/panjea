package it.eurotn.panjea.vending.rich.editors.areaordine;

import java.beans.PropertyChangeEvent;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.binding.form.FormModel;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaFormModel;

public abstract class AreaOrdineVendingPropertyChangeListener implements FormModelPropertyChangeListeners {

    protected FormModel formModel;

    protected abstract AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue);

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (formModel.isReadOnly() || ((PanjeaFormModel) formModel).isAdjustingMode()
                || Objects.equals(evt.getOldValue(), evt.getNewValue())) {
            return;
        }

        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) formModel.getFormObject();
        if (areaOrdineFullDTO.getAreaOrdine().getTipoAreaOrdine() == null || areaOrdineFullDTO.getAreaOrdine()
                .getTipoAreaOrdine().getTipoDocumento().getTipoEntita() != TipoEntita.CLIENTE) {
            return;
        }

        AreaRifornimento areaRifornimento = ObjectUtils
                .defaultIfNull((AreaRifornimento) areaOrdineFullDTO.getAreaRifornimento(), new AreaRifornimento());
        areaRifornimento.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());

        areaRifornimento = doPropertyChange(areaRifornimento, evt.getNewValue());

        ((PanjeaFormModel) formModel).setAdjustingMode(true);
        try {
            formModel.getValueModel("areaOrdine.documento.entita")
                    .setValue(areaRifornimento.getAreaOrdine().getDocumento().getEntita());
            formModel.getValueModel("areaOrdine.documento.sedeEntita")
                    .setValue(areaRifornimento.getAreaOrdine().getDocumento().getSedeEntita());
            formModel.getValueModel("areaOrdine.depositoOrigine")
                    .setValue(areaRifornimento.getAreaOrdine().getDepositoOrigine());
            formModel.getValueModel("areaOrdine.tipologiaCodiceIvaAlternativo")
                    .setValue(areaRifornimento.getAreaOrdine().getTipologiaCodiceIvaAlternativo());
            formModel.getValueModel("areaOrdine.codiceIvaAlternativo")
                    .setValue(areaRifornimento.getAreaOrdine().getCodiceIvaAlternativo());
            formModel.getValueModel("areaRifornimento").setValue(areaRifornimento);
            formModel.getValueModel("areaOrdine.documento.dataDocumento")
                    .setValue(areaRifornimento.getAreaOrdine().getDocumento().getDataDocumento());
            formModel.getFieldMetadata("areaOrdine.documento.dataDocumento").setEnabled(true);
            formModel.getValueModel("areaOrdine.tipoAreaOrdine")
                    .setValue(areaRifornimento.getAreaOrdine().getTipoAreaOrdine());

            formModel.getValueModel("areaOrdine.listino").setValue(areaRifornimento.getAreaOrdine().getListino());
            formModel.getValueModel("areaOrdine.listinoAlternativo")
                    .setValue(areaRifornimento.getAreaOrdine().getListinoAlternativo());
        } finally {
            ((PanjeaFormModel) formModel).setAdjustingMode(false);
        }
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

}
