package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormComponentInterceptor;
import org.springframework.richclient.form.builder.FormComponentInterceptorFactory;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.commands.analisi.ApriAnalisiArticoloEntitaCommand;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.componenti.RigheArticoliComponentiCommand;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.ImportoBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RigaArticoloForm extends PanjeaAbstractForm implements Focussable {

    private class AggiornaListinoCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand actioncommand) {
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            RigaArticolo rigaArticolo = ((RigaArticolo) RigaArticoloForm.this.getFormObject());

            List<RigaMagazzino> righe = new ArrayList<RigaMagazzino>();
            righe.add(rigaArticolo);
            actioncommand.addParameter(AggiornaListinoCommand.PARAMETER_RIGHE_MAGAZZINO, righe);
            actioncommand.addParameter(AggiornaListinoCommand.PARAMETER_AREA_MAGAZZINO,
                    rigaArticolo.getAreaMagazzino());
            return true;
        }
    }

    private class ApriAnalisiArticoloCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand actioncommand) {
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            RigaArticolo rigaArticolo = ((RigaArticolo) RigaArticoloForm.this.getFormObject());

            ArticoloLite articolo = rigaArticolo.getArticolo();
            actioncommand.addParameter(ApriAnalisiArticoloEntitaCommand.PARAM_ID_ARTICOLO,
                    articolo == null ? null : articolo.getId());
            EntitaLite entita = rigaArticolo.getAreaMagazzino().getDocumento().getEntita();
            actioncommand.addParameter(ApriAnalisiArticoloEntitaCommand.PARAM_ID_ENTITA,
                    entita == null ? null : entita.getId());

            return true;
        }
    }

    private class ApriStatisticaArticoloCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand actioncommand) {
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            RigaArticolo rigaArticolo = ((RigaArticolo) RigaArticoloForm.this.getFormObject());
            actioncommand.addParameter(ApriStatisticaArticoloCommand.PARAM_LOADER_OBJECT, rigaArticolo.getArticolo());
            actioncommand.addParameter(ApriStatisticaArticoloCommand.PARAMETER_DEPOSITO,
                    rigaArticolo.getAreaMagazzino().getDepositoOrigine());
            return true;
        }
    }

    private class ArticoloPropertyChangeListener implements PropertyChangeListener {

        /**
         * Restituisce la stringa passata come parametro con la prima lettera maiuscola e le
         * rimanenti minuscole.
         *
         * @param word
         *            stringa da formattare
         * @return stringa formattata
         */
        private String capitalizeWord(String word) {

            String firstLetter = word.substring(0, 1); // Get first letter
            String remainder = word.substring(1); // Get remainder of word.
            String capitalized = firstLetter.toUpperCase() + remainder.toLowerCase();

            return capitalized;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            RigaArticolo rigaArticolo = (RigaArticolo) getFormObject();

            if (rigaArticolo.getArticolo() == null) {
                return;
            }

            lblGiacenza.setVisible(
                    !getFormModel().isReadOnly() && rigaArticolo.getArticolo().getId() != null && isGiacenzaEnable);
            lblGiacenza.setOpaque(true);
            lblGiacenza.setForeground(descrizioneLabel.getForeground());

            if (!getFormModel().isReadOnly()) {
                DefaultNumberFormatterFactory factory = new DefaultNumberFormatterFactory("#,##0",
                        rigaArticolo.getNumeroDecimaliQta() == null ? 0 : rigaArticolo.getNumeroDecimaliQta(),
                        Double.class);
                try {
                    StringBuilder sb = new StringBuilder(60);
                    sb.append("G:");
                    sb.append(factory.getDefaultFormatter().valueToString(rigaArticolo.getGiacenza().getGiacenza()));
                    lblGiacenza.setText(sb.toString());
                    if (rigaArticolo.getGiacenza().isSottoScorta()) {
                        lblGiacenza.setForeground(Color.RED);
                    }
                } catch (ParseException e) {
                    logger.error("-->errore durante il format della giacenza.", e);
                }
            }

            // se e' articolo libero deve inserire la descrizione. non passo il
            // focus alla quantita.
            boolean focusable = rigaArticolo.getArticolo() != null && rigaArticolo.getArticolo().getId() != null
                    && rigaArticolo.isArticoloLibero();

            getRigheArticoliComponentiCommand().setVisible(rigaArticolo.getComponenti() != null);

            descrizioneTextField.setFocusable(focusable);
            descrizioneLinguaTextField.setFocusable(focusable);
            umComponent.setFocusable(focusable);

            descrizioneLabel.setIcon(null);
            descrizioneLinguaLabel.setIcon(null);

            String linguaSede = (rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita() != null)
                    ? rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita().getLingua() : null;

            if (linguaSede == null || aziendaCorrente.getLingua().equals(linguaSede)) {
                // nascondo i componenti per la lingua perchè non servono
                descrizioneLinguaTextField.setVisible(false);
                descrizioneLinguaLabel.setVisible(false);
            } else {
                descrizioneLinguaTextField.setVisible(true);
                descrizioneLinguaLabel.setVisible(true);

                // icona
                descrizioneLabel.setIcon(RcpSupport.getIcon(aziendaCorrente.getLingua()));
                descrizioneLinguaLabel.setIcon(RcpSupport.getIcon(linguaSede));

                // tooltip
                Locale locale = new Locale(aziendaCorrente.getLingua());
                descrizioneLabel.setToolTipText(capitalizeWord(locale.getDisplayLanguage()));
                locale = new Locale(linguaSede);
                descrizioneLinguaLabel.setToolTipText(capitalizeWord(locale.getDisplayLanguage()));
            }
        }
    }

    private class OpenDescrizioneCalcoloPrezzoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
        }

        @Override
        public boolean preExecution(ActionCommand command) {

            RigaArticolo rigaArticolo = (RigaArticolo) getFormObject();

            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.ARTICOLO_PARAM, rigaArticolo.getArticolo());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.LISTINO_PARAM,
                    rigaArticolo.getAreaMagazzino().getListino());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.LISTINO_ALTERNATIVO_PARAM,
                    rigaArticolo.getAreaMagazzino().getListinoAlternativo());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.DATA_PARAM,
                    rigaArticolo.getAreaMagazzino().getDocumento().getDataDocumento());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.SEDE_ENTITA,
                    rigaArticolo.getAreaMagazzino().getDocumento().getSedeEntita());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.ENTITA_PARAM,
                    rigaArticolo.getAreaMagazzino().getDocumento().getEntita());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.CODICE_VALUTA_PARAM,
                    rigaArticolo.getAreaMagazzino().getDocumento().getTotale().getCodiceValuta());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.AGENTE_PARAM, rigaArticolo.getAgente());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.CODICE_PAGAMENTO_PARAM,
                    getValueModel("codicePagamento").getValue());
            command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.MEZZO_TRASPORTO_PARAM,
                    rigaArticolo.getAreaMagazzino().getMezzoTrasporto());
            return true;
        }
    }

    /**
     * Focus listener che si preoccupa, nel caso di articolo padre, di mostrare il dialogo per
     * l'impostazione delle quantità degli articoli figli.
     *
     * @author leonardo
     */
    private class QtaFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {

            if (!getFormModel().isReadOnly()) {
                if (getFormModel().getValueModel("formulaTrasformazione").getValue() != null) {
                    Component prevFocus = e.getOppositeComponent();
                    if ("rigaArticoloFormModelModel.articolo.descrizione".equals(prevFocus.getName())
                            || "rigaArticoloFormModelModel.articolo.codice".equals(prevFocus.getName())
                            || "rigaArticoloFormModelModel.descrizione".equals(prevFocus.getName())) {
                        e.getComponent().transferFocus();
                    }
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            getFormModel().getFieldMetadata("politicaPrezzo").setEnabled(false);
        }
    }

    private class RigaBloccataPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // DIRTY,ENABLED,COMMITTABLE e HASERRORS sono le proprietà che
            // cambiano stato; per coprire tutti i casi,
            // tra cui il caso in cui preparo un nuovo documento da uno
            // esistente, devo aggiornare lo stato readOnly
            // di tipoAreaMagazzino in tutti questi casi.
            RigaArticolo rigaArticolo = (RigaArticolo) getFormObject();
            boolean chiusa = rigaArticolo.getQtaChiusa() != 0.00;
            // getFormModel().setReadOnly(chiusa);

            boolean sconto1Bloccato = (Boolean) getFormModel().getValueModel("sconto1Bloccato").getValue();

            getFormModel().getFieldMetadata("articolo").setReadOnly(chiusa);
            getFormModel().getFieldMetadata("prezzoUnitarioReale").setReadOnly(chiusa);

            getFormModel().getFieldMetadata("variazione1").setReadOnly(chiusa || sconto1Bloccato);
            getFormModel().getFieldMetadata("variazione2").setReadOnly(chiusa);
            getFormModel().getFieldMetadata("variazione3").setReadOnly(chiusa);
            getFormModel().getFieldMetadata("variazione4").setReadOnly(chiusa);

            getFormModel().getFieldMetadata("categoriaContabileArticolo").setReadOnly(chiusa);
            getFormModel().getFieldMetadata("noteRiga").setReadOnly(chiusa);

        }

    }

    private class RigheArticoliFigliCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            IRigaArticoloDocumento rigaAggiornata = ((RigheArticoliComponentiCommand) arg0).getRigaArticoloDocumento();
            if (rigaAggiornata != null) {
                getValueModel("componenti").setValue(rigaAggiornata.getComponenti());
                getValueModel("qta").setValue(rigaAggiornata.getQta());
            }
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            IRigaArticoloDocumento rigaArticolo = (IRigaArticoloDocumento) getFormObject();
            if (rigaArticolo.getArticolo() == null) {
                return false;
            }

            ((RigheArticoliComponentiCommand) arg0).setRigaArticoloDocumento(rigaArticolo);
            ((RigheArticoliComponentiCommand) arg0)
                    .setReadOnly(getFormModel().isReadOnly() || rigaArticolo.getArticolo().isDistinta());
            return true;
        }

    }

    public static final String FORM_ID = "rigaArticoloForm";
    private AziendaCorrente aziendaCorrente;

    private AreaMagazzino areaMagazzinoCorrente;

    private final FormComponentInterceptorFactory formComponentInterceptorFactory;
    // private JLabel waitLabel;
    private JFormattedTextField qtaFormattedTextField;
    private JTextComponent descrizioneTextField;
    private JLabel descrizioneLabel;
    private JTextComponent descrizioneLinguaTextField;
    private JLabel descrizioneLinguaLabel;
    private ImportoTextField txtImporto;
    private JComponent[] agenteControl;

    private JTextField umComponent;
    private AggiornaListinoCommand aggiornaListinoCommand;
    private ApriAnalisiArticoloEntitaCommand apriAnalisiArticoloEntitaCommand;
    private ApriStatisticaArticoloCommand apriStatisticaArticolo;
    private OpenDescrizioneCalcoloPrezzoCommand verificaPrezzoCommand;
    private ActionCommandInterceptor aggiornaListinoCommandInterceptor;
    private ActionCommandInterceptor apriAnasiliArticoloCommandInterceptor;
    private ApriStatisticaArticoloCommandInterceptor apriStatisticaArticoloCommandInterceptor;
    private OpenDescrizioneCalcoloPrezzoCommandInterceptor openDescrizioneCalcoloPrezzoCommandInterceptor;
    private RigheArticoliComponentiCommand righeArticoliComponentiCommand = null;
    private RigheArticoliFigliCommandInterceptor righeArticoliFigliCommandInterceptor = null;
    private QtaFocusListener qtaFocusListener = null;

    private PluginManager pluginManager = null;
    private SearchPanel searchPanelArticolo;
    private ArticoloPropertyChangeListener articoloPropertyChangeListener = null;
    private RigaArticoloArticoloPropertyChange rigaArticoloArticoloPropertyChange = null;
    private RigaArticoloNumeroDecimaliQtaPropertyChange rigaArticoloNumeroDecimaliQtaPropertyChange = null;
    private RigaArticoloReadOnlyPropertyChange rigaArticoloReadOnlyPropertyChange = null;
    private RigaBloccataPropertyChangeListener rigaBloccataPropertyChangeListener = null;
    private JLabel lblGiacenza;
    private Boolean isGiacenzaEnable;

    /**
     * Costruttore di default.
     */
    public RigaArticoloForm() {
        super(PanjeaFormModelHelper.createFormModel(new RigaArticolo(), false, FORM_ID + "Model"), FORM_ID);
        formComponentInterceptorFactory = (FormComponentInterceptorFactory) Application.services()
                .getService(FormComponentInterceptorFactory.class);

        pluginManager = (PluginManager) Application.instance().getApplicationContext().getBean("pluginManager");

        // Aggiungo al formModel il value model dinamino per importoRateAperte
        ValueModel codicePagamentoValueModel = new ValueHolder(new CodicePagamento());
        DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(codicePagamentoValueModel), CodicePagamento.class, true, null);
        getFormModel().add("codicePagamento", codicePagamentoValueModel, metaData);

        // aggiungo la finta proprietà tipiEntita per far si che la search text
        // degli agenti mi selezioni solo agenti e
        // non altri tipi entità
        List<TipoEntita> tipiEntitaAgente = new ArrayList<TipoEntita>();
        tipiEntitaAgente.add(TipoEntita.AGENTE);

        ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
        DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
        getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
        IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
        isGiacenzaEnable = magazzinoAnagraficaBD.caricaMagazzinoSettings().getCalcolaGiacenzeInCreazioneRiga();

        PanjeaSwingUtil.addValueModelToForm(null, getFormModel(), Integer.class, "idInstallazione", false);
    }

    protected Binding createArticoloBinding(PanjeaSwingBindingFactory bf) {
        return bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" },
                new String[] { "areaMagazzino.documento.entita", "idInstallazione" },
                new String[] { ArticoloSearchObject.ENTITA_KEY, ArticoloSearchObject.ID_INSTALLAZIONE_KEY });
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "left:pref,4dlu,fill:80dlu,fill:30dlu,40dlu,6dlu,left:pref,4dlu,fill:80dlu,50dlu,default:grow(0.7)",
                "1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.addLabel("articolo", 1, 2);
        searchPanelArticolo = (SearchPanel) builder.addBinding(createArticoloBinding(bf), 3, 2, 7, 1);
        searchPanelArticolo.getTextFields().get("codice").setColumns(10);
        searchPanelArticolo.getTextFields().get("descrizione").setColumns(25);

        builder.nextRow();

        JComponent[] descrizioneComponents = builder.addPropertyAndLabel("descrizione", 1, 4, 2, 1);
        descrizioneLabel = (JLabel) descrizioneComponents[0];
        descrizioneLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        descrizioneTextField = (JTextComponent) descrizioneComponents[1];

        JComponent[] linguaComponents = builder.addPropertyAndLabel("descrizioneLingua", 7, 4, 2, 1);
        descrizioneLinguaLabel = (JLabel) linguaComponents[0];
        descrizioneLinguaLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        descrizioneLinguaTextField = (JTextComponent) linguaComponents[1];
        descrizioneLinguaTextField.setVisible(false);
        descrizioneLinguaLabel.setVisible(false);

        // visualizzo la resa e le battute se ho il plugin del vending. Per ora me ne frego se
        // verranno posizionati al
        // posto della descrizione in lingua dell'articolo. Se si presenterà il problema si vedrà di
        // sistemarlo.
        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            builder.addLabel("resa", 7, 4);
            builder.addProperty("resa", 9, 4).setFocusable(false);
            getFormModel().getFieldMetadata("resa").setReadOnly(true);
            builder.nextRow();
            builder.addLabel("qtaBattute");

            Binding qtaBinding = bf.createBoundFormattedTextField("qta", getFactory());
            qtaFormattedTextField = (JFormattedTextField) qtaBinding.getControl();
            Binding battuteBinding = bf.createBoundFormattedTextField("battute", getFactory());
            battuteBinding.getControl().setFocusable(false);
            JPanel panel = getComponentFactory().createPanel(new GridLayout(1, 2, 2, 0));
            getFormModel().getFieldMetadata("battute").setReadOnly(true);
            panel.add(qtaFormattedTextField);
            panel.add(battuteBinding.getControl());
            builder.addComponent(panel, 3);
        } else {
            builder.nextRow();
            builder.addLabel("qta");
            qtaFormattedTextField = (JFormattedTextField) builder
                    .addBinding(bf.createBoundFormattedTextField("qta", getFactory()), 3);
        }

        qtaFormattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        qtaFormattedTextField.addFocusListener(getQtaFocusListener());

        umComponent = (JTextField) builder.addProperty("unitaMisura", 4);
        umComponent.setColumns(5);

        JPanel panel = getComponentFactory().createPanel(new HorizontalLayout());
        lblGiacenza = getComponentFactory().createLabel("");
        lblGiacenza.setName("giacenzaLabel");
        lblGiacenza.setAlignmentX(SwingConstants.LEFT);
        panel.add(lblGiacenza);
        panel.add(getRigheArticoliComponentiCommand().createButton());
        builder.addComponent(panel, 5, 6, 2, 1);

        builder.addPropertyAndLabel("tipoOmaggio", 7)[1].setFocusable(false);

        builder.nextRow();

        Binding attributiBinding = new AttributiRigaArticoloBinder().bind(getFormModel(), "attributi",
                new HashMap<String, Object>());
        FormComponentInterceptor interceptor = formComponentInterceptorFactory.getInterceptor(getFormModel());
        interceptor.processComponent("attributi", attributiBinding.getControl());
        builder.addComponent(attributiBinding.getControl(), 3, 8, 9, 1);
        builder.nextRow();

        JPanel importoComponent = getComponentFactory().createPanel(new HorizontalLayout(3));

        builder.addLabel("prezzoUnitario", 1, 10);
        ImportoBinding importoUnitarioBinding = (ImportoBinding) bf.createBoundImportoTextField("prezzoUnitarioReale",
                "areaMagazzino.documento.totale", "numeroDecimaliPrezzo");
        txtImporto = (ImportoTextField) builder.addBinding(importoUnitarioBinding, 3, 10);

        new RangePrezzoControl(getFormModel(), txtImporto);

        importoComponent.add(getAggiornaListinoCommand().createButton());
        importoComponent.add(getVerificaPrezzoCommand().createButton());
        importoComponent.add(getApriStatisticaArticoloCommand().createButton());
        importoComponent.add(getApriAnalisiArticoloEntitaCommand().createButton());
        builder.addComponent(importoComponent, 4, 10, 2, 1);

        JPanel variazioniPanel = getComponentFactory().createPanel(new HorizontalLayout(3));
        builder.addLabel("variazione1", 7);
        Binding variazione1Binding = bf.createBoundPercentageText("variazione1");
        Binding variazione2Binding = bf.createBoundPercentageText("variazione2");
        Binding variazione3Binding = bf.createBoundPercentageText("variazione3");
        Binding variazione4Binding = bf.createBoundPercentageText("variazione4");
        variazioniPanel.add(variazione1Binding.getControl());
        variazioniPanel.add(variazione2Binding.getControl());
        variazioniPanel.add(variazione3Binding.getControl());
        variazioniPanel.add(variazione4Binding.getControl());
        builder.addComponent(variazioniPanel, 9, 10, 3, 1);

        builder.nextRow();

        builder.addLabel("prezzoNetto");
        ImportoBinding prezzoNettoBinding = (ImportoBinding) bf.createBoundImportoTextField("prezzoNetto",
                "areaMagazzino.documento.totale", "numeroDecimaliPrezzo");
        ((ImportoTextField) builder.addBinding(prezzoNettoBinding, 3)).getTextField().setFocusable(false);

        builder.addLabel("prezzoTotale", 7);
        ImportoBinding prezzoTotaleBinding = (ImportoBinding) bf.createBoundImportoTextField("prezzoTotale",
                "areaMagazzino.documento.totale");
        ((ImportoTextField) builder.addBinding(prezzoTotaleBinding, 9)).getTextField().setFocusable(false);
        prezzoTotaleBinding.setNrOfDecimals(RigaArticolo.SCALE_IMPORTO_TOTALE);

        builder.nextRow();

        if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
            agenteControl = new JComponent[4];

            agenteControl[0] = builder.addLabel("agente");
            agenteControl[1] = builder.addBinding(
                    bf.createBoundSearchText("agente", new String[] { "codice", "anagrafica.denominazione" },
                            new String[] { "tipiEntitaAgente" },
                            new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class),
                    3, 14, 3, 1);
            ((SearchPanel) agenteControl[1]).getTextFields().get("codice").setColumns(4);
            ((SearchPanel) agenteControl[1]).getTextFields().get("anagrafica.denominazione").setColumns(20);
            // perc. provvigione
            agenteControl[2] = builder.addLabel("percProvvigione", 7);
            builder.setComponentAttributes("l,c");
            agenteControl[3] = builder.addBinding(bf.createBoundPercentageText("percProvvigione"), 9);
            updateAgenteControl();
        }

        // HACK esegue getValue degli attributi per evitare una
        // ConcurrentModificationException
        getFormModel().getValueModel("prezzoUnitarioReale").getValue();
        getFormModel().getValueModel("attributi").getValue();
        getFormModel().getValueModel("formulaTrasformazione").getValue();
        getFormModel().getValueModel("formulaTrasformazioneQtaMagazzino").getValue();
        getFormModel().getValueModel("codiceIva").getValue();
        getFormModel().getValueModel("politicaPrezzo").getValue();
        getFormModel().getValueModel("articoloLibero").getValue();
        getFormModel().getValueModel("prezzoDeterminato").getValue();
        getFormModel().getValueModel("numeroDecimaliQta").getValue();
        getFormModel().getValueModel("qtaChiusa").getValue();
        getFormModel().getFieldMetadata("numeroDecimaliPrezzo").setEnabled(false);
        getFormModel().getFieldMetadata("politicaPrezzo").setEnabled(false);
        installReadOnlyPropertyChange();
        installArticoloPropertyChange();
        installNumerDecimaliQtaPropertyChange();

        JPanel rigaArticoloPanel = builder.getPanel();
        rigaArticoloPanel.setName("rigaArticoloPanel");
        return rigaArticoloPanel;
    }

    @Override
    protected Object createNewObject() {
        RigaArticolo rigaArticolo = new RigaArticolo();
        rigaArticolo.setAreaMagazzino(areaMagazzinoCorrente);

        if (areaMagazzinoCorrente.getTipoAreaMagazzino() != null
                && areaMagazzinoCorrente.getTipoAreaMagazzino().isGestioneAgenti()) {
            if (areaMagazzinoCorrente.getId() != null) {
                AgenteLite agenteSelezionato = ((RigaArticolo) getFormObject()).getAgente();
                if (agenteSelezionato == null
                        || !areaMagazzinoCorrente.equals(((RigaArticolo) getFormObject()).getAreaMagazzino())) {
                    // copio quello dell'entità
                    rigaArticolo.setAgente(areaMagazzinoCorrente.getDocumento().getSedeEntita().getAgente());
                } else {
                    // mantengo questo
                    rigaArticolo.setAgente(agenteSelezionato);
                }
            } else {
                rigaArticolo.setAgente(null);
            }
        }
        searchPanelArticolo.getTextFields().get("codice").requestFocus();
        return rigaArticolo;
    }

    @Override
    public void dispose() {
        if (aggiornaListinoCommand != null) {
            aggiornaListinoCommand.removeCommandInterceptor(getAggiornaListinoCommandInterceptor());
        }
        if (apriAnalisiArticoloEntitaCommand != null) {
            apriAnalisiArticoloEntitaCommand.removeCommandInterceptor(getApriAnasiliArticoloCommandInterceptor());
        }
        if (righeArticoliComponentiCommand != null) {
            righeArticoliComponentiCommand.removeCommandInterceptor(getRigheArticoliFigliCommandInterceptor());
        }
        if (apriStatisticaArticolo != null) {
            apriStatisticaArticolo.removeCommandInterceptor(getApriStatisticaArticoloCommandInterceptor());
        }
        if (verificaPrezzoCommand != null) {
            verificaPrezzoCommand.removeCommandInterceptor(getOpenDescrizioneCalcoloPrezzoCommandInterceptor());
        }
        if (qtaFormattedTextField != null) {
            qtaFormattedTextField.removeFocusListener(getQtaFocusListener());
        }
        getFormModel().getValueModel("articolo").removeValueChangeListener(getRigaArticoloArticoloPropertyChange());
        getFormModel().getValueModel("articolo").removeValueChangeListener(getArticoloPropertyChangeListener());
        getFormModel().getValueModel("numeroDecimaliQta")
                .removeValueChangeListener(getRigaArticoloNumeroDecimaliQtaPropertyChange());
        getFormModel().removePropertyChangeListener(FormModel.DIRTY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
        getFormModel().removePropertyChangeListener(getRigaBloccataPropertyChangeListener());

        verificaPrezzoCommand = null;
        aggiornaListinoCommand = null;
        apriStatisticaArticolo = null;
        super.dispose();
    }

    /**
     * @return command per l'aggiornamento del listino
     */
    private AggiornaListinoCommand getAggiornaListinoCommand() {
        if (aggiornaListinoCommand == null) {
            aggiornaListinoCommand = new AggiornaListinoCommand();
            aggiornaListinoCommand.addCommandInterceptor(getAggiornaListinoCommandInterceptor());
        }
        return aggiornaListinoCommand;
    }

    /**
     * @return AggiornaListinoCommandInterceptor
     */
    private ActionCommandInterceptor getAggiornaListinoCommandInterceptor() {
        if (aggiornaListinoCommandInterceptor == null) {
            aggiornaListinoCommandInterceptor = new AggiornaListinoCommandInterceptor();
        }
        return aggiornaListinoCommandInterceptor;
    }

    /**
     * @return the apriAnalisiArticoloEntitaCommand
     */
    private ApriAnalisiArticoloEntitaCommand getApriAnalisiArticoloEntitaCommand() {
        if (apriAnalisiArticoloEntitaCommand == null) {
            apriAnalisiArticoloEntitaCommand = new ApriAnalisiArticoloEntitaCommand();
            apriAnalisiArticoloEntitaCommand.addCommandInterceptor(getApriAnasiliArticoloCommandInterceptor());
        }

        return apriAnalisiArticoloEntitaCommand;
    }

    /**
     * @return AggiornaListinoCommandInterceptor
     */
    private ActionCommandInterceptor getApriAnasiliArticoloCommandInterceptor() {
        if (apriAnasiliArticoloCommandInterceptor == null) {
            apriAnasiliArticoloCommandInterceptor = new ApriAnalisiArticoloCommandInterceptor();
        }
        return apriAnasiliArticoloCommandInterceptor;
    }

    /**
     * @return comando per aprire le statistiche relative al deposito di origine
     */
    private ApriStatisticaArticoloCommand getApriStatisticaArticoloCommand() {
        if (apriStatisticaArticolo == null) {
            apriStatisticaArticolo = new ApriStatisticaArticoloCommand();
            // sul form visualizzo solo l'icona del command
            apriStatisticaArticolo
                    .setFaceDescriptor(new CommandFaceDescriptor(null, apriStatisticaArticolo.getIcon(), null));
            apriStatisticaArticolo.addCommandInterceptor(getApriStatisticaArticoloCommandInterceptor());
        }
        return apriStatisticaArticolo;
    }

    /**
     * @return interceptor per ApriStatisticaArticoloCommand
     */
    private ActionCommandInterceptor getApriStatisticaArticoloCommandInterceptor() {
        if (apriStatisticaArticoloCommandInterceptor == null) {
            apriStatisticaArticoloCommandInterceptor = new ApriStatisticaArticoloCommandInterceptor();
        }
        return apriStatisticaArticoloCommandInterceptor;
    }

    /**
     * @return ArticoloPropertyChangeListener
     */
    private ArticoloPropertyChangeListener getArticoloPropertyChangeListener() {
        if (articoloPropertyChangeListener == null) {
            articoloPropertyChangeListener = new ArticoloPropertyChangeListener();
        }
        return articoloPropertyChangeListener;
    }

    /**
     * @return factory per la formattazione qtaDecimanli
     */
    private DefaultFormatterFactory getFactory() {
        Integer numeroDecimaliQt = (Integer) getFormModel().getValueModel("numeroDecimaliQta").getValue();
        DefaultNumberFormatterFactory f = new DefaultNumberFormatterFactory("#,##0", numeroDecimaliQt, Double.class);
        return f;
    }

    /**
     * @return interceptor per OpenDescrizioneCalcoloPrezzoCommand
     */
    private ActionCommandInterceptor getOpenDescrizioneCalcoloPrezzoCommandInterceptor() {
        if (openDescrizioneCalcoloPrezzoCommandInterceptor == null) {
            openDescrizioneCalcoloPrezzoCommandInterceptor = new OpenDescrizioneCalcoloPrezzoCommandInterceptor();
        }
        return openDescrizioneCalcoloPrezzoCommandInterceptor;
    }

    /**
     * @return QtaFocusListener
     */
    private FocusListener getQtaFocusListener() {
        if (qtaFocusListener == null) {
            qtaFocusListener = new QtaFocusListener();
        }
        return qtaFocusListener;
    }

    /**
     * @return RigaArticoloArticoloPropertyChange
     */
    private RigaArticoloArticoloPropertyChange getRigaArticoloArticoloPropertyChange() {
        if (rigaArticoloArticoloPropertyChange == null) {
            rigaArticoloArticoloPropertyChange = new RigaArticoloArticoloPropertyChange(getFormModel());
        }
        return rigaArticoloArticoloPropertyChange;
    }

    /**
     * @return RigaArticoloNumeroDecimaliQtaPropertyChange
     */
    private RigaArticoloNumeroDecimaliQtaPropertyChange getRigaArticoloNumeroDecimaliQtaPropertyChange() {
        if (rigaArticoloNumeroDecimaliQtaPropertyChange == null) {
            rigaArticoloNumeroDecimaliQtaPropertyChange = new RigaArticoloNumeroDecimaliQtaPropertyChange(
                    getFormModel(), qtaFormattedTextField);
        }
        return rigaArticoloNumeroDecimaliQtaPropertyChange;
    }

    /**
     * @return rigaArticoloReadOnlyPropertyChange
     */
    private RigaArticoloReadOnlyPropertyChange getRigaArticoloReadOnlyPropertyChange() {
        if (rigaArticoloReadOnlyPropertyChange == null) {
            rigaArticoloReadOnlyPropertyChange = new RigaArticoloReadOnlyPropertyChange(getFormModel());
        }
        return rigaArticoloReadOnlyPropertyChange;
    }

    /**
     * @return rigaBloccataPropertyChangeListener
     */
    private RigaBloccataPropertyChangeListener getRigaBloccataPropertyChangeListener() {
        if (rigaBloccataPropertyChangeListener == null) {
            rigaBloccataPropertyChangeListener = new RigaBloccataPropertyChangeListener();
        }
        return rigaBloccataPropertyChangeListener;
    }

    /**
     * @return RigheArticoliComponentiCommand
     */
    private RigheArticoliComponentiCommand getRigheArticoliComponentiCommand() {
        if (righeArticoliComponentiCommand == null) {
            righeArticoliComponentiCommand = new RigheArticoliComponentiCommand();
            righeArticoliComponentiCommand.addCommandInterceptor(getRigheArticoliFigliCommandInterceptor());
        }
        return righeArticoliComponentiCommand;
    }

    /**
     * @return RigheArticoliFigliCommandInterceptor
     */
    private RigheArticoliFigliCommandInterceptor getRigheArticoliFigliCommandInterceptor() {
        if (righeArticoliFigliCommandInterceptor == null) {
            righeArticoliFigliCommandInterceptor = new RigheArticoliFigliCommandInterceptor();
        }
        return righeArticoliFigliCommandInterceptor;
    }

    /**
     * @return command per aprire la verifica prezzo con i parametri già impostati.
     */
    private OpenDescrizioneCalcoloPrezzoCommand getVerificaPrezzoCommand() {
        if (verificaPrezzoCommand == null) {
            verificaPrezzoCommand = new OpenDescrizioneCalcoloPrezzoCommand();
            verificaPrezzoCommand.addCommandInterceptor(getOpenDescrizioneCalcoloPrezzoCommandInterceptor());
        }
        return verificaPrezzoCommand;

    }

    @Override
    public void grabFocus() {
        searchPanelArticolo.getTextFields().get("codice").requestFocus();
    }

    /**
     * Al cambio articolo porto il focus sulla qta, quindi non posso usare il defaultController per
     * i property change perchè avrei solamente il formmodel.
     */
    private void installArticoloPropertyChange() {
        getFormModel().getValueModel("articolo").addValueChangeListener(getRigaArticoloArticoloPropertyChange());
        getFormModel().getValueModel("articolo").addValueChangeListener(getArticoloPropertyChangeListener());
    }

    /**
     * Installa il listener sulla proprietà "rigaArticolo.numeroDecimaliQta".
     */
    private void installNumerDecimaliQtaPropertyChange() {
        getFormModel().getValueModel("numeroDecimaliQta")
                .addValueChangeListener(getRigaArticoloNumeroDecimaliQtaPropertyChange());
    }

    /**
     * Installa un listener sulla proprietà dirty del formmodel per disabilitare alcune proprietà
     * della riga articolo.
     */
    private void installReadOnlyPropertyChange() {
        getFormModel().addPropertyChangeListener(FormModel.DIRTY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
        getFormModel().addPropertyChangeListener(getRigaBloccataPropertyChangeListener());
    }

    /**
     * @param areaMagazzinoCorrente
     *            the areaMagazzinoCorrente to set
     */
    public void setAreaMagazzinoCorrente(AreaMagazzino areaMagazzinoCorrente) {
        boolean changeArea = this.areaMagazzinoCorrente == null
                || (this.areaMagazzinoCorrente != null && !this.areaMagazzinoCorrente.equals(areaMagazzinoCorrente));
        this.areaMagazzinoCorrente = areaMagazzinoCorrente;

        getValueModel("areaMagazzino").setValue(areaMagazzinoCorrente);
        if (areaMagazzinoCorrente.isNew() || changeArea) {
            // ripulisco l'agente per far si che venga caricato quello della
            // sede
            getValueModel("agente").setValue(null);
            RigaArticolo ra = (RigaArticolo) createNewObject();
            // risetto l'agente solo per attivare il property change
            getValueModel("agente").setValue(ra.getAgente());
        }
        if (getFormModel().isCommittable()) {
            commit();
        }
        updateAgenteControl();
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param codicePagamentoCorrente
     *            the codicePagamentoCorrente to set
     */
    public void setCodicePagamentoCorrente(CodicePagamento codicePagamentoCorrente) {
        getValueModel("codicePagamento").setValue(codicePagamentoCorrente);
        if (getFormModel().isCommittable()) {
            commit();
        }
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
    }

    /**
     * Visualizza l'indicatore della politica prezzo.
     *
     * @param show
     *            <code>true</code> per visualizzarlo, <code>false</code> altrimenti.
     */
    public void showPoliticaPrezzoIndicator(boolean show) {
        // if (waitLabel == null) {
        // waitLabel = new JLabel(getIconSource().getIcon("waitsmall"));
        // waitLabel = new JLabel("*");
        // JECOverlayHelper.attachOverlay(waitLabel, txtImporto, 10, 10);
        // txtImporto.add(waitLabel);
        // }
        // waitLabel.setVisible(show);
    }

    /**
     * Aggiorna i controlli per l'agente se il tipoAreaMagazzino ha abilitato la gestione agenti.
     */
    private void updateAgenteControl() {
        boolean gestioneAgenti = areaMagazzinoCorrente != null && areaMagazzinoCorrente.getTipoAreaMagazzino() != null
                && areaMagazzinoCorrente.getTipoAreaMagazzino().isGestioneAgenti();
        if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
            for (JComponent componente : agenteControl) {
                componente.setVisible(gestioneAgenti);
            }
        }
    }
}