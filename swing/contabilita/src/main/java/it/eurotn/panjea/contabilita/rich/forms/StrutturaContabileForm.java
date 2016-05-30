package it.eurotn.panjea.contabilita.rich.forms;

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
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ETipologiaConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.pm.StrutturaContabilePM;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;
import it.eurotn.rich.form.builder.support.OverlayHelper;

/**
 * Classe per la gestione della struttura contabile dove la tipologia del conto e' diversa da CONTRO_PARTITA.
 *
 * @author fattazzo
 * @version 1.0, 30/ago/07
 */
public class StrutturaContabileForm extends PanjeaAbstractForm {

    private class ContoBaseChangeListener implements PropertyChangeListener {

        private String property = null;

        /**
         *
         * Costruttore.
         *
         * @param property
         *            proprietà
         */
        public ContoBaseChangeListener(final String property) {
            this.property = property;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> ContoChangeListener property " + evt.getSource() + " value " + evt.getNewValue());
            ContoBase base = (ContoBase) evt.getNewValue();
            if (base != null) {
                if (base.getId() != -1) {
                    enableContoBase(property);
                } else {
                    enableAllContoBase();
                }
            }
        }
    }

    /**
     * Property change listener per rendere esclusiva la selezione dei campi dare ed avere; quando cancello il valore
     * inserito in uno dei due i due campi vengono abilitati nuovamente.
     *
     * @author Leonardo
     */
    private class ContoChangeListener implements PropertyChangeListener {

        private String property = null;

        /**
         *
         * Costruttore.
         *
         * @param property
         *            proprietà
         */
        public ContoChangeListener(final String property) {
            this.property = property;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("--> ContoChangeListener property " + evt.getSource() + " value " + evt.getNewValue());
            SottoConto sotto = (SottoConto) evt.getNewValue();
            if (sotto != null) {
                if (sotto.getId() != null && sotto.getId().intValue() != -1) {
                    enableConto(property);
                    // updateLabel(property,sotto);
                } else {
                    enableAll();
                    // updateLabel(property,null);
                }
            }
        }
    }

    private static Logger logger = Logger.getLogger(StrutturaContabileForm.class);
    private static final String FORM_ID = "strutturaContabileForm";

    private static final String DARE_LABEL = FORM_ID + ".dare.label";

    private static final String AVERE_LABEL = FORM_ID + ".avere.label";
    private static final String RADIO_BUTTON_CONTO_LABEL = FORM_ID + ".radioButtonConto.label";
    private static final String RADIO_BUTTON_CONTO_BASE_LABEL = FORM_ID + ".radioButtonContoBase.label";
    private static final String RADIO_BUTTON_ENTITA_LABEL = FORM_ID + ".radioButtonEntita.label";
    private static final String RADIO_BUTTON_CONTO_BASE_CONTO_NULLO_DESC = FORM_ID
            + ".radioButtonContoBase.sottoContoNullo.descrizione.label";
    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    private JRadioButton radioButtonConto;
    private JRadioButton radioButtonContoBase;
    private JRadioButton radioButtonEntita;
    private JRadioButton radioButtonEntitaDare;
    private JRadioButton radioButtonEntitaAvere;

    private SearchTextField contoDareSearch;
    private SearchTextField contoAvereSearch;

    private JComponent contoBaseDare;
    private JComponent contoBaseAvere;

    private ContoBase contoBaseNullo;

