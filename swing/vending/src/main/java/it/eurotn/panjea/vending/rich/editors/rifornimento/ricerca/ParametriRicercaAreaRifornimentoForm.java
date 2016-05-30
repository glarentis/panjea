package it.eurotn.panjea.vending.rich.editors.rifornimento.ricerca;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;
import it.eurotn.panjea.vending.rich.editors.rilevazionievadts.ricerca.EntitaPropertyChange;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ParametriRicercaAreaRifornimentoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "parametriRicercaAreaRifornimentoForm";

    /**
     * Costruttore.
     */
    public ParametriRicercaAreaRifornimentoForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaAreeRifornimento(), false, FORM_ID), FORM_ID);

        PanjeaSwingUtil.addValueModelToForm(TipoEntita.CLIENTE, getFormModel(), TipoEntita.class, "tipoEntita", true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "caricatoreSearch", true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:pref,10dlu,right:pref,4dlu,fill:pref", "2dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);
        builder.addPropertyAndLabel("periodo");
        builder.nextRow();
        builder.addLabel("entita");
        SearchPanel searchEntita = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" },
                        new String[] { "tipoEntita" }, new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }), 3);
        searchEntita.getTextFields().get("codice").setColumns(5);
        searchEntita.getTextFields().get("anagrafica.denominazione").setColumns(15);

        builder.addLabel("sedeEntita", 5);
        Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", null, new String[] { "entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 7);
        searchPanelSede.getTextFields().get(null).setColumns(40);
        getValueModel("entita")
                .addValueChangeListener(new EntitaPropertyChange(getFormModel(), "entita", "sedeEntita"));
        builder.nextRow();

        builder.addLabel("installazione");
        SearchPanel searchInstallazionePanel = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("installazione", new String[] { "codice", "descrizione" },
                        new String[] { "entita", "sedeEntita" }, new String[] { "clienteLite", "sede" }), 3);
        searchInstallazionePanel.getTextFields().get("codice").setColumns(10);
        searchInstallazionePanel.getTextFields().get("descrizione").setColumns(20);

        builder.addLabel("distributore", 5);
        SearchPanel searchDistributore = (SearchPanel) builder.addBinding(bf.createBoundSearchText("distributore",
                new String[] { "codice", "descrizione", "datiVending.modello" },
                new String[] { "entita", "sedeEntita" }, new String[] { "entitaParam", "sedeEntitaParam" }), 7);
        searchDistributore.getTextFields().get("codice").setColumns(6);
        searchDistributore.getTextFields().get("descrizione").setColumns(15);
        searchDistributore.getTextFields().get("datiVending.modello").setColumns(9);
        builder.nextRow();

        builder.addLabel("operatore");
        SearchPanel searchOperatore = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("operatore", new String[] { "codice", "denominazione" },
                        new String[] { "caricatoreSearch" }, new String[] { "caricatoreParam" }), 3);
        searchOperatore.getTextFields().get("codice").setColumns(10);
        searchOperatore.getTextFields().get("denominazione").setColumns(20);

        return builder.getPanel();
    }
}
