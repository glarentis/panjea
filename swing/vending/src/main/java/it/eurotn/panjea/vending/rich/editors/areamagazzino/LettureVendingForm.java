package it.eurotn.panjea.vending.rich.editors.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.util.GuiStandardUtils;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;

public class LettureVendingForm extends PanjeaAbstractForm implements PropertyChangeListener {

    private static final String FORM_ID = "lettureVendingForm";

    /**
     * Costruttore di default.
     *
     */
    public LettureVendingForm() {
        super(FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu, 10dlu, right:pref,4dlu,fill:50dlu",
                "1dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setRow(2);

        builder.addHorizontalSeparator("Letture", 23);
        builder.nextRow();

        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.lettureContatore.prezzo", 1);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.lettureContatore.precedente", 5);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.lettureContatore.lettura", 9);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.lettureContatore.prove", 13);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.lettureContatore.battute", 17);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.lettureContatore.importo", 21);
        builder.nextRow();

        builder.addHorizontalSeparator("Incassi", 23);
        builder.nextRow();

        builder.addPropertyAndLabel("areaRifornimento.incasso", 1);
        builder.addPropertyAndLabel("areaRifornimento.reso", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Battute", 23);
        builder.nextRow();

        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.battute.contatori");
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.battute.rifornite", 5);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.battute.prepagate", 9);
        builder.addPropertyAndLabel("areaRifornimento.datiVendingArea.battute.omaggio", 13);
        builder.nextRow();

        builder.addLabel("areaRifornimento.datiVendingArea.dataInizioIntervento");
        builder.addBinding(bf.createBoundCalendar("areaRifornimento.datiVendingArea.dataInizioIntervento",
                "dd/MM/yy HH:mm", "##/##/## ##:##"), 3, 3, 1);
        builder.addLabel("areaRifornimento.datiVendingArea.dataFineIntervento", 9);
        builder.addBinding(bf.createBoundCalendar("areaRifornimento.datiVendingArea.dataFineIntervento",
                "dd/MM/yy HH:mm", "##/##/## ##:##"), 11, 3, 1);

        getFormModel().getFieldMetadata("areaRifornimento.datiVendingArea.lettureContatore.prezzo").setReadOnly(true);
        getFormModel().getFieldMetadata("areaRifornimento.datiVendingArea.lettureContatore.importo").setReadOnly(true);

        getFormModel().getValueModel("areaRifornimento.datiVendingArea.lettureContatore.lettura")
                .addValueChangeListener(this);
        getFormModel().getValueModel("areaRifornimento.datiVendingArea.lettureContatore.precedente")
                .addValueChangeListener(this);
        getFormModel().getValueModel("areaRifornimento.datiVendingArea.lettureContatore.prove")
                .addValueChangeListener(this);
        addFormObjectChangeListener(this);

        return GuiStandardUtils.attachBorder(builder.getPanel());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (getValueModel("areaRifornimento.datiVendingArea.lettureContatore.battute").getValue() != null) {
            getValueModel("areaRifornimento.datiVendingArea.lettureContatore.battute")
                    .setValue(getValueModel("areaRifornimento.datiVendingArea.lettureContatore.battute").getValue());
        }

        if (getValueModel("areaRifornimento.datiVendingArea.lettureContatore.importo").getValue() != null) {
            getValueModel("areaRifornimento.datiVendingArea.lettureContatore.importo")
                    .setValue(getValueModel("areaRifornimento.datiVendingArea.lettureContatore.importo").getValue());
        }

    }
}
