package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form di {@link ContattoSedeEntita}.
 *
 * @author adriano
 * @version 1.0, 18/dic/07
 */
public class ContattoSedeEntitaForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "contattoSedeEntitaForm";

    /**
     * Costruttore.
     * 
     * @param contattoSedeEntita
     *            {@link ContattoSedeEntita}
     */
    public ContattoSedeEntitaForm(final ContattoSedeEntita contattoSedeEntita) {
        super(PanjeaFormModelHelper.createFormModel(contattoSedeEntita, false, FORM_ID), FORM_ID);
        ValueModel entitaValueModel = new ValueHolder(new EntitaLite());
        DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(entitaValueModel), BigDecimal.class, true, null);
        getFormModel().add("entita", entitaValueModel, metaData);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");
        builder.row();
        Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
                new String[] { "entita" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        builder.addBinding(sedeEntitaBinding, "align=left", "");
        builder.row();
        ((JTextField) builder.add("contatto.nome", "align=left")[1]).setColumns(25);
        ((JTextField) builder.add("contatto.cognome", "align=left")[1]).setColumns(25);
        builder.row();
        ((JTextField) builder.add("contatto.interno", "align=left")[1]).setColumns(25);
        ((JTextField) builder.add("contatto.cellulare", "align=left")[1]).setColumns(25);
        builder.row();
        ((JTextField) builder.add("contatto.email", "align=left")[1]).setColumns(25);
        ((JTextField) builder.add("contatto.fax", "align=left")[1]).setColumns(25);
        builder.row();
        SearchPanel searchPanel = (SearchPanel) builder
                .add(bf.createBoundSearchText("mansione", new String[] { Mansione.PROP_DESCRIZIONE }), "align=left")[1];
        searchPanel.getTextFields().get(Mansione.PROP_DESCRIZIONE).setColumns(25);
        builder.row();

        return builder.getForm();
    }

    /**
     * 
     * @param entita
     *            entità alla quale è legata la sede del contatto.
     */
    public void setEntita(Entita entita) {
        getFormModel().getValueModel("entita").setValue(entita.getEntitaLite());
    }

}
