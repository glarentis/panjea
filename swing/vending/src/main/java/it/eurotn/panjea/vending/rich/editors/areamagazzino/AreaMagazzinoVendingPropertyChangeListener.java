package it.eurotn.panjea.vending.rich.editors.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.binding.form.FormModel;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaFormModel;

public abstract class AreaMagazzinoVendingPropertyChangeListener implements FormModelPropertyChangeListeners {

    protected FormModel formModel;

    protected abstract AreaRifornimento doPropertyChange(AreaRifornimento areaRifornimento, Object propertyValue);

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (formModel.isReadOnly() || ((PanjeaFormModel) formModel).isAdjustingMode()
                || Objects.equals(evt.getOldValue(), evt.getNewValue())) {
            return;
        }

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) formModel.getFormObject();
        if (areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino() == null
                || !areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().isGestioneVending()) {
            return;
        }

        AreaRifornimento areaRifornimento = ObjectUtils
                .defaultIfNull((AreaRifornimento) areaMagazzinoFullDTO.getAreaRifornimento(), new AreaRifornimento());
        areaRifornimento.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());

        areaRifornimento = doPropertyChange(areaRifornimento, evt.getNewValue());

        ((PanjeaFormModel) formModel).setAdjustingMode(true);
        try {
            formModel.getValueModel("areaMagazzino.documento.entita")
                    .setValue(areaRifornimento.getAreaMagazzino().getDocumento().getEntita());
            formModel.getValueModel("areaMagazzino.documento.sedeEntita")
                    .setValue(areaRifornimento.getAreaMagazzino().getDocumento().getSedeEntita());
            formModel.getValueModel("areaMagazzino.depositoOrigine")
                    .setValue(areaRifornimento.getAreaMagazzino().getDepositoOrigine());
            formModel.getValueModel("areaMagazzino.depositoDestinazione")
                    .setValue(areaRifornimento.getAreaMagazzino().getDepositoDestinazione());
            formModel.getValueModel("areaMagazzino.tipologiaCodiceIvaAlternativo")
                    .setValue(areaRifornimento.getAreaMagazzino().getTipologiaCodiceIvaAlternativo());
            formModel.getValueModel("areaMagazzino.codiceIvaAlternativo")
                    .setValue(areaRifornimento.getAreaMagazzino().getCodiceIvaAlternativo());
            formModel.getValueModel("areaRifornimento").setValue(areaRifornimento);
            formModel.getValueModel("areaMagazzino.documento.dataDocumento")
                    .setValue(areaRifornimento.getAreaMagazzino().getDocumento().getDataDocumento());
            formModel.getFieldMetadata("areaMagazzino.documento.dataDocumento").setEnabled(true);
            formModel.getValueModel("areaMagazzino.tipoAreaMagazzino")
                    .setValue(areaRifornimento.getAreaMagazzino().getTipoAreaMagazzino());

            formModel.getValueModel("areaMagazzino.listino").setValue(areaRifornimento.getAreaMagazzino().getListino());
            formModel.getValueModel("areaMagazzino.listinoAlternativo")
                    .setValue(areaRifornimento.getAreaMagazzino().getListinoAlternativo());
        } finally {
            ((PanjeaFormModel) formModel).setAdjustingMode(false);
        }
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

}
