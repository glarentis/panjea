package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.control.LinguaListCellRenderer;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form di {@link SedeEntita}.
 *
 * @author adriano
 * @version 1.0, 18/dic/07
 */
public class SedeEntitaForm extends PanjeaAbstractForm implements Focussable {

    /**
     * Guarded per disabilitare la selezione di tipoSede se questa e' gia' la sede principale.
     *
     * @author adriano
     * @version 1.0, 18/nov/2008
     */
    private class ReadOnlySedeEntitaFormGuard implements Guarded {

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void setEnabled(boolean enabled) {
            SedeEntita sedeEntita = (SedeEntita) getFormModel().getFormObject();
            // if (sedeEntita.getId() != null) {
            boolean isEnabled = (sedeEntita.getId() == null
                    || (sedeEntita.getTipoSede() != null && !(sedeEntita.getTipoSede().isSedePrincipale()))) && enabled;
            for (JComponent componente : componentiSedeSecondaria) {
                // nell'array sono inserite anche le label che non disabilito
                // per pulizia si dovrebbero mettere solamente i controlli utilizzati
                // ma nel caso di searchText diventa impegnativo.
                if (!(componente instanceof JLabel)) {
                    componente.setEnabled(isEnabled);
                }
            }
            // }
        }

    }

    private static final String FORM_ID = "sedeEntitaForm";

    private static final String FORMMODEL_ID = "sedeEntitaFormModel";
    private static final String PANJEA_AGENTI_PLUGN_NAME = "panjeaAgenti";

    // indica se viene visualizzata una sede generica nel form
    private boolean sedeGenerica = false;

    // componenti visibili solamente se la sede non e' quella principale
    private final List<JComponent> componentiSedeSecondaria = new ArrayList<JComponent>();

    private PluginManager pluginManager = null;

    private RefreshableValueHolder categoriaEntitaValueHolder;

    private TipoEntita tipoEntita;

    private JTextField codiceSedeTextField;

    /**
     * Costruttore di default.
     *
     * @param sedeEntita
     *            la sede per inizializzare il form
     */
    public SedeEntitaForm(final SedeEntita sedeEntita) {
        this(sedeEntita, false);
    }

