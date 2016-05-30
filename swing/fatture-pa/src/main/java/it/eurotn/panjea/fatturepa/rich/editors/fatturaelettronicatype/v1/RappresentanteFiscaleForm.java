package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class RappresentanteFiscaleForm extends PanjeaAbstractForm {

    private class RappresentanteFiscaleListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            getFormModel().validate();
        }

    }

    private static final String FORM_ID = "rappresentanteFiscaleFormV1";

    private RappresentanteFiscaleListener rappresentanteFiscaleListener;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public RappresentanteFiscaleForm(final FormModel formModel) {
        super(formModel, FORM_ID);
        rappresentanteFiscaleListener = new RappresentanteFiscaleListener();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,4dlu,left:90dlu,10dlu,left:pref,4dlu,left:90dlu", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.idFiscaleIVA.idPaese", 1);
        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.idFiscaleIVA.idCodice", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.codiceFiscale", 1);
        builder.nextRow();

        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.denominazione", 1, 6, 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.nome", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.cognome",
                5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.titolo");
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.codEORI");
        builder.nextRow();

        // mi registro alle proprietà nome,cognome e denominazione perchè sono legate tra loro sulle regole di
        // validazione e se non chiamo il validate al loro cambiamento non vengono testate le regole presenti sul plugin
        // rules source
        getValueModel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.nome")
                .addValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.cognome")
                .addValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.denominazione")
                .addValueChangeListener(rappresentanteFiscaleListener);
        addFormObjectChangeListener(rappresentanteFiscaleListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, rappresentanteFiscaleListener);

        getFormModel().setReadOnly(true);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getValueModel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.nome")
                .removeValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.cognome")
                .removeValueChangeListener(rappresentanteFiscaleListener);
        getValueModel("fatturaElettronicaHeader.rappresentanteFiscale.datiAnagrafici.anagrafica.denominazione")
                .removeValueChangeListener(rappresentanteFiscaleListener);
        removeFormObjectChangeListener(rappresentanteFiscaleListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, rappresentanteFiscaleListener);
        rappresentanteFiscaleListener = null;

        super.dispose();
    }

}
