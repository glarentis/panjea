package it.eurotn.panjea.vending.rich.editors.tipimodello;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class TipoModelloForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "tipoModelloForm";

    /**
     * Costruttore.
     */
    public TipoModelloForm() {
        super(PanjeaFormModelHelper.createFormModel(new TipoModello(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,right:pref,4dlu,left:pref", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        JTextField codiceComponent = (JTextField) builder.addPropertyAndLabel("codice", 1)[1];
        codiceComponent.setColumns(10);
        builder.addPropertyAndLabel("descrizione", 5);
        builder.nextRow();

        builder.addLabel("tipoComunicazione");
        Binding bindingTipoCom = bf.createBoundSearchText("tipoComunicazione",
                new String[] { "codice", "descrizione" });
        SearchPanel searchPanel = (SearchPanel) bindingTipoCom.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("descrizione").setColumns(20);
        builder.addBinding(bindingTipoCom, 3, 4, 5, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("caldo", 1);
        builder.addPropertyAndLabel("freddo", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("snack", 1);
        builder.addPropertyAndLabel("snackRefrigerati", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("kit", 1);
        builder.addPropertyAndLabel("acqua", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("gelato", 1);

        return builder.getPanel();
    }

}
