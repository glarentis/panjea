package it.eurotn.panjea.vending.rich.editors.modelli;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class ModelloForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "modelloForm";

    /**
     * Costruttore.
     */
    public ModelloForm() {
        super(PanjeaFormModelHelper.createFormModel(new Modello(), false, FORM_ID));
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:50dlu,fill:120dlu", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("codice");
        builder.nextRow();

        builder.addPropertyAndLabel("descrizione", 1, 4, 2);
        builder.nextRow();

        builder.addLabel("tipoModello");
        Binding bindingTipoCom = bf.createBoundSearchText("tipoModello", new String[] { "codice", "descrizione" });
        SearchPanel searchPanel = (SearchPanel) bindingTipoCom.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("descrizione").setColumns(20);
        builder.addBinding(bindingTipoCom, 3, 6, 2, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("cassettaPresente");
        builder.nextRow();

        builder.addPropertyAndLabel("obbligoLetturaCassetta");
        builder.nextRow();

        builder.addPropertyAndLabel("ritiroCassetta");
        builder.nextRow();

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        return new Modello();
    }
}
