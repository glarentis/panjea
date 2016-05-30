/**
 * 
 */
package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form del ruolo, vengono aggiunti codice, descrizione e permessi.
 * 
 * @author Leonardo
 * 
 */
public class RuoloForm extends PanjeaAbstractForm {

    private static Logger logger = Logger.getLogger(RuoloForm.class);
    private static final String ID_FORM = "ruoloForm";
    private JTextField fieldDescrizione;

    /**
     * Costruttore.
     * 
     */
    public RuoloForm() {
        super(PanjeaFormModelHelper.createFormModel(new Ruolo(), false, ID_FORM), ID_FORM);
    }

    @Override
    protected JComponent createFormControl() {
        logger.debug("-->createFormControl del ruolo");
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) this.getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");
        builder.row();
        fieldDescrizione = (JTextField) builder.add("descrizione", "colSpan=1 align=left")[1];
        fieldDescrizione.setColumns(20);
        builder.row();
        ((JTextField) builder.add("codice", "colSpan=1 align=left")[1]).setColumns(20);
        builder.row();

        return builder.getForm();
    }

    @Override
    protected String getCommitCommandFaceDescriptorId() {
        return this.getId() + ".save";
    }

    @Override
    protected String getNewFormObjectCommandId() {
        return this.getId() + ".new";
    }

    @Override
    protected String getRevertCommandFaceDescriptorId() {
        return this.getId() + ".revert";
    }
}
