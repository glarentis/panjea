package it.eurotn.panjea.preventivi.rich.forms.righepreventivo;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.ApriStatisticaArticoloCommand;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.AttributiRigaArticoloBinder;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.OpenDescrizioneCalcoloPrezzoCommand;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaArticoloNumeroDecimaliQtaPropertyChange;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.ordini.rich.forms.righeordine.RangePrezzoOrdineControl;
import it.eurotn.panjea.ordini.rich.forms.righeordine.RigaArticoloReadOnlyPropertyChange;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RigaArticoloForm extends PanjeaAbstractForm {

    private class ApriStatisticaArticoloCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand actioncommand) {
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            RigaArticolo rigaArticolo = ((RigaArticolo) RigaArticoloForm.this.getFormObject());
            actioncommand.addParameter(ApriStatisticaArticoloCommand.PARAM_LOADER_OBJECT, rigaArticolo.getArticolo());
            // actioncommand.addParameter(ApriStatisticaArticoloCommand.PARAMETER_DEPOSITO, rigaArticolo.getAreaOrdine()
            // .getDepositoOrigine());
            return true;
        }
    }

    private class ArticoloPropertyChangeListener implements PropertyChangeListener {

        /**
         * Restituisce la stringa passata come parametro con la prima lettera maiuscola e le rimanenti minuscole.
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

            // se e' articolo libero deve inserire la descrizione. non passo il
            // focus alla quantita.
            if (rigaArticolo.getArticolo() == null) {
                return;
            }

            // se e' articolo libero deve inserire la descrizione. non passo il
            // focus alla quantita.
            boolean focusable = rigaArticolo.getArticolo() != null && rigaArticolo.getArticolo().getId() != null
                    && rigaArticolo.isArticoloLibero();

            descrizioneTextField.setFocusable(focusable);
            descrizioneLinguaTextField.setFocusable(focusable);
            umComponent.setFocusable(focusable);

            descrizioneLabel.setIcon(null);
            descrizioneLinguaLabel.setIcon(null);
            descrizioneLabel.setIcon(null);
            descrizioneLinguaLabel.setIcon(null);

            String linguaSede = null;
            if (rigaArticolo.getAreaPreventivo().getDocumento().getSedeEntita() != null) {
                linguaSede = rigaArticolo.getAreaPreventivo().getDocumento().getSedeEntita().getLingua();
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

    /**
     * Focus listener che si preoccupa, nel caso di articolo padre, di mostrare il dialogo per l'impostazione delle
     * quantità degli articoli figli.
     *
     * @author leonardo
     */
    private class QtaFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (getFormModel().getValueModel("formulaTrasformazione").getValue() != null) {
                Component prevFocus = e.getOppositeComponent();
                if ("rigaArticoloPreventivoFormModelModel.articolo.descrizione".equals(prevFocus.getName())
                        || "rigaArticoloPreventivoFormModelModel.articolo.codice".equals(prevFocus.getName())
                        || "rigaArticoloPreventivoFormModelModel.descrizione".equals(prevFocus.getName())) {
                    e.getComponent().transferFocus();
                }
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            getFormModel().getFieldMetadata("politicaPrezzo").setEnabled(false);
        }
    }

    private class SostituzioneArticoloListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            sostituzioneArticoloLabel
                    .setVisible((boolean) getFormModel().getValueModel("sostituzioneArticolo").getValue());
        }

    }

    private class SostituzioneArticoloToggleCommand extends JideToggleCommand {

        /**
         * Costruttore.
         */
        public SostituzioneArticoloToggleCommand() {
            super("sostituzioneArticoloToggleCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void onDeselection() {
            getFormModel().getValueModel("sostituzioneArticolo").setValue(Boolean.FALSE);
        }

        @Override
        protected void onSelection() {
            getFormModel().getValueModel("sostituzioneArticolo").setValue(Boolean.TRUE);
        }
    }

    public static final String FORM_ID = "rigaArticoloPreventivoForm";

    private AreaPreventivo areaPreventivoCorrente;

    private AziendaCorrente aziendaCorrente = null;
    private boolean areaOrdineChanged = false;
    private JFormattedTextField qtaFormattedTextField = null;

    private JTextComponent descrizioneTextField = null;

    private JLabel descrizioneLabel = null;
    private JTextComponent descrizioneLinguaTextField = null;
    private JLabel descrizioneLinguaLabel = null;
    private final FormComponentInterceptorFactory formComponentInterceptorFactory;
    private ApriStatisticaArticoloCommandInterceptor apriStatisticaArticoloCommandInterceptor;
    private OpenDescrizioneCalcoloPrezzoCommand verificaPrezzoCommand = null;
    private RigaArticoloNumeroDecimaliQtaPropertyChange rigaArticoloNumeroDecimaliQtaPropertyChange = null;
    private ArticoloPropertyChangeListener articoloPropertyChangeListener = null;
    private RigaArticoloNumeroDecimaliQtaPropertyChange rigaArticoloNumeroDecimaliQtaRigaPropertyChange = null;
    private RigaArticoloReadOnlyPropertyChange rigaArticoloReadOnlyPropertyChange;
    private QtaFocusListener qtaFocusListener = null;
    private JTextField umComponent;
    private JLabel giacenzaLabel = new JLabel("Disp.");
    private ApriStatisticaArticoloCommand apriStatisticaArticoloCommand;
    private PropertyChangeListener rigaArticoloPreventivoArticoloPropertyChange;

    private SostituzioneArticoloToggleCommand sostituzioneArticoloCommand = new SostituzioneArticoloToggleCommand();
    private JLabel sostituzioneArticoloLabel = new JLabel("Modalità sostituzione articolo attiva");
    private SostituzioneArticoloListener sostituzioneArticoloListener;

    /**
     * Costruttore di default.
     */
    public RigaArticoloForm() {
        super(PanjeaFormModelHelper.createFormModel(new RigaArticolo(), false, FORM_ID + "Model"), FORM_ID);
        formComponentInterceptorFactory = (FormComponentInterceptorFactory) Application.services()
                .getService(FormComponentInterceptorFactory.class);

        sostituzioneArticoloLabel.setBackground(Color.RED);
        sostituzioneArticoloLabel.setForeground(Color.BLACK);
        sostituzioneArticoloLabel.setVisible(false);
        sostituzioneArticoloLabel.setOpaque(true);

        // Aggiungo al formModel il value model dinamino per codicePagamento
        ValueModel codicePagamentoValueModel = new ValueHolder(new CodicePagamento());
        DefaultFieldMetadata metaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(codicePagamentoValueModel), CodicePagamento.class, true, null);
        getFormModel().add("codicePagamento", codicePagamentoValueModel, metaData);

        // Aggiungo al formModel il value model dinamino per l'indicatore della sostituzione articolo
        ValueModel sostituzioneArticoloValueModel = new ValueHolder(Boolean.FALSE);
        DefaultFieldMetadata sostituzioneArticoloMetaData = new DefaultFieldMetadata(getFormModel(),
                new FormModelMediatingValueModel(sostituzioneArticoloValueModel), Boolean.class, false, null);
        getFormModel().add("sostituzioneArticolo", sostituzioneArticoloValueModel, sostituzioneArticoloMetaData);
    }

    @Override
    protected JComponent createFormControl() {

        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "left:pref,4dlu,fill:80dlu,left:35dlu,50dlu,left:pref,4dlu,fill:45dlu,left:pref,fill:70dlu,4dlu,left:pref,left:60dlu,default:grow(0.7)",
                "2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);
        builder.addComponent(sostituzioneArticoloLabel, 3, 2, 10, 1);
        builder.nextRow();

        builder.addLabel("articolo", 1, 4);
        SearchPanel searchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText("articolo",
                new String[] { "codice", "descrizione" }, new String[] { "areaPreventivo.documento.entita" },
                new String[] { ArticoloSearchObject.ENTITA_KEY }), 3, 4, 10, 1);
        searchPanel.getTextFields().get("codice").setColumns(10);
        searchPanel.getTextFields().get("descrizione").setColumns(35);

        AbstractButton button = sostituzioneArticoloCommand.createButton();
        button.setFocusable(false);
        builder.addComponent(button, 13, 4);

        builder.nextRow();

        JComponent[] descrizioneComponents = builder.addPropertyAndLabel("descrizione", 1, 6, 2, 1);
        descrizioneLabel = (JLabel) descrizioneComponents[0];
        descrizioneLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        descrizioneTextField = (JTextComponent) descrizioneComponents[1];

        JComponent[] linguaComponents = builder.addPropertyAndLabel("descrizioneLingua", 6, 6, 5, 1);
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
        panel.add(giacenzaLabel);
        builder.addComponent(panel, 5);

        builder.addPropertyAndLabel("tipoOmaggio", 6, 8)[1].setFocusable(false);

        Binding attributiBinding = new AttributiRigaArticoloBinder().bind(getFormModel(), "attributi",
                new HashMap<String, Object>());
        FormComponentInterceptor interceptor = formComponentInterceptorFactory.getInterceptor(getFormModel());
        interceptor.processComponent("attributi", attributiBinding.getControl());
        builder.addComponent(attributiBinding.getControl(), 3, 10, 11, 1);

        builder.nextRow();
        builder.addLabel("prezzoUnitario", 1, 12);
        JComponent txtImporto = builder.addBinding(bf.createBoundImportoTextField("prezzoUnitario",
                "areaPreventivo.documento.totale", "numeroDecimaliPrezzo"), 3, 12);
        new RangePrezzoOrdineControl(getFormModel(), txtImporto);

        JPanel importoComponent = getComponentFactory().createPanel(new HorizontalLayout(3));
        importoComponent.add(getVerificaPrezzoCommand().createButton());
        importoComponent.add(getApriStatisticaArticoloCommand().createButton());
        builder.addComponent(importoComponent, 4);

        JPanel variazioniPanel = getComponentFactory().createPanel(new HorizontalLayout(3));
        builder.addLabel("variazione1", 6, 12);
        Binding variazione1Binding = bf.createBoundPercentageText("variazione1");
        Binding variazione2Binding = bf.createBoundPercentageText("variazione2");
        Binding variazione3Binding = bf.createBoundPercentageText("variazione3");
        Binding variazione4Binding = bf.createBoundPercentageText("variazione4");
        variazioniPanel.add(variazione1Binding.getControl());
        variazioniPanel.add(variazione2Binding.getControl());
        variazioniPanel.add(variazione3Binding.getControl());
        variazioniPanel.add(variazione4Binding.getControl());
        builder.addComponent(variazioniPanel, 8, 12, 6, 1);

        builder.nextRow();
        builder.addLabel("prezzoNetto", 1, 14);
        builder.addBinding(bf.createBoundImportoTextField("prezzoNetto", "areaPreventivo.documento.totale",
                "numeroDecimaliPrezzo"), 3, 14);
        builder.addLabel("prezzoTotale", 6, 14);
        builder.addBinding(bf.createBoundImportoTextField("prezzoTotale", "areaPreventivo.documento.totale"), 8, 14, 3,
                1);

        // HACK esegue getValue dei due attributi per evitare una
        // ConcurrentModificationException all'interno di
        // AttributiRigaArticoloPropertyChange
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

        sostituzioneArticoloListener = new SostituzioneArticoloListener();
        getFormModel().getFormObjectHolder().addValueChangeListener(sostituzioneArticoloListener);
        getFormModel().getValueModel("sostituzioneArticolo").addValueChangeListener(sostituzioneArticoloListener);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        RigaArticolo rigaArticoloNew = new RigaArticolo();
        Date dataConsegna = ((RigaArticolo) getFormObject()).getDataConsegna();
        if (areaPreventivoCorrente != null && (dataConsegna == null || areaOrdineChanged)) {
            // copio quello dell'area
            rigaArticoloNew.setDataConsegna(areaPreventivoCorrente.getDataScadenza());
        } else {
            // mantengo questo
            rigaArticoloNew.setDataConsegna(dataConsegna);
        }
        rigaArticoloNew.setAreaPreventivo(areaPreventivoCorrente);

        return rigaArticoloNew;
    }

    @Override
    public void dispose() {
        if (qtaFormattedTextField != null) {
            qtaFormattedTextField.removeFocusListener(getQtaFocusListener());
        }

        if (apriStatisticaArticoloCommand != null) {
            apriStatisticaArticoloCommand.removeCommandInterceptor(getApriStatisticaArticoloCommandInterceptor());
        }
        getFormModel().getValueModel("articolo")
                .removeValueChangeListener(getRigaArticoloPreventivoArticoloPropertyChange());
        getFormModel().getValueModel("articolo").removeValueChangeListener(getArticoloPropertyChangeListener());
        getFormModel().getValueModel("numeroDecimaliQta")
                .removeValueChangeListener(getRigaArticoloNumeroDecimaliQtaPropertyChange());
        getFormModel().getFormObjectHolder()
                .removeValueChangeListener(getRigaArticoloNumeroDecimaliQtaRigaPropertyChange());
        getFormModel().removePropertyChangeListener(FormModel.DIRTY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
        getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY,
                getRigaArticoloReadOnlyPropertyChange());

        verificaPrezzoCommand = null;
        apriStatisticaArticoloCommand = null;
        super.dispose();
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
     *
     * @return listener per il cambio dataConsegna
     */
    // private PropertyChangeListener getDataConsegnaPropertyChange() {
    // return new PropertyChangeListener() {
    //
    // @Override
    // public void propertyChange(PropertyChangeEvent evt) {
    // aggiornaGiacenza(((RigaArticolo) getFormObject()).getArticolo());
    // }
    // };
    // }

    /**
     * @return factory per la formattazione qtaDecimanli
     */
    private DefaultFormatterFactory getFactory() {
        Integer numeroDecimaliQt = (Integer) getFormModel().getValueModel("numeroDecimaliQta").getValue();
        DefaultNumberFormatterFactory f = new DefaultNumberFormatterFactory("#,##0", numeroDecimaliQt, Double.class);
        return f;
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
    private PropertyChangeListener getRigaArticoloPreventivoArticoloPropertyChange() {
        if (rigaArticoloPreventivoArticoloPropertyChange == null) {
            rigaArticoloPreventivoArticoloPropertyChange = new RigaArticoloPreventivoArticoloPropertyChange(
                    getFormModel());
        }
        return rigaArticoloPreventivoArticoloPropertyChange;
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
     *
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
                            rigaArticolo.getAreaPreventivo().getListino());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.LISTINO_ALTERNATIVO_PARAM,
                            rigaArticolo.getAreaPreventivo().getListinoAlternativo());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.DATA_PARAM,
                            rigaArticolo.getAreaPreventivo().getDocumento().getDataDocumento());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.SEDE_ENTITA,
                            rigaArticolo.getAreaPreventivo().getDocumento().getSedeEntita());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.ENTITA_PARAM,
                            rigaArticolo.getAreaPreventivo().getDocumento().getEntita());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.CODICE_VALUTA_PARAM,
                            rigaArticolo.getAreaPreventivo().getDocumento().getTotale().getCodiceValuta());
                    command.addParameter(OpenDescrizioneCalcoloPrezzoCommand.CODICE_PAGAMENTO_PARAM,
                            getValueModel("codicePagamento").getValue());
                    return true;
                }
            });
        }
        return verificaPrezzoCommand;

    }

    /**
     * Al cambio articolo porto il focus sulla qta, quindi non posso usare il defaultController per i property change
     * perchè avrei solamente il formmodel.
     */
    private void installArticoloPropertyChange() {
        getFormModel().getValueModel("articolo")
                .addValueChangeListener(getRigaArticoloPreventivoArticoloPropertyChange());
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
     * Installa un listener sulla proprietà dirty del formmodel per disabilitare alcune proprietà della riga articolo.
     */
    private void installReadOnlyPropertyChange() {
        getFormModel().addPropertyChangeListener(FormModel.DIRTY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, getRigaArticoloReadOnlyPropertyChange());
    }

    @Override
    public void revert() {
        giacenzaLabel.setVisible(false);
        super.revert();
    }

    /**
     * @param areaPreventivoCorrente
     *            the areaOrdineCorrente to set
     */
    public void setAreaPreventivoCorrente(AreaPreventivo areaPreventivoCorrente) {
        areaOrdineChanged = this.areaPreventivoCorrente == null
                || !this.areaPreventivoCorrente.equals(areaPreventivoCorrente);

        this.areaPreventivoCorrente = areaPreventivoCorrente;
        getValueModel("areaPreventivo").setValue(areaPreventivoCorrente);
        if (getFormModel().isCommittable()) {
            commit();
        }
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
}
