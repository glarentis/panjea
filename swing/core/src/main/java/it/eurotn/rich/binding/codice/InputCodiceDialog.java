package it.eurotn.rich.binding.codice;

import java.awt.Dimension;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.rules.closure.Closure;

import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * @author adriano
 * @version 1.0, 12/nov/07
 */
public class InputCodiceDialog extends PanjeaTitledPageApplicationDialog {

    private static final String DIALOG_ID = "inputCodice";

    private String propertyMessage = "codice";

    private Closure finishActionClosure;

    /**
     * Costruttore.
     * 
     * @param codiceDocumento
     *            codicePM
     * @param entityFormModel
     *            form model dell'entità
     * @param entityFormCodicePropertyPath
     *            nome della proprità codice dell'entita
     * @param pattenCodicePath
     *            nome della proprietà del pattern
     * @param finishActionCLosure
     *            azione da lanciare alla chiusura del dialogo
     */
    public InputCodiceDialog(final CodiceDocumentoPM codiceDocumento, final FormModel entityFormModel,
            final String entityFormCodicePropertyPath, final String pattenCodicePath,
            final Closure finishActionCLosure) {
        super(new CodiceDocumentoPMForm(codiceDocumento, entityFormModel, entityFormCodicePropertyPath,
                pattenCodicePath), null);
        setTitlePaneTitle(getMessage(DIALOG_ID + ".pane.title", new String[] { getMessage(propertyMessage) }));
        setPreferredSize(new Dimension(400, 100));
        this.finishActionClosure = finishActionCLosure;
    }

    @Override
    protected String getTitle() {
        return getMessage(DIALOG_ID + ".title", new String[] { getMessage(propertyMessage) });
    }

    @Override
    protected boolean onFinish() {
        if (finishActionClosure != null) {
            ((FormBackedDialogPage) this.getDialogPage()).getBackingFormPage().commit();
            CodiceDocumentoPM codiceDocumento = (CodiceDocumentoPM) ((FormBackedDialogPage) this.getDialogPage())
                    .getBackingFormPage().getFormObject();
            finishActionClosure.call(codiceDocumento);
        }
        return true;
    }

}