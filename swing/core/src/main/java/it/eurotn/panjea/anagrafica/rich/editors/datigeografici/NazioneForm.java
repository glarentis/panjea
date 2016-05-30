package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class NazioneForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "nazioneForm";

    /**
     * Costruttore.
     */
    public NazioneForm() {
        super(PanjeaFormModelHelper.createFormModel(new Nazione(), false, FORM_ID));
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("right:pref,4dlu,fill:default:grow", "2dlu,default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
        builder.setLabelAttributes("r, c");
        builder.nextRow();
        builder.setRow(2);

        builder.nextRow();
        ((JTextField) builder.addPropertyAndLabel("codice")[1]).setColumns(3);
        builder.nextRow();
        ((JTextField) builder.addPropertyAndLabel("descrizione")[1]).setColumns(25);
        builder.nextRow();
        builder.addLabel("codiceValuta");
        builder.addBinding(bf.createBoundSearchText("codiceValuta", null, ValutaAzienda.class), 3);
        builder.nextRow();
        builder.addPropertyAndLabel("intra");
        builder.nextRow();
        ((JTextField) builder.addPropertyAndLabel("codiceNazioneUIC")[1]).setColumns(3);
        builder.nextRow();
        builder.addPropertyAndLabel("blacklist");
        builder.nextRow();
        builder.addPropertyAndLabel("livelloAmministrativo1");
        builder.nextRow();
        builder.addPropertyAndLabel("livelloAmministrativo2");
        builder.nextRow();
        builder.addPropertyAndLabel("livelloAmministrativo3");
        builder.nextRow();
        builder.addPropertyAndLabel("livelloAmministrativo4");
        builder.nextRow();
        builder.addHorizontalSeparator("Maschera indirizzo stampa", 3);
        builder.nextRow();
        builder.addProperty("mascheraIndirizzo", 1, 26, 3, 1);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.LIVELLO1_PLACEHOLDER));
        builder.addComponent(new JLabel("nome livello amm. 1"), 3);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.LIVELLO2_PLACEHOLDER));
        builder.addComponent(new JLabel("nome livello amm. 2"), 3);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.LIVELLO2_SIGLA_PLACEHOLDER));
        builder.addComponent(new JLabel("sigla  livello amm. 2"), 3);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.LIVELLO3_PLACEHOLDER));
        builder.addComponent(new JLabel("nome livello amm. 3"), 3);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.LIVELLO4_PLACEHOLDER));
        builder.addComponent(new JLabel("nome livello amm. 4"), 3);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.CAP_PLACEHOLDER));
        builder.addComponent(new JLabel("desc. cap"), 3);
        builder.nextRow();
        builder.addComponent(new JLabel(Nazione.LOCALITA_PLACEHOLDER));
        builder.addComponent(new JLabel("desc. località"), 3);
        return builder.getPanel();
    }

    @Override
    public void setFormObject(Object formObject) {
        if (formObject instanceof NazioneUI) {
            super.setFormObject(((NazioneUI) formObject).getNazione());
        }
        if (formObject instanceof Nazione) {
            super.setFormObject(formObject);
        }
    }

}
