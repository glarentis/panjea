package it.eurotn.panjea.vending.rich.editors.rilevazionievadts.ricerca;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.form.PanjeaFormModel;

public class EntitaPropertyChange implements PropertyChangeListener {

    private FormModel formModel;
    private IAnagraficaBD anagraficaBD;
    private String pathEntita;
    private String pathSede;

    /**
     * @param formModel
     *            formModel sorgente del propertyChange
     * @param pathEntita
     *            della proprieta entita
     * @param pathSede
     *            della proprieta sede entita
     */
    public EntitaPropertyChange(final FormModel formModel, final String pathEntita, final String pathSede) {
        super();
        this.formModel = formModel;
        this.pathEntita = pathEntita;
        this.pathSede = pathSede;
        anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (formModel.isReadOnly() || ((PanjeaFormModel) formModel).isAdjustingMode()
                || Objects.equals(evt.getOldValue(), evt.getNewValue())) {
            return;
        }
        EntitaLite entitaLite = (EntitaLite) formModel.getValueModel(pathEntita).getValue();
        if (entitaLite == null) {
            formModel.getValueModel(pathSede).setValue(null);
        } else {
            formModel.getValueModel(pathSede)
                    .setValue(anagraficaBD.caricaSedePrincipaleEntita(entitaLite.creaProxyEntita()));
        }
    }
}
