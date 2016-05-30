package it.eurotn.panjea.ordini.rich.forms.areaordine;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.support.ClassPropertyAccessStrategy;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class AreaOrdineForm extends PanjeaAbstractForm {

    private class DataRegistrazioneChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (getFormModel().isReadOnly()) {
                return;
            }
            Date dataRegistrazione = (Date) evt.getNewValue();
            Date dataConsegnaCorrente = (Date) getValueModel("areaOrdine.dataConsegna").getValue();
            if (dataConsegnaCorrente == null) {
                getValueModel("areaOrdine.dataConsegna").setValue(dataRegistrazione);
            }
        }
    }

    private class DatRiferimentoOrdinePropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean visible = (Boolean) evt.getNewValue();
            for (JComponent component : riferimentoOrdineComponents) {
                component.setVisible(visible);
            }
        }
    }

    private class DirtyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            getFormModel().getFieldMetadata("areaOrdine.tipoAreaOrdine")
                    .setReadOnly(getFormModel().getValueModel("areaOrdine.id").getValue() != null);
        }
    }

    private class EnabledChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (getFormModel().isReadOnly()) {
                getFormModel().getFieldMetadata("areaOrdine.tipoAreaOrdine")
                        .setReadOnly(getFormModel().getValueModel("areaOrdine.id").getValue() != null);
            }
        }
    }

    private class TipoAreaOrdinePropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ValueModel tipoDocumentoValueModel = getFormModel()
                    .getValueModel("areaOrdine.tipoAreaOrdine.tipoDocumento");
            TipoEntita tipoEntitaArea = tipoDocumentoValueModel.getValue() != null
                    ? ((TipoDocumento) tipoDocumentoValueModel.getValue()).getTipoEntita() : null;

            setComponentsRifornimentoVisible(tipoEntitaArea != null && TipoEntita.CLIENTE.equals(tipoEntitaArea));
            if (tipoEntitaArea != null
                    && (TipoEntita.CLIENTE.equals(tipoEntitaArea) || (TipoEntita.FORNITORE.equals(tipoEntitaArea)))) {
                setComponentsEntitaVisible(true);
            } else {
                setComponentsEntitaVisible(false);
            }
        }

        /**
         * @param visible
         *            visualizza o nasconde i componenti
         */
        private void setComponentsEntitaVisible(boolean visible) {
            if (entitaComponents != null) {
                for (JComponent component : entitaComponents) {
                    component.setVisible(visible);
                }
            }

            if (sedeEntitaComponents != null) {
                for (JComponent component : sedeEntitaComponents) {
                    component.setVisible(visible);
                }
            }
        }
    }

    public static final String FORM_ID = "areaOrdineForm";

    public static final String FORMMODEL_ID = "areaOrdineFormModel";
    private JComponent dataRegistrazioneComponent = null;

    private JComponent[] riferimentoOrdineComponents = null;

    private JComponent[] sedeEntitaComponents = null;

    private AziendaCorrente aziendaCorrente = null;
    private JTextField fieldAnnoCompetenza = null;
    private PanjeaFocusTraversalPolicy panjeaFocusTraversalPolicy = null;

    private JComponent[] entitaComponents = null;

    private TipoAreaOrdinePropertyChange tipoAreaOrdinePropertyChange = null;

    private DirtyChangeListener dirtyChangeListener = null;
    private DatRiferimentoOrdinePropertyChange datRiferimentoOrdinePropertyChange = null;
    private EnabledChangeListener enabledChangeListener = null;
    private DataRegistrazioneChangeListener datRegistrazioneChangeListener = null;

    private JComponent[] rifornimentoComponents;

    /**
     * Costruttore.
     */
    public AreaOrdineForm() {
        super(PanjeaFormModelHelper.createFormModel(new AreaOrdineFullDTO(), false, FORMMODEL_ID,
                (ClassPropertyAccessStrategy) RcpSupport.getBean(AreaOrdineFullDTOPropertyAccessStrategy.BEAN_ID)),
                FORM_ID);

        // Aggiungo il value model che mi servirà solamente nella search text
        // delle entità per escludere le entità potenziali
        ValueModel entitaPotenzialiInRicercaValueModel = new ValueHolder(Boolean.FALSE);
        DefaultFieldMetadata entitaPotenzialimetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(entitaPotenzialiInRicercaValueModel), Boolean.class, true, null);
        getFormModel().add("entitaPotenzialiPerRicerca", entitaPotenzialiInRicercaValueModel, entitaPotenzialimetaData);

        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "caricatoreSearch", true);
    }

    /**
     * Crea e restituisce il binding per le note di testata.
     *
     * @param bf
     *            BindingFactory
     * @return binding
     */
    private Binding createBindingForNote(PanjeaSwingBindingFactory bf) {
        Binding noteBinding = bf.createBinding("areaOrdine.areaOrdineNote.noteTestata");
        return noteBinding;
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:100dlu, left:default, 10dlu, left:default",
                "1dlu,default, 1dlu,default, 1dlu,default, 1dlu,default, 1dlu,default, 1dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        // ### ROW 2: data registrazione e tipo area magazzino
        dataRegistrazioneComponent = builder.addPropertyAndLabel("areaOrdine.dataRegistrazione", 1)[1];
        builder.addPropertyAndLabel("areaOrdine.dataConsegna", 5);

        builder.addLabel("areaOrdine.tipoAreaOrdine", 8);
        Binding bindingTipoDoc = bf.createBoundSearchText("areaOrdine.tipoAreaOrdine",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" });
        SearchPanel tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 10);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
        builder.nextRow();

        // // ### ROW 4: data documento, numero documento e riferimenti ordine
        builder.addPropertyAndLabel("areaOrdine.documento.dataDocumento", 1);
        builder.addPropertyAndLabel("areaOrdine.dataInizioTrasporto", 5);

        builder.addLabel("areaOrdine.documento.codice", 8);
        Binding bindingCodice = bf.createBoundCodice("areaOrdine.documento.codice",
                "areaOrdine.tipoAreaOrdine.tipoDocumento.registroProtocollo", "areaOrdine.documento.valoreProtocollo",
                "areaOrdine.tipoAreaOrdine.tipoDocumento.patternNumeroDocumento", null);
        builder.addBinding(bindingCodice, 10);

        builder.nextRow();

        // // ### entita e sede
        JLabel labelEntita = builder.addLabel("areaOrdine.documento.entita", 1);
        Binding bindEntita = getEntitaBinding(bf);
        SearchPanel searchEntita = (SearchPanel) builder.addBinding(bindEntita, 3, 5, 1);

        entitaComponents = new JComponent[] { labelEntita, searchEntita };

        JLabel labelSedeEntita = builder.addLabel("areaOrdine.documento.sedeEntita", 8);
        Binding sedeEntitaBinding = bf.createBoundSearchText("areaOrdine.documento.sedeEntita", null,
                new String[] { "areaOrdine.documento.entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 10);
        searchPanelSede.getTextFields().get(null).setColumns(30);
        sedeEntitaComponents = new JComponent[] { labelSedeEntita, searchPanelSede };
        builder.nextRow();

        rifornimentoComponents = new JComponent[] {};
        PluginManager pluginManager = (PluginManager) Application.instance().getApplicationContext()
                .getBean("pluginManager");
        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            SearchPanel searchDistributore = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
                    "areaRifornimento.distributore", new String[] { "codice", "descrizione", "datiVending.modello" },
                    new String[] { "areaOrdine.documento.entita", "areaOrdine.documento.sedeEntita" },
                    new String[] { "entitaParam", "sedeEntitaParam" }), 3, 5, 1);
            searchDistributore.getTextFields().get("codice").setColumns(5);
            searchDistributore.getTextFields().get("descrizione").setColumns(12);
            searchDistributore.getTextFields().get("datiVending.modello").setColumns(8);

            JLabel labelInstallazione = builder.addLabel("installazione", 8);
            SearchPanel searchInstallazionePanel = (SearchPanel) builder.addBinding(
                    bf.createBoundSearchText("areaRifornimento.installazione", new String[] { "codice", "descrizione" },
                            new String[] { "areaOrdine.documento.entita", "areaOrdine.documento.sedeEntita" },
                            new String[] { "clienteLite", "sede" }),
                    10);
            searchInstallazionePanel.getTextFields().get("codice").setColumns(8);
            searchInstallazionePanel.getTextFields().get("descrizione").setColumns(20);

            JLabel labelDistributore = builder.addLabel("distributore");
            rifornimentoComponents = new JComponent[] { labelDistributore, searchDistributore, labelInstallazione,
                    searchInstallazionePanel };

            builder.nextRow();

            JLabel labelOperatore = builder.addLabel("operatore", 1);
            SearchPanel searchOperatore = (SearchPanel) builder.addBinding(
                    bf.createBoundSearchText("areaRifornimento.operatore", new String[] { "codice", "denominazione" },
                            new String[] { "caricatoreSearch" }, new String[] { "caricatoreParam" }),
                    3, 5, 1);
            searchOperatore.getTextFields().get("codice").setColumns(5);
            searchOperatore.getTextFields().get("denominazione").setColumns(24);

            rifornimentoComponents = ArrayUtils.addAll(rifornimentoComponents, labelOperatore, searchOperatore);
            builder.nextRow();

        }

        // // ### depositi origine
        builder.addLabel("areaOrdine.depositoOrigine", 1);

        Binding bindDepOrigine = bf.createBoundSearchText("areaOrdine.depositoOrigine",
                new String[] { "codice", "descrizione" });
        SearchPanel searchPanelDepositoOrigine = (SearchPanel) builder.addBinding(bindDepOrigine, 3, 5, 1);
        searchPanelDepositoOrigine.getTextFields().get("codice").setColumns(5);
        searchPanelDepositoOrigine.getTextFields().get("descrizione").setColumns(15);

        JLabel rifOrdineLabel = builder.addLabel("areaOrdine.riferimentiOrdine", 8);
        builder.addComponent(createRiferimentiOrdine(bf), 10);
        // Ho creato l'array con i controlli con l'ultimo indice a null. Lo sostituisco
        riferimentoOrdineComponents[riferimentoOrdineComponents.length - 1] = rifOrdineLabel;

        builder.nextRow();

        // // ### note, anno competenza
        builder.setComponentAttributes("f,c");
        builder.addLabel("areaOrdine.areaOrdineNote.noteTestata", 1);
        builder.addBinding(createBindingForNote(bf), 3, 5, 1);
        builder.setComponentAttributes("l,c");

        fieldAnnoCompetenza = (JTextField) builder.addPropertyAndLabel("areaOrdine.annoMovimento", 8)[1];
        fieldAnnoCompetenza.setColumns(5);

        installChangeListeners();
        // Initializzo il valueModel
        getValueModel("areaOrdine");
        getValueModel("areaOrdine.id");
        getValueModel("areaOrdine.tipoAreaOrdine");
        getValueModel("areaOrdine.documento.sedeEntita");
        getValueModel("areaOrdine.inserimentoBloccato");
        getValueModel("areaRateEnabled");

        // lista di componenti che voglio saltare nella normale policy di ciclo del focus
        List<Component> componentsToSkip = new ArrayList<Component>();
        componentsToSkip.add(fieldAnnoCompetenza);

        panjeaFocusTraversalPolicy = new PanjeaFocusTraversalPolicy(
                getActiveWindow().getControl().getFocusTraversalPolicy(), componentsToSkip);

        setInitialVisibleComponent();

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        logger.debug("--> Enter createNewObject");
        // recupera il precedente oggetto dal Form e ne recupera i valori
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) getFormObject();
        areaOrdineFullDTO = areaOrdineFullDTO.getInitializedNewObject();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        areaOrdineFullDTO.getAreaOrdine().setDataRegistrazione(calendar.getTime());
        areaOrdineFullDTO.getAreaOrdine().setDataConsegna(calendar.getTime());

        String codiceValutaAzienda = aziendaCorrente.getCodiceValuta();
        // HACK inizializzazione di codiceValuta
        areaOrdineFullDTO.getAreaOrdine().getDocumento().getTotale().setCodiceValuta(codiceValutaAzienda);
        areaOrdineFullDTO.getAreaOrdine().getDocumento().getTotale().setCodiceValuta(codiceValutaAzienda);
        areaOrdineFullDTO.getAreaOrdine().getTotaliArea().getSpeseTrasporto().setCodiceValuta(codiceValutaAzienda);
        areaOrdineFullDTO.getAreaOrdine().getTotaliArea().getAltreSpese().setCodiceValuta(codiceValutaAzienda);
        areaOrdineFullDTO.getAreaOrdine().getTotaliArea().getTotaleMerce().setCodiceValuta(codiceValutaAzienda);

        if (areaOrdineFullDTO.getAreaOrdine().getAnnoMovimento() == -1) {
            areaOrdineFullDTO.getAreaOrdine().setAnnoMovimento(aziendaCorrente.getAnnoMagazzino());
        }
        return areaOrdineFullDTO;
    }

    /**
     *
     * @param bf
     *            {@link PanjeaSwingBindingFactory}
     * @return componente per la gestione dei riferimenti ordine
     */
    private JComponent createRiferimentiOrdine(PanjeaSwingBindingFactory bf) {
        JPanel rootPanel = getComponentFactory().createPanel(new HorizontalLayout(5));
        // rootPanel.add(new JLabel("data"));
        JComponent dataOrdineComponent = bf.createBinding("areaOrdine.riferimentiOrdine.dataOrdine").getControl();
        rootPanel.add(dataOrdineComponent);

        JComponent labelNr = new JLabel("nr.");
        rootPanel.add(labelNr);

        JTextField numeroOrd = (JTextField) bf.createBinding("areaOrdine.riferimentiOrdine.numeroOrdine").getControl();
        numeroOrd.setColumns(10);
        rootPanel.add(numeroOrd);

        JComponent labelRic = new JLabel("ric");
        rootPanel.add(labelRic);

        JComponent modRicezioneCompoment = bf.createBinding("areaOrdine.riferimentiOrdine.modalitaRicezione")
                .getControl();
        rootPanel.add(modRicezioneCompoment);

        riferimentoOrdineComponents = new JComponent[] { dataOrdineComponent, labelNr, numeroOrd, labelRic,
                modRicezioneCompoment, null };

        return rootPanel;
    }

    @Override
    public void dispose() {
        getFormModel().removePropertyChangeListener(FormModel.DIRTY_PROPERTY, getDirtyChangeListener());
        getFormModel().removePropertyChangeListener(FormModel.ENABLED_PROPERTY, getEnabledChangeListener());
        getFormModel().getFieldMetadata("areaOrdine.riferimentiOrdine.dataOrdine")
                .removePropertyChangeListener(FormModel.ENABLED_PROPERTY, getDatRiferimentoOrdinePropertyChange());
        super.dispose();
        getFormModel().getFieldMetadata("areaOrdine.dataRegistrazione")
                .removePropertyChangeListener(getDatRegistrazioneChangeListener());
    }

    /**
     * @return focus policy per la testata dell'area ordine
     */
    public PanjeaFocusTraversalPolicy getAreaOrdineFocusTraversalPolicy() {
        return panjeaFocusTraversalPolicy;
    }

    /**
     * @return datRegistrazioneChangeListener
     */
    private DataRegistrazioneChangeListener getDatRegistrazioneChangeListener() {
        if (datRegistrazioneChangeListener == null) {
            datRegistrazioneChangeListener = new DataRegistrazioneChangeListener();
        }
        return datRegistrazioneChangeListener;
    }

    /**
     * @return DatRiferimentoOrdinePropertyChange
     */
    private DatRiferimentoOrdinePropertyChange getDatRiferimentoOrdinePropertyChange() {
        if (datRiferimentoOrdinePropertyChange == null) {
            datRiferimentoOrdinePropertyChange = new DatRiferimentoOrdinePropertyChange();
        }
        return datRiferimentoOrdinePropertyChange;
    }

    /**
     * @return dirtyChangeListener
     */
    private DirtyChangeListener getDirtyChangeListener() {
        if (dirtyChangeListener == null) {
            dirtyChangeListener = new DirtyChangeListener();
        }
        return dirtyChangeListener;
    }

    /**
     * @return enabledChangeListener
     */
    private EnabledChangeListener getEnabledChangeListener() {
        if (enabledChangeListener == null) {
            enabledChangeListener = new EnabledChangeListener();
        }
        return enabledChangeListener;
    }

    /**
     * crea e restituisce il SearchTextBinding di Entita.
     *
     * @param bf
     *            BindingFactory
     * @return binding
     */
    private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
        Binding bindingEntita = bf.createBoundSearchText("areaOrdine.documento.entita",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "areaOrdine.tipoAreaOrdine.tipoDocumento.tipoEntita", "entitaPotenzialiPerRicerca" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY,
                        EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(24);
        return bindingEntita;
    }

    /**
     * Installa i listener sul form model o sulle proprietà del form.
     */
    private void installChangeListeners() {
        getFormModel().addPropertyChangeListener(FormModel.DIRTY_PROPERTY, getDirtyChangeListener());

        getFormModel().addPropertyChangeListener(FormModel.ENABLED_PROPERTY, getEnabledChangeListener());

        tipoAreaOrdinePropertyChange = new TipoAreaOrdinePropertyChange();
        addFormValueChangeListener("areaOrdine.tipoAreaOrdine", tipoAreaOrdinePropertyChange);

        getFormModel().getFieldMetadata("areaOrdine.riferimentiOrdine.dataOrdine")
                .addPropertyChangeListener(FormModel.ENABLED_PROPERTY, getDatRiferimentoOrdinePropertyChange());

        getFormModel().getValueModel("areaOrdine.dataRegistrazione")
                .addValueChangeListener(getDatRegistrazioneChangeListener());
    }

    /**
     * Metodo che assegna il focus al componente.
     */
    public void requestFocusForFormComponent() {
        ((JDateChooser) dataRegistrazioneComponent).getComponent(0).requestFocusInWindow();
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    private void setComponentsRifornimentoVisible(boolean visible) {
        for (JComponent componente : rifornimentoComponents) {
            componente.setVisible(visible);
        }
    }

    /**
     * Inizializza lo stato iniziale dei controlli.
     */
    private void setInitialVisibleComponent() {

        getFormModel().getFieldMetadata("areaOrdine.documento.entita").setEnabled(false);

        setComponentsRifornimentoVisible(false);

        if (entitaComponents != null) {
            for (JComponent component : entitaComponents) {
                component.setVisible(false);
            }
        }

        if (sedeEntitaComponents != null) {
            for (JComponent component : sedeEntitaComponents) {
                component.setVisible(false);
            }
        }
    }

}
