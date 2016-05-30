package it.eurotn.rich.report.editor;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class LayoutStampaForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "layoutStampaForm";

    /**
     * Costruttore.
     * 
     * @param layoutStampa
     *            layout stampa
     */
    public LayoutStampaForm(final LayoutStampa layoutStampa) {
        super(PanjeaFormModelHelper.createFormModel(layoutStampa, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,150dlu", "4dlu,default,4dlu,default,4dlu,40dlu");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
        builder.setLabelAttributes("r, c");

        builder.addLabel("reportName", 1, 2);
        builder.addBinding(bf.createBoundLabel("reportName"), 3, 2);

        builder.addLabel("stampante", 1, 4);
        builder.addBinding(bf.createBoundComboBox("stampante", new ValueHolder(getPrintersName())), 3, 4);

        builder.addComponent(
                new JLabel(
                        "<html>ATTENZIONE: annullando l'associazione non verrà impostata una stampante per il layout corrente ma sarà possibile continuare la stampa.</html>"),
                1, 6, 3, 1);

        return builder.getPanel();
    }

    /**
     * Carica i nomi delle stampanti di sistema.
     * 
     * @return nomi delle stampanti caricati
     */
    private List<String> getPrintersName() {

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

        List<String> printers = new ArrayList<String>();
        printers.add("");
        for (PrintService printer : printServices) {
            printers.add(printer.getName());
        }

        return printers;
    }

}
