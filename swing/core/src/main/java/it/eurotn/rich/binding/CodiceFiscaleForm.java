package it.eurotn.rich.binding;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.swing.JComponent;

import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.GuiStandardUtils;

public class CodiceFiscaleForm extends PanjeaAbstractForm {

    private class CalcolaGuard implements Guarded {

        private void calcola() {
            try {
                commit();
            } catch (Exception e) {
                logger.error("-->errore nel calcolare il codice fiscale", e);
            }
            CodiceFiscalePM codiceFiscalePM = (CodiceFiscalePM) getFormObject();

            if ((codiceFiscalePM.getNome() != null) && (!codiceFiscalePM.getNome().equals(""))
                    && (codiceFiscalePM.getCognome() != null) && (!codiceFiscalePM.getCognome().equals(""))
                    && (codiceFiscalePM.getComune() != null)
                    && (codiceFiscalePM.getComune().getCodiceCatastale() != null)
                    && (codiceFiscalePM.getDataNascita() != null) && (codiceFiscalePM.getDataNascita() != null)
                    && (codiceFiscalePM.getSesso() != null) && (!codiceFiscalePM.getSesso().equals(""))) {

                CalcoloCodiceFiscale cf = new CalcoloCodiceFiscale();
                // setto il nome
                cf.setNome(codiceFiscalePM.getNome());
                // setto il cognome
                cf.setCognome(codiceFiscalePM.getCognome());
                // setto il codice fiscale
                cf.setSesso(codiceFiscalePM.getSesso());
                // setto la data di nascita (gg/mm/aa)
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                cf.setDataNascita(formatter.format(codiceFiscalePM.getDataNascita()));
                // setto il codice del comune
                String codiceCatastale = codiceFiscalePM.getComune().getCodiceCatastale().toUpperCase();
                cf.setComune(codiceCatastale);

                getFormModel().getValueModel("codiceFiscale").setValue(cf.getCodiceFiscale());
            }
        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (getFormModel().isCommittable()) {
                calcola();
            } else {
                getFormModel().getValueModel("codiceFiscale").setValue("");
            }
        }

    }

    private static final String FORM_ID = "codiceFiscaleForm";

    public CodiceFiscaleForm(Map<String, Object> context) {
        super(PanjeaFormModelHelper.createFormModel(new CodiceFiscalePM(context), false, FORM_ID), FORM_ID);
        attachFormGuard(new CalcolaGuard(), FormGuard.ON_NOERRORS);
    }

    @Override
    protected JComponent createFormControl() {
        logger.debug("--> Creo controlli per il form dell'anagrafica");
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");

        builder.row();
        builder.add(CodiceFiscalePM.PROP_NOME, "align=left");
        builder.add(CodiceFiscalePM.PROP_COGNOME, "align=left");
        builder.row();
        builder.add(CodiceFiscalePM.PROP_DATA_NASCITA, "align=left");
        builder.add(CodiceFiscalePM.PROP_SESSO, "align=left");
        builder.row();
        builder.add(bf.createBoundSearchText("comune", new String[] { "nome" }), "align=left");
        builder.row();

        getFormModel().getValueModel("comune.codiceCatastale").getValue();
        JComponent label = builder.add(bf.createBoundLabel(CodiceFiscalePM.PROP_CODICE_FISCALE), "align=left")[1];
        GuiStandardUtils.attachBorder(label);
        builder.row();

        return builder.getForm();
    }
}
