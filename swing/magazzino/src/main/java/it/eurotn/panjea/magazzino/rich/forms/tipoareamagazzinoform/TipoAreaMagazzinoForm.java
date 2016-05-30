package it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.SortableTable;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData.TipoDatoAccompagnatorioMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Gestisce i controlli per il tipoAreaMagazzino.<br>
 * Nasconde il tipo movimento scarico se il TipoEntità non è azienda
 *
 * @author giangi
 */
public class TipoAreaMagazzinoForm extends PanjeaAbstractForm {

    public class GestioneTipoEntitaListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent arg0) {
            logger.debug("--> Enter propertyChange per il form Obje");
            Assert.notNull(getValueModel("tipoDocumento"), "Tipo documento non può essere nullo");
            reloadTipiMovimentoList();
            updateListinoVisibilita();
            logger.debug("--> Exit propertyChange");
        }

    }

    public class ProvenienzaPrezzoListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateListinoVisibilita();
        }

    }

    private class TipoDocFatturazioneListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            getFormModel().getFieldMetadata("tipoDocumentoPerFatturazioneDescrizioneMaschera").setEnabled(
                    getValueModel("tipoDocumentoPerFatturazione").getValue() != null && !getFormModel().isReadOnly());
            // visualizzo la legenda se impostato un tipo documento
            // per fatturazione
            pannelloLegendaMascheraTipoDocFatturazione.setVisible(
                    getValueModel("tipoDocumentoPerFatturazione").getValue() != null && !getFormModel().isReadOnly());
        }
    }

    private static Logger logger = Logger.getLogger(TipoAreaMagazzinoForm.class);

    private static final String FORM_ID = "tipoAreaMagazzinoForm";
    private static final String SEPARATOR_TIPO_AREA_MAGAZZINO = "tipoAreaMagazzino.areaMagazzino.label";
    private static final String SEPARATOR_PREZZO_MAGAZZINO = "tipoAreaMagazzino.prezzo.label";
    private static final String SEPARATOR_FATTURAZIONE_MAGAZZINO = "tipoAreaMagazzino.fatturazione.label";
    private static final String SEPARATOR_STAMPA_MAGAZZINO = "tipoAreaMagazzino.stampa.label";
    private static final String SEPARATOR_DEFAULT_MAGAZZINO = "tipoAreaMagazzino.default.label";
    private static final String SEPARATOR_DATI_ACCOMPAGNATORI = "tipoAreaMagazzino.datiAccompagnatori.label";

    protected ValueHolder tipiMovimentoEnum;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private AziendaCorrente aziendaCorrente;

    private JPanel pannelloLegendaMascheraTipoDocFatturazione;
    private List<JComponent> componentsListino = null;

    private PluginManager pluginManager;

    private PropertyChangeListener datiAccompagnatoriMetaDataChangeListener = new PropertyChangeListener() {

        @SuppressWarnings("unchecked")
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Set<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatoriImpostati = (Set<DatoAccompagnatorioMagazzinoMetaData>) getValue(
                    "datiAccompagnatoriMetaData");
            getValueModel("datiAccompagnatoriRichiesti").setValue(tuttiDatiAccompagnatori(datiAccompagnatoriImpostati));
        }

        private Set<DatoAccompagnatorioMagazzinoMetaDataPM> tuttiDatiAccompagnatori(
                final Set<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatori) {

            Set<DatoAccompagnatorioMagazzinoMetaDataPM> copia = new HashSet<DatoAccompagnatorioMagazzinoMetaDataPM>();

            for (DatoAccompagnatorioMagazzinoMetaData datoImpostato : datiAccompagnatori) {
                copia.add(new DatoAccompagnatorioMagazzinoMetaDataPM(datoImpostato));
            }

            List<DatoAccompagnatorioMagazzinoMetaDataPM> righeDaAggiungere = new ArrayList<DatoAccompagnatorioMagazzinoMetaDataPM>(
                    TipoDatoAccompagnatorioMagazzino.values().length);

            for (TipoDatoAccompagnatorioMagazzino tipo : TipoDatoAccompagnatorioMagazzino.values()) {
                if (!DatoAccompagnatorioMagazzinoMetaData.contieneTipoDatoAccompagnatorio(tipo, datiAccompagnatori)) {
                    DatoAccompagnatorioMagazzinoMetaDataPM datoNonRichiesto = new DatoAccompagnatorioMagazzinoMetaDataPM(
                            tipo);
                    righeDaAggiungere.add(datoNonRichiesto);
                }
            }

            copia.addAll(righeDaAggiungere);
            return copia;
        }
    };

    private PropertyChangeListener datiAccompagnatoriRichiestiChangeListener = new PropertyChangeListener() {

        private Set<DatoAccompagnatorioMagazzinoMetaData> estraiDatiAccompagnatoriRichiesti(
                final Set<DatoAccompagnatorioMagazzinoMetaDataPM> datiAccompagnatoriPM) {

            Set<DatoAccompagnatorioMagazzinoMetaData> richiesti = new HashSet<DatoAccompagnatorioMagazzinoMetaData>();

            for (DatoAccompagnatorioMagazzinoMetaDataPM datoAccompagnatorioPM : datiAccompagnatoriPM) {
                if (datoAccompagnatorioPM.isRichiesto()) {
                    richiesti.add(datoAccompagnatorioPM.getDatoAccompagnatorio());
                }
            }
            return richiesti;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!getFormModel().isReadOnly()) {
                Set<DatoAccompagnatorioMagazzinoMetaDataPM> datiAccompagnatoriPM = (Set<DatoAccompagnatorioMagazzinoMetaDataPM>) getValue(
                        "datiAccompagnatoriRichiesti");
                Set<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatori = estraiDatiAccompagnatoriRichiesti(
                        datiAccompagnatoriPM);
                getValueModel("datiAccompagnatoriMetaData").setValueSilently(datiAccompagnatori,
                        datiAccompagnatoriMetaDataChangeListener);
            }
        }
    };

    /**
     * Costruttore.
     *
     * @param tipoAreaMagazzino
     *            tipo area magazzino
     */
    public TipoAreaMagazzinoForm(final TipoAreaMagazzino tipoAreaMagazzino) {
        super(PanjeaFormModelHelper.createFormModel(tipoAreaMagazzino, false, FORM_ID), FORM_ID);
        this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

        tipiMovimentoEnum = new ValueHolder();
        updateListinoVisibilita();

        // Aggiungo il value model che mi servirà solamente nella search text
        // delle entità
        // per cercare solo le entità abilitate
        ValueModel entitaAbilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
        DefaultFieldMetadata entitaAbilitateMetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(entitaAbilitateInRicercaValueModel), Boolean.class, true, null);
        getFormModel().add("entitaAbilitateInRicerca", entitaAbilitateInRicercaValueModel, entitaAbilitateMetaData);

        ValueModel datiAccompagnatoriRichiestiModel = new ValueHolder(
                new HashSet<DatoAccompagnatorioMagazzinoMetaDataPM>());
        DefaultFieldMetadata datiAccompagnatoriRichiestiFieldMetadata = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(datiAccompagnatoriRichiestiModel), Set.class, false, null);
        getFormModel().add("datiAccompagnatoriRichiesti", datiAccompagnatoriRichiestiModel,
                datiAccompagnatoriRichiestiFieldMetadata);

        getValueModel("datiAccompagnatoriRichiesti").addValueChangeListener(datiAccompagnatoriRichiestiChangeListener);
        getValueModel("datiAccompagnatoriMetaData").addValueChangeListener(datiAccompagnatoriMetaDataChangeListener);
    }

    /**
     * Aggiunge tutti i listener alle proprietà.
     */
    private void addListener() {
        logger.debug("--> Enter addListener");
        addFormValueChangeListener("tipoDocumento.tipoEntita", new GestioneTipoEntitaListener());
        addFormValueChangeListener("provenienzaPrezzo", new ProvenienzaPrezzoListener());

        getValueModel("tipoDocumentoPerFatturazione").addValueChangeListener(new TipoDocFatturazioneListener());
        addFormObjectChangeListener(new TipoDocFatturazioneListener());
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new TipoDocFatturazioneListener());

        logger.debug("--> Exit addListener");
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:50dlu, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,default:grow,100dlu",
                "3dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);

        builder.setLabelAttributes("r, c");
        componentsListino = new ArrayList<JComponent>();

        builder.nextRow();
        builder.setRow(2);

        JLabel tdcodlabel = builder.addLabel("tipoDocumento.codice", getComponentFactory().createLabel(""), 1, 2);
        tdcodlabel.setFont(new Font(tdcodlabel.getFont().getName(), Font.BOLD, tdcodlabel.getFont().getSize()));
        JTextField tdcod = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "codice", 3, 2, 3, 1);
        tdcod.setColumns(18);
        tdcod.setFont(new Font(tdcod.getFont().getName(), Font.BOLD, tdcod.getFont().getSize()));

        JLabel tddesclabel = builder.addLabel("tipoDocumento.descrizione", getComponentFactory().createLabel(""), 9, 2);
        tddesclabel.setFont(new Font(tddesclabel.getFont().getName(), Font.BOLD, tddesclabel.getFont().getSize()));
        JTextField tddesc = (JTextField) builder.addNestedPropertyReadOnly("tipoDocumento", "descrizione", 11, 2);
        tddesc.setColumns(18);
        tddesc.setFont(new Font(tddesc.getFont().getName(), Font.BOLD, tddesc.getFont().getSize()));
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_TIPO_AREA_MAGAZZINO), 11);
        builder.nextRow();

        reloadTipiMovimentoList();

        builder.addLabel("tipoMovimento", 1, 6);
        builder.addBinding(bf.createBoundComboBox("tipoMovimento", tipiMovimentoEnum), 3, 6, 3, 1);
        builder.addPropertyAndLabel("sezioneTipoMovimento", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("operazioneAreaContabileNonTrovata", 1, 10, 3);
        builder.nextRow();

        builder.addPropertyAndLabel("dataDocLikeDataReg", 1, 12);
        builder.addPropertyAndLabel("qtaZeroPermessa", 5, 12);
        if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
            builder.addLabel("gestioneAgenti", 9, 12)
                    .setIcon(RcpSupport.getIcon("it.eurotn.panjea.agenti.domain.Agente"));
            builder.addProperty("gestioneAgenti", 11, 12);
        }
        builder.nextRow();

        builder.addPropertyAndLabel("valoriFatturato", 1, 14);
        builder.addPropertyAndLabel("aggiornaCostoUltimo", 5, 14);
        if (pluginManager.isPresente(PluginManager.PLUGIN_CONAI)) {
            builder.addLabel("gestioneConai", 9, 14).setIcon(RcpSupport.getIcon("conaiArticoloEditor.image"));
            builder.addProperty("gestioneConai", 11, 14);
        }
        builder.nextRow();

        builder.addPropertyAndLabel("richiestaMezzoTrasporto", 1, 16);
        builder.addPropertyAndLabel("richiestaMezzoTrasportoObbligatorio", 5, 16);
        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            builder.addLabel("gestioneVending", 9, 16).setIcon(RcpSupport.getIcon("vendingMenu.icon"));
            builder.addProperty("gestioneVending", 11, 16);
        }

        getFormModel().getFieldMetadata("richiestaMezzoTrasportoObbligatorio")
                .setEnabled((boolean) getFormModel().getValueModel("richiestaMezzoTrasporto").getValue());

        builder.nextRow();

        builder.nextRow();
        builder.addHorizontalSeparator(getMessage(SEPARATOR_PREZZO_MAGAZZINO), 11);
        builder.nextRow();

        builder.addPropertyAndLabel("provenienzaPrezzo", 1, 22, 3);
        builder.addPropertyAndLabel("strategiaEsportazioneFlusso", 9, 22, 1);
        builder.nextRow();

        builder.addPropertyAndLabel("strategiaTotalizzazioneDocumento", 1, 24, 3);
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_DEFAULT_MAGAZZINO), 11);
        builder.nextRow();

        Binding bindingEntita = bf.createBoundSearchText("entitaPredefinita",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "tipoDocumento.tipoEntita", "entitaAbilitateInRicerca" }, new String[] {
                        EntitaByTipoSearchObject.TIPOENTITA_KEY, EntitaByTipoSearchObject.FILTRO_ENTITA_ABILITATO });
        builder.addLabel("entitaPredefinita", 1, 28);
        SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(bindingEntita, 3, 28, 3, 1);
        searchPanelEntita.getTextFields().get("codice").setColumns(5);
        searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(20);

        JLabel labelListino = builder.addLabel("listino", 1, 30);
        SearchPanel searchListino = (SearchPanel) builder.addBinding(bf.createBoundSearchText("listino", null), 3, 30,
                3, 1);
        componentsListino.add(labelListino);
        componentsListino.add(searchListino);
        builder.nextRow();

        builder.addPropertyAndLabel("depositoOrigineBloccato", 1, 32);
        Azienda azienda = new Azienda();
        azienda.setId(aziendaCorrente.getId());
        Binding bindingDepOrigine = bf.createBoundSearchText("depositoOrigine",
                new String[] { "codice", "descrizione" });
        builder.addLabel("depositoOrigine", 5, 32);
        SearchPanel searchPanelDeposito = (SearchPanel) builder.addBinding(bindingDepOrigine, 7, 32, 5, 1);
        searchPanelDeposito.getTextFields().get("codice").setColumns(5);
        searchPanelDeposito.getTextFields().get("descrizione").setColumns(18);
        builder.nextRow();

        builder.addPropertyAndLabel("depositoDestinazioneBloccato", 1, 34);
        Binding bindingDepDest = bf.createBoundSearchText("depositoDestinazione",
                new String[] { "codice", "descrizione" });
        builder.addLabel("depositoDestinazione", 5, 34);
        SearchPanel searchDepositoDestinazine = (SearchPanel) builder.addBinding(bindingDepDest, 7, 34, 5, 1);
        searchDepositoDestinazine.getTextFields().get("codice").setColumns(5);
        searchDepositoDestinazine.getTextFields().get("descrizione").setColumns(18);
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_STAMPA_MAGAZZINO), 11);
        builder.nextRow();

        builder.setLabelAttributes("r, t");

        builder.addPropertyAndLabel("descrizionePerStampa", 1, 40, 3);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("numeroCopiePerStampa", 1, 42)[1]).setColumns(3);
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_FATTURAZIONE_MAGAZZINO), 11);
        builder.nextRow();

        ValueHolder tipoDocumenti = new ValueHolder(
                magazzinoDocumentoBD.caricaTipiDocumentoAnagraficaPerFatturazione());
        ComboBoxBinding tipoDocFattBinding = (ComboBoxBinding) bf.createBoundComboBox("tipoDocumentoPerFatturazione",
                tipoDocumenti, "descrizione");
        // do la possibilità di non selezionare il tipo documento per fatturazione
        TipoDocumento tipoDocumentoVuoto = new TipoDocumento();
        tipoDocumentoVuoto.setDescrizione(getMessageSource().getMessage("none", new Object[] {}, Locale.getDefault()));
        tipoDocFattBinding.setEmptySelectionValue(tipoDocumentoVuoto);
        builder.addLabel("tipoDocumentoPerFatturazione", 1, 46);
        builder.addBinding(tipoDocFattBinding, 3, 46, 3, 1);

        if (pluginManager.isPresente(PluginManager.PLUGIN_FATTURAZIONE_PA)) {
            ComboBoxBinding tipoDocFattPABinding = (ComboBoxBinding) bf
                    .createBoundComboBox("tipoDocumentoPerFatturazionePA", tipoDocumenti, "descrizione");
            // do la possibilità di non selezionare il tipo documento per fatturazione PA
            tipoDocFattPABinding.setEmptySelectionValue(tipoDocumentoVuoto);
            builder.addLabel("tipoDocumentoPerFatturazionePA", 9, 46);
            builder.addBinding(tipoDocFattPABinding, 11, 46);
        }
        builder.nextRow();

        builder.addPropertyAndLabel("tipoDocumentoPerFatturazioneDescrizioneMaschera", 1, 48, 3);
        getFormModel().getFieldMetadata("tipoDocumentoPerFatturazioneDescrizioneMaschera")
                .setEnabled(getValueModel("tipoDocumentoPerFatturazione").getValue() != null);
        builder.nextRow();

        builder.addPropertyAndLabel("generaAreaContabile", 1, 50);
        builder.nextRow();

        builder.addPropertyAndLabel("noteSuDestinazione", 1, 52);
        builder.nextRow();

        builder.addPropertyAndLabel("riepilogoIva", 1, 54);
        builder.nextRow();

        builder.addHorizontalSeparator(getMessage(SEPARATOR_DATI_ACCOMPAGNATORI), 5);
        builder.nextRow();

        // Aggiungo una tabella per la configurazione dei tipi dati accompagnatori
        TableEditableBinding<DatoAccompagnatorioMagazzinoMetaDataPM> datiAccompagnatoriTableBinding = new TableEditableBinding<DatoAccompagnatorioMagazzinoMetaDataPM>(
                getFormModel(), "datiAccompagnatoriRichiesti", Set.class, new DatoAccompagnatorioTableModel());
        datiAccompagnatoriTableBinding.getControl().setPreferredSize(new Dimension(500, 150));
        // devo inizializzarla editable a false, me la trovo abilitata
        datiAccompagnatoriTableBinding.getTableWidget().setEditable(false);
        SortableTable jtable = (SortableTable) datiAccompagnatoriTableBinding.getTableWidget().getTable();
        jtable.setSortingEnabled(false);
        jtable.sortColumn(1, true, false);
        jtable.sortColumn(2, false, true);
        builder.addBinding(datiAccompagnatoriTableBinding, 1, 58, 5, 1);
        builder.nextRow();

        // pannello per visualizzare la legenda della maschera per il tipo
        // documento di fatturazione
        createPanelLegendaFatturazione();
        builder.setComponentAttributes("f,d");
        builder.addComponent(pannelloLegendaMascheraTipoDocFatturazione, 7, 48, 6, 11);

        addListener();
        updateListinoVisibilita();

        return builder.getPanel();
    }

    /**
     * Crea la legenda per le variabili possibili per la maschera di fatturazione.
     *
     * @return maschera in formato html
     */
    private String createLegendaTextForMascheraFatturazione() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html>");
        stringBuffer.append("<B>");
        stringBuffer.append(getMessage("legenda").toUpperCase());
        stringBuffer.append("</B><BR><hr></hr>");
        stringBuffer.append("<ul>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$numeroDocumento$</B> = ");
        stringBuffer.append(getMessage("numeroDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$dataDocumento$</B> = ");
        stringBuffer.append(getMessage("dataDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$codiceTipoDocumento$</B> = ");
        stringBuffer.append(getMessage("codiceTipoDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$descrizioneTipoDocumento$</B> = ");
        stringBuffer.append(getMessage("descrizioneTipoDocumento"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$codiceEntita$</B> = ");
        stringBuffer.append(getMessage("codiceEntita"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$descrizioneEntita$</B> = ");
        stringBuffer.append(getMessage("descrizioneEntita"));
        stringBuffer.append("</li>");

        stringBuffer.append("<li>");
        stringBuffer.append("<B>$sede$</B> = ");
        stringBuffer.append(getMessage("descrizioneSede"));
        stringBuffer.append("</li>");

        stringBuffer.append("</ul>");
        stringBuffer.append("</html>");

        return stringBuffer.toString();
    }

    /**
     *
     * @return pannello con la legenda per la maschera di rifatturazione.
     */
    private JPanel createPanelLegendaFatturazione() {
        pannelloLegendaMascheraTipoDocFatturazione = getComponentFactory().createPanel(new BorderLayout());
        JLabel labelLegenda = new JLabel(createLegendaTextForMascheraFatturazione());
        pannelloLegendaMascheraTipoDocFatturazione.add(labelLegenda, BorderLayout.NORTH);
        pannelloLegendaMascheraTipoDocFatturazione.setVisible(false);
        return pannelloLegendaMascheraTipoDocFatturazione;
    }

    /**
     * Nasconde il tipo movimento scarico se il TipoEntità non è azienda.
     *
     * @return
     */
    private void reloadTipiMovimentoList() {
        List<TipoMovimento> tipologiaMovimento = new ArrayList<TipoMovimento>();
        for (TipoMovimento tipoMovimento : TipoMovimento.values()) {
            tipologiaMovimento.add(tipoMovimento);
        }
        tipiMovimentoEnum.setValue(tipologiaMovimento);
        logger.debug("--> Exit getTipiMovimentoList ");
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setFormModel(ValidatingFormModel formModel) {
        super.setFormModel(formModel);

    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     * Aggiorna la visibilità dei controlli sul listino.
     */
    private void updateListinoVisibilita() {
        TipoEntita tipoEntita = (TipoEntita) getValueModel("tipoDocumento.tipoEntita").getValue();
        ProvenienzaPrezzo provenienzaPrezzo = (ProvenienzaPrezzo) getValueModel("provenienzaPrezzo").getValue();
        if (tipoEntita != null && provenienzaPrezzo != null) {
            boolean isListinoComponentsVisible = tipoEntita == TipoEntita.AZIENDA
                    && provenienzaPrezzo == ProvenienzaPrezzo.LISTINO;
            for (JComponent component : componentsListino) {
                component.setVisible(isListinoComponentsVisible);
            }
        }
    }
}
