package it.eurotn.panjea.giroclienti.rich.editors.riepilogo;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RiepilogoGiornalieroForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "riepilogoGiornalieroForm";

    private List<Utente> utenti;

    /**
     * Costruttore.
     *
     * @param riepilogoGiornalieroPM
     *            riepilogo PM
     * @param utenti
     *            utenti disponibili
     */
    public RiepilogoGiornalieroForm(final RiepilogoGiornalieroPM riepilogoGiornalieroPM, final List<Utente> utenti) {
        super(PanjeaFormModelHelper.createFormModel(riepilogoGiornalieroPM, false, FORM_ID), FORM_ID);
        this.utenti = utenti;
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()

        builder.setRow(2);
        builder.addLabel("utenti", 1);
        builder.addBinding(bf.createBoundShuttleList("utenti", utenti, "userName"), 3);
        builder.nextRow();

        builder.addPropertyAndLabel("data", 1);
        builder.nextRow();

        builder.addPropertyAndLabel("daEseguire", 1);
        builder.nextRow();

        return builder.getPanel();
    }

}
