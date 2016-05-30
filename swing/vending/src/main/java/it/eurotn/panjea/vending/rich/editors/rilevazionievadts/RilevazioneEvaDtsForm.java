package it.eurotn.panjea.vending.rich.editors.rilevazionievadts;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.domain.evadts.RilevazioniEvaDtsErrori;
import it.eurotn.panjea.vending.domain.evadts.RilevazioniFasceEva;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RilevazioneEvaDtsForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "rilevazioneEvaDtsForm";

    /**
     * Costruttore.
     */
    public RilevazioneEvaDtsForm() {
        super(PanjeaFormModelHelper.createFormModel(new RilevazioneEvaDts(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,30dlu,10dlu,right:pref,4dlu,30dlu,10 dlu,right:pref,4dlu,30dlu", "1dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addHorizontalSeparator("Incasso", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("ca301");
        builder.addPropertyAndLabel("ca302", 5);
        builder.addPropertyAndLabel("ca309", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("ca303");
        builder.addPropertyAndLabel("ca304", 5);
        builder.addPropertyAndLabel("ca311", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("ca801");
        builder.addPropertyAndLabel("da901", 5);

        builder.nextRow();
        builder.addHorizontalSeparator("Venduto", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("ca203");
        builder.addPropertyAndLabel("ca204", 5);
        builder.addPropertyAndLabel("va103", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("da203");
        builder.addPropertyAndLabel("da204", 5);
        builder.addPropertyAndLabel("va104", 9);
        builder.nextRow();

        builder.addHorizontalSeparator("Rendiresto", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("ca401");
        builder.addPropertyAndLabel("ca402", 5);
        builder.addPropertyAndLabel("ca1001", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("ca406");
        builder.addPropertyAndLabel("ca407", 5);
        builder.addPropertyAndLabel("ca1003", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("ca1501");
        builder.nextRow();

        builder.addHorizontalSeparator("Credito chiave", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("da302");
        builder.addPropertyAndLabel("da602", 5);
        builder.addPropertyAndLabel("da402", 9);
        builder.nextRow();

        builder.addHorizontalSeparator("Supplementi", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("ca705");
        builder.addPropertyAndLabel("ca707", 5);
        builder.addPropertyAndLabel("va112", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("da505");
        builder.addPropertyAndLabel("da506", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("Sconti", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("da501");
        builder.addPropertyAndLabel("da502", 5);
        builder.addPropertyAndLabel("ca701", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("va108");
        builder.addPropertyAndLabel("va111", 5);
        builder.addPropertyAndLabel("ca703", 9);
        builder.nextRow();
        builder.addPropertyAndLabel("va303");
        builder.addPropertyAndLabel("va304", 5);
        builder.addPropertyAndLabel("va107", 9);
        builder.nextRow();

        builder.addHorizontalSeparator("Test", 11);
        builder.nextRow();
        builder.addPropertyAndLabel("va203");
        builder.addPropertyAndLabel("va204", 5);
        builder.addPropertyAndLabel("va206", 9);
        builder.nextRow();

        TableEditableBinding<RilevazioniFasceEva> righeFascieBinding = new TableEditableBinding<>(getFormModel(),
                "fasce", List.class, new RilevazioniFasceEvaDtsTableModel());

        TableEditableBinding<RilevazioniEvaDtsErrori> righeErroriBinding = new TableEditableBinding<>(getFormModel(),
                "errori", List.class, new RilevazioniEvaDtsErroriTableModel());

        JTabbedPane tabbedPane = getComponentFactory().createTabbedPane();
        tabbedPane.add("Eventi", righeErroriBinding.getControl());
        tabbedPane.add("Fasce", righeFascieBinding.getControl());
        tabbedPane.setSelectedIndex(0);

        builder.addComponent(tabbedPane, 1, 11, 1);

        return builder.getPanel();

    }

}