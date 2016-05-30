package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.components.Focussable;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione.TipoMovimento;
import it.eurotn.panjea.manutenzioni.rich.search.ArticoloMISearchObject;
import it.eurotn.panjea.manutenzioni.rich.search.CausaleInstallazioneSearchObject;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RigaInstallazioneForm extends PanjeaAbstractForm implements Focussable {

    private class CausaleRitiroPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            getFormModel().getValueModel("articoloRitiro")
                    .setValue(getFormModel().getValueModel("articoloRitiro").getValue());
        }

    }

    private class TipoInstallazionePropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateControlsVisibility();
        }

    }

    private static final String FORM_ID = "rigaInstallazioneForm";
    private List<JComponent> componentiInstallazione;
    private List<JComponent> componentiRitiro;
    private SearchPanel searchPanelCausaleInstallazione;
    private SearchPanel searchPanelCausaleRitiro;

    /**
     * Costruttore.
     */
    public RigaInstallazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new RigaInstallazione(), false, FORM_ID), FORM_ID);
        PanjeaSwingUtil.addValueModelToForm(2, getFormModel(), Integer.class, "tipoMovimentoRitiro", false);
        PanjeaSwingUtil.addValueModelToForm(0, getFormModel(), Integer.class, "tipoMovimentoInstallazione", false);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "soloDisponibili", true);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);
        createRitiroPartControl(bf, builder);
        builder.nextRow();
        createInstallazionePartControl(bf, builder);

        getValueModel("tipoMovimento").addValueChangeListener(new TipoInstallazionePropertyChange());
        getValueModel("causaleRitiro").addValueChangeListener(new CausaleRitiroPropertyChange());
        visualizzaComponentiInstallazione(false);
        visualizzaComponentiRitiro(false);
        return builder.getPanel();
    }

    protected void createInstallazionePartControl(PanjeaSwingBindingFactory bf, PanjeaFormLayoutBuilder builder) {
        componentiInstallazione = new ArrayList<>();
        JComponent separator = getComponentFactory().createLabeledSeparator("INSTALLAZIONE");
        componentiInstallazione.add(separator);
        builder.addComponent(separator);
        builder.nextRow();
        componentiInstallazione.add(builder.addLabel("causaleInstallazione"));
        searchPanelCausaleInstallazione = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("causaleInstallazione", new String[] { "codice", "descrizione" },
                        new String[] { "tipoMovimentoInstallazione", "tipoMovimento" },
                        new String[] { CausaleInstallazioneSearchObject.CAUSALI_TIPO_PARAM_KEY,
                                CausaleInstallazioneSearchObject.TIPO_MOVIMENTO_PARAM_KEY }),
                        3);
        searchPanelCausaleInstallazione.getTextFields().get("codice").setColumns(5);
        componentiInstallazione.add(searchPanelCausaleInstallazione);

        builder.nextRow();
        componentiInstallazione.add(builder.addLabel("articoloInstallazione"));
        SearchPanel searchPanelArticolo = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
                "articoloInstallazione", new String[] { "codice", "descrizione" }, new String[] { "soloDisponibili" },
                new String[] { ArticoloMISearchObject.SOLO_DISPONIBILI_PARAM_KEY }), 3);
        searchPanelArticolo.getTextFields().get("codice").setColumns(10);
        searchPanelArticolo.getTextFields().get("descrizione").setColumns(30);
        componentiInstallazione.add(searchPanelArticolo);
    }

    private void createRitiroPartControl(PanjeaSwingBindingFactory bf, PanjeaFormLayoutBuilder builder) {
        componentiRitiro = new ArrayList<>();
        JComponent separator = getComponentFactory().createLabeledSeparator("RITIRO");
        componentiRitiro.add(separator);
        builder.addComponent(separator);
        builder.nextRow();
        componentiRitiro.add(builder.addLabel("causaleRitiro"));
        searchPanelCausaleRitiro = (SearchPanel) builder.addBinding(bf.createBoundSearchText("causaleRitiro",
                new String[] { "codice", "descrizione" }, new String[] { "tipoMovimentoRitiro", "tipoMovimento" },
                new String[] { CausaleInstallazioneSearchObject.CAUSALI_TIPO_PARAM_KEY,
                        CausaleInstallazioneSearchObject.TIPO_MOVIMENTO_PARAM_KEY }),
                3);
        searchPanelCausaleRitiro.getTextFields().get("codice").setColumns(5);
        searchPanelCausaleRitiro.getTextFields().get("descrizione").setColumns(20);
        componentiRitiro.add(searchPanelCausaleRitiro);

        builder.nextRow();
        componentiRitiro.add(builder.addLabel("articoloRitiro"));
        SearchPanel searchPanelArticolo = (SearchPanel) builder
                .addBinding(bf.createBoundSearchText("articoloRitiro", new String[] { "codice", "descrizione" }), 3);
        searchPanelArticolo.getTextFields().get("codice").setColumns(10);
        searchPanelArticolo.getTextFields().get("descrizione").setColumns(30);
        componentiRitiro.add(searchPanelArticolo);
    }

    @Override
    public void grabFocus() {
        TipoMovimento tipoMovimento = (TipoMovimento) getValueModel("tipoMovimento").getValue();
        switch (tipoMovimento) {
        case RITIRO:
        case SOSTITUZIONE:
            searchPanelCausaleRitiro.getTextFields().get("codice").getTextField().requestFocus();
            break;

        default:
            searchPanelCausaleInstallazione.getTextFields().get("codice").getTextField().requestFocus();
        }
    }

    private void updateControlsVisibility() {
        TipoMovimento tipoMovimento = (TipoMovimento) getValueModel("tipoMovimento").getValue();
        if (tipoMovimento == null) {
            visualizzaComponentiInstallazione(false);
            visualizzaComponentiRitiro(false);
            return;
        }

        switch (tipoMovimento) {
        case INSTALLAZIONE:
            visualizzaComponentiInstallazione(true);
            visualizzaComponentiRitiro(false);
            break;
        case SOSTITUZIONE:
            visualizzaComponentiInstallazione(true);
            visualizzaComponentiRitiro(true);
            break;
        case RITIRO:
            visualizzaComponentiInstallazione(false);
            visualizzaComponentiRitiro(true);
            break;

        default:
            visualizzaComponentiInstallazione(false);
            visualizzaComponentiRitiro(false);
        }
    }

    private void visualizzaComponentiInstallazione(boolean visible) {
        for (JComponent componente : componentiInstallazione) {
            componente.setVisible(visible);
        }
    }

    private void visualizzaComponentiRitiro(boolean visible) {
        for (JComponent componente : componentiRitiro) {
            componente.setVisible(visible);
        }
    }

}