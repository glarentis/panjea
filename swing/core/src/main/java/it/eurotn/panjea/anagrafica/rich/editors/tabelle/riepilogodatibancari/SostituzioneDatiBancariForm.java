package it.eurotn.panjea.anagrafica.rich.editors.tabelle.riepilogodatibancari;

import javax.swing.JComponent;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class SostituzioneDatiBancariForm extends PanjeaAbstractForm {

    /**
     * Costruttore.
     */
    public SostituzioneDatiBancariForm() {
        super(PanjeaFormModelHelper.createFormModel(new RapportoBancarioSedeEntita(), false,
                "SostituzioneDatiBancariForm"), "SostituzioneDatiBancariForm");
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addLabel("banca", 1);
        SearchPanel searchPanelBanca = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("banca", new String[] { Banca.PROP_CODICE, Banca.PROP_DESCRIZIONE }), 3);
        searchPanelBanca.getTextFields().get(Banca.PROP_CODICE).setColumns(6);
        builder.nextRow();

        builder.addLabel("filiale", 1);
        SearchPanel searchPanelFiliale = (SearchPanel) builder.addBinding(
                bf.createBoundSearchText("filiale", new String[] { Filiale.PROP_CODICE, Filiale.PROP_INDIRIZZO },
                        new String[] { "banca" }, new String[] { Banca.REF }),
                3);
        searchPanelFiliale.getTextFields().get(Filiale.PROP_CODICE).setColumns(5);
        builder.nextRow();

        return builder.getPanel();
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
    }

}
