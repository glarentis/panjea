package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivilinktipodocumento;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class CorrispettivoLinkTipoDocumentoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "corrispettivoLinkTipoDocumentoForm";

    /**
     * Costruttore.
     */
    public CorrispettivoLinkTipoDocumentoForm() {
        super(PanjeaFormModelHelper.createFormModel(new CorrispettivoLinkTipoDocumento(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("tipoDocumentoOrigine", 1);
        Binding bindingTipoDocOrigine = bf.createBoundSearchText("tipoDocumentoOrigine",
                new String[] { "codice", "descrizione" });
        SearchPanel tipoDocOrigineSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDocOrigine, 3);
        tipoDocOrigineSearchPanel.getTextFields().get("codice").setColumns(4);
        tipoDocOrigineSearchPanel.getTextFields().get("descrizione").setColumns(18);
        builder.nextRow();

        builder.addLabel("tipoDocumentoDestinazione", 1);
        Binding bindingTipoDocDest = bf.createBoundSearchText("tipoDocumentoDestinazione",
                new String[] { "codice", "descrizione" });
        SearchPanel tipoDocDestSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDocDest, 3);
        tipoDocDestSearchPanel.getTextFields().get("codice").setColumns(4);
        tipoDocDestSearchPanel.getTextFields().get("descrizione").setColumns(18);

        return builder.getPanel();
    }

}