    /**
     *
     * Costruttore.
     *
     * @param strutturaContabilePM
     *            struttura contabile
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public StrutturaContabileForm(final StrutturaContabilePM strutturaContabilePM,
            final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PanjeaFormModelHelper.createFormModel(strutturaContabilePM, false, FORM_ID), FORM_ID);
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    @Override
    protected JComponent createFormControl() {
        logger.debug("--> Enter createFormControl");
        createRadioButtonGroup();

        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:65dlu,4dlu,fill:100dlu,4dlu,fill:100dlu", "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");
        builder.setRow(10);

        // Prima riga-----------------------------------------------------------------------------------------------
        builder.addComponent(radioButtonConto, 1);

        Binding sottoContoDareBinding = bf.createBinding("sottoContoDare");
        Binding sottoContoAvereBinding = bf.createBinding("sottoContoAvere");
        contoDareSearch = ((SearchPanel) sottoContoDareBinding.getControl()).getTextFields().get("descrizione");
        contoAvereSearch = ((SearchPanel) sottoContoAvereBinding.getControl()).getTextFields().get("descrizione");
        contoDareSearch.setColumns(12);
        contoAvereSearch.setColumns(12);

        JLabel labelDare = getComponentFactory()
                .createLabel(getMessageSource().getMessage(DARE_LABEL, new Object[] {}, Locale.getDefault()));
        JLabel labelAvere = getComponentFactory()
                .createLabel(getMessageSource().getMessage(AVERE_LABEL, new Object[] {}, Locale.getDefault()));

        OverlayHelper.attachOverlay(labelDare, contoDareSearch, SwingConstants.NORTH, 0, -8);
        OverlayHelper.attachOverlay(labelAvere, contoAvereSearch, SwingConstants.NORTH, 0, -8);

        builder.addComponent(contoDareSearch, 3);
        builder.addComponent(contoAvereSearch, 5);

        addFormValueChangeListener("sottoContoDare", new ContoChangeListener("sottoContoDare"));
        addFormValueChangeListener("sottoContoAvere", new ContoChangeListener("sottoContoAvere"));
        builder.nextRow();

        // Seconda riga-----------------------------------------------------------------------------------------------
        builder.addComponent(radioButtonContoBase, 1);

        List<ContoBase> listContiBase = new ArrayList<ContoBase>();

        listContiBase.add(getContoBaseNullo());
        listContiBase.addAll(contabilitaAnagraficaBD.caricaContiBase());

        Binding bindingContoBaseDare = bf.createBoundComboBox("contoBaseDare", new ValueHolder(listContiBase),
                "descrizione");
        Binding bindingContoBaseAvere = bf.createBoundComboBox("contoBaseAvere", new ValueHolder(listContiBase),
                "descrizione");
        contoBaseDare = bindingContoBaseDare.getControl();
        contoBaseAvere = bindingContoBaseAvere.getControl();

        builder.addComponent(contoBaseDare, 3);
        builder.addComponent(contoBaseAvere, 5);

        addFormValueChangeListener("contoBaseDare", new ContoBaseChangeListener("contoBaseDare"));
        addFormValueChangeListener("contoBaseAvere", new ContoBaseChangeListener("contoBaseAvere"));
        builder.nextRow();

        // Terza riga-----------------------------------------------------------------------------------------------
        builder.addComponent(radioButtonEntita, 1);

        builder.setComponentAttributes("c, c");
        builder.addComponent(radioButtonEntitaDare, 3);
        builder.addComponent(radioButtonEntitaAvere, 5);
        builder.setComponentAttributes("f, c");
        builder.nextRow();

        builder.setRow(25);

        // Quarta riga-----------------------------------------------------------------------------------------------
        builder.addPropertyAndLabel("formula", 1, 26, 3);
        builder.nextRow();

        // Quinta riga-----------------------------------------------------------------------------------------------
        builder.addPropertyAndLabel("ordine", 1);
        builder.nextRow();

        initializeControl();

        return builder.getPanel();
    }

    private void createRadioButtonGroup() {
        radioButtonConto = new JRadioButton(
                getMessageSource().getMessage(RADIO_BUTTON_CONTO_LABEL, new Object[] {}, Locale.getDefault()));
        radioButtonConto.setHorizontalTextPosition(SwingConstants.LEFT);
        radioButtonConto.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (radioButtonConto.isSelected()) {
                    refreshControls(ETipologiaConto.CONTO);
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
                    refreshControls(ETipologiaConto.CONTO_BASE);
                }
            }
        });
        radioButtonEntita = new JRadioButton(
                getMessageSource().getMessage(RADIO_BUTTON_ENTITA_LABEL, new Object[] {}, Locale.getDefault()));
        radioButtonEntita.setHorizontalTextPosition(SwingConstants.LEFT);
        radioButtonEntita.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (radioButtonEntita.isSelected()) {
                    refreshControls(ETipologiaConto.ENTITA);
                    radioButtonEntitaDare.setSelected(true);
                    if (((StrutturaContabilePM) getFormObject()).isEntitaDare() == false
                            && ((StrutturaContabilePM) getFormObject()).isEntitaAvere() == false) {
                        ((StrutturaContabilePM) getFormObject()).setEntitaDare(true);
                    }
                }
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonConto);
        buttonGroup.add(radioButtonContoBase);
        buttonGroup.add(radioButtonEntita);

        radioButtonEntitaDare = new JRadioButton("");

        radioButtonEntitaAvere = new JRadioButton("");

        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(radioButtonEntitaDare);
        buttonGroup2.add(radioButtonEntitaAvere);
    }

    private void enableAll() {
        logger.debug("--> Enter enableAll");
        contoAvereSearch.setEnabled(true);
        contoDareSearch.setEnabled(true);
    }

    private void enableAllContoBase() {
        contoBaseAvere.setEnabled(true);
        contoBaseDare.setEnabled(true);
    }

    /**
     * Data la proprieta' fa in modo che sia abilitata solo quella scelta, quindi viene disabilitata la search text
     * rimasta.
     *
     * @param property
     *            la proprieta' da mantenere abilitata
     */
    private void enableConto(String property) {
        logger.debug("--> Enter disableConto " + property);
        if ("sottoContoDare".equals(property)) {
            contoAvereSearch.setEnabled(false);
        } else if ("sottoContoAvere".equals(property)) {
            contoDareSearch.setEnabled(false);
        }
    }

