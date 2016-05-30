package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class CedentePrestatoreForm extends PanjeaAbstractForm {

    private class CedentePrestatoreListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            getFormModel().validate();
        }

    }

    private static final String FORM_ID = "cedentePrestatoreFormV1";

    private CedentePrestatoreListener cedentePrestatoreListener;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public CedentePrestatoreForm(final FormModel formModel) {
        super(formModel, FORM_ID);
        cedentePrestatoreListener = new CedentePrestatoreListener();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "left:pref,4dlu,left:90dlu,5dlu,left:pref,4dlu,left:80dlu,5dlu,left:pref,4dlu,left:90dlu",
                "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Dati fiscali (1.2.1.1)", 11);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.idFiscaleIVA.idPaese",
                1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.idFiscaleIVA.idCodice",
                5);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati anagrafici (1.2.1)", 11);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.codiceFiscale");
        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.denominazione", 5, 8, 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.nome", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.cognome", 5);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.titolo", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.codEORI", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.regimeFiscale", 5, 12,
                5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.alboProfessionale", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.provinciaAlbo", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.numeroIscrizioneAlbo",
                1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.dataIscrizioneAlbo", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati sede (1.2.2)", 11);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.sede.indirizzo", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.sede.numeroCivico", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.sede.nazione", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.sede.provincia", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.sede.comune", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.sede.cap", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Contatti (1.2.5)", 11);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.contatti.telefono", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.contatti.fax", 5);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.contatti.email", 9);
        builder.nextRow();

        builder.addHorizontalSeparator("Riferimento Amministrazione (1.2.6)", 11);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cedentePrestatore.riferimentoAmministrazione", 1);

        // mi registro alle proprietà nome,cognome e denominazione perchè sono legate tra loro sulle regole di
        // validazione e se non chiamo il validate al loro cambiamento non vengono testate le regole presenti sul plugin
        // rules source
        getValueModel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.nome")
                .addValueChangeListener(cedentePrestatoreListener);
        getValueModel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.cognome")
                .addValueChangeListener(cedentePrestatoreListener);
        getValueModel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.denominazione")
                .addValueChangeListener(cedentePrestatoreListener);
        addFormObjectChangeListener(cedentePrestatoreListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, cedentePrestatoreListener);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getValueModel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.nome")
                .removeValueChangeListener(cedentePrestatoreListener);
        getValueModel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.cognome")
                .removeValueChangeListener(cedentePrestatoreListener);
        getValueModel("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.denominazione")
                .removeValueChangeListener(cedentePrestatoreListener);
        removeFormObjectChangeListener(cedentePrestatoreListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, cedentePrestatoreListener);
        cedentePrestatoreListener = null;

        super.dispose();
    }
}
