package it.eurotn.panjea.rich.editors.email;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author leonardo
 */
public class ParametriRicercaMailForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "parametriRicercaMailForm";
    private static final String FORMMODEL_ID = "parametriRicercaMailFormModel";
    private SearchPanel searchEntita;

    /**
     * Costruttore.
     */
    public ParametriRicercaMailForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaMail(), false, FORMMODEL_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:pref, 10dlu, right:pref,4dlu,left:pref,fill:default:grow",
                "2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("periodo");

        builder.addLabel("entita", 5);
        searchEntita = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 7);
        searchEntita.getTextFields().get("codice").setColumns(5);
        searchEntita.getTextFields().get("anagrafica.denominazione").setColumns(23);
        builder.nextRow();

        builder.addPropertyAndLabel("testo", 1, 4, 5);

        return builder.getPanel();
    }

    /**
     *
     * @param readOnly
     *            readOnly
     */
    public void setEntitaReadOnly(boolean readOnly) {
        searchEntita.setReadOnly(readOnly);
    }

}
