package it.eurotn.panjea.compoli.rich.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaInvio;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * @author leonardo
 *
 */
public class ParametriCreazioneComPolivalenteForm extends PanjeaAbstractForm {

    private class IntermediarioPresenteListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {

            if (!((Boolean) event.getNewValue())) {
                ParametriCreazioneComPolivalente parametri = (ParametriCreazioneComPolivalente) getFormObject();
                getFormModel().getValueModel("intermediario").setValue(parametri.new Intermediario());
            }

            boolean readOnly = !(Boolean) event.getNewValue();
            getFormModel().getFieldMetadata("intermediario.codiceFiscale").setReadOnly(readOnly);
            getFormModel().getFieldMetadata("intermediario.numeroIscrizioneCAF").setReadOnly(readOnly);
            getFormModel().getFieldMetadata("intermediario.impegnoATrasmettere").setReadOnly(readOnly);
            getFormModel().getFieldMetadata("intermediario.dataImpegno").setReadOnly(readOnly);
        }
    }

    private class TipologiaInvioListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            TipologiaInvio tipologiaInvio = (TipologiaInvio) evt.getNewValue();

            boolean protocolliRichiesti = tipologiaInvio != null && tipologiaInvio != TipologiaInvio.INVIO_ORDINARIO;

            if (!protocolliRichiesti) {
                getFormModel().getValueModel("protocolloComunicazione").setValue(null);
                getFormModel().getValueModel("protocolloDocumento").setValue(null);
            }

            getFormModel().getFieldMetadata("protocolloComunicazione").setReadOnly(!protocolliRichiesti);
            getFormModel().getFieldMetadata("protocolloDocumento").setReadOnly(!protocolliRichiesti);
        }

    }

    private static final String FORM_ID = "parametriCreazioneSpesometroForm";

    private AziendaCorrente aziendaCorrente = null;
    private JTextField textFieldAnno = null;

    private IntermediarioPresenteListener intermediarioPresenteListener;
    private TipologiaInvioListener tipologiaInvioListener;

    /**
     * Costruttore.
     */
    public ParametriCreazioneComPolivalenteForm() {
        super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneComPolivalente(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,4dlu,fill:80dlu,4dlu,right:pref,4dlu,fill:80dlu,4dlu,right:pref,4dlu,fill:80dlu",
                "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        textFieldAnno = (JTextField) builder.addPropertyAndLabel("annoRiferimento")[1];
        builder.nextRow();

        builder.addPropertyAndLabel("tipologiaInvio");
        builder.addPropertyAndLabel("protocolloComunicazione", 5);
        builder.addPropertyAndLabel("protocolloDocumento", 9);
        builder.nextRow();

        builder.addPropertyAndLabel("tipologiaDati");
        builder.nextRow();

        builder.addPropertyAndLabel("intermediarioPresente");
        builder.nextRow();

        builder.addHorizontalSeparator("Dati intermediario", 3);
        builder.nextRow();

        builder.addPropertyAndLabel("intermediario.codiceFiscale");
        builder.nextRow();

        builder.addPropertyAndLabel("intermediario.numeroIscrizioneCAF");
        builder.nextRow();

        builder.addPropertyAndLabel("intermediario.impegnoATrasmettere");
        builder.nextRow();

        builder.addPropertyAndLabel("intermediario.dataImpegno");
        builder.nextRow();

        intermediarioPresenteListener = new IntermediarioPresenteListener();
        getFormModel().getValueModel("intermediarioPresente").addValueChangeListener(intermediarioPresenteListener);
        getFormModel().getFieldMetadata("intermediario.codiceFiscale").setReadOnly(true);
        getFormModel().getFieldMetadata("intermediario.numeroIscrizioneCAF").setReadOnly(true);
        getFormModel().getFieldMetadata("intermediario.impegnoATrasmettere").setReadOnly(true);
        getFormModel().getFieldMetadata("intermediario.dataImpegno").setReadOnly(true);

        tipologiaInvioListener = new TipologiaInvioListener();
        getFormModel().getValueModel("tipologiaInvio").addValueChangeListener(tipologiaInvioListener);
        getFormModel().getFieldMetadata("protocolloComunicazione").setReadOnly(true);
        getFormModel().getFieldMetadata("protocolloDocumento").setReadOnly(true);

        return builder.getPanel();
    }

    @Override
    protected Object createNewObject() {
        ParametriCreazioneComPolivalente parametriCreazione = new ParametriCreazioneComPolivalente();
        parametriCreazione.setAnnoRiferimento(aziendaCorrente.getAnnoContabile());
        return parametriCreazione;
    }

    @Override
    public void dispose() {
        getFormModel().getValueModel("intermediarioPresente").removeValueChangeListener(intermediarioPresenteListener);
        getFormModel().getValueModel("tipologiaInvio").removeValueChangeListener(tipologiaInvioListener);
        super.dispose();
    }

    /**
     * @return the aziendaCorrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * Indica a quale component dare il focus all'apertura del form.
     */
    public void requestFocusForData() {
        if (textFieldAnno != null) {
            textFieldAnno.requestFocusInWindow();
        }
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

}
