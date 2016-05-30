package it.eurotn.panjea.giroclienti.rich.editors.scheda.header.copiascheda;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class SchedaCopiaFrom extends PanjeaAbstractForm {

    private static final String FORM_ID = "SchedaCopiaPMForm";

    /**
     * Costruttore.
     *
     * @param schedaCopiaPM
     *            SchedaCopiaPM
     */
    public SchedaCopiaFrom(final SchedaCopiaPM schedaCopiaPM) {
        super(PanjeaFormModelHelper.createFormModel(schedaCopiaPM, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addHorizontalSeparator("Scheda di origine", 3);
        builder.nextRow();

        builder.addLabel("utente", 1);
        JComponent componentsUtente = builder
                .addBinding(bf.createBoundSearchText("utente", new String[] { "userName", "nome" }), 3);
        ((SearchPanel) componentsUtente).getTextFields().get("userName").setColumns(5);
        ((SearchPanel) componentsUtente).getTextFields().get("nome").setColumns(23);
        builder.nextRow();

        builder.addPropertyAndLabel("giorno", 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Scheda di destinazione", 3);
        builder.nextRow();

        builder.addLabel("utenteDestinazione", 1);
        JComponent componentsUtenteD = builder
                .addBinding(bf.createBoundSearchText("utenteDestinazione", new String[] { "userName", "nome" }), 3);
        ((SearchPanel) componentsUtenteD).getTextFields().get("userName").setColumns(5);
        ((SearchPanel) componentsUtenteD).getTextFields().get("nome").setColumns(23);
        builder.nextRow();

        builder.addPropertyAndLabel("giornoDestinazione", 1);
        builder.nextRow();

        builder.addHorizontalSeparator("Opzioni di copia", 3);
        builder.nextRow();

        builder.addPropertyAndLabel("modalitaCopia", 1);

        return builder.getPanel();
    }

}
