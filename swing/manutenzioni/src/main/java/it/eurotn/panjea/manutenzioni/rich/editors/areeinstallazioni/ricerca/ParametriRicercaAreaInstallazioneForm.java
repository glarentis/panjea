package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni.ricerca;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni.EntitaPropertyChange;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ParametriRicercaAreaInstallazioneForm extends PanjeaAbstractForm {

    /**
     *
     * @param parametriRicercaAreeInstallazione
     *            parametri ricerca installazione
     */
    public ParametriRicercaAreaInstallazioneForm(
            final ParametriRicercaAreeInstallazione parametriRicercaAreeInstallazione) {
        super(PanjeaFormModelHelper.createFormModel(parametriRicercaAreeInstallazione, false,
                "parametriRicercaAreaInstallazioneFormModel"), "parametriRicercaAreaInstallazioneForm");
        PanjeaSwingUtil.addValueModelToForm(TipoEntita.CLIENTE, getFormModel(), TipoEntita.class, "tipoEntita", true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:200dlu,20dlu,fill:pref,4dlu,200dlu,20dlu,right:pref,4dlu,left:default,20dlu,fill:default:grow",
                "default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
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
        return builder.getPanel();
    }
}
