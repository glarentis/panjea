package it.eurotn.panjea.contabilita.rich.forms;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipologiaContoControPartita;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.form.builder.support.OverlayHelper;

/**
 * Classe form per la gestione della ControPartita.
 *
 * @author fattazzo
 * @version 1.0, 03/set/07
 */
public class ControPartitaForm extends PanjeaAbstractForm implements Focussable {

    /**
     * Property change listener per rendere esclusiva la selezione dei campi conto base dare ed avere; quando cancello
     * il valore inserito in uno dei due i due campi vengono abilitati nuovamente.
     *
     * @author Leonardo
     */
    private class ContoBaseChangeListener implements PropertyChangeListener {

        private String property = null;
        private ControPartita controPartita = null;

        /**
         * Costruttore.
         *
         * @param property
         *            proprietà
         * @param controPartita
         *            contro partita
         */
        public ContoBaseChangeListener(final String property, final ControPartita controPartita) {
            this.property = property;
            this.controPartita = controPartita;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> Enter ContoBaseChangeListener " + evt.getSource() + " property " + property + " value "
                    + evt.getNewValue());
            ContoBase base = (ContoBase) evt.getNewValue();
            if (base != null && controPartita.getTipologiaContoControPartita() != null) {
                if (base.getId() != null && base.getId().intValue() != -1) {
                    getFormModel().setFormObject(getFormModel().getFormObject());
                }
            }
            logger.debug("--> Exit ContoBaseChangeListener");
        }
    }

    /**
     * Property change listener per rendere esclusiva la selezione dei campi conto dare ed avere; quando cancello il
     * valore inserito in uno dei due i due campi vengono abilitati nuovamente.
     *
     * @author Leonardo
     */
    private class ContoChangeListener implements PropertyChangeListener {

        private String property = null;
        private ControPartita controPartita = null;

        /**
         * Costruttore.
         *
         * @param property
         *            proprietà
         * @param controPartita
         *            contro partita
         */
        public ContoChangeListener(final String property, final ControPartita controPartita) {
            this.property = property;
            this.controPartita = controPartita;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> ContoChangeListener " + evt.getSource() + " property " + property + " value "
                    + evt.getNewValue());
            Conto conto = (Conto) evt.getNewValue();
            if (conto != null && controPartita.getTipologiaContoControPartita() != null) {
                if (conto.getId() != null && conto.getId().intValue() != -1) {
                    getFormModel().setFormObject(getFormModel().getFormObject());
                }
            }
        }
    }

    /**
     * Property change listener per rendere esclusiva la selezione dei campi sottoconto dare ed avere; quando cancello
     * il valore inserito in uno dei due i due campi vengono abilitati nuovamente.
     *
     * @author Leonardo
     */
    private class SottoContoChangeListener implements PropertyChangeListener {

        private String property = null;
        private ControPartita controPartita = null;

        /**
         * Costruttore.
         *
         * @param property
         *            proprietà
         * @param controPartita
         *            contro partita
         */
        public SottoContoChangeListener(final String property, final ControPartita controPartita) {
            this.property = property;
            this.controPartita = controPartita;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> ContoChangeListener " + evt.getSource() + "property " + property + " value "
                    + evt.getNewValue());
            SottoConto sotto = (SottoConto) evt.getNewValue();
            if (sotto != null && controPartita.getTipologiaContoControPartita() != null) {
                if (sotto.getId() != null && sotto.getId().intValue() != -1) {
                    getFormModel().setFormObject(getFormModel().getFormObject());
                }
            }
        }
    }

    private static Logger logger = Logger.getLogger(ControPartitaForm.class);
    private static final String FORM_ID = "controPartitaForm";
    private static final String RADIO_BUTTON_CONTO_LABEL = FORM_ID + ".radioButtonConto.label";

    private static final String RADIO_BUTTON_CONTO_BASE_LABEL = FORM_ID + ".radioButtonContoBase.label";
    private static final String RADIO_BUTTON_SOTTOCONTO_LABEL = FORM_ID + ".radioButtonSottoConto.label";
    private static final String RADIO_BUTTON_CONTO_BASE_CONTO_NULLO_DESC = FORM_ID
            + ".radioButtonContoBase.sottoContoNullo.descrizione.label";
    private JRadioButton radioButtonConto;

    private JRadioButton radioButtonContoBase;

    private JRadioButton radioButtonSottoConto;

    private ContoBase contoBaseNullo;

    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;
    private Component sottocontoDare;
    private Component sottoContoAvere;
    private Component contoAvere;
    private Component contoDare;
    private JComponent contoBaseAvere;
    private JComponent contoBaseDare;
    private OverlayHelper overlayAvere;
    private OverlayHelper overlayDare;
    private JComponent descrizioneControl;

    /**
     * Costruttore.
     *
     * @param controPartita
     *            controPartita
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public ControPartitaForm(final ControPartita controPartita,
            final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PanjeaFormModelHelper.createFormModel(controPartita, false, FORM_ID), FORM_ID);
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.richclient.form.AbstractForm#createFormControl()
     */
    @Override
    protected JComponent createFormControl() {
        logger.debug("--> Enter createFormControl");
        createRadioButtonGroup();

        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:65dlu,4dlu,fill:120dlu,4dlu,fill:120dlu", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");

        // Prima riga
        // -----------------------------------------------------------------------------------------------
        descrizioneControl = builder.addPropertyAndLabel("descrizione", 1, 2, 3)[1];
        builder.nextRow();

        // Seconda riga
        // ---------------------------------------------------------------------------------------------
        // gap row per mostrare label dare e avere al di sopra dei conti
        builder.setRow(14);

        // Terza riga
        // -----------------------------------------------------------------------------------------------
        builder.addComponent(radioButtonSottoConto, 1);

        Binding bindingDare = bf.createBinding("dare");
        sottocontoDare = bindingDare.getControl().getComponent(0);
        SearchTextField searchTextFieldDare = ((SearchPanel) bindingDare.getControl()).getTextFields()
                .get("descrizione");
        searchTextFieldDare.setColumns(12);

        Binding bindingAvere = bf.createBinding("avere");
        sottoContoAvere = bindingAvere.getControl().getComponent(0);
        SearchTextField searchTextFieldAvere = ((SearchPanel) bindingAvere.getControl()).getTextFields()
                .get("descrizione");
        searchTextFieldAvere.setColumns(12);

        JLabel labelDare = getComponentFactory()
                .createLabel(getMessageSource().getMessage("dare", new Object[] {}, Locale.getDefault()));
        JLabel labelAvere = getComponentFactory()
                .createLabel(getMessageSource().getMessage("avere", new Object[] {}, Locale.getDefault()));

        overlayDare = OverlayHelper.attachOverlay(labelDare, searchTextFieldDare, SwingConstants.NORTH, 0, -8);
        overlayAvere = OverlayHelper.attachOverlay(labelAvere, searchTextFieldAvere, SwingConstants.NORTH, 0, -8);

        builder.addComponent(searchTextFieldDare, 3);
        builder.addComponent(searchTextFieldAvere, 5);

        addFormValueChangeListener("dare", new SottoContoChangeListener("dare", (ControPartita) this.getFormObject()));
        addFormValueChangeListener("avere",
                new SottoContoChangeListener("avere", (ControPartita) this.getFormObject()));
        builder.nextRow();

        // Quarta riga
        // ----------------------------------------------------------------------------------------------
        builder.addComponent(radioButtonConto, 1);

        Binding bindingContoDare = bf.createBinding("contoDare");
        contoDare = bindingContoDare.getControl().getComponent(0);
        SearchTextField searchTextFieldContoDare = ((SearchPanel) bindingContoDare.getControl()).getTextFields()
                .get("contoCodice");
        searchTextFieldContoDare.setColumns(12);

        Binding bindingContoAvere = bf.createBinding("contoAvere");
        contoAvere = bindingContoAvere.getControl().getComponent(0);
        SearchTextField searchTextFieldContoAvere = ((SearchPanel) bindingContoAvere.getControl()).getTextFields()
                .get("contoCodice");
        searchTextFieldContoAvere.setColumns(12);

        builder.addComponent(searchTextFieldContoDare, 3);
        builder.addComponent(searchTextFieldContoAvere, 5);
        builder.nextRow();

        addFormValueChangeListener("contoDare",
                new ContoChangeListener("contoDare", (ControPartita) this.getFormObject()));
        addFormValueChangeListener("contoAvere",
                new ContoChangeListener("contoAvere", (ControPartita) this.getFormObject()));

        // Quinta riga
        // ----------------------------------------------------------------------------------------------
        builder.addComponent(radioButtonContoBase, 1);

        List<ContoBase> listContiBase = new ArrayList<ContoBase>();
        listContiBase.add(getContoBaseNullo());
        listContiBase.addAll(contabilitaAnagraficaBD.caricaContiBase());

        Binding bindingContoBaseDare = bf.createBoundComboBox("contoBaseDare", new ValueHolder(listContiBase),
                "descrizione");
        contoBaseDare = bindingContoBaseDare.getControl();
        Binding bindingContoBaseAvere = bf.createBoundComboBox("contoBaseAvere", new ValueHolder(listContiBase),
                "descrizione");
        contoBaseAvere = bindingContoBaseAvere.getControl();

        builder.addComponent(bindingContoBaseDare.getControl(), 3);
        builder.addComponent(bindingContoBaseAvere.getControl(), 5);
        builder.nextRow();

        addFormValueChangeListener("contoBaseDare",
                new ContoBaseChangeListener("contoBaseDare", (ControPartita) this.getFormObject()));
        addFormValueChangeListener("contoBaseAvere",
                new ContoBaseChangeListener("contoBaseAvere", (ControPartita) this.getFormObject()));

        // Sesta riga
        // ----------------------------------------------------------------------------------------------
        builder.addPropertyAndLabel("formula", 1, 20, 3);
        builder.nextRow();

        // Settima riga
        // ----------------------------------------------------------------------------------------------
        builder.addLabel("codiceIva", 1);
        builder.addBinding(bf.createBoundSearchText("codiceIva", new String[] { "codice" }), 3);
        builder.nextRow();

        // Sesta riga
        // -----------------------------------------------------------------------------------------------
        builder.addPropertyAndLabel("ordine", 1);

        initialize();

        getFormModel().addPropertyChangeListener(FormModel.ENABLED_PROPERTY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                initialize();

            }
        });
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                initialize();

            }
        });

        logger.debug("--> Exit createFormControl");
        return builder.getPanel();
    }

    /**
     * Creo tutti i radio button per la selezione della tipologia conto contro partita.
     */
    private void createRadioButtonGroup() {
        radioButtonSottoConto = new JRadioButton(
                getMessageSource().getMessage(RADIO_BUTTON_SOTTOCONTO_LABEL, new Object[] {}, Locale.getDefault()));
        radioButtonSottoConto.setHorizontalTextPosition(SwingConstants.LEFT);
        radioButtonSottoConto.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (radioButtonSottoConto.isSelected()) {
                    sottoContoAvere.setEnabled(true);
                    sottocontoDare.setEnabled(true);

                    contoAvere.setEnabled(false);
                    contoDare.setEnabled(false);

                    contoBaseAvere.setEnabled(false);
                    contoBaseDare.setEnabled(false);

                    ControPartita controPartita = ((ControPartita) getFormObject());
                    controPartita.setTipologiaContoControPartita(ETipologiaContoControPartita.SOTTOCONTO);
                }

            }
        });

        radioButtonContoBase = new JRadioButton(
                getMessageSource().getMessage(RADIO_BUTTON_CONTO_BASE_LABEL, new Object[] {}, Locale.getDefault()));
        radioButtonContoBase.setHorizontalTextPosition(SwingConstants.LEFT);
        radioButtonContoBase.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (radioButtonContoBase.isSelected()) {
                    sottoContoAvere.setEnabled(false);
                    sottocontoDare.setEnabled(false);

                    contoAvere.setEnabled(false);
                    contoDare.setEnabled(false);

                    contoBaseAvere.setEnabled(true);
                    contoBaseDare.setEnabled(true);

                    ControPartita controPartita = ((ControPartita) getFormObject());
                    controPartita.setTipologiaContoControPartita(ETipologiaContoControPartita.CONTO_BASE);
                }
            }
        });

        radioButtonConto = new JRadioButton(
                getMessageSource().getMessage(RADIO_BUTTON_CONTO_LABEL, new Object[] {}, Locale.getDefault()));
        radioButtonConto.setHorizontalTextPosition(SwingConstants.LEFT);
        radioButtonConto.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (radioButtonConto.isSelected()) {

                    sottoContoAvere.setEnabled(false);
                    sottocontoDare.setEnabled(false);

                    contoAvere.setEnabled(true);
                    contoDare.setEnabled(true);

                    contoBaseAvere.setEnabled(false);
                    contoBaseDare.setEnabled(false);

                    ControPartita controPartita = ((ControPartita) getFormObject());
                    controPartita.setTipologiaContoControPartita(ETipologiaContoControPartita.CONTO);
                }

            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonSottoConto);
        buttonGroup.add(radioButtonConto);
        buttonGroup.add(radioButtonContoBase);
    }

    @Override
    public void dispose() {
        super.dispose();
        overlayAvere.dispose();
        overlayDare.dispose();
    }

    /**
     * Crea un conto base vuoto.
     *
     * @return {@link ContoBase} creato
     */
    private ContoBase getContoBaseNullo() {
        if (contoBaseNullo == null) {
            contoBaseNullo = new ContoBase();
            contoBaseNullo.setId(-1);
            contoBaseNullo.setDescrizione(" " + getMessageSource().getMessage(RADIO_BUTTON_CONTO_BASE_CONTO_NULLO_DESC,
                    new Object[] {}, Locale.getDefault()));
        }
        return contoBaseNullo;
    }

    @Override
    public ActionCommand getNewFormObjectCommand() {
        ActionCommand command = super.getNewFormObjectCommand();

        return command;
    }

    @Override
    public void grabFocus() {
        descrizioneControl.requestFocusInWindow();
    }

    /**
     * Seleziona il radio button appropriato rispetto alla tipologia del conto della contropartita contenuta nel form.
     */
    private void initialize() {
        sottoContoAvere.setEnabled(false);
        sottocontoDare.setEnabled(false);

        contoAvere.setEnabled(false);
        contoDare.setEnabled(false);

        contoBaseAvere.setEnabled(false);
        contoBaseDare.setEnabled(false);

        radioButtonConto.setSelected(false);
        radioButtonContoBase.setSelected(false);
        radioButtonSottoConto.setSelected(false);

        ControPartita controPartita = (ControPartita) getFormObject();
        if (controPartita.getTipologiaContoControPartita() != null) {
            switch (controPartita.getTipologiaContoControPartita()) {
            case CONTO:
                radioButtonConto.setSelected(true);
                break;
            case CONTO_BASE:
                radioButtonContoBase.setSelected(true);
                break;
            case SOTTOCONTO:
                radioButtonSottoConto.setSelected(true);
                break;
            default:
                break;
            }
        } else {
            radioButtonSottoConto.setSelected(true);
        }

    }
}
