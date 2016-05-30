package it.eurotn.panjea.vending.rich.editors.tipimodello;

import javax.swing.JComponent;

import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ProdottoCollegatoInsertForm extends PanjeaAbstractForm implements Focussable {

    private static final String FORM_ID = "prodottoCollegatoInsertForm";
    private SearchPanel articoloSearchPanel;

    /**
     * Costruttore.
     *
     * @param prodotto
     *            prodotto
     */
    public ProdottoCollegatoInsertForm(final ProdottoCollegato prodotto) {
        super(PanjeaFormModelHelper.createFormModel(prodotto, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("fill:default:grow", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        Binding articoloBinding = bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" });
        articoloSearchPanel = (SearchPanel) builder.addBinding(articoloBinding);
        articoloSearchPanel.getTextFields().get("codice").setColumns(5);

        return builder.getPanel();
    }

    @Override
    public void grabFocus() {
        articoloSearchPanel.getTextFields().get("codice").requestFocusInWindow();
    }

}
