package it.eurotn.panjea.magazzino.rich.forms.mezzotrasporto;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class MezzoTrasportoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "mezzoTrasportoForm";

    /**
     * Costruttore di default, inizializza un nuovo tipo mezzo di trasporto.
     */
    public MezzoTrasportoForm() {
        super(PanjeaFormModelHelper.createFormModel(new MezzoTrasporto(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");

        builder.row();
        ((JTextField) builder.add("targa", "align=left")[1]).setColumns(10);
        builder.row();
        ((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
        builder.row();
        builder.add("abilitato");
        builder.row();
        SearchPanel searchPanel = (SearchPanel) builder.add(
                bf.createBoundSearchText("tipoMezzoTrasporto", new String[] { "codice", "descrizione" }, true),
                "align=left")[1];
        searchPanel.getTextFields().get("codice").setColumns(5);
        builder.row();

        Binding bindingEntita = bf.createBoundSearchText("entita",
                new String[] { "codice", "anagrafica.denominazione" });
        searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        builder.add(bindingEntita, "align=left");
        builder.row();
        Binding bindingDeposito = bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" });
        searchPanel = (SearchPanel) bindingDeposito.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("descrizione").setColumns(20);
        builder.add(bindingDeposito, "align=left");

        getFormModel().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                getFormModel().getFieldMetadata("deposito").setReadOnly(true);
            }
        });

        return builder.getForm();
    }

}
