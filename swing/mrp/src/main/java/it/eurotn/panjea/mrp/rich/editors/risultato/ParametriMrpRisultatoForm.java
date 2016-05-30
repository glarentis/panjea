package it.eurotn.panjea.mrp.rich.editors.risultato;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.mrp.rich.editors.risultato.command.CalcolaMrpCommand;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class ParametriMrpRisultatoForm extends PanjeaAbstractForm {

    private class DataInizioChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            getValueModel("numBucket")
                    .setValue(getFormModel().getValueModel("numBucket").getValue());
        }
    }

    private class MrpActionCommandInteceptor implements ActionCommandInterceptor {

        private String titleKey = null;
        private String descriptionKey = null;

        private boolean confirmed = false;

        /**
         * Costruttore.
         *
         * @param titleKey
         *            titolo del dialogo di conferma
         * @param descriptionKey
         *            descrizione del dialogo di conferma
         */
        public MrpActionCommandInteceptor(final String titleKey, final String descriptionKey) {
            super();
            this.titleKey = titleKey;
            this.descriptionKey = descriptionKey;
        }

        @Override
        public void postExecution(ActionCommand arg0) {

        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            arg0.addParameter("parametriMrp", getFormObject());
            ParametriMrpRisultato par = (ParametriMrpRisultato) getFormObject();
            if (par.isEseguiCalcoloSuApertura()) {
                par.setEseguiCalcoloSuApertura(false);
                setFormObject(par);
                return true;
            }

            confirmed = false;
            ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                    RcpSupport.getMessage(titleKey), RcpSupport.getMessage(descriptionKey)) {

                @Override
                protected void onConfirm() {
                    confirmed = true;

                }
            };
            confirmationDialog.showDialog();
            return confirmed;
        }

    }

    private static final String FORM_ID = "ParametriMrpRisultatoForm";

    private CalcolaMrpCommand calcolaMrpCommand = null;
    private MrpActionCommandInteceptor calcolaMrpActionCommandInteceptor = null;

    private DataInizioChangeListener dataInizioChangeListener = null;

    /**
     * @param parametriMrpRisultato
     *            parametri per "modellare" i risultati mrp
     */
    public ParametriMrpRisultatoForm(final ParametriMrpRisultato parametriMrpRisultato) {
        super(PanjeaFormModelHelper.createFormModel(parametriMrpRisultato, false, FORM_ID),
                FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "right:pref,    4dlu,       left: default,   6dlu,   right:pref,    6dlu,       right:pref,    4dlu,    20dlu,  20dlu,     default:grow,     left:pref,   left:pref,  left:pref",
                "2dlu,        default   ");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// new
                                                                              // FormDebugPanelNumbered());
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("dataInizio", 1);
        builder.addBinding(bf.createBoundSearchText("areaOrdine", null, AreaOrdineRicerca.class),
                5);

        builder.addPropertyAndLabel("numBucket", 7);

        builder.addComponent(getCalcolaMrpCommand().createButton(), 10);
        new PanjeaFormGuard(getFormModel(), getCalcolaMrpCommand());

        builder.addBinding(bf.createBoundToggleButton("evidenziaOrdine"), 13);

        builder.addBinding(bf.createBoundToggleButton("escludiOrdinati"), 14);

        getFormModel().getValueModel("dataInizio")
                .addValueChangeListener(getDataInizioChangeListener());

        return builder.getPanel();
    }

    @Override
    public void dispose() {
        if (calcolaMrpCommand != null) {
            calcolaMrpCommand.removeCommandInterceptor(getCalcolaMrpActionCommandInteceptor());
        }
        getFormModel().getValueModel("dataInizio")
                .removeValueChangeListener(getDataInizioChangeListener());
        super.dispose();
    }

    /**
     * @return generaOrdiniActionCommandInteceptor
     */
    private MrpActionCommandInteceptor getCalcolaMrpActionCommandInteceptor() {
        if (calcolaMrpActionCommandInteceptor == null) {
            calcolaMrpActionCommandInteceptor = new MrpActionCommandInteceptor(
                    "parametriMrpCalcolo.confirmationDialog.title",
                    "parametriMrpCalcolo.confirmationDialog.description");
        }
        return calcolaMrpActionCommandInteceptor;
    }

    /**
     * @return generaOrdiniCommand
     */
    private CalcolaMrpCommand getCalcolaMrpCommand() {
        if (calcolaMrpCommand == null) {
            calcolaMrpCommand = new CalcolaMrpCommand();
            calcolaMrpCommand.addCommandInterceptor(getCalcolaMrpActionCommandInteceptor());
        }
        return calcolaMrpCommand;
    }

    /**
     * @return dataInizioChangeListener
     */
    public DataInizioChangeListener getDataInizioChangeListener() {
        if (dataInizioChangeListener == null) {
            dataInizioChangeListener = new DataInizioChangeListener();
        }
        return dataInizioChangeListener;
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
        ParametriMrpRisultato par = (ParametriMrpRisultato) getFormObject();
        if (par.isEseguiCalcoloSuApertura()) {
            getCalcolaMrpCommand().execute();
        }
    }

}
