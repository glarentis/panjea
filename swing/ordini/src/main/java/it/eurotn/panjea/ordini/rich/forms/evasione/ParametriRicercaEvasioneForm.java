package it.eurotn.panjea.ordini.rich.forms.evasione;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.CheckBoxTreeBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ParametriRicercaEvasioneForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "parametriRicercaEvasioneForm";
    public static final String FORMMODEL_ID = "parametriRicercaEvasioneFormModel";

    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private PluginManager pluginManager = null;

    private CheckBoxTreeBinding tipiDocCheckBoxBinding;

    /**
     * Costruttore.
     */
    public ParametriRicercaEvasioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaEvasione(), false, FORMMODEL_ID), FORM_ID);

        pluginManager = (PluginManager) Application.instance().getApplicationContext().getBean("pluginManager");

        // Aggiungo il value model che mi servirà solamente nella search text
        // delle entità per escludere le entità potenziali
        ValueModel entitaPotenzialiInRicercaValueModel = new ValueHolder(Boolean.FALSE);
        DefaultFieldMetadata entitaPotenzialimetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(entitaPotenzialiInRicercaValueModel), Boolean.class, true, null);
        getFormModel().add("entitaPotenzialiPerRicerca", entitaPotenzialiInRicercaValueModel, entitaPotenzialimetaData);
        ValueModel sediDisabilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
        DefaultFieldMetadata sediDisabilitatemetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(sediDisabilitateInRicercaValueModel), Boolean.class, true, null);
        getFormModel().add("sediDisabilitate", sediDisabilitateInRicercaValueModel, sediDisabilitatemetaData);

        // aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi
        // selezioni solo agenti e
        // non altri tipi entità
        List<TipoEntita> tipiEntitaAgente = new ArrayList<>();
        tipiEntitaAgente.add(TipoEntita.AGENTE);

        ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
        DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
        getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,fill:60dlu, 10dlu, right:pref,4dlu,fill:60dlu,10dlu, fill:default:grow",
                "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r,c");

        builder.setRow(2);
        builder.addPropertyAndLabel("dataRegistrazione", 1, 2);
        builder.addPropertyAndLabel("dataConsegna", 1, 4);
        builder.addLabel("entita", 1, 6);
        builder.addBinding(getEntitaBinding(bf), 3, 6);
        builder.addLabel("articolo", 1, 8);
        builder.addBinding(getArticoloBinding(bf), 3, 8);
        builder.addLabel("numeroDocumentoIniziale", 5, 2);
        builder.addBinding(bf.createBoundCodice("numeroDocumentoIniziale", true, false), 7, 2);
        builder.addLabel("numeroDocumentoFinale", 9, 2);
        builder.addBinding(bf.createBoundCodice("numeroDocumentoFinale", true, false), 11, 2);
        builder.addLabel("deposito", 5, 4);
        builder.addBinding(getDepositoBinding(bf), 7, 4, 5, 1);
        builder.nextRow();

        builder.addLabel("sedeEntita", 5, 6);
        builder.addBinding(getSedeEntitaBinding(bf), 7, 6, 5, 1);
        builder.nextRow();

        if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
            builder.addLabel("agente", 5, 8);
            builder.addBinding(getAgenteBinding(bf), 7, 8, 5, 1);
        }
        builder.addPropertyAndLabel("trasportoCuraMittente", 1, 10);
        builder.addPropertyAndLabel("dataInizioTrasporto", 5, 10);

        List<TipoAreaOrdine> tipiAreeOrdine = ordiniDocumentoBD.caricaTipiAreaOrdine("tipoDocumento.codice", null,
                true);
        List<TipoAreaOrdine> tipiCliente = new ArrayList<>();
        for (TipoAreaOrdine tipoAreaOrdine : tipiAreeOrdine) {
            if (tipoAreaOrdine.getTipoDocumento().getTipoEntita().equals(getValue("tipoEntita"))
                    || getValue("tipoEntita") == null) {
                tipiCliente.add(tipoAreaOrdine);
            }
        }
        tipiDocCheckBoxBinding = (CheckBoxTreeBinding) bf.createBoundCheckBoxTree("tipiAreaOrdine",
                new String[] { "tipoDocumento.classeTipoDocumento" }, new ValueHolder(tipiAreeOrdine));
        builder.addComponent(tipiDocCheckBoxBinding.getControl(), 13, 2, 1, 7);

        logger.debug("--> Exit createFormControl");
        return builder.getPanel();
    }

    /**
     * @param bf
     *            {@link PanjeaSwingBindingFactory}
     * @return binding per l'agente
     */
    private Binding getAgenteBinding(PanjeaSwingBindingFactory bf) {
        Binding bindingAgente = bf.createBoundSearchText("agente",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "entitaPotenzialiPerRicerca", "tipiEntitaAgente" },
                new String[] { EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI,
                        EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY },
                EntitaLite.class);
        SearchPanel searchPanel = (SearchPanel) bindingAgente.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        return bindingAgente;
    }

    /**
     * Crea e restituisce il SearchTextBinding per l'articolo.
     *
     * @param bf
     *            il binding factory
     * @return Binding
     */
    private Binding getArticoloBinding(PanjeaSwingBindingFactory bf) {
        Binding bindingArticolo = bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" });
        SearchPanel searchPanelArticolo = (SearchPanel) bindingArticolo.getControl();
        searchPanelArticolo.getTextFields().get("codice").setColumns(5);
        searchPanelArticolo.getTextFields().get("descrizione").setColumns(20);
        return bindingArticolo;
    }

    /**
     * Crea e restituisce il SearchTextBinding per il deposito.
     *
     * @param bf
     *            il binding factory
     * @return Binding
     */
    private Binding getDepositoBinding(PanjeaSwingBindingFactory bf) {
        Binding bindDeposito = bf.createBoundSearchText("deposito", new String[] { "codice", "descrizione" });
        SearchPanel searchPanelDepositoOrigine = (SearchPanel) bindDeposito.getControl();
        searchPanelDepositoOrigine.getTextFields().get("codice").setColumns(5);
        searchPanelDepositoOrigine.getTextFields().get("descrizione").setColumns(20);
        return bindDeposito;
    }

    /**
     * Crea e restituisce il SearchTextBinding per l' entita.
     *
     * @param bf
     *            il binding factory
     * @return Binding
     */
    private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
        Binding bindingEntita = bf.createBoundSearchText("entita",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "entitaPotenzialiPerRicerca", "tipoEntita" }, new String[] {
                        EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI, EntitaByTipoSearchObject.TIPOENTITA_KEY });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        return bindingEntita;
    }

    /**
     * Crea e restituisce il SearchTextBinding per la sede entita.
     *
     * @param bf
     *            il binding factory
     * @return Binding
     */
    private Binding getSedeEntitaBinding(PanjeaSwingBindingFactory bf) {
        Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
                new String[] { "entita", "sediDisabilitate" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID,
                        SedeEntitaSearchObject.PARAMETER_SEDE_DISABILITATE });
        return sedeEntitaBinding;
    }

    /**
     * @param ordiniDocumentoBD
     *            the ordiniDocumentoBD to set
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    /**
     * Visualizza o nasconde il pulsante per lo scambio di visualizzazione dei tipidocumento.
     *
     * @param visible
     *            <code>true</code> lo rende visibile, <code>false</code> no
     */
    public void setScambioVisualizzazioneTipiDocVisible(boolean visible) {
        tipiDocCheckBoxBinding.setScambioVisualizzazioneVisible(visible);
    }

}
