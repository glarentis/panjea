package it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 *
 * @author Aracno
 * @version 1.0, 15-mag-2006
 */
public class LegaleRappresentanteForm extends PanjeaAbstractForm {

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     * @param formId
     *            id del form
     */
    public LegaleRappresentanteForm(final FormModel formModel, final String formId) {
        super(formModel, formId);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "70dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow", "3dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        builder.addPropertyAndLabel("anagrafica.legaleRappresentante.nome", 1);
        builder.addPropertyAndLabel("anagrafica.legaleRappresentante.cognome", 5);
        builder.nextRow();

        builder.addPropertyAndLabel("anagrafica.legaleRappresentante.sesso");
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("anagrafica.legaleRappresentante.telefono", 1)[1]).setColumns(13);
        ((JTextField) builder.addPropertyAndLabel("anagrafica.legaleRappresentante.fax", 5)[1]).setColumns(13);
        builder.nextRow();

        builder.addLabel("anagrafica.legaleRappresentante.codiceFiscale", 1);
        builder.addBinding(
                bf.createBoundCodiceFiscale("anagrafica.legaleRappresentante.codiceFiscale",
                        "anagrafica.legaleRappresentante.nome", "anagrafica.legaleRappresentante.cognome",
                        "anagrafica.legaleRappresentante.dataNascita", null, "anagrafica.legaleRappresentante.sesso"),
                3).setPreferredSize(new Dimension(180, 22));
        ((JTextField) builder.addPropertyAndLabel("anagrafica.legaleRappresentante.partitaIVA", 5)[1]).setColumns(11);
        builder.nextRow();
        ;

        builder.addLabel("anagrafica.legaleRappresentante.carica", 1);
        builder.addBinding(bf.createBoundSearchText("anagrafica.legaleRappresentante.carica",
                new String[] { Carica.PROP_DESCRIZIONE }), 3);
        builder.addPropertyAndLabel("anagrafica.legaleRappresentante.dataCarica", 5);
        builder.nextRow();

        builder.addHorizontalSeparator("estremiDiNascitaSeparator", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("anagrafica.legaleRappresentante.dataNascita");
        builder.nextRow();

        DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("anagrafica.legaleRappresentante.datiGeograficiNascita", "right:70dlu");
        builder.addBinding(bindingDatiGeografici, 1, 16, 7, 1);
        builder.nextRow();

        builder.addHorizontalSeparator("estremiDiResidenzaSeparator", 7);
        builder.nextRow();

        builder.addPropertyAndLabel("anagrafica.legaleRappresentante.viaResidenza");
        builder.nextRow();

        DatiGeograficiBinding bindingDatiGeograficiResidenza = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("anagrafica.legaleRappresentante.datiGeograficiResidenza", "right:70dlu");
        builder.addBinding(bindingDatiGeograficiResidenza, 1, 22, 7, 1);
        builder.nextRow();

        return builder.getPanel();
    }

}
