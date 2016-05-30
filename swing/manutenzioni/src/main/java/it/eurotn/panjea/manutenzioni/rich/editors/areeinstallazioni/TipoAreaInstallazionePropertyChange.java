package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.rich.form.PanjeaFormModel;

public class TipoAreaInstallazionePropertyChange implements PropertyChangeListener {

    private FormModel formModel;

    /**
     *
     * @param formModel
     *            form model
     */
    public TipoAreaInstallazionePropertyChange(final FormModel formModel) {
        this.formModel = formModel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (formModel.isReadOnly() || ((PanjeaFormModel) formModel).isAdjustingMode()
                || Objects.equals(evt.getOldValue(), evt.getNewValue())) {
            return;
        }
        IMagazzinoDocumentoBD bd = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
        TipoAreaInstallazione tai = (TipoAreaInstallazione) formModel.getValueModel("tipoAreaDocumento").getValue();
        TipoAreaMagazzino tam = bd.caricaTipoAreaMagazzinoPerTipoDocumento(tai.getTipoDocumento().getId());
        if (tam.getDepositoOrigine() != null) {
            formModel.getValueModel("depositoOrigine").setValue(tam.getDepositoOrigine());
        }

    }

}
