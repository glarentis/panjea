package it.eurotn.rich.editors.table;

import java.awt.Dimension;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.rich.editors.IPageEditor;

public class EditFrameChangeSelectionConstraint {

    private boolean testChange;

    /**
     * Crea il dialogo per la conferma dell'annullamento delle modifiche non valide nella pagina di
     * edit.
     *
     */
    private void createErrorValidationConfirmationDialog() {
        String message = RcpSupport.getMessage("tablePage.message.confirm.error.validation");
        String title = RcpSupport.getMessage("tablePage.title.confirm.error.validation");
        MessageDialog messageDialog = new MessageDialog(title, message);
        messageDialog.setPreferredSize(new Dimension(250, 50));
        messageDialog.setResizable(false);
        messageDialog.showDialog();
    }

    /**
     * Crea il dialogo per la conferma del salvataggio.
     *
     */
    private void createSaveConfirmationDialog() {
        String message = RcpSupport.getMessage("tablePage.message.confirm.save");
        String title = RcpSupport.getMessage("tablePage.title.confirm.save");
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(title, message) {

            @Override
            protected void onCancel() {
                testChange = false;
                super.onCancel();
            }

            @Override
            protected void onConfirm() {
                testChange = true;
            }
        };
        confirmationDialog.setPreferredSize(new Dimension(250, 50));
        confirmationDialog.setResizable(false);
        confirmationDialog.showDialog();
    }

    /**
     * Esegue il test sulla page editor.
     *
     * @param pageEditor
     *            page editor
     * @return esito test
     */
    public boolean test(IPageEditor pageEditor) {

        testChange = false;

        if (pageEditor.isCommittable()) {
            createSaveConfirmationDialog();
        } else {
            createErrorValidationConfirmationDialog();
        }
        return testChange;
    }

}
