package it.eurotn.panjea.contabilita.rich.editors.areacontabile.datiritenutaacconto;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.StyleRange;
import com.jidesoft.swing.StyledLabel;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author fattazzo
 *
 */
public class DatiRitenutaAccontoForm extends PanjeaAbstractForm {

    private class ImponibileListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            BigDecimal imponibileRiferimento = (BigDecimal) getValueModel(
                    "datiRitenutaAccontoAreaContabile.imponibileRiferimento").getValue();
            BigDecimal imponibileSoggetto = (BigDecimal) getValueModel(
                    "datiRitenutaAccontoAreaContabile.imponibileSoggettoRitenuta").getValue();
            BigDecimal imponibileNonSoggetto = (BigDecimal) getValueModel(
                    "datiRitenutaAccontoAreaContabile.imponibileNonSoggettoRitenuta").getValue();

            imponibileRiferimento = imponibileRiferimento == null ? BigDecimal.ZERO : imponibileRiferimento;
            imponibileSoggetto = imponibileSoggetto == null ? BigDecimal.ZERO : imponibileSoggetto;
            imponibileNonSoggetto = imponibileNonSoggetto == null ? BigDecimal.ZERO : imponibileNonSoggetto;

            imponibileNonQuadratoLabel
                    .setVisible(imponibileRiferimento.compareTo(imponibileSoggetto.add(imponibileNonSoggetto)) != 0);
            if (imponibileNonQuadratoLabel.isVisible()) {
                imponibileNonQuadratoLabel.setText("Attenzione: la somma degli imponibili deve errese pari a "
                        + new DecimalFormat("#,##0.00").format(imponibileRiferimento));
            }

        }
    }

    private class PercAziendaleListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Double percAz = (Double) getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda")
                    .getValue();
            BigDecimal percAzDecimal = percAz == null ? BigDecimal.ZERO : BigDecimal.valueOf(percAz);

            getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore")
                    .setValue(new BigDecimal(100).subtract(percAzDecimal).doubleValue());
        }
    }

    private class PercLavoratoreListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Double percLav = (Double) getValueModel(
                    "datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore").getValue();
            BigDecimal percLavDecimal = percLav == null ? BigDecimal.ZERO : BigDecimal.valueOf(percLav);

            getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda")
                    .setValue(new BigDecimal(100).subtract(percLavDecimal).doubleValue());
        }
    }

    private class UpdateControlListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateEnableControl();
        }
    }

    public static final String FORM_ID = "datiRitenutaAccontoForm";

    private UpdateControlListener updateControlListener = new UpdateControlListener();
    private PercAziendaleListener percAziendaleListener = new PercAziendaleListener();
    private PercLavoratoreListener percLavoratoreListener = new PercLavoratoreListener();
    private ImponibileListener imponibileListener = new ImponibileListener();

    private StyledLabel imponibileNonQuadratoLabel;

    {
        imponibileNonQuadratoLabel = new StyledLabel("Attenzione: la somma degli imponibili deve errese pari a ");
        imponibileNonQuadratoLabel
                .addStyleRange(new StyleRange(Font.PLAIN, Color.RED, StyleRange.STYLE_UNDERLINED, Color.BLACK));
    }

    /**
     * Costruttore.
     */
    public DatiRitenutaAccontoForm() {
        super(PanjeaFormModelHelper.createFormModel(new AreaContabile(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,right:pref,4dlu,fill:40dlu, 10dlu, right:pref,4dlu,fill:40dlu",
                "4dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        builder.addPropertyAndLabel("datiRitenutaAccontoAreaContabile.imponibileSoggettoRitenuta", 2);
        builder.addPropertyAndLabel("datiRitenutaAccontoAreaContabile.imponibileNonSoggettoRitenuta", 6);
        builder.nextRow();

        builder.addPropertyAndLabel("datiRitenutaAccontoAreaContabile.speseRimborso", 2);
        builder.nextRow();

        builder.addComponent(imponibileNonQuadratoLabel, 2, 6, 7, 1, "r,c");
        builder.nextRow();

        builder.addProperty("datiRitenutaAccontoAreaContabile.fondoProfessionistiPresente", 1);
        builder.addHorizontalSeparator("Fondo professionisti", 2, 7);
        builder.nextRow();
        builder.addLabel("datiRitenutaAccontoAreaContabile.percFondoProfessionisti", 2);
        builder.addBinding(bf.createBoundPercentageText("datiRitenutaAccontoAreaContabile.percFondoProfessionisti"), 4);
        builder.nextRow();

        builder.addProperty("datiRitenutaAccontoAreaContabile.causaleRitenutaPresente", 1);
        builder.addHorizontalSeparator("Dati ritenuta", 2, 7);
        builder.nextRow();
        builder.addPropertyAndLabel("datiRitenutaAccontoAreaContabile.tributo", 2);
        builder.addPropertyAndLabel("datiRitenutaAccontoAreaContabile.sezione", 6);
        builder.nextRow();
        builder.addLabel("datiRitenutaAccontoAreaContabile.percentualeImponibile", 2);
        builder.addBinding(bf.createBoundPercentageText("datiRitenutaAccontoAreaContabile.percentualeImponibile"), 4);
        builder.addLabel("datiRitenutaAccontoAreaContabile.percentualeAliquota", 6);
        builder.addBinding(bf.createBoundPercentageText("datiRitenutaAccontoAreaContabile.percentualeAliquota"), 8);
        builder.nextRow();

        builder.addProperty("datiRitenutaAccontoAreaContabile.contributoPrevidenzialePresente", 1);
        builder.addHorizontalSeparator("Contributo previdenziale", 2, 7);
        builder.nextRow();
        builder.addLabel("tipo", 2);
        builder.addBinding(bf.createBinding("tipoAreaContabile.tipoRitenutaAcconto"), 4);
        builder.nextRow();
        builder.addLabel("datiRitenutaAccontoAreaContabile.percContributiva", 2);
        builder.addBinding(bf.createBoundPercentageText("datiRitenutaAccontoAreaContabile.percContributiva"), 4);
        builder.nextRow();
        builder.addLabel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore", 2);
        builder.addBinding(
                bf.createBoundPercentageText("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore"), 4);
        builder.addLabel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda", 6);
        builder.addBinding(
                bf.createBoundPercentageText("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda"), 8);

        getFormModel().getFieldMetadata("tipoAreaContabile.tipoRitenutaAcconto").setReadOnly(true);

        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.fondoProfessionistiPresente")
                .addValueChangeListener(updateControlListener);
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.causaleRitenutaPresente")
                .addValueChangeListener(updateControlListener);
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.contributoPrevidenzialePresente")
                .addValueChangeListener(updateControlListener);

        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore")
                .addValueChangeListener(percLavoratoreListener);
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda")
                .addValueChangeListener(percAziendaleListener);

        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.imponibileSoggettoRitenuta")
                .addValueChangeListener(imponibileListener);
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.imponibileNonSoggettoRitenuta")
                .addValueChangeListener(imponibileListener);

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.fondoProfessionistiPresente")
                .removeValueChangeListener(updateControlListener);
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.causaleRitenutaPresente")
                .removeValueChangeListener(updateControlListener);
        getFormModel().getValueModel("datiRitenutaAccontoAreaContabile.contributoPrevidenzialePresente")
                .removeValueChangeListener(updateControlListener);
        super.dispose();
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
        updateEnableControl();
    }

    /**
     * Aggiorna la proprietà enable dei controlli in base ai dati per le ritenute d'acconto.
     */
    private void updateEnableControl() {

        // fondo professionisti
        boolean fondoPresente = (Boolean) getFormModel()
                .getValueModel("datiRitenutaAccontoAreaContabile.fondoProfessionistiPresente").getValue();

        if (!fondoPresente) {
            getValueModel("datiRitenutaAccontoAreaContabile.percFondoProfessionisti").setValue(0.0);
        }
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.percFondoProfessionisti")
                .setEnabled(fondoPresente);

        // dati ritenuta
        boolean datiPresenti = (Boolean) getFormModel()
                .getValueModel("datiRitenutaAccontoAreaContabile.causaleRitenutaPresente").getValue();

        if (!datiPresenti) {
            getValueModel("datiRitenutaAccontoAreaContabile.percentualeImponibile").setValue(0.0);
            getValueModel("datiRitenutaAccontoAreaContabile.percentualeAliquota").setValue(0.0);
        }
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.tributo").setEnabled(datiPresenti);
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.sezione").setEnabled(datiPresenti);
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.percentualeImponibile")
                .setEnabled(datiPresenti);
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.percentualeAliquota")
                .setEnabled(datiPresenti);

        // contributo previdenziale
        boolean contributoPresente = (Boolean) getFormModel()
                .getValueModel("datiRitenutaAccontoAreaContabile.contributoPrevidenzialePresente").getValue();

        if (!contributoPresente) {
            getValueModel("datiRitenutaAccontoAreaContabile.percContributiva").setValue(0.0);
            getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore").setValueSilently(0.0,
                    percLavoratoreListener);
            getValueModel("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda").setValueSilently(0.0,
                    percAziendaleListener);
        }
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.percContributiva")
                .setEnabled(contributoPresente);
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoLavoratore")
                .setEnabled(contributoPresente);
        getFormModel().getFieldMetadata("datiRitenutaAccontoAreaContabile.percPrevidenzialeCaricoAzienda")
                .setEnabled(contributoPresente);

        // aggiorna la label per la verifica degli imponibili
        imponibileListener.propertyChange(null);
    }

}
