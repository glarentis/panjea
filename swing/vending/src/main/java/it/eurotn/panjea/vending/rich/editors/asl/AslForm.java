package it.eurotn.panjea.vending.rich.editors.asl;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.Binding;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.vending.domain.Asl;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class AslForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "aslForm";

    /**
     * Costruttore.
     */
    public AslForm() {
        super(PanjeaFormModelHelper.createFormModel(new Asl(), false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
        PanjeaFormLayoutBuilder builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.setRow(2);

        Binding aslBinding = bf.createBoundSearchText("anagraficaAsl", new String[] { "codice", "descrizione" });
        builder.addLabel("asl");
        builder.addBinding(aslBinding, 3);
        builder.nextRow();

        Binding localitaBinding = bf.createBoundSearchText("localita", new String[] { "descrizione" },
                new String[] { "cap" }, new String[] { Cap.class.getName() });
        builder.addLabel("localita");
        builder.addBinding(localitaBinding, 3);
        builder.nextRow();

        Binding capBinding = bf.createBoundSearchText("cap", new String[] { "descrizione" },
                new String[] { "localita" }, new String[] { Localita.class.getName() });
        builder.addLabel("cap");
        builder.addBinding(capBinding, 3);
        builder.nextRow();

        return builder.getPanel();
    }

}