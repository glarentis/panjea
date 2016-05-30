package it.eurotn.panjea.ordini.rich.forms.righeordine;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractButton;
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
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormComponentInterceptor;
import org.springframework.richclient.form.builder.FormComponentInterceptorFactory;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.commands.analisi.ApriAnalisiArticoloEntitaCommand;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.ApriStatisticaArticoloCommand;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.AttributiRigaArticoloBinder;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.OpenDescrizioneCalcoloPrezzoCommand;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaArticoloNumeroDecimaliQtaPropertyChange;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.forms.righeordine.componenti.RigheArticoliComponentiCommand;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RigaArticoloForm extends PanjeaAbstractForm {

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
            EntitaLite entita = rigaArticolo.getAreaOrdine().getDocumento().getEntita();
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
                    rigaArticolo.getAreaOrdine().getDepositoOrigine());
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

            ArticoloLite articoloLite = (ArticoloLite) evt.getNewValue();
            aggiornaGiacenza(articoloLite);

            // se e' articolo libero deve inserire la descrizione. non passo il
            // focus alla quantita.
            if (rigaArticolo.getArticolo() == null) {
                return;
            }

            // se e' articolo libero deve inserire la descrizione. non passo il
            // focus alla quantita.
            boolean focusable = rigaArticolo.getArticolo() != null && rigaArticolo.getArticolo().getId() != null
                    && rigaArticolo.isArticoloLibero();

            getRigheArticoliComponentiCommand().setVisible(rigaArticolo.getComponenti() != null);
            boolean isRigaOrdineClienteDistinta = isRigaOrdineClienteDistinta();

            TipoAreaOrdine tipoAreaOrdine = areaOrdineCorrente.getTipoAreaOrdine();
            boolean isOrdineProduzione = tipoAreaOrdine != null ? tipoAreaOrdine.isOrdineProduzione() : false;
            getConfigurazioneDistintaCommand().setVisible(isRigaOrdineClienteDistinta || isOrdineProduzione);
            showDataProduzione(rigaArticolo.getAreaOrdine());

            descrizioneTextField.setFocusable(focusable);
            descrizioneLinguaTextField.setFocusable(focusable);
            umComponent.setFocusable(focusable);

            descrizioneLabel.setIcon(null);
            descrizioneLinguaLabel.setIcon(null);
            descrizioneLabel.setIcon(null);
            descrizioneLinguaLabel.setIcon(null);

            String linguaSede = null;
            if (rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita() != null) {
                linguaSede = rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getLingua();
            }

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

    public class GestioneConfigurazioneArticoloCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand paramActionCommand) {
            // post execution nella RigaArticoloPage
        }

        @Override
        public boolean preExecution(ActionCommand paramActionCommand) {
            RigaArticolo rigaArticolo = (RigaArticolo) RigaArticoloForm.this.getFormObject();
            if (rigaArticolo.getId() == null) {
                return false;
            }
            paramActionCommand.addParameter(GestioneConfigurazioneArticoloCommand.PARAM_ID_RIGA_ORDINE,
                    getFormObject());
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
                    if ("rigaArticoloOrdineFormModelModel.articolo.descrizione".equals(prevFocus.getName())
                            || "rigaArticoloOrdineFormModelModel.articolo.codice".equals(prevFocus.getName())
                            || "rigaArticoloOrdineFormModelModel.descrizione".equals(prevFocus.getName())) {
                        // e.getComponent().transferFocus();
                        Component componentFocusable = PanjeaSwingUtil
                                .findComponentFocusable(new Component[] { attributiComponent });
                        if (componentFocusable != null) {
                            componentFocusable.requestFocusInWindow();
                        }
                    }
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            getFormModel().getFieldMetadata("politicaPrezzo").setEnabled(false);
        }
    }

    private class ReadOnlyPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            boolean visible = !(Boolean) evt.getNewValue();
            giacenzaLabel.setVisible(visible);
            giacenzaLabel.setText("Disp. nd");
            if (visible) {
                ArticoloLite articoloLite = ((RigaArticolo) getFormObject()).getArticolo();
                aggiornaGiacenza(articoloLite);
            }
        }
    }

    private class RigheArticoliComponentiCommandInterceptor implements ActionCommandInterceptor {

        @SuppressWarnings("unchecked")
        @Override
        public void postExecution(ActionCommand arg0) {
            IRigaArticoloDocumento rigaAggiornata = getRigheArticoliComponentiCommand().getRigaArticoloDocumento();
            if (rigaAggiornata != null) {
                // ne caso di distinte i componenti vengono salvati nel dialog
                // quindi qui devo solo aggiornare la
                // collection senza sporcare il formmodel
                ((Set<IRigaArticoloDocumento>) getValueModel("componenti").getValue()).clear();
                ((Set<IRigaArticoloDocumento>) getValueModel("componenti").getValue())
                        .addAll(rigaAggiornata.getComponenti());

                // quando si implementeranno i padre/figlio considerare il
                // cambio della quantità e componenti
                // getValueModel("componenti").setValue(rigaAggiornata.getComponenti());
                // getValueModel("qta").setValue(rigaAggiornata.getQta());
            }
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            IRigaArticoloDocumento rigaArticolo = (IRigaArticoloDocumento) getFormObject();
            ((RigheArticoliComponentiCommand) arg0).setRigaArticoloDocumento(rigaArticolo);
            ((RigheArticoliComponentiCommand) arg0)
                    .setReadOnly(getFormModel().isReadOnly() || rigaArticolo.getArticolo().isDistinta());
            return true;
        }

    }

    public static final String FORM_ID = "rigaArticoloOrdineForm";

    private AziendaCorrente aziendaCorrente = null;
    private AreaOrdine areaOrdineCorrente = null;
    private boolean areaOrdineChanged = false;

    private JFormattedTextField qtaFormattedTextField = null;

    private JTextComponent descrizioneTextField = null;
    private JLabel descrizioneLabel = null;
    private JTextComponent descrizioneLinguaTextField = null;
    private JLabel descrizioneLinguaLabel = null;
    private final FormComponentInterceptorFactory formComponentInterceptorFactory;
    private ApriStatisticaArticoloCommandInterceptor apriStatisticaArticoloCommandInterceptor;
    private ActionCommandInterceptor apriAnasiliArticoloCommandInterceptor;

    private ApriAnalisiArticoloEntitaCommand apriAnalisiArticoloEntitaCommand;

    private OpenDescrizioneCalcoloPrezzoCommand verificaPrezzoCommand = null;

    private RigheArticoliComponentiCommand righeArticoliComponentiCommand = null;
    private RigheArticoliComponentiCommandInterceptor righeArticoliComponentiCommandInterceptor = null;

    private RigaArticoloOrdineArticoloPropertyChange rigaArticoloOrdineArticoloPropertyChange = null;
    private RigaArticoloNumeroDecimaliQtaPropertyChange rigaArticoloNumeroDecimaliQtaPropertyChange = null;
    private ArticoloPropertyChangeListener articoloPropertyChangeListener = null;
    private RigaArticoloNumeroDecimaliQtaPropertyChange rigaArticoloNumeroDecimaliQtaRigaPropertyChange = null;
    private RigaArticoloReadOnlyPropertyChange rigaArticoloReadOnlyPropertyChange;
    private ReadOnlyPropertyChangeListener readOnlyPropertyChangeListener = null;
    private QtaFocusListener qtaFocusListener = null;

    private JTextField umComponent;
    private PluginManager pluginManager = null;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private JLabel giacenzaLabel = new JLabel("");

    private ApriStatisticaArticoloCommand apriStatisticaArticoloCommand;
    private JComponent dataProduzioneComponent;
    private JComponent dataFineProduzioneComponent;
    private JLabel dataConsegnaLabel;
    private GestioneConfigurazioneArticoloCommand gestioneConfigurazioneArticoloCommand;
    private GestioneConfigurazioneArticoloCommandInterceptor gestioneConfigurazioneArticoloCommandInterceptor;

    private boolean isGiacenzaEnable;

    private JTextField tolleranzaComponent;

    private AbstractButton buttonConfigurazioneDistinta;

    private JComponent attributiComponent;

    /**
     * Costruttore di default.
     */
    public RigaArticoloForm() {
        super(PanjeaFormModelHelper.createFormModel(new it.eurotn.panjea.ordini.domain.RigaArticolo(), false,
                FORM_ID + "Model"), FORM_ID);

        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
        IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
        isGiacenzaEnable = magazzinoAnagraficaBD.caricaMagazzinoSettings().getCalcolaGiacenzeInCreazioneRiga();

        formComponentInterceptorFactory = (FormComponentInterceptorFactory) Application.services()
                .getService(FormComponentInterceptorFactory.class);

        pluginManager = (PluginManager) Application.instance().getApplicationContext().getBean("pluginManager");

        // Aggiungo al formModel il value model dinamino per codicePagamento
        ValueModel codicePagamentoValueModel = new ValueHolder(new CodicePagamento());
        DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(codicePagamentoValueModel), CodicePagamento.class, true, null);
        getFormModel().add("codicePagamento", codicePagamentoValueModel, metaData);
    }

    /**
     * Aggiorna la label della giacenza dell'articolo.
     *
     * @param articoloLite
     *            articolo
     */
    private void aggiornaGiacenza(final ArticoloLite articoloLite) {
        if (getFormModel().isReadOnly()) {
            return;
        }
        giacenzaLabel.setVisible(articoloLite != null && articoloLite.getId() != null);
        giacenzaLabel.setOpaque(true);
        giacenzaLabel.setForeground(descrizioneLabel.getForeground());

        final Integer numeroDecimaliQta = ((RigaArticolo) getFormObject()).getNumeroDecimaliQta();
        StringBuilder sb = new StringBuilder("nd.");
        Giacenza giacenza = ((RigaArticolo) getFormObject()).getGiacenza();
        if (giacenza != null) {
            DefaultNumberFormatterFactory factory = new DefaultNumberFormatterFactory("#,##0",
                    numeroDecimaliQta == null ? 0 : numeroDecimaliQta, Double.class);
            sb.append(" (");
            try {
                sb.append(factory.getDefaultFormatter().valueToString(giacenza.getGiacenza()));
            } catch (ParseException e) {
                logger.warn("Parse error in giacenza parse ugli ordini");
            }
            sb.append(")");
        }
        giacenzaLabel.setText(sb.toString());

        AsyncWorker.post(new AsyncTask() {

            @Override
            public void failure(Throwable arg0) {
                giacenzaLabel.setText("Err.");
            }

            @Override
            public Object run() throws Exception {

                Double disponibilita = 0.0;
                if (articoloLite != null) {
                    RigaArticolo ra = (RigaArticolo) getFormObject();
                    Date dataConsegna = ra.getDataConsegna();
                    if (dataConsegna == null) {
                        dataConsegna = areaOrdineCorrente.getDataRegistrazione();
                    }
                    disponibilita = magazzinoDocumentoBD.calcolaDisponibilita(articoloLite, dataConsegna,
                            areaOrdineCorrente.getDepositoOrigine());
                }
                return disponibilita;
            }

            @Override
            public void success(Object arg0) {
                DefaultNumberFormatterFactory factory = new DefaultNumberFormatterFactory("#,##0",
                        numeroDecimaliQta == null ? 0 : numeroDecimaliQta, Double.class);
                try {
                    Giacenza giacenza = ((RigaArticolo) getFormObject()).getGiacenza();
                    StringBuilder sb = new StringBuilder(60);
                    if (isGiacenzaEnable) {
                        sb.append("G:");
                        sb.append(factory.getDefaultFormatter().valueToString(giacenza.getGiacenza()));
                        sb.append(" ");
                        if (giacenza.isSottoScorta()) {
                            giacenzaLabel.setForeground(Color.RED);
                        }
                    }
                    sb.append("D:");
                    sb.append(factory.getDefaultFormatter().valueToString(arg0));
                    giacenzaLabel.setText(sb.toString());

                } catch (ParseException e) {
                    logger.error("-->errore durante il format della giacenza.", e);
                }
            }
        });
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "left:pref,4dlu,fill:80dlu,left:35dlu,left:pref,left:" + getWidth4Colonna()
                        + ",left:pref,4dlu,fill:45dlu,left:pref,fill:70dlu,50dlu,left:pref,60dlu,left:pref",
                "1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// ,
                                                                              // new
                                                                              // FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");

        builder.addPropertyAndLabel("bloccata", 1, 2);
        builder.addPropertyAndLabel("evasioneForzata", 7);
        builder.nextRow();

        builder.addLabel("articolo", 1, 4);
        SearchPanel searchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText("articolo",
                new String[] { "codice", "descrizione" }, new String[] { "areaOrdine.documento.entita" },
                new String[] { ArticoloSearchObject.ENTITA_KEY }), 3, 4, 10, 1);
        searchPanel.getTextFields().get("codice").setColumns(10);
        searchPanel.getTextFields().get("descrizione").setColumns(35);

        builder.nextRow();

        JComponent[] descrizioneComponents = builder.addPropertyAndLabel("descrizione", 1, 6, 2, 1);
        descrizioneLabel = (JLabel) descrizioneComponents[0];
        descrizioneLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        descrizioneTextField = (JTextComponent) descrizioneComponents[1];

        JComponent[] linguaComponents = builder.addPropertyAndLabel("descrizioneLingua", 7, 6, 5, 1);
        descrizioneLinguaLabel = (JLabel) linguaComponents[0];
        descrizioneLinguaLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        descrizioneLinguaTextField = (JTextComponent) linguaComponents[1];
        descrizioneLinguaTextField.setVisible(false);
        descrizioneLinguaLabel.setVisible(false);

        builder.nextRow();

        builder.addLabel("qta", 1, 8);
        qtaFormattedTextField = (JFormattedTextField) builder
                .addBinding(bf.createBoundFormattedTextField("qta", getFactory()), 3, 8);
        qtaFormattedTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        qtaFormattedTextField.addFocusListener(getQtaFocusListener());

        umComponent = (JTextField) builder.addProperty("unitaMisura", 4);
        umComponent.setColumns(5);

        giacenzaLabel.setName("giacenzaLabel");
        JPanel panel = getComponentFactory().createPanel(new HorizontalLayout());
        if (pluginManager.isPresente("panjeaMrp")) {
            tolleranzaComponent = (JFormattedTextField) builder
                    .addBinding(bf.createBoundFormattedTextField("qtaMagazzinoTolleranza", getFactory()), 5, 8);
            tolleranzaComponent.setColumns(6);
            // panel.add(tolleranzaComponent);
        }
        AbstractButton buttonRigheComponenti = getRigheArticoliComponentiCommand().createButton();
        buttonRigheComponenti.setFocusable(false);
        panel.add(buttonRigheComponenti);

        buttonConfigurazioneDistinta = getConfigurazioneDistintaCommand().createButton();
        buttonConfigurazioneDistinta.setFocusable(false);
        panel.add(buttonConfigurazioneDistinta);

        AbstractButton buttonAnalisiArticoli = getApriAnalisiArticoloEntitaCommand().createButton();
        buttonAnalisiArticoli.setFocusable(false);
        panel.add(buttonAnalisiArticoli);
        panel.add(giacenzaLabel);
        builder.addComponent(panel, 6);

        builder.addPropertyAndLabel("tipoOmaggio", 7, 8);// [1].setFocusable(false);
        dataConsegnaLabel = builder.addLabel("dataConsegna", 11, 8);
        JComponent dataConsegnaControl = bf.createBinding("dataConsegna").getControl();
        // dataConsegnaControl.setFocusable(false);
        builder.addComponent(dataConsegnaControl, 12, 8, 2, 1, "l,c");
        dataProduzioneComponent = bf.createBinding("dataProduzione").getControl();
        builder.addComponent(dataProduzioneComponent, 15, 8, 1, 1, "l,c");
        dataFineProduzioneComponent = bf.createBinding("dataFineProduzione").getControl();
        builder.addComponent(dataFineProduzioneComponent, 14, 8, 1, 1, "l,c");

        Binding attributiBinding = new AttributiRigaArticoloBinder().bind(getFormModel(), "attributi",
                new HashMap<String, Object>());
        FormComponentInterceptor interceptor = formComponentInterceptorFactory.getInterceptor(getFormModel());
        interceptor.processComponent("attributi", attributiBinding.getControl());
        attributiComponent = attributiBinding.getControl();
        builder.addComponent(attributiComponent, 3, 10, 12, 1);

        builder.nextRow();
        builder.addLabel("prezzoUnitario", 1, 12);
        JComponent txtImporto = builder.addBinding(bf.createBoundImportoTextField("prezzoUnitarioReale",
                "areaOrdine.documento.totale", "numeroDecimaliPrezzo"), 3, 12);
        new RangePrezzoOrdineControl(getFormModel(), txtImporto);

        JPanel importoComponent = getComponentFactory().createPanel(new HorizontalLayout(3));
        importoComponent.add(getVerificaPrezzoCommand().createButton());
        importoComponent.add(getApriStatisticaArticoloCommand().createButton());
        builder.addComponent(importoComponent, 4);

        JPanel variazioniPanel = getComponentFactory().createPanel(new HorizontalLayout(3));
        builder.addLabel("variazione1", 7, 12);
        Binding variazione1Binding = bf.createBoundPercentageText("variazione1");
        Binding variazione2Binding = bf.createBoundPercentageText("variazione2");
        Binding variazione3Binding = bf.createBoundPercentageText("variazione3");
        Binding variazione4Binding = bf.createBoundPercentageText("variazione4");
        variazioniPanel.add(variazione1Binding.getControl());
        variazioniPanel.add(variazione2Binding.getControl());
        variazioniPanel.add(variazione3Binding.getControl());
        variazioniPanel.add(variazione4Binding.getControl());
        builder.addComponent(variazioniPanel, 9, 12, 6, 1);

        builder.nextRow();
        builder.addLabel("prezzoNetto", 1, 14);
        builder.addBinding(
                bf.createBoundImportoTextField("prezzoNetto", "areaOrdine.documento.totale", "numeroDecimaliPrezzo"), 3,
                14);
        builder.addLabel("prezzoTotale", 7, 14);
        builder.addBinding(bf.createBoundImportoTextField("prezzoTotale", "areaOrdine.documento.totale"), 9, 14, 3, 1);

        builder.nextRow();
        if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
            builder.addLabel("percProvvigione", 1, 16);
            builder.addBinding(bf.createBoundPercentageText("percProvvigione"), 3, 16);
        }

        // HACK esegue getValue dei due attributi per evitare una
        // ConcurrentModificationException all'interno di
        // AttributiRigaArticoloPropertyChange
        getFormModel().getValueModel("prezzoUnitarioReale").getValue();
        getFormModel().getValueModel("attributi").getValue();
        getFormModel().getValueModel("formulaTrasformazione").getValue();
        getFormModel().getValueModel("formulaTrasformazioneQtaMagazzino").getValue();
        getFormModel().getValueModel("codiceIva").getValue();
        getFormModel().getValueModel("politicaPrezzo").getValue();
        getFormModel().getValueModel("articoloLibero").getValue();
        getFormModel().getValueModel("prezzoDeterminato").getValue();
        getFormModel().getValueModel("numeroDecimaliQta").getValue();
        getFormModel().getFieldMetadata("numeroDecimaliPrezzo").setEnabled(false);
        getFormModel().getFieldMetadata("politicaPrezzo").setEnabled(false);
        getFormModel().getValueModel("tipoOmaggio").getValue();

        installReadOnlyPropertyChange();
        installArticoloPropertyChange();
        installNumerDecimaliQtaPropertyChange();

        addFormValueChangeListener("dataConsegna", getDataConsegnaPropertyChange());
        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        RigaArticolo rigaArticoloNew = new RigaArticolo();
        Date dataConsegna = ((RigaArticolo) getFormObject()).getDataConsegna();
        if (areaOrdineCorrente != null && (dataConsegna == null || areaOrdineChanged)) {
            // copio quello dell'area
            rigaArticoloNew.setDataConsegna(areaOrdineCorrente.getDataConsegna());
        } else {
            // mantengo questo
            rigaArticoloNew.setDataConsegna(dataConsegna);
        }
        rigaArticoloNew.setAreaOrdine(areaOrdineCorrente);

        return rigaArticoloNew;
    }

    @Override
    public void dispose() {
        if (righeArticoliComponentiCommand != null) {
            righeArticoliComponentiCommand.removeCommandInterceptor(righeArticoliComponentiCommandInterceptor);
        }
        if (apriAnalisiArticoloEntitaCommand != null) {
            apriAnalisiArticoloEntitaCommand.removeCommandInterceptor(getApriAnasiliArticoloCommandInterceptor());
        }
        if (qtaFormattedTextField != null) {
            qtaFormattedTextField.removeFocusListener(getQtaFocusListener());
        }

        if (apriStatisticaArticoloCommand != null) {
            apriStatisticaArticoloCommand.removeCommandInterceptor(getApriStatisticaArticoloCommandInterceptor());
        }
        if (gestioneConfigurazioneArticoloCommand != null) {
            gestioneConfigurazioneArticoloCommand
                    .removeCommandInterceptor(getGestioneConfigurazioneArticoloCommandInterceptor());
        }
        getFormModel().getValueModel("articolo")
                .removeValueChangeListener(getRigaArticoloOrdineArticoloPropertyChange());
        getFormModel().getValueModel("articolo").removeValueChangeListener(getArticoloPropertyChangeListener());
        getFormModel().getValueModel("numeroDecimaliQta")
                .removeValueChangeListener(getRigaArticoloNumeroDecimaliQtaPropertyChange());
        getFormModel().getFormObjectHolder()
                .removeValueChangeListener(getRigaArticoloNumeroDecimaliQtaRigaPropertyChange());
        getFormModel().removePropertyChangeListener(FormModel.DIRTY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
        getFormModel().removePropertyChangeListener(FormModel.DIRTY_PROPERTY, getReadOnlyPropertyChangeListener());

        readOnlyPropertyChangeListener = null;

        verificaPrezzoCommand = null;
        righeArticoliComponentiCommand = null;
        apriStatisticaArticoloCommand = null;
        super.dispose();
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
        if (apriStatisticaArticoloCommand == null) {
            apriStatisticaArticoloCommand = new ApriStatisticaArticoloCommand();
            // sul form visualizzo solo l'icona del command
            apriStatisticaArticoloCommand
                    .setFaceDescriptor(new CommandFaceDescriptor(null, apriStatisticaArticoloCommand.getIcon(), null));
            apriStatisticaArticoloCommand.addCommandInterceptor(getApriStatisticaArticoloCommandInterceptor());
        }
        return apriStatisticaArticoloCommand;
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
    private PropertyChangeListener getArticoloPropertyChangeListener() {
        if (articoloPropertyChangeListener == null) {
            articoloPropertyChangeListener = new ArticoloPropertyChangeListener();
        }
        return articoloPropertyChangeListener;
    }

    /**
     * @return Configurazione distinta command
     */
    public GestioneConfigurazioneArticoloCommand getConfigurazioneDistintaCommand() {
        if (gestioneConfigurazioneArticoloCommand == null) {
            gestioneConfigurazioneArticoloCommand = new GestioneConfigurazioneArticoloCommand();
            gestioneConfigurazioneArticoloCommand
                    .addCommandInterceptor(getGestioneConfigurazioneArticoloCommandInterceptor());
        }
        return gestioneConfigurazioneArticoloCommand;
    }

    /**
     * @return listener per il cambio dataConsegna
     */
    private PropertyChangeListener getDataConsegnaPropertyChange() {
        return new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                aggiornaGiacenza(((RigaArticolo) getFormObject()).getArticolo());
            }
        };
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
     * @return GestioneConfigurazioneArticoloCommandInterceptor
     */
    private GestioneConfigurazioneArticoloCommandInterceptor getGestioneConfigurazioneArticoloCommandInterceptor() {
        if (gestioneConfigurazioneArticoloCommandInterceptor == null) {
            gestioneConfigurazioneArticoloCommandInterceptor = new GestioneConfigurazioneArticoloCommandInterceptor();
        }
        return gestioneConfigurazioneArticoloCommandInterceptor;
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
     * @return readOnlyPropertyChangeListener
     */
    private ReadOnlyPropertyChangeListener getReadOnlyPropertyChangeListener() {
        if (readOnlyPropertyChangeListener == null) {
            readOnlyPropertyChangeListener = new ReadOnlyPropertyChangeListener();
        }

        return readOnlyPropertyChangeListener;
    }

    /**
     * @return RigaArticoloNumeroDecimaliQtaPropertyChange
     */
    private PropertyChangeListener getRigaArticoloNumeroDecimaliQtaPropertyChange() {
        if (rigaArticoloNumeroDecimaliQtaPropertyChange == null) {
            rigaArticoloNumeroDecimaliQtaPropertyChange = new RigaArticoloNumeroDecimaliQtaPropertyChange(
                    getFormModel(), qtaFormattedTextField);
        }
        return rigaArticoloNumeroDecimaliQtaPropertyChange;
    }

    /**
     * @return RigaArticoloNumeroDecimaliQtaRigaPropertyChange
     */
    private PropertyChangeListener getRigaArticoloNumeroDecimaliQtaRigaPropertyChange() {
        if (rigaArticoloNumeroDecimaliQtaRigaPropertyChange == null) {
            rigaArticoloNumeroDecimaliQtaRigaPropertyChange = new RigaArticoloNumeroDecimaliQtaPropertyChange(
                    getFormModel(), qtaFormattedTextField);
        }
        return rigaArticoloNumeroDecimaliQtaRigaPropertyChange;
    }

    /**
     * @return RigaArticoloOrdineArticoloPropertyChange
     */
    private PropertyChangeListener getRigaArticoloOrdineArticoloPropertyChange() {
        if (rigaArticoloOrdineArticoloPropertyChange == null) {
            rigaArticoloOrdineArticoloPropertyChange = new RigaArticoloOrdineArticoloPropertyChange(getFormModel());
        }
        return rigaArticoloOrdineArticoloPropertyChange;
    }

    /**
     * @return RigaArticoloReadOnlyPropertyChange
     */
    private PropertyChangeListener getRigaArticoloReadOnlyPropertyChange() {
        if (rigaArticoloReadOnlyPropertyChange == null) {
            rigaArticoloReadOnlyPropertyChange = new RigaArticoloReadOnlyPropertyChange(getFormModel());
        }
        return rigaArticoloReadOnlyPropertyChange;
    }

    /**
     * @return RigheArticoliComponentiCommand
     */
    private RigheArticoliComponentiCommand getRigheArticoliComponentiCommand() {
        if (righeArticoliComponentiCommand == null) {
            righeArticoliComponentiCommand = new RigheArticoliComponentiCommand();
            righeArticoliComponentiCommandInterceptor = new RigheArticoliComponentiCommandInterceptor();
            righeArticoliComponentiCommand.addCommandInterceptor(righeArticoliComponentiCommandInterceptor);
        }
        return righeArticoliComponentiCommand;
    }

    /**
     * @return command per aprire la verifica prezzo con i parametri già impostati.
     */
    private OpenDescrizioneCalcoloPrezzoCommand getVerificaPrezzoCommand() {
        if (verificaPrezzoCommand == null) {
            verificaPrezzoCommand = new OpenDescrizioneCalcoloPrezzoCommand();
            verificaPrezzoCommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand arg0) {
                }

                @Override
                public boolean preExecution(ActionCommand command) {
                    RigaArticolo rigaArticolo = (RigaArticolo) RigaArticoloForm.this.getFormObject();
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.ARTICOLO_PARAM,
                            rigaArticolo.getArticolo());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.LISTINO_PARAM,
                            rigaArticolo.getAreaOrdine().getListino());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.LISTINO_ALTERNATIVO_PARAM,
                            rigaArticolo.getAreaOrdine().getListinoAlternativo());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.DATA_PARAM,
                            rigaArticolo.getAreaOrdine().getDocumento().getDataDocumento());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.SEDE_ENTITA,
                            rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.ENTITA_PARAM,
                            rigaArticolo.getAreaOrdine().getDocumento().getEntita());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.CODICE_VALUTA_PARAM,
                            rigaArticolo.getAreaOrdine().getDocumento().getTotale().getCodiceValuta());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.AGENTE_PARAM,
                            rigaArticolo.getAreaOrdine().getAgente());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.CODICE_PAGAMENTO_PARAM,
                            getValueModel("codicePagamento").getValue());
                    return true;
                }
            });
        }
        return verificaPrezzoCommand;

    }

    private String getWidth4Colonna() {
        return "70dlu";
    }

    /**
     * Al cambio articolo porto il focus sulla qta, quindi non posso usare il defaultController per
     * i property change perchè avrei solamente il formmodel.
     */
    private void installArticoloPropertyChange() {
        getFormModel().getValueModel("articolo").addValueChangeListener(getRigaArticoloOrdineArticoloPropertyChange());
        getFormModel().getValueModel("articolo").addValueChangeListener(getArticoloPropertyChangeListener());
    }

    /**
     * Installa il listener sulla proprietà "rigaArticolo.numeroDecimaliQta".
     */
    private void installNumerDecimaliQtaPropertyChange() {
        getFormModel().getValueModel("numeroDecimaliQta")
                .addValueChangeListener(getRigaArticoloNumeroDecimaliQtaPropertyChange());
        getFormModel().getFormObjectHolder()
                .addValueChangeListener(getRigaArticoloNumeroDecimaliQtaRigaPropertyChange());
    }

    /**
     * Installa un listener sulla proprietà dirty del formmodel per disabilitare alcune proprietà
     * della riga articolo.
     */
    private void installReadOnlyPropertyChange() {
        getFormModel().addPropertyChangeListener(FormModel.DIRTY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, getReadOnlyPropertyChangeListener());
    }

    /**
     * @return se ho un ordine cliente e l'articolo della riga è distinta
     */
    private boolean isRigaOrdineClienteDistinta() {
        RigaArticolo rigaArticolo = (RigaArticolo) getFormObject();
        boolean isRigaOrdineClienteDistinta = rigaArticolo.getAreaOrdine() != null && rigaArticolo.getArticolo() != null
                && rigaArticolo.getAreaOrdine() != null && rigaArticolo.getArticolo().isDistinta() && rigaArticolo
                        .getAreaOrdine().getTipoAreaOrdine().getTipoDocumento().getTipoEntita() == TipoEntita.CLIENTE;
        return isRigaOrdineClienteDistinta;
    }

    @Override
    public void revert() {
        giacenzaLabel.setVisible(false);
        super.revert();
    }

    /**
     * @param areaOrdineCorrente
     *            the areaOrdineCorrente to set
     */
    public void setAreaOrdineCorrente(AreaOrdine areaOrdineCorrente) {
        areaOrdineChanged = this.areaOrdineCorrente == null
                || (this.areaOrdineCorrente != null && !this.areaOrdineCorrente.equals(areaOrdineCorrente));
        this.areaOrdineCorrente = areaOrdineCorrente;
        getValueModel("areaOrdine").setValue(areaOrdineCorrente);
        if (tolleranzaComponent != null) {
            if (areaOrdineCorrente.getTipoAreaDocumento() != null) {
                tolleranzaComponent.setVisible(areaOrdineCorrente.getTipoAreaDocumento().getTipoDocumento()
                        .getTipoEntita() == TipoEntita.CLIENTE);
            } else {
                tolleranzaComponent.setVisible(false);
            }
        }
        if (getFormModel().isCommittable()) {
            commit();
        }
        showDataProduzione(areaOrdineCorrente);
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
        // RigaArticolo rigaArticolo = (RigaArticolo) formObject;
        // showDataProduzione(rigaArticolo.getAreaOrdine());
    }

    /**
     * Visualizza/nasconde il componente data produzione.
     *
     * @param areaOrdine
     *            areaOrdine
     */
    private void showDataProduzione(AreaOrdine areaOrdine) {
        TipoAreaOrdine tipoAreaOrdine = areaOrdineCorrente.getTipoAreaOrdine();
        boolean isOrdineProduzione = tipoAreaOrdine != null ? tipoAreaOrdine.isOrdineProduzione() : false;
        boolean isRigaOrdineClienteDistinta = isRigaOrdineClienteDistinta();
        boolean isDataProduzioneVisible = isOrdineProduzione || isRigaOrdineClienteDistinta;

        dataProduzioneComponent.setVisible(isDataProduzioneVisible);
        dataFineProduzioneComponent.setVisible(isDataProduzioneVisible);
        String dataConsegnaKey = isDataProduzioneVisible ? "dataConsegnaProduzione" : "dataConsegna";
        dataConsegnaLabel.setText(RcpSupport.getMessage(dataConsegnaKey));
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
        // JECOverlayHelper.attachOverlay(waitLabel,
        // importoComponent.getImportoTextField(), 10, 10);
        // importoComponent.add(waitLabel);
        // }
        // waitLabel.setVisible(show);
    }
}
