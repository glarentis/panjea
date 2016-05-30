package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class CommittenteForm extends PanjeaAbstractForm {

    private class CommittenteListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            getFormModel().validate();
        }

    }

    private static final String FORM_ID = "committenteFormV1";

    private CommittenteListener committenteListener;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public CommittenteForm(final FormModel formModel) {
        super(formModel, FORM_ID);
        committenteListener = new CommittenteListener();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("left:pref,4dlu,left:90dlu,10dlu,left:pref,4dlu,left:90dlu", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Dati fiscali (1.4.1.1)", 7);
        builder.nextRow();

        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.idFiscaleIVA.idPaese", 1);
        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.idFiscaleIVA.idCodice", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Dati anagrafici (1.4.1)", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.codiceFiscale", 1);
        builder.nextRow();

        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.denominazione", 1, 10, 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.nome",
                1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.cognome",
                5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.titolo");
        builder.nextRow();

        builder.addPropertyAndLabel(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.codEORI");
        builder.nextRow();

        builder.addHorizontalSeparator("Dati sede (1.4.2)", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.sede.indirizzo", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.sede.numeroCivico", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.sede.nazione", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.sede.provincia", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.sede.comune", 1);
        builder.addPropertyAndLabel("fatturaElettronicaHeader.cessionarioCommittente.sede.cap", 5);
        builder.nextRow();

        // mi registro alle proprietà nome,cognome e denominazione perchè sono legate tra loro sulle regole di
        // validazione e se non chiamo il validate al loro cambiamento non vengono testate le regole presenti sul plugin
        // rules source
        getValueModel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.nome")
                .addValueChangeListener(committenteListener);
        getValueModel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.cognome")
                .addValueChangeListener(committenteListener);
        getValueModel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.denominazione")
                .addValueChangeListener(committenteListener);
        addFormObjectChangeListener(committenteListener);
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, committenteListener);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getValueModel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.nome")
                .removeValueChangeListener(committenteListener);
        getValueModel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.cognome")
                .removeValueChangeListener(committenteListener);
        getValueModel("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.denominazione")
                .removeValueChangeListener(committenteListener);
        removeFormObjectChangeListener(committenteListener);
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY, committenteListener);
        committenteListener = null;

        super.dispose();
    }

}