    private void enableContoBase(String property) {
        if ("contoBaseDare".equals(property)) {
            contoBaseAvere.setEnabled(false);
            ((StrutturaContabilePM) getFormObject()).setContoBaseAvere(getContoBaseNullo());
        } else if ("contoBaseAvere".equals(property)) {
            contoBaseDare.setEnabled(false);
            ((StrutturaContabilePM) getFormObject()).setContoBaseDare(getContoBaseNullo());
        }
    }

    private ContoBase getContoBaseNullo() {
        if (contoBaseNullo == null) {
            contoBaseNullo = new ContoBase();
            contoBaseNullo.setId(-1);
            contoBaseNullo.setDescrizione(" " + getMessageSource().getMessage(RADIO_BUTTON_CONTO_BASE_CONTO_NULLO_DESC,
                    new Object[] {}, Locale.getDefault()));
        }
        return contoBaseNullo;
    }

    private void initializeControl() {
        refreshControls(null);
        StrutturaContabilePM strutturaContabilePM = (StrutturaContabilePM) getFormObject();
        if (strutturaContabilePM.getStrutturaContabile().getTipoDocumento() != null) {
            TipoDocumento tipoDocumento = strutturaContabilePM.getStrutturaContabile().getTipoDocumento();
            if (tipoDocumento.getTipoEntita().equals(TipoDocumento.TipoEntita.AZIENDA)) {
                radioButtonEntita.setEnabled(false);
            } else {
                radioButtonEntita.setEnabled(true);
            }
        } else {
            radioButtonEntita.setEnabled(true);
        }
        if (strutturaContabilePM.getTipologiaConto() != null) {
            switch (strutturaContabilePM.getTipologiaConto()) {
            case CONTO:
                radioButtonConto.setSelected(true);
                break;
            case CONTO_BASE:
                radioButtonContoBase.setSelected(true);
                break;
            case ENTITA:
                radioButtonEntita.setSelected(true);
                if (strutturaContabilePM.isEntitaDare()) {
                    radioButtonEntitaDare.setSelected(true);
                } else {
                    radioButtonEntitaAvere.setSelected(true);
                }
                break;
            default:
                // non previsto
            }
        }

        radioButtonEntitaDare.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {

                if (radioButtonEntitaDare.isSelected()) {
                    ((StrutturaContabilePM) getFormObject()).setEntitaDare(true);
                    ((StrutturaContabilePM) getFormObject()).setEntitaAvere(false);
                }
            }
        });

        radioButtonEntitaAvere.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (radioButtonEntitaAvere.isSelected()) {
                    ((StrutturaContabilePM) getFormObject()).setEntitaDare(false);
                    ((StrutturaContabilePM) getFormObject()).setEntitaAvere(true);
                }
            }
        });
    }

    private void refreshControls(ETipologiaConto tipologiaConto) {

        boolean isConto = tipologiaConto != null && tipologiaConto == ETipologiaConto.CONTO;
        contoDareSearch.setEnabled(isConto);
        contoAvereSearch.setEnabled(isConto);

        boolean isContoBase = tipologiaConto != null && tipologiaConto == ETipologiaConto.CONTO_BASE;
        contoBaseDare.setEnabled(isContoBase);
        contoBaseAvere.setEnabled(isContoBase);

        boolean isEntita = tipologiaConto != null && tipologiaConto == ETipologiaConto.ENTITA;
        radioButtonEntitaAvere.setEnabled(isEntita);
        radioButtonEntitaDare.setEnabled(isEntita);

        if (tipologiaConto != null) {
            ((StrutturaContabilePM) getFormObject()).setTipologiaConto(tipologiaConto);
        }
    }

}
