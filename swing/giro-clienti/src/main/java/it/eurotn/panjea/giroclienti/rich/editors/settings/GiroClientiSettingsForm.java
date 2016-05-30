package it.eurotn.panjea.giroclienti.rich.editors.settings;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class GiroClientiSettingsForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "GiroClientiSettingsForm";

    /**
     * Costruttore.
     */
    public GiroClientiSettingsForm() {
        super(PanjeaFormModelHelper.createFormModel(new GiroClientiSettings(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("tipoAreaOrdineScheda", 1);
        Binding bindingTipoDoc = bf.createBoundSearchText("tipoAreaOrdineScheda",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" });
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 3);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);

        return builder.getPanel();
    }

}
