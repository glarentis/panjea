package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.support.ClassPropertyAccessStrategy;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.DepositoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.search.TipoAreaMagazzinoSearchObject;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.form.builder.support.OverlayHelper;

/**
 * Form per la creazione dei controlli legati alla testata del documento di magazzino.
 *
 * @author adriano
 * @version 1.0, 29/ago/2008
 */
public class AreaMagazzinoForm extends PanjeaAbstractForm {

    public class AnnoMovimentiChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Integer annoCompetenzaAzienda = aziendaCorrente.getAnnoContabile();
            Integer annoCompetenza = (Integer) evt.getNewValue();
            if (annoCompetenza != null && annoCompetenza.equals(annoCompetenzaAzienda)) {
                fieldAnnoCompetenza.setBackground(UIManager.getDefaults().getColor("TextField.background"));
            } else {
                fieldAnnoCompetenza.setBackground(new Color(255, 235, 235));
            }
        }

    }

    private class ApriNoteEntitaCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "apriNoteEntitaCommand";
        private IMagazzinoDocumentoBD magazzinoDocumentoBD;

        /**
         * Costruttore.
         *
         */
        public ApriNoteEntitaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);

            this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
        }

        @Override
        protected void doExecuteCommand() {

            SedeEntita sedeEntita = (SedeEntita) getFormModel().getValueModel("areaMagazzino.documento.sedeEntita")
                    .getValue();

            if (sedeEntita != null && sedeEntita.getId() != null) {
                NoteAreaMagazzino note = magazzinoDocumentoBD.caricaNoteSede(sedeEntita);

                // se arriva note[]!=null allora visualizzo il dialogo
                if (!note.isEmpty()) {
                    NoteMagazzinoEntitaDialog noteDialog = new NoteMagazzinoEntitaDialog(note);
                    noteDialog.setPreferredSize(new Dimension(400, 400));
                    noteDialog.showDialog();
                }
            }
        }

    }

    private class ApriSituazioneRateCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand command) {

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            ((ApriSitauzioneRateClienteCommand) command)
                    .setEntita((EntitaLite) getFormModel().getValueModel("areaMagazzino.documento.entita").getValue());
            return true;
        }
    }

    /**
     * PropertyChange incaricato di rendere visibile/invisibile i componenti per la selezione del deposito di
     * destinazione. <br>
     * Vedi UseCase PNJ.MA.02.02
     *
     * @author adriano
     * @version 1.0, 08/set/2008
     */
    private class DepositoDestinazioneMetaDataPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter propertyChange DepositoDestinazionePropertyChange");

            boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
            AreaMagazzinoForm.this.setVisibleDepositoDestinazione(visible);
        }

    }

    /**
     * Property change per attivare o disattivare (stato readonly) la scelta di tipo area magazzino a seconda se il
     * documento e' nuovo oppure no.
     *
     * @author leonardo
     */
    private class FormModelAreaMagazzinoChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // DIRTY,ENABLED,COMMITTABLE e HASERRORS sono le proprietà che
            // cambiano stato; per coprire tutti i casi,
            // tra cui il caso in cui preparo un nuovo documento da uno
            // esistente, devo aggiornare lo stato readOnly
            // di tipoAreaMagazzino in tutti questi casi.
            Integer idAreaMagazzino = (Integer) getFormModel().getValueModel("areaMagazzino.id").getValue();
            getFormModel().getFieldMetadata("areaMagazzino.tipoAreaMagazzino").setReadOnly(idAreaMagazzino != null);
        }

    }

    private class MezzoTrasportoMetaDataPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter propertyChange");

            boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
            AreaMagazzinoForm.this.setVisibleMezzoTrasporto(visible);
        }
    }

    private static Logger logger = Logger.getLogger(AreaMagazzinoForm.class);

    private static final String FORM_ID = "areaMagazzinoForm";

    private PluginManager pluginManager = null;
    private AziendaCorrente aziendaCorrente;

    private JComponent[] entitaComponents = null;
    private JComponent[] sedeEntitaComponents = null;
    private JComponent[] rifornimentoComponents = null;
    private JComponent dataRegistrazioneComponent;
    private JLabel labelMezzoTrasporto;
    private SearchPanel searchPaneMezzoTrasporto;
    private JLabel labelDepositoDestinazione;
    private SearchPanel searchPanelDepositoDestinazione;
    private PannelloInformazioniSede pannelloInformazioniSede = null;
    private JTextField fieldAnnoCompetenza;

    private AbstractButton buttonSituazioneRate;
    private AbstractButton buttonApriNoteEntita;
    private AbstractButton buttonToggleVisibleFido;
    private ApriSitauzioneRateClienteCommand apriSituazioneRatecommand;
    private ApriSituazioneRateCommandInterceptor apriSituazioneRateCommandInterceptor;

    private MezzoTrasportoMetaDataPropertyChange mezzoTrasportoMetaDataPropertyChange;
    private DepositoDestinazioneMetaDataPropertyChange depositoDestinazioneMetaDataPropertyChange;
    private TipoAreaMagazzinoPropertyChange tipoAreaMagazzinoPropertyChange;
    private AnnoMovimentiChangeListener annoMovimentoChangeListener;

    private OverlayHelper overlayFido;
    private PanjeaFocusTraversalPolicy panjeaFocusTraversalPolicy;

    private SearchPanel tipoDocumentoSearchPanel;

    private CodicePanel codicePanel;

    /**
     * Costruttore.
     */
    public AreaMagazzinoForm() {
        super(PanjeaFormModelHelper.createFormModel(new AreaMagazzinoFullDTO(), false, FORM_ID,
                (ClassPropertyAccessStrategy) RcpSupport.getBean(AreaMagazzinoFullDTOPropertyAccessStrategy.BEAN_ID)),
                FORM_ID);

        aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);

        // Aggiungo al formModel il value model dinamino per importoRateAperte
        PanjeaSwingUtil.addValueModelToForm(BigDecimal.ZERO, getFormModel(), BigDecimal.class, "importoRateAperte",
                true);
        PanjeaSwingUtil.addValueModelToForm(BigDecimal.ZERO, getFormModel(), BigDecimal.class, "importoDocumentiAperti",
                true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.FALSE, getFormModel(), Boolean.class, "entitaPotenzialiPerRicerca",
                true);
        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "entitaAbilitateInRicerca",
                true);

        PanjeaSwingUtil.addValueModelToForm(Boolean.TRUE, getFormModel(), Boolean.class, "caricatoreSearch", true);

        PanjeaSwingUtil.addValueModelToForm(Boolean.FALSE, getFormModel(), Boolean.class, "soloDepositiFurgoni", false);

        PanjeaSwingUtil.addValueModelToForm(new ArrayList<>(), getFormModel(), List.class, "depositiAggiuntivi", false);

    }

    /**
     * Aggiunge un {@link PropertyChangeListener} sulla proprietà enabled ai metadata degli attributi del FormModel<br>
     * per interagire sui componenti.
     */
    private void addListeners() {
        mezzoTrasportoMetaDataPropertyChange = new MezzoTrasportoMetaDataPropertyChange();
        getFormModel().getFieldMetadata("areaMagazzino.mezzoTrasporto")
                .addPropertyChangeListener(FormModel.ENABLED_PROPERTY, mezzoTrasportoMetaDataPropertyChange);

        depositoDestinazioneMetaDataPropertyChange = new DepositoDestinazioneMetaDataPropertyChange();
        getFormModel().getFieldMetadata("areaMagazzino.depositoDestinazione")
                .addPropertyChangeListener(FormModel.ENABLED_PROPERTY, depositoDestinazioneMetaDataPropertyChange);

        annoMovimentoChangeListener = new AnnoMovimentiChangeListener();
        addFormValueChangeListener("areaMagazzino.annoMovimento", annoMovimentoChangeListener);

        tipoAreaMagazzinoPropertyChange = new TipoAreaMagazzinoPropertyChange();
        tipoAreaMagazzinoPropertyChange.setFormModel(getFormModel());
        addFormValueChangeListener("areaMagazzino.tipoAreaMagazzino", tipoAreaMagazzinoPropertyChange);
    }

    /**
     * Crea il binding per le note.
     *
     * @param bf
     *            il binding factory
     * @return Binding
     */
    private Binding createBindingForNote(PanjeaSwingBindingFactory bf) {
        Binding noteBinding = bf.createBinding("areaMagazzino.areaMagazzinoNote.noteTestata");
        return noteBinding;
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,left:default, left:pref:grow, 10dlu, left:default,4dlu,left:default,left:15dlu,left:15dlu",
                "1dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        // ### ROW 2: data registrazione e tipo area magazzino
        dataRegistrazioneComponent = builder.addPropertyAndLabel("areaMagazzino.dataRegistrazione", 1)[1];

        builder.addLabel("areaMagazzino.tipoAreaMagazzino", 6);
        Binding bindingTipoDoc = bf.createBoundSearchText("areaMagazzino.tipoAreaMagazzino",
                new String[] { "tipoDocumento.codice", "tipoDocumento.descrizione" },
                new String[] { "areaMagazzino.tipoAreaMagazzino.gestioneVending" },
                new String[] { TipoAreaMagazzinoSearchObject.PARAMETRO_GESTIONE_VENDING });
        tipoDocumentoSearchPanel = (SearchPanel) builder.addBinding(bindingTipoDoc, 8);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.codice").setColumns(5);
        tipoDocumentoSearchPanel.getTextFields().get("tipoDocumento.descrizione").setColumns(23);
        builder.nextRow();

        builder.addPropertyAndLabel("areaMagazzino.documento.dataDocumento", 1);

        builder.addLabel("areaMagazzino.documento.codice", 6);
        Binding bindingCodice = bf.createBoundCodice("areaMagazzino.documento.codice",
                "areaMagazzino.tipoAreaMagazzino.tipoDocumento.registroProtocollo",
                "areaMagazzino.documento.valoreProtocollo",
                "areaMagazzino.tipoAreaMagazzino.tipoDocumento.patternNumeroDocumento", null);
        codicePanel = (CodicePanel) builder.addBinding(bindingCodice, 8);
        codicePanel.getTextFieldCodice().setColumns(15);
        builder.nextRow();

        // ### entita e sede
        JLabel labelEntita = builder.addLabel("areaMagazzino.documento.entita", 1);
        Binding bindEntita = getEntitaBinding(bf);
        SearchPanel searchEntita = (SearchPanel) builder.addBinding(bindEntita, 3, 2, 1);

        builder.addComponent(getButtonSituazioneRate(), 5);

        entitaComponents = new JComponent[] { labelEntita, searchEntita, getButtonSituazioneRate(),
                getButtonApriNoteEntita() };

        JLabel labelSedeEntita = builder.addLabel("areaMagazzino.documento.sedeEntita", 6);
        Binding sedeEntitaBinding = bf.createBoundSearchText("areaMagazzino.documento.sedeEntita", null,
                new String[] { "areaMagazzino.documento.entita" },
                new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
        SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 8);
        searchPanelSede.getTextFields().get(null).setColumns(30);
        sedeEntitaComponents = new JComponent[] { labelSedeEntita, searchPanelSede };

        builder.addComponent(getButtonApriNoteEntita(), 9);

        pluginManager = (PluginManager) Application.instance().getApplicationContext().getBean("pluginManager");
        if (pluginManager.isPresente("panjeaFido")) {
            pannelloInformazioniSede = new PannelloInformazioniSede(getFormModel());
            pannelloInformazioniSede.setName("fido");
            pannelloInformazioniSede.setMinimumSize(searchPanelSede.getSize());
            overlayFido = OverlayHelper.attachOverlay(pannelloInformazioniSede, searchPanelSede, SwingConstants.NORTH,
                    -50, 46);

            buttonToggleVisibleFido = new ToggleFidoVisibilityCommand(pannelloInformazioniSede).createButton();
            buttonToggleVisibleFido.setFocusable(false);
            builder.addComponent(buttonToggleVisibleFido, 10);
            entitaComponents = ArrayUtils.add(entitaComponents, buttonToggleVisibleFido);
        }
        builder.nextRow();

        rifornimentoComponents = new JComponent[] {};
        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            SearchPanel searchDistributore = (SearchPanel) builder.addBinding(bf.createBoundSearchText(
                    "areaRifornimento.distributore", new String[] { "codice", "descrizione", "datiVending.modello" },
                    new String[] { "areaMagazzino.documento.entita", "areaMagazzino.documento.sedeEntita" },
                    new String[] { "entitaParam", "sedeEntitaParam" }), 3, 2, 1);
            searchDistributore.getTextFields().get("codice").setColumns(6);
            searchDistributore.getTextFields().get("descrizione").setColumns(15);
            searchDistributore.getTextFields().get("datiVending.modello").setColumns(9);

            JLabel labelInstallazione = builder.addLabel("installazione", 6);
            SearchPanel searchInstallazionePanel = (SearchPanel) builder.addBinding(
                    bf.createBoundSearchText("areaRifornimento.installazione", new String[] { "codice", "descrizione" },
                            new String[] { "areaMagazzino.documento.entita", "areaMagazzino.documento.sedeEntita" },
                            new String[] { "clienteLite", "sede" }),
                    8);
            searchInstallazionePanel.getTextFields().get("codice").setColumns(10);
            searchInstallazionePanel.getTextFields().get("descrizione").setColumns(20);

            JLabel labelDistributore = builder.addLabel("distributore");
            rifornimentoComponents = new JComponent[] { labelDistributore, searchDistributore, labelInstallazione,
                    searchInstallazionePanel };

            builder.nextRow();
        }

        // ##### mezzo di trasporto
        labelMezzoTrasporto = builder.addLabel("areaMagazzino.mezzoTrasporto", 1);
        Binding bindingMezzoTrasporto = bf.createBoundSearchText("areaMagazzino.mezzoTrasporto",
                new String[] { "targa", "descrizione" }, new String[] { "areaMagazzino.documento.entita" },
                new String[] { "entita" });
        searchPaneMezzoTrasporto = (SearchPanel) builder.addBinding(bindingMezzoTrasporto, 3, 2, 1);
        searchPaneMezzoTrasporto.getTextFields().get("targa").setColumns(5);
        searchPaneMezzoTrasporto.getTextFields().get("descrizione").setColumns(20);
        builder.nextRow();

        // ### depositi origine e destinazione
        builder.addLabel("areaMagazzino.depositoOrigine", 1);
        Binding bindDepOrigine = bf.createBoundSearchText("areaMagazzino.depositoOrigine",
                new String[] { "codice", "descrizione" }, new String[] { "soloDepositiFurgoni", "depositiAggiuntivi" },
                new String[] { DepositoSearchObject.SOLO_DEPOSITO_MEZZO_TRASPORTO_PARAM,
                        DepositoSearchObject.DEPOSITI_AGGIUNTIVI_PARAM });
        SearchPanel searchPanelDepositoOrigine = (SearchPanel) builder.addBinding(bindDepOrigine, 3, 2, 1);
        searchPanelDepositoOrigine.getTextFields().get("codice").setColumns(5);
        searchPanelDepositoOrigine.getTextFields().get("descrizione").setColumns(20);

        labelDepositoDestinazione = builder.addLabel("areaMagazzino.depositoDestinazione", 6);

        Binding bindDepDest = bf.createBoundSearchText("areaMagazzino.depositoDestinazione",
                new String[] { "codice", "descrizione" });
        searchPanelDepositoDestinazione = (SearchPanel) builder.addBinding(bindDepDest, 8);
        searchPanelDepositoDestinazione.getTextFields().get("codice").setColumns(5);
        searchPanelDepositoDestinazione.getTextFields().get("descrizione").setColumns(20);

        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            JLabel labelOperatore = builder.addLabel("operatore", 6);
            SearchPanel searchOperatore = (SearchPanel) builder.addBinding(
                    bf.createBoundSearchText("areaRifornimento.operatore", new String[] { "codice", "denominazione" },
                            new String[] { "caricatoreSearch" }, new String[] { "caricatoreParam" }),
                    8);
            searchOperatore.getTextFields().get("codice").setColumns(10);
            searchOperatore.getTextFields().get("denominazione").setColumns(20);

            rifornimentoComponents = ArrayUtils.addAll(rifornimentoComponents, labelOperatore, searchOperatore);
        }

        builder.nextRow();

        // ### note, anno competenza
        builder.addLabel("areaMagazzino.areaMagazzinoNote.noteTestata", 1);
        builder.addBinding(createBindingForNote(bf), 3, 2, 1);

        fieldAnnoCompetenza = (JTextField) builder.addPropertyAndLabel("areaMagazzino.annoMovimento", 6)[1];
        fieldAnnoCompetenza.setColumns(5);

        addListeners();
        // Initializzo il valueModel
        getValueModel("areaMagazzino");
        getValueModel("areaMagazzino.id");
        getValueModel("areaMagazzino.tipoAreaMagazzino.depositoOrigine");
        getValueModel("areaMagazzino.mezzoTrasporto");
        getValueModel("areaMagazzino.stampaPrezzi");
        getValueModel("areaMagazzino.documento.sedeEntita");
        getValueModel("importoRateAperte");
        getValueModel("importoDocumentiAperti");
        getValueModel("areaMagazzino.idZonaGeografica");
        getValueModel("areaMagazzino.inserimentoBloccato");

        installReadOnlyPropertyChange();
        if (pannelloInformazioniSede != null) {
            getValueModel("importoRateAperte").addValueChangeListener(pannelloInformazioniSede);
            getValueModel("areaMagazzino").addValueChangeListener(pannelloInformazioniSede);
        }

        // lista di componenti che voglio saltare nella normale policy di ciclo del focus
        List<Component> componentsToSkip = new ArrayList<>();
        componentsToSkip.add(fieldAnnoCompetenza);

        panjeaFocusTraversalPolicy = new PanjeaFocusTraversalPolicy(
                getActiveWindow().getControl().getFocusTraversalPolicy(), componentsToSkip);

        setInitialVisibleComponent();

        tipoAreaMagazzinoPropertyChange.setEntitaComponents(entitaComponents);
        tipoAreaMagazzinoPropertyChange.setSedeEntitaComponents(sedeEntitaComponents);
        tipoAreaMagazzinoPropertyChange.setRifornimentoComponent(rifornimentoComponents);
        tipoAreaMagazzinoPropertyChange.setLabelDepositoDestinazione(labelDepositoDestinazione);

        logger.debug("--> Exit createFormControl");
        return builder.getPanel();
    }

    @Override
    public Object createNewObject() {
        logger.debug("--> Enter createNewObject");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // se ho data documento uguale a data registrazione non lo faccio qui,
        // altrimenti mi ritrovo l'oggetto finale
        // già pronto (committable senza errori) e nel caso non abbia altri dati
        // da modificare non posso salvarlo; nella
        // page dell'areaMagazzino quando preparo un nuovo oggetto reimposto il
        // TipoAreaMagazzino
        // attivando così il propertyChange che imposta tutti i valori di
        // default per il tam scelto
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
        areaMagazzinoFullDTO.getAreaMagazzino().setDataRegistrazione(calendar.getTime());

        String codiceValutaAzienda = aziendaCorrente.getCodiceValuta();
        // HACK inizializzazione di codiceValuta
        areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getTotale().setCodiceValuta(codiceValutaAzienda);
        areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getTotale().setCodiceValuta(codiceValutaAzienda);
        areaMagazzinoFullDTO.getAreaMagazzino().getSpeseTrasporto().setCodiceValuta(codiceValutaAzienda);
        areaMagazzinoFullDTO.getAreaMagazzino().getAltreSpese().setCodiceValuta(codiceValutaAzienda);
        areaMagazzinoFullDTO.getAreaMagazzino().getTotaleMerce().setCodiceValuta(codiceValutaAzienda);

        if (areaMagazzinoFullDTO.getAreaMagazzino().getAnnoMovimento() == -1) {
            areaMagazzinoFullDTO.getAreaMagazzino().setAnnoMovimento(aziendaCorrente.getAnnoMagazzino());
        }
        return areaMagazzinoFullDTO;
    }

    @Override
    public void dispose() {

        apriSituazioneRatecommand.removeCommandInterceptor(apriSituazioneRateCommandInterceptor);
        apriSituazioneRatecommand = null;

        getFormModel().getFieldMetadata("areaMagazzino.mezzoTrasporto")
                .removePropertyChangeListener(FormModel.ENABLED_PROPERTY, mezzoTrasportoMetaDataPropertyChange);

        getFormModel().getFieldMetadata("areaMagazzino.depositoDestinazione")
                .removePropertyChangeListener(FormModel.ENABLED_PROPERTY, depositoDestinazioneMetaDataPropertyChange);

        addFormValueChangeListener("areaMagazzino.annoMovimento", annoMovimentoChangeListener);
        removeFormValueChangeListener("areaMagazzino.annoMovimento", annoMovimentoChangeListener);

        removeFormValueChangeListener("areaMagazzino.tipoAreaMagazzino", tipoAreaMagazzinoPropertyChange);

        if (pluginManager.isPresente("panjeaFido")) {
            overlayFido.dispose();
        }
        super.dispose();
    }

    /**
     * @return focus policy per la testata dell'area magazzino
     */
    public PanjeaFocusTraversalPolicy getAreaMagazzinoFocusTraversalPolicy() {
        return panjeaFocusTraversalPolicy;
    }

    /**
     * @return restituisce il bottone per le note entità.
     */
    private JComponent getButtonApriNoteEntita() {
        if (buttonApriNoteEntita == null) {
            buttonApriNoteEntita = new ApriNoteEntitaCommand().createButton();
            buttonApriNoteEntita.setFocusable(false);
        }
        return buttonApriNoteEntita;
    }

    /**
     * @return restituisce il bottone per la situazione rate.
     */
    private JComponent getButtonSituazioneRate() {
        if (buttonSituazioneRate == null) {
            buttonSituazioneRate = getSituazioneRateCommand().createButton();
            buttonSituazioneRate.setFocusable(false);
        }
        return buttonSituazioneRate;
    }

    /**
     * Crea e restituisce il SearchTextBinding per l' entita aggiungendo il pulsante per la richiesta della situazione
     * rate.
     *
     * @param bf
     *            il binding factory
     * @return Binding
     */
    private Binding getEntitaBinding(PanjeaSwingBindingFactory bf) {
        Binding bindingEntita = bf.createBoundSearchText("areaMagazzino.documento.entita",
                new String[] { "codice", "anagrafica.denominazione" },
                new String[] { "areaMagazzino.tipoAreaMagazzino.tipoDocumento.tipoEntita", "entitaPotenzialiPerRicerca",
                        "entitaAbilitateInRicerca" },
                new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY,
                        EntitaByTipoSearchObject.INCLUDI_ENTITA_POTENZIALI,
                        EntitaByTipoSearchObject.FILTRO_ENTITA_ABILITATO });
        SearchPanel searchPanel = (SearchPanel) bindingEntita.getControl();
        searchPanel.getTextFields().get("codice").setColumns(5);
        searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(20);
        return bindingEntita;
    }

    /**
     * @return Command per richiedere la situazione rate di una entita'.
     */
    private ActionCommand getSituazioneRateCommand() {
        apriSituazioneRatecommand = new ApriSitauzioneRateClienteCommand();
        apriSituazioneRateCommandInterceptor = new ApriSituazioneRateCommandInterceptor();
        apriSituazioneRatecommand.addCommandInterceptor(apriSituazioneRateCommandInterceptor);
        return apriSituazioneRatecommand;
    }

    /**
     * Aggiunge i property change per questo form, utili per vincolare l'inserimento.
     */
    private void installReadOnlyPropertyChange() {
        getFormModel().addPropertyChangeListener(new FormModelAreaMagazzinoChangeListener());

    }

    /**
     * Richiede il focus il componente della data di registrazione.
     */
    public void requestFocusForFormComponent() {
        // ((JDateChooser) dataRegistrazioneComponent).requestFocusInWindow();
        ((JDateChooser) dataRegistrazioneComponent).getDateEditor().getUiComponent().requestFocusInWindow();
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
    }

    /**
     * Inizializza lo stato iniziale dei controlli.
     */
    private void setInitialVisibleComponent() {

        getFormModel().getFieldMetadata("areaMagazzino.mezzoTrasporto").setEnabled(false);
        getFormModel().getFieldMetadata("areaMagazzino.depositoDestinazione").setEnabled(false);

        AreaMagazzinoFullDTO amfdto = (AreaMagazzinoFullDTO) getFormObject();

        boolean isRifornimento = !amfdto.getAreaMagazzino().getTipoAreaMagazzino().isNew()
                && amfdto.getAreaMagazzino().getTipoAreaMagazzino().isGestioneVending();
        codicePanel.getTextFieldCodice().setEnabled(!isRifornimento);

        for (JComponent componente : entitaComponents) {
            componente.setVisible(isRifornimento);
        }
        for (JComponent componente : sedeEntitaComponents) {
            componente.setVisible(isRifornimento);
        }
        // dataDocumento.setEnabled(!isRifornimento);
        // getFormModel().getFieldMetadata("areaMagazzino.documento.entita").setEnabled(isRifornimento);
        // getFormModel().getFieldMetadata("areaMagazzino.documento.sedeEntita").setEnabled(isRifornimento);
        getFormModel().getFieldMetadata("areaMagazzino.documento.dataDocumento").setEnabled(!isRifornimento);

        if (rifornimentoComponents != null) {
            for (JComponent component : rifornimentoComponents) {
                component.setVisible(isRifornimento);
            }
        }

        setVisibleMezzoTrasporto(false);
        setVisibleDepositoDestinazione(false);
    }

    /**
     * rende visibili/invisibili i componenti del deposito di destinazione.
     *
     * @param visible
     *            visible
     */
    private void setVisibleDepositoDestinazione(boolean visible) {
        logger.debug("--> Enter setVisibleDepositoDestinazione " + visible);
        labelDepositoDestinazione.setVisible(visible);
        searchPanelDepositoDestinazione.setVisible(visible);
    }

    /**
     * rende visibili/invisibili i componenti del mezzo di trasporto.
     *
     * @param visible
     *            visible
     */
    private void setVisibleMezzoTrasporto(boolean visible) {
        logger.debug("--> Enter setVisibleMezzoTrasporto " + visible);
        labelMezzoTrasporto.setVisible(visible);
        searchPaneMezzoTrasporto.setVisible(visible);
    }

}