    /**
     * Costruttore che prevede la scelta se predisporre il form per una sede generica.
     *
     * @param sedeEntita
     *            sede entità
     * @param sedeGenerica
     *            flag sede generica
     */
    public SedeEntitaForm(final SedeEntita sedeEntita, final boolean sedeGenerica) {
        super(PanjeaFormModelHelper.createFormModel(sedeEntita, false, FORMMODEL_ID), FORM_ID);
        pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
        this.sedeGenerica = sedeGenerica;

        // Aggiungo al formModel il value model dinamino per poter caricare tutte le sedi (
        // selezione nella sede per
        // spedizione)
        ValueModel tutteSediFilterValueModel = new ValueHolder(CaricamentoSediEntita.TUTTE);
        DefaultFieldMetadata tutteSediMetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tutteSediFilterValueModel), CaricamentoSediEntita.class, true, null);
        getFormModel().add("tutteSediFilter", tutteSediFilterValueModel, tutteSediMetaData);

        // Aggiungo al formModel il value model dinamino per poter caricare solo
        // le sedi per spedizione
        ValueModel sediPerSpedizioneFilterValueModel = new ValueHolder(
                CaricamentoSediEntita.SOLO_SEDI_SPEDIZIONE_SERVIZI);
        DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(sediPerSpedizioneFilterValueModel), CaricamentoSediEntita.class, true,
                null);
        getFormModel().add("sediPerSpedizioneFilter", sediPerSpedizioneFilterValueModel, metaData);

        // aggiungo la finta proprietà tipiEntita per far si che la search text dei vettori mi
        // selezioni solo vettori e
        // non altri tipi entità
        List<TipoEntita> tipiEntitaVettori = new ArrayList<TipoEntita>();
        tipiEntitaVettori.add(TipoEntita.VETTORE);

        ValueModel tipiEntitaVettoriValueModel = new ValueHolder(tipiEntitaVettori);
        DefaultFieldMetadata tipiEntitaVettoriData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tipiEntitaVettoriValueModel), List.class, true, null);
        getFormModel().add("tipiEntitaVettori", tipiEntitaVettoriValueModel, tipiEntitaVettoriData);

        // aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi
        // selezioni solo agenti e
        // non altri tipi entità
        List<TipoEntita> tipiEntitaAgente = new ArrayList<TipoEntita>();
        tipiEntitaAgente.add(TipoEntita.AGENTE);

        ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
        DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
        getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "80dlu,4dlu,70dlu,10dlu,left:pref,4dlu,70dlu,10dlu,left:pref,4dlu,left:pref,4dlu,70dlu,4dlu,60dlu,fill:default:grow",
                "2dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);

        builder.setLabelAttributes("r, c");
        JComponent[] components = null;

        builder.nextRow();
        builder.setRow(2);

        builder.addHorizontalSeparator("Dati anagrafici", 16);
        builder.nextRow();

        if (!sedeGenerica) {
            components = builder.addPropertyAndLabel("abilitato", 1);
            for (JComponent component : components) {
                componentiSedeSecondaria.add(component);
            }

            JLabel labelTipoSede = builder.addLabel("tipoSede", 5);
            SearchPanel searchPanelTipoSede = (SearchPanel) builder
                    .addBinding(bf.createBoundSearchText("tipoSede", new String[] { null }), 7, 5, 1);
            searchPanelTipoSede.getTextFields().get(null).setColumns(70);
            componentiSedeSecondaria.add(labelTipoSede);
            componentiSedeSecondaria.add(searchPanelTipoSede);
            builder.nextRow();
        } else {
            JLabel labelTipoSede = builder.addLabel("tipoSede", 5);
            JComponent componentLabelTipoSede = builder.addBinding(bf.createBoundLabel("tipoSede.descrizione"), 7, 5,
                    1);
            componentiSedeSecondaria.add(labelTipoSede);
            componentiSedeSecondaria.add(componentLabelTipoSede);
            builder.nextRow();
        }

        codiceSedeTextField = (JTextField) builder.addPropertyAndLabel("codice")[1];
        codiceSedeTextField.setColumns(11);

        JTextField descrizioneField = (JTextField) builder
                .addPropertyAndLabel("sede." + SedeAnagrafica.PROP_DESCRIZIONE, 5, 5, 1)[1];
        descrizioneField.setColumns(70);
        builder.nextRow();

        JTextField indirizzoField = (JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_INDIRIZZO, 1,
                9, 1)[1];
        indirizzoField.setColumns(70);
        builder.nextRow();

        DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("sede.datiGeografici", "right:80dlu");
        builder.addBinding(bindingDatiGeografici, 1, 13, 1);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_TELEFONO, 1)[1]).setColumns(20);
        ((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_FAX, 5)[1]).setColumns(20);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, 1)[1]).setColumns(20);
        builder.addPropertyAndLabel("sede.indirizzoPEC", 5);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("sede." + SedeAnagrafica.PROP_WEB, 1, 5, 1)[1]).setColumns(20);
        if (pluginManager.isPresente(PluginManager.PLUGIN_FATTURAZIONE_PA)) {
            builder.addPropertyAndLabel("codiceUfficioPA", 11);
        }
        builder.nextRow();

        builder.addHorizontalSeparator("Dati spedizione", 16);
        builder.nextRow();

        builder.addPropertyAndLabel("sede.tipoSpedizioneDocumenti", 1);
        builder.addPropertyAndLabel("sede.indirizzoMailSpedizione", 5);
        builder.addPropertyAndLabel("sede.spedizioneDocumentiViaPEC", 11);
        builder.nextRow();

        Binding sedeSpedizioneBinding = bf.createBoundSearchText("sedeSpedizione", new String[] { "sede.descrizione" },
                new String[] { "entita.entitaLite", "tutteSediFilter" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID,
                        SedeEntitaSearchObject.PARAMETER_TIPO_SEDE_FILTER });
        builder.addLabel("sedeSpedizione", 1);
        SearchPanel searchPanelSedeSpedizione = (SearchPanel) builder.addBinding(sedeSpedizioneBinding, 3, 5, 1);
        searchPanelSedeSpedizione.getTextFields().get("sede.descrizione").setColumns(30);
        builder.nextRow();

        builder.addHorizontalSeparator("Altri dati", 16);
        builder.nextRow();

        if (tipoEntita == TipoEntita.FORNITORE) {
            builder.addLabel("rapportoBancarioAzienda", 1);
            builder.addBinding(bf.createBoundSearchText("rapportoBancarioAzienda", null, RapportoBancarioAzienda.class),
                    3, 5, 1);
            builder.nextRow();
        }

        builder.addLabel("codiceValuta", 1);
        builder.addBinding(bf.createBoundSearchText("codiceValuta", null, ValutaAzienda.class), 3);

        // builder.setComponentAttributes("l,c");

        builder.addLabel("lingua", 5);
        LinguaListCellRenderer linguaListCellRenderer = new LinguaListCellRenderer();
        JComboBox comboboxLingua = (JComboBox) builder
                .addBinding(bf.createBoundComboBox("lingua", linguaListCellRenderer.getLingue()), 7);
        comboboxLingua.setRenderer(linguaListCellRenderer);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("cig", 1)[1]).setColumns(11);
        ((JTextField) builder.addPropertyAndLabel("cup", 5)[1]).setColumns(11);
        builder.nextRow();

        Binding bindingZonaGeo = bf.createBoundSearchText("zonaGeografica", new String[] { "codice", "descrizione" });
        SearchPanel searchPanel = (SearchPanel) bindingZonaGeo.getControl();
        searchPanel.getTextFields().get("codice").setColumns(7);
        searchPanel.getTextFields().get("descrizione").setColumns(21);
        builder.addLabel("zonaGeografica", 1);
        builder.addBinding(bindingZonaGeo, 3, 5, 1);
        builder.nextRow();

        Binding sedeEntitaBinding = bf.createBoundSearchText("sedeCollegata", new String[] { "sede.descrizione" },
                new String[] { "entita.entitaLite", "sediPerSpedizioneFilter" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID,
                        SedeEntitaSearchObject.PARAMETER_TIPO_SEDE_FILTER });
        builder.addLabel("sedeCollegata", 1);
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 3, 5, 1);
        searchPanelSede.getTextFields().get("sede.descrizione").setColumns(30);
        builder.nextRow();

        Binding bindingEntita = bf.createBoundSearchText("vettore",
                new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaVettori" },
                new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
        ((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(6);
        builder.addLabel("vettore", 1);
        builder.addBinding(bindingEntita, 3, 5, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("ordinamento", 1);
        builder.addPropertyAndLabel("predefinita", 5);
        builder.nextRow();

        if (!sedeGenerica) {
            components = builder.addPropertyAndLabel("ereditaDatiCommerciali", 1);
            for (JComponent component : components) {
                componentiSedeSecondaria.add(component);
            }

            components = builder.addPropertyAndLabel("ereditaRapportiBancari", 5);
            for (JComponent component : components) {
                componentiSedeSecondaria.add(component);
            }

            builder.nextRow();

            new PanjeaFormGuard(getFormModel(), new ReadOnlySedeEntitaFormGuard(), FormGuard.ON_ENABLED);
        }

        builder.addPropertyAndLabel("bloccoSede.blocco", 1);
        builder.addPropertyAndLabel("bloccoSede.noteBlocco", 5, 5, 1);
        builder.nextRow();

        if (pluginManager.isPresente(PANJEA_AGENTI_PLUGN_NAME)) {

            builder.addLabel("agente", 1);
            SearchPanel searchAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("agente",
                    new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
                    new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class), 3, 5, 1);
            searchAgente.getTextFields().get("codice").setColumns(6);
            builder.nextRow();
        }

        // // creo la lista per le categorie
        builder.setLabelAttributes("r, t");
        final IAnagraficaBD anagraficaBD = RcpSupport.getBean(IAnagraficaBD.BEAN_ID);
        categoriaEntitaValueHolder = new RefreshableValueHolder(new Closure() {

            @Override
            public Object call(Object arg0) {
                List<CategoriaEntita> categorie = anagraficaBD.caricaCategorieEntita("descrizione", null);
                return categorie;
            }
        });
        categoriaEntitaValueHolder.refresh();
        Binding categoriaEntitaBinding = bf.createBoundShuttleList("categoriaEntita", categoriaEntitaValueHolder,
                "descrizione");
        builder.addLabel("categoriaEntita", 1);
        builder.addBinding(categoriaEntitaBinding, 3, 5, 1);
        builder.nextRow();

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        SedeEntita sedeEntita = new SedeEntita();
        AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        sedeEntita.getSede().getDatiGeografici().setNazione(aziendaCorrente.getNazione());
        sedeEntita.setCodiceValuta(aziendaCorrente.getCodiceValuta());
        return super.createNewObject();
    }

    /**
     * @return the tipoEntita
     */
    public TipoEntita getTipoEntita() {
        return tipoEntita;
    }

    @Override
    public void grabFocus() {
        if (CollectionUtils.isNotEmpty(componentiSedeSecondaria) && componentiSedeSecondaria.get(1).isEnabled()) {
            componentiSedeSecondaria.get(1).requestFocusInWindow();
        } else {
            codiceSedeTextField.requestFocusInWindow();
        }
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        this.tipoEntita = tipoEntita;
    }

}
