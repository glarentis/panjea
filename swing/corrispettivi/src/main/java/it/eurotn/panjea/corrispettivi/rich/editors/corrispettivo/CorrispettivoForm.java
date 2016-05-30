package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CorrispettivoForm extends PanjeaAbstractForm {

    private static final String FORM_ID = "corrispettivoForm";

    /**
     * Costruttore.
     *
     * @param corrispettivo
     *            corrispettivo
     */
    public CorrispettivoForm(final Corrispettivo corrispettivo) {
        super(PanjeaFormModelHelper.createFormModel(corrispettivo, false, FORM_ID), FORM_ID);
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");

        builder.row();
        JTextField totaleField = (JTextField) builder.add("totale", "align=left")[1];
        totaleField.setColumns(10);
        totaleField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {

                if (event.getKeyCode() == KeyEvent.VK_ENTER && CorrispettivoForm.this.getFormModel().isCommittable()) {
                    // per coprire il caso di conferma con importo a null e
                    // quindi con errori nel form devo verificare
                    // se committable per evitare errori
                    CorrispettivoForm.this.getFormModel().commit();
                }
                super.keyPressed(event);
            }

        });
        builder.row();

        return builder.getForm();
    }
}
