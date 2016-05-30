package it.eurotn.panjea.magazzino.rich.forms.articolo;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.DescrizioniEstesaEntityPanel;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Form delle descrizioni estese di {@link Articolo}.
 *
 * @author adriano
 * @version 1.0, 21/apr/08
 *
 */
public class DescrizioniEsteseArticoloForm extends PanjeaAbstractForm {

    public static final String FORM_ID = "descrizioniEsteseArticoloForm";

    private IAnagraficaTabelleBD anagraficaTabelleBD;
    private AziendaCorrente aziendaCorrente;

    /**
     * Costruttore.
     *
     * @param formModel
     *            formModel articolo
     */
    public DescrizioniEsteseArticoloForm(final FormModel formModel) {
        super(formModel, FORM_ID);

        this.anagraficaTabelleBD = RcpSupport.getBean("anagraficaTabelleBD");
        this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout("fill:default:grow", "10dlu,10dlu,fill:default:grow");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);// , new FormDebugPanel());
        builder.setLabelAttributes("r, c");
        builder.addComponent(new JLabel(RcpSupport.getMessage("parametriDescrizioneEstesaArticolo")));
        builder.nextRow();
        builder.addComponent(new DescrizioniEstesaEntityPanel(getFormModel(), "descrizioniLinguaEstesa", null,
                anagraficaTabelleBD, aziendaCorrente), 1, 3);

        return builder.getPanel();
    }
